<div style="text-align: center;">Memorandum</div>

TO: Ben Lerner

FROM: Monroe Chung and Yolanda Jarrin

DATE: September, 30, 2022

SUBJECT: Design for Game State

&nbsp;&nbsp;&nbsp;&nbsp;
The game state will be a class that contains all the knowledge that a referee needs throughout the game. 
We will have attributes for the board as an instance of our board class, the last move made by any player as a coordinate
to check the validity of the next shift, a list of current players, and the player whose turn it currently is. 

&nbsp;&nbsp;&nbsp;&nbsp;
The game state will contain a list of player states corresponding to each player that contains information such 
as if the player has their treasure, the players location, and the gems the player is searching for. 

&nbsp;&nbsp;&nbsp;&nbsp;
The class will have functions that can update whose turn it currently is, check if the game has ended, 
based on if a player is home and has their gems. 

&nbsp;&nbsp;&nbsp;&nbsp;
The game state will also keep track of all moves that have occurred from each player in order to later calculate the 
outcome of the game. 
