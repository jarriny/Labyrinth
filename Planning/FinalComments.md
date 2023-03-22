<div style="text-align: center;">Final Comments</div>

BY: Yolanda Jarrin and Grace Miller

&nbsp;&nbsp;&nbsp;&nbsp;

This is a complied list of our unresolved problems in our code, along with
to do's that would address each problem. 

#### Unresolved Problems

- [ ] Runnable Tasks for Milestone 9 and 10 run, but are incorrect
  - [ ] To Do: continue to debug and do a deeper dive into the communication to look for errors
  - [ ] we attempted to print the communication to the terminal, but could also try printing it to a file

- [ ] Strategy was choosing tiles that were not accessible while running 9 and 10
    - [ ] every other use of strategy has no problems (shown in passing integration tests)

- [ ] Failing xbad2 staff-tests

___
#### Recently Resolved Problems

- [X] Milestone 9 was falling because our fullTurnInfo was not parsing correctly
    - [X] To do: made a new deserialize fullTurnInfo function, separate from our adapter class that was not working

___
#### What we know works in Milestone 9 and 10

- [X] Two waiting periods
- [X] players connect from the clients side
- [X] method calls are going to and from the proxyRef and proxyPlayer
- [X] state objects are sent over JSON
- [X] runs to game completion and compiles a list of winners and losers

