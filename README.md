# LobbyHoleFix

A quick tool to fix the voids that are created when modelling/building terrain using world edit. Whilst these voids aren't visible from the outside, they are there, they exist and hide inside the walls of the landscape, and they haunt me. So this was to fix those as there are thousands of them.

Obviously of no use to anyone other than me and my paranoia, but feel free to use the code as an example of processing blocks in a chunk, or visit the clean lobby at ob-mc.net and go into spectator mode, or take a look a these shots:

BEFORE
![alt text](https://ob-mc.net/repo/lobby-pre-cleanup.png)

AFTER
![alt text](https://ob-mc.net/repo/lobby-post-cleanup.png)

TODO:
* Use ChunkSnapshot instead of using chunk directly
* Currently manual per chunk. Kind of tedious. See about processing region files
