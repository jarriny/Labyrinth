FROM: Monroe Chung and Yolanda Jarrin

DATE: November 2, 2022

SUBJECT: Design for Distributed Software System

&nbsp;&nbsp;&nbsp;&nbsp;

We will use the remote-proxy design pattern to create a server class that 
has an instance of our referee. The server class will allow the referee to 
communicate with clients, aka players. 
The server class will allow the referee to communicate with the players
through logical interactions, such as the ones we implemented in Milestone 5. 

Before starting a game a number of potential players will connect to the server. 
Once connected, the players will be prompted for their name. After a 
sufficient number of players have connected or a certain amount of time has 
passed while the server waits for players to join, the server will let the 
referee know that they can begin the setup process for a game. 

The Referee will work with the server to communicate to the players to ask them
to propose a board. 
The player returns a JSON representation of the board, which follows the same 
pattern as the board from our integration test. 
The referee will then choose a board to begin the game and 
communicate the setup of the game to the players. 


This pattern of communication between the referee and the players repeats 
until the game has ended, at which point the referee will let the players know 
who has won. 

