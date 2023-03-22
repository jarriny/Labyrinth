<div style="text-align: center;">Revisions</div>

BY: Yolanda Jarrin and Grace Miller

&nbsp;&nbsp;&nbsp;&nbsp;

Below is our to do list, followed by changes we made after completing each one.

- [X] receive players and assign homes
  
  
This already happens at the beginning of runEntireGame in referee.
  

- [X] find and arrange all potential goals as an ordered sequence
  
We created findAllUnusedGoals() within referee to gather all possible goals
that have not already been assigned to a player.

- [X] first n goals are assigned to players during setup
  

  This is already implemented in our method tellAllPlayersStateAndGoal 
that is called within runEntiregame.
  

- [X] when a player reaches a goal, assign them the next goal in order
- [X] player is informed of their new goal in the usual setup method call
  

  We updated the if statement at the end of the method havePlayerDoTurn in 
referee that checks if this.state.isOnGoalTile(index). In order to inform
  a player of a new goal, we use the same format as the initial set up call.

- [X] once the sequence is empty, players who reach their goal are told to go home

This stayed the same within our referee setup method.
  
We added a counter for number of goals reached into privatePlayerInfo and 
made their current goal not final. We increment this counter within the 
setup method of our player class.

- [X] to decide winners first we find the player who collected the same highest number of treasures
- [X] we find the euclidean distance to their next goal if there is a tie
- [X] the winners are who share the minimal distance
   
We adjusted our winnersIfExists method in the Referee to account for the
new way that winners are determined. We utilize the counter that keeps track
  of the number of goals a player has reached to find the players who have
  reached the most goals. From there, we reuse existing functionality to
  determine the players that are closest to their next destination
  based on Euclidean distance.

- [X] for now do not have any additional goals, implement but don't use
  
We made use of an instance variable to control whether multiple goals
are being used or not. This boolean was checked when making calls that
  differed between the old functionality and the new functionality.


- [X] adapt your referee method that accepts players and an already formed state, which includes player 
knowledge with the initially assigned goal 
   
We account for this in our use of unusedGoals, an instance variable in
    the referee. Based on the state and the previously initialized goals,
    we compile a list of the possible goals and do not include the ones
    that have already been assigned to players. This is then the list we use
    to assign future goals to players.


As for changes with regard to the Player, we made few. In the Player class,
we adjusted the set up method so that if set up is called after the initial
  call and it includes a state (indicating it is not an update call to inform
  the player to go home), the Player's goal is updated and the counter used
  to keep track of how many goals their have reached is incremented. We also
  adjusted PrivatePlayerInfo to now house that counter, and also adjusted
  the naming of the goingHome boolean to line up with its new meaning.

