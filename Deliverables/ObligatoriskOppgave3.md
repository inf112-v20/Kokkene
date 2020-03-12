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
which is a decision we all agreed to.\

#### Methodology
Scrum and XP is still our strategy for teamwork.

#### Retrospection
Project board has become a better tool in our group.\
At the same time we agree that the project board is hard to use to its\
fullest potential.

#### Improvement in retrospection
Project board shall be used efficiently.

#### Task priorities
TODO

#### Communication
The communication in the group works really well, we use slack and github project board\
to keep in touch and inform eachother of updates we have made. We also meet twice a week\
for 2 hours where we go through tasks to be done, and work on the project together.\
We get along well and will see this project through.

#### User Stories

1. As a player, I want to choose which cards I want to program in my desired order.
2. As a player, I want board objects to work as intended.

#### Acceptance Criterias

1. Program cards in the desired order
    1. Add a method to display cards
    2. Add a method to choose the order of cards
    3. Lock in the chosen cards.
    
2. Board objects work as intended
    1. Conveyors should move players.
    2. Wrenches heal
    3. Walls stop the player
    4. Objectives are displayed and updates.

##### Implementation

1. Program cards in the desired order
    1. TODO

2. Board objects work as intended
    1. Tile class added
        1. All important tiles registered as enums.
        2. Methods to get all values and directions.
    2. Used Tile.java methods in Board.java.
    3. Refactored Board.java