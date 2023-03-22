<div style="text-align: center;">Memorandum</div>

TO: Ben Lerner

FROM: Monroe Chung and Yolanda Jarrin

DATE: October 13, 2022

SUBJECT: Design for Player

&nbsp;&nbsp;&nbsp;&nbsp;


We will have a player interface. 

//The player asks the referee for current state information

//receives an instance of the infoForPlayer class

def get_information(self):

We will have a method called get_information where the player asks the 
referee for current state information. The referee will return an instance of our infoForPlayer class.

The infoForPlayer class will contain attributes that inform the player of the current state of the game. 
The attributes will be a copy of the state's board represented as a 2D array of Tiles, a Coordinate that is their current coordinate 
on the board, a coordinate that represents their home tile, a coordinate that represents  their goal tile, a boolean that will be true if the player
has already been to their goal Tile. It will also contain an attribute for both a list of coordinates 
which represent the current locations of the other players and a list of coordinates that are the corresponding home tiles to 
those players. 

//The player completes an entire turn including sliding, inserting, and moving by interacting with the referee

//returns a boolean that will be true if the turn was successful

def turn():

We will have a method called turn. We are implementing our own strategy for an
AI player. Other instances of players will be able to chose their own strategy for their turn. 


Turn will not take in any inputs. Turn will return a boolean if the turn was successful or not. Within turn the player will ask the referee
for permission to make a turn. The referee will respond with a boolean that will be true if the player is allowed to 
take their turn. The player will call the get_information class to get the current state of the board. The player 
will then call a helper method called slideAndInsertStrategy. 

//This method is a helper for turn that allows the player to slide and insert

//takes in an instance of the get_information class

//returns the list of coordinates that represent the reachables after the shift given from the referee

def slideAndInsertStrategy(info)

slideAndInsertStrategy will take in the instance of the get_information class that the player has recieved. This method 
will compute the degree of rotation the player wishes to turn the spare tile as an int, the index of the row or column 
that they wish to shift as an int, and the direction they wish to shift the row or column as a string. 
The player will then send these three attributes to the referee. The referee will return a
list of coordinates that represent the reachables after the shift. slideAndInsertStrategy will return this list to turn. 


Within turn we will receive the list of coordinates and call another helper method called moveLocationStrategy, giving
this helper the same list we just received as an input.

//This method is a helper for turn that allows the player to move the location of their avatar

//given a list of coordinates that represent the reachables after the shift given from the referee

def moveLocationStrategy(List[Coordinate])

Within moveLocationStrategy the player will choose and send their desired coordinate back to 
the referee. Then the referee will return a boolean that will be true if the coordiante is valid and the move 
was successful. This boolean value from the referee will be returned by moveLocationStrategy and by the entire turn 
method. 

If the referee finds the entire move to be successful, the referee will then update the actual board. The referee 
does not alter the board until the entire turn is completed by the player. To find reachable 
the referee will alter a copy of the board. This ensures that a turn is only completed in its entirety. 


