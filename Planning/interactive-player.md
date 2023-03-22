<div style="text-align: center;">Memorandum</div>

TO: Ben Lerner

FROM: Monroe Chung and Yolanda Jarrin

DATE: October 26, 2022

SUBJECT: Design for Interactive Player Mechanism

&nbsp;&nbsp;&nbsp;&nbsp;


Our interactive player mechanism will interact with a person. An example of how we can do this
is using stdout to give the person information and will receive inputs from the player via stdin. 
The interactive player mechanism will then pass these inputs to an instance
of our referee class who will actually run the game and make sure the players are following the rules.


Our interactive player mechanism will interact with the people with something like stdin and stdout. 
As the referee class is running a game, it will communicate with a person's player class with stdout or a GUI to send the 
player information such as the current state and what their goal tile. 
When it is a players turn, we will also use stdout to prompt the player to give 
a value for the degrees they wish to rotate the spare tile, the index of the row or column
that they wish to slide, the direction they wish to slide the row or column, and a coordinate that represents
where the player wishes to move to on the board. The person will then write those arguments.
The interactive player mechanism will then give in these inputs through stdin for example and 
pass them to our referee who will determine if this is a legal move or not. The referee
will act as it sees fit according to this move, either by moving the player's avatar 
or by kicking the player out of the game for an illegal move.

For a graphical user interface, each player will be displayed their own instance of a GUI 
of the current board. This will be first given during
the referee "setup" process where it tells each player the beginning state and their treasure coordinate.
It will be updated again whenever it is a players turn.

