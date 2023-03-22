<div style="text-align: center;">Memorandum</div>

TO: Ben Lerner

FROM: Monroe Chung and Yolanda Jarrin

DATE: September, 30, 2022

SUBJECT: Project Analysis

&nbsp;&nbsp;&nbsp;&nbsp;
This is the timeline structure we deem appropriate for this project. It will be broken up into three sprints.

&nbsp;&nbsp;&nbsp;&nbsp;
In the first sprint, we will focus on the interfaces and backend of the board, tiles, gems, and players.
Here we will implement the way boards are randomly created, the set of tiles and gems the board is made of, and the way
the board tiles can be moved for each turn. The player interfaces represent the different types 
of players (referee, player, observer) and allow appropriate players to make moves. We will also make a way to store information on the moves that
have been played to make a ranking system for the end of the game. In a MVC pattern, this is our model.

&nbsp;&nbsp;&nbsp;&nbsp;
In the second sprint, we will set up communication between the server and clients. We 
want to set up who/what is communicating and where to store the data they send/receive.
This is necessary so the players can make their moves like changing their location and shifting the tiles.
The clients communicate when they want to join the game and the ref can say where the homes go and such.
In a MVC pattern, this is our controller. We do this after Sprint 1 since we would need our interfaces previously set up 
so we have an implementation of who is connecting to the server and what they are doing to the board.

&nbsp;&nbsp;&nbsp;&nbsp;
In the third sprint, we will create a gui for displaying the board, gems, players, homes, and movement of tiles.
Gui is a UI representation of the board and players as they communicate. In a MVC pattern, this is our view. 
We do this last because at this point all of our backend should be done. Players can make moves and interact 
with the board. We will make a visualization based on what is happening in the game.
