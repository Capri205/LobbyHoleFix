package net.obmc.LobbyHoleFix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// for now only op can use the command
		if (!sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "Sorry, command is reserved for server operators.");
			return true;
		}

		// process the command and any arguments
		if (command.getName().equalsIgnoreCase("holefix") || command.getName().equalsIgnoreCase("hf")) {
			
			Player player = (Player) sender;
			Chunk chunk = player.getLocation().getChunk();
			World world = player.getLocation().getWorld();
			Bukkit.getLogger().info("[LobbyHoleFix] - debug - player in X="+chunk.getX()+",Z="+chunk.getZ());

			int vtermstart = 255;
			if (args.length == 1) {
				vtermstart = Integer.parseInt(args[0]);
				Bukkit.getLogger().info("[LobbyHoleFix] - debug - user provided start height of " + vtermstart);
			}
			
			// iterate across layer 38 of our chunk
	        int cx = chunk.getX() << 4;
	        Bukkit.getLogger().info("[LobbyHoleFix] - debug - cx = "+ cx);
	        int cz = chunk.getZ() << 4;
	        Bukkit.getLogger().info("[LobbyHoleFix] - debug - cz = "+ cz);
	        Block block = null;
	        Block vblock = null;
	        Block cblock = null;
	        boolean foundterminator = false;
	        int cntr = 0; int vterm = vtermstart; int fillcount = 0;
			for (int x = cx; x < cx + 16; x++) {
				for (int z = cz; z < cz + 16; z++) {
					block = world.getBlockAt(x, 37, z);
					Bukkit.getLogger().info("[LobbyHoleFix] - debug - block " + cntr + ": ("+block.getX()+","+block.getY()+","+block.getZ()+")="+block.getType().name());
					cntr++;
					if (block.getType().name().equals("SMOOTH_SANDSTONE") || block.getType().name().equals("DIRT")
							|| block.getType().name().equals("GRASS_BLOCK")) {
						
						vterm = vtermstart;

						vblock = world.getBlockAt(x, vterm, z);
						foundterminator = blockmatch(vblock.getType().name(), world, x, vterm, z);
						while (vterm > 37 && !foundterminator) {
							vterm--;
							vblock = world.getBlockAt(x, vterm, z);
							foundterminator = blockmatch(vblock.getType().name(), world, x, vterm, z);
						}
						if (vterm == 37) {
							Bukkit.getLogger().info("[LobbyHoleFix] - debug - failed to find terminator vblock for X="+vblock.getX()+",Z="+vblock.getZ());
							continue;
						}
						Bukkit.getLogger().info("[LobbyHoleFix] - debug - found terminator vblock ("+vblock.getX()+","+vblock.getY()+","+vblock.getZ()+")="+vblock.getType().name());
						for (int vup = 38; vup < vterm; vup++) {
							cblock = world.getBlockAt(x, vup, z);
							if (blockcheck(cblock.getType().name())) {
								Bukkit.getLogger().info("[LobbyHoleFix] - debug - fill block ("+cblock.getX()+","+cblock.getY()+","+cblock.getZ()+")="+cblock.getType().name());
								cblock.setType(Material.DIRT);
								fillcount++;
							}
						}
					} else {
						Bukkit.getLogger().info("[LobbyHoleFix] - debug - ignore");
					}
				}
			}
			Bukkit.getLogger().info("[LobbyHoleFix] - filled " + fillcount + " block" + (fillcount != 1 ? "s" : "") + " in this chunk");
			sender.sendMessage("[LobbyHoleFix] - filled " + fillcount + " block" + (fillcount != 1 ? "s" : "") + " in this chunk");
			Bukkit.getLogger().info("[LobbyHoleFix] - debug ----------------------------------");
			
			if (fillcount > 0) {
				world.refreshChunk(cx, cz);
				GameMode currmode = player.getGameMode();
				if (currmode.equals(GameMode.SPECTATOR)) {
					player.setGameMode(GameMode.CREATIVE);
					player.setGameMode(currmode);
				}
			}
		}

    	return true;
	}
	
	boolean blockmatch(String blocktype, World w, int x, int y, int z) {
		boolean found = false;
		if (blocktype.equals("STONE")) {
			found = true;
		} else if (blocktype.equals("GRASS_BLOCK")) {
			found = true;
		} else if (blocktype.equals("DIRT")) {
			found = true;
		} else if (blocktype.equals("SNOW")) {
			if (!Tag.LEAVES.isTagged(w.getBlockAt(x, y-1,z).getType())) {
				found = true;
			} else {
				Bukkit.getLogger().info("[LobbyHoleFix] - debug - snow on leaves. ignore");
			}
		}
		if (found) {
			Bukkit.getLogger().info("[LobbyHoleFix] - debug - found match for " + blocktype);
		}
		return found;
	}
	
	boolean blockcheck(String blocktype) {
		boolean fixit = false;
		if (blocktype.equals("AIR")) {
			fixit = true;
		} else if (blocktype.equals("WATER")) {
			fixit = true;
		} else if (blocktype.equals("GRASS")) {
			fixit = true;
		} else if (blocktype.equals("LAVA")) {
			fixit = true;
		} else if (blocktype.equals("SPAWNER")) {
			fixit = true;
		}
		if (fixit) {
			Bukkit.getLogger().info("[LobbyHoleFix] - debug - found block to fix " + blocktype);
		}
		return fixit;
	}
}

