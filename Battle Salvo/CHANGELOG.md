# Change Log.

## Cleaning PA03 Code.
- Remove Public Methods not in the Player Interface and implementing through Dependency Injection. This will allow
players to be declared as Player not AiPlayer or UserPlayer.
- Added the Seed, ShotBoard, and GameBoard to the constructor of the players.
- Added both GameBoards to the constructor for BattleSalvoModel.
- Methods Removed: GetShotBoard, GetGameBoard, GetMisses
- Added new implementation in takeShots of User and AI to track shot board due to removal of GetMisses.
- Changed TIED to DRAW in GameResults in order at align with the Json Input.
- Changed BattleSalvoTerminalView to use Readable and Appendable for ease of use.
- Changed BattleSalvoTerminalView to add the DisplayMessage class, to avoid Try catching every method and ease of use.
- Changed AiPlayer to increase the efficiency and accuracy of the AI. Added two new functions to help
takeShots effectiveness.
- added toJson class for Ship and Coord to access Json more effectively.
- Changed Driver to account for both Proxy and Game controller
- Adjusted test classes accordingly.


