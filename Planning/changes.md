<div style="text-align: center;">Changes</div>

BY: Yolanda Jarrin and Lowell Camp

&nbsp;&nbsp;&nbsp;&nbsp;

### blank tiles for the board
  - 1 or 2

Explanation:
We can create a tile interface and a class that implements this interface 
called blankTile. A blank tile is essentially a tile with no roads 
and possibly no gems, and we do not see this causing any problems. 

### use movable tiles as goals
  - 4

Explanation:
This change will cause many internal changes such as how player's goals 
are stored and strategies will have to account for sliding your goal 
as part of the turn. Additionally, some interface changes may be needed, 
since other players can slide your goal. 


### ask player to sequentially pursue several goals, one at a time
  - 2 or 3


Explanation:
Players already pursue two goals sequentially, their goal tile and their home tile. 
While some implementation details will need to be changes no large 
interface changes will have to occur. The main difficulties
are determining if the game is over, and determining the list of 
winning players and possible ties. 