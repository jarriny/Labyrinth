<div style="text-align: center;">Memorandum</div>

TO: Ben Lerner

FROM: Monroe Chung and Yolanda Jarrin

DATE: October 19, 2022

SUBJECT: Design for Player Protocol

&nbsp;&nbsp;&nbsp;&nbsp;


Our Player Protocol class will act as the line of communication between the referee and the players. 
Players will interact with the Player Protocol to sign up and join a game. 

Whenever a new player interacts with the Player Protocol to join the game, the 
Player Protocol will prompt the referee to get new player information. 
The referee will 
create a home, goal, and avatar for the player and return this information
to the Player Protocol. 

Whenever the Player Protocol deems that it is ok to start the game, the class will create
an instance of a referee that will have an instance of our refState class. Creating this 
refState class includes creating our
representation of a board, a list of PlayerInfoPublic and PlayerInfoPublic. 

The Player Protocol will now start the game and let the first player know that
it is their turn. We can let the player know that it is their turn by prompting
them with a message from Stdout. The Player Protocol will also send a JSON 
representation of our PublicGameState. This PublicGameState is essentially 
our RefGameState without any private information about other players.
The player will now
send the Player Protocol a stream of 5 inputs. These inputs will be the degrees 
to rotate the spareTile by, the index of the row or column that is to be
slid, a string that matches one of our enum DIRECTION values, and 2 ints that 
represent the row and column of the coordinate they wish to move their avatar to. 
The Player Protocol will take in this stream of inputs and send it to the referee
who will check that the inputs correspond to a valid turn. The referee will then
 execute the turn if the inputs are valid. Finally, the Player Protocol will send
the player the updated JSON representation of the PublicGameState now that their
turn has occurred. 


The Player Protocol will continue in this manner, telling each player when 
it is their turn, and executing said turn if it is valid. 

After every turn the Player Protocol will ask the referee if the game 
has ended. If it has ended, the Player Protocol will use Stdout to alert 
the players that the game has ended
and send a final JSON representation of our RefGameState. 

[The link to our UML](uml.png)
