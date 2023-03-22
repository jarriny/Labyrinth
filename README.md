<div style="text-align: center;">Final README</div>

BY: Yolanda Jarrin and Grace Miller

&nbsp;&nbsp;&nbsp;&nbsp;

### Client and Server design

Purpose: allow players to play across the network by introducing a server and client 


- A Client represents a player that wishes to join a game and creates both a player and proxy referee
- A Server represents the referee that runs the game and creates both an instance of a referee and proxy players

These remote proxies add a remote-communication layer between the server and client  without our implementations of referees and players directly communicating

### Client and Server basic interactions

- a client connects to a server and sends the names of the players they contain over the network
- the client also contains a proxy referee
- the server creates remote proxy players that correspond with the connected players, and gives them to 
an instance of a referee who will begin the game


- The game will be run from the server's referee which will interact with the server's proxy players 
who communicate with the client's proxy referee, and in turn the players that the client contains
  
#### Remote Referee
Has a
- Socket
- IPlayer

#### Remote Player
Has a
- Socket
- Name

#### Referee
Has a
- RefState
- list of IPlayers
- list of kicked out IPlayers
- list of IObservers
- list of Coordinates (goals list)
Can
- runEntireGame
- addObserver
- removeObserver

#### Player
Has a
- Name
- Strategy
- PlayerInfoPrivate
- Boolean (win status)
Can
- proposeBoard0
- setUp
- takeTurn
- win

##### Bad Player
Has a
- BadFM, throws division by 0 error on specified method

##### Bad Player 2
Has a
- BadFM and counter n, enters infinite loop on nth call of the specified method

#### PlayerInfoPrivate
Has a
- Coordinate (goal)
- counter for goals reached
- boolean for going home

#### PlayerInfoPublic
Has a
- Coordinate (home)
- Coordinate (current)
- Color

#### RefState
Has a
- PlayerInfoPrivate
- PlayerInfoPublic
- Board
- Spare Tiles
- index of the current player
- ShiftInfo (previous move)
Can
- doTurn
- goToNextPlayer
- isOnGoalTile
- playerGotToGoalTile
- kickPlayerOut
- toPlayerGameState

#### FullTurnInfo
Has a
- int Degree
- ShiftInto
- Coordinate

##### ShiftInfo
Has a
- index
- direction