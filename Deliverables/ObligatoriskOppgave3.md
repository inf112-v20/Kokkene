# Oblig 3

## RoboRally - a game made by Kokkene
RoboRally is a multiplayer game in which robots move around the map attempting to get to all the flags. 

### Part 1 - Team and Project

#### Roles
The past month has been a good time for our teamwork.\
We find ourselves more comfortable in our roles, and the communication between\
roles are going smoothly.\
Our roles have been more refined, and we feel we elected the best person for\
each role.

#### Decision making 
Our choices have been mostly done by our team members individually.\
Doing that, we waste little time on planning together.\
As a counterpart, we may spend some time refactoring afterwards,\
which is a decision we all agreed to.

#### Methodology
We are still using the same methodology as when we first started this project.\
A mixture of Scrum and XP. At the beginning of working towards a mandatory assignment\
we create a backlog of all the features which are to be added through user stories.\
We then from week to week work on our separate feature as a sprint.\
Then when we do the next meeting online(discord) we discuss any additions or changes\
to do until the next meeting.

#### Retrospection
The project started off well. There was a lot to do, and everyone took initiative to work on smaller projects.\
Communication was good, even if we did not take full advantage of what the project board on github had to offer.\
This could have been a big detriment, due to bad organizing and prioritization.\
However, we had weekly scheduled meetings. We met twice a week,\
where we shared results and talked about what we planned to work on until next time.\
Communication also got even better over time, as every time someone made changes outside of the meetings,\
the results where shared on Slack.\
The results shared, also included any bugs they had encountered,\
making it easier for everyone to work out a potential solution for the bugs that had been encountered.\
Even though, communication is good. We are very lacking when it comes to commit messages, and documenting code.

#### Improvement in retrospection
More efficient usage of the project board, as it is currently not being used to its full potential.\
Better commit messages, and documentation of code.\
We can start to use more functions github offers in general, for example issues.

#### Task priorities
Most of the game elements are now working, except for cards and some board objects.\
We still need to fully implement phases and rounds, but in order to do that, we need the cards to work.\
Therefore, our priority this time, is to make the cards work as intended.

#### Communication
The communication in the group works really well, we use slack and github project board\
to keep in touch and inform eachother of updates we have made. We also meet twice a week\
for 2 hours where we go through tasks to be done, and work on the project together.\
We get along well and will see this project through.

#### User Stories

1.    As a player, I want to choose which cards I want to program in my desired order.
2.    As a player, I want board objects to work as intended.
3.    As a programmer on this project, I want the tests to be clear and concise.

#### Acceptance Criterias

1.    Program cards in the desired order
      1.    Add a method to display cards
      2.    Add a method to choose the order of cards
      3.    Lock in the chosen cards.  
    
2.    Board objects work as intended
      1.    Conveyors should move players.
      2.    Wrenches heal
      3.    Walls stop the player
      4.    Objectives are displayed and updates.
      
3.    Test method's names are clear and concise
      1.    The name of the method should give an idea of what it tests.
      2.    The name of the method should be camel case
      3.    If we are creating some object in several of the tests,\
      the object creation should be done in "@before"
      4.    No duplicate tests, or tests which checks the same thing.

##### Implementation

1.    Program cards in the desired order
      1.    Cards can now be displayed through calling toggleDeck.render().
      2.    You can click the cards in the order you want to program them.
      3.    Confirm the order with the key "C" or the button on screen.

2.    Board objects work as intended
      1.    Tile class added
            1.    All important tiles registered as enums.
            2.    Methods to get all values and directions.
         
      2.    Used Tile.java methods in Board.java.
      
      3.    Refactored Board.java

3.    Test method's names are clear and concise
      1.    Check all the current tests and make sure they follow our set criteria.
      2.    When implementing new tests be sure to check the set criteria
      3.    Remove any redundant/duplicate tests
      
##### Meeting minutes
[Meeting 1](https://github.com/inf112-v20/Kokkene/blob/master/Deliverables/MinutesOblig3/Tuesday030320.md)  
[Meeting 2](https://github.com/inf112-v20/Kokkene/blob/master/Deliverables/MinutesOblig3/Tuesday100320.md)  
[Meeting 3](https://github.com/inf112-v20/Kokkene/blob/master/Deliverables/MinutesOblig3/Thursday120320.md)  
[Meeting 4](https://github.com/inf112-v20/Kokkene/blob/master/Deliverables/MinutesOblig3/Tuesday170320.md)  
[Meeting 5](https://github.com/inf112-v20/Kokkene/blob/master/Deliverables/MinutesOblig3/Thursday190320.md)  
[Meeting 6](https://github.com/inf112-v20/Kokkene/blob/master/Deliverables/MinutesOblig3/Tuesday240320.md)  
[Meeting 7](https://github.com/inf112-v20/Kokkene/blob/master/Deliverables/MinutesOblig3/Tuesday260320.md)
