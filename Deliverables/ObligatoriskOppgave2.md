## RoboRally - a game made by Kokkene. 
RoboRally is a multiplayer game in which robots move around the map attempting to get to all the flags. 

###User Stories:
These are our user stories which are needed for completing the second obligatory task.


#####User story 1:
As a player I expect the walls and holes on the board to behave appropriately.
    
#####Acceptance criteria:
1. The player is not able to cross over walls.
2. Walking out of the board reboots(kills) the player.
3. Falling into holes also reboots the player.
    
#####User story 2:
As a player I want to be able to know my health and objective.
    
#####Acceptance criteria:
1. Show the health of the robot as an overlay
2. Make the objective more obvious (display which flag to move to).

#####User story 3:
As a player I want to be able to get a hand of cards.
    
#####Acceptance criteria:
1. Create a card element.
1. Give "cards" and give it to the player.

#####User story 4:
As a player I want to have sound in my game.
    
#####Acceptance criteria:
1. Create some music and play it to the player.
2. Have the player make a sound when it gets hurt.

###Project and Project-Structure

#####Roles
We feel that the assigned roles could have been utilized more properly.
This was fortunately the idea we had going into this, because everyone wanted to try a bit of everything.
Even so we feel that the roles we got suited us.

#####Kristoffer: Teamleader
I feel like this role suits me because i am not afraid to speak up when something is wrong or needs fixing.\
I very much enjoy speaking in-front of people and have some control over what is happening.\
My experience with this role so far is that it is more about being a part of every role of the team more so than controlling them.\
People often like to do things their way, and im not going to tell them to do something else if it works fine.

#####Martin: Customer contact
I enjoy the work from idea to product, I therefore felt like I could make an impact\
in this role. I am very engaged in the boardgame and enjoy formulating the rules into\
real code. That is why I chose this role, and so far I am happy with it.

#####Jan Kåre: Code leader
I enjoy coding and find it relaxing.\
We needed someone to look through the code to find errors, incompatibilities, inefficiencies and such.\
This seemed to be something which I could enjoy, which in retrospect was a good call.\
I spend most of my time in this role talking with the others about specific implementation of code.\
As in how and where to implement a specific idea into the game, and how to do it practically.\
Due to all of us being skilled coders I have had no problems with any of my duties.\
The only hiccups are inexperience with both the role and libGDX causing a somewhat low tempo. 

#####Arthur: Artistic leader
When it comes to making a game, the visual part of it is by far one of the most important parts of it.\
Therefore I felt it was natural for me to work with the visual parts of this project.\
I feel comfortable with working with sound and photo editing.\
At the same time I work with the game Art, I also get to write a lot of code.

#####Markus: Test leader
As a test leader, I quality check tests and change them if needed.\
Going into this role, I had as little experience with testing as everyone else on the group,\
but seeing how integral testing is, I wanted to learn more about it.\
The role doesn't feel too demanding either, so it has let me focus on writing and refactoring code.

#####Methodology
The group feels like we hit the spot on our methodology.\
We decided to go with the "Scrum" methodology since everyone is comfortable working alone and as a team.\
There are some risks involved with scrum, however we feel like the positives outweigh the negatives.
We have group session's twice a week where we arrange sprints, such that everyone can come with ideas and ask for help with coding. 

#####Communication
Everyone on the group have created a "Slack" account and post updates on our slack workspace when something is edited.\
We have all grown quite accustomed to each-other at this point, and no one is afraid to ask questions when we meet up.\
No arguments or disagreements have broken out and probably never will.\
Only negative is that we post a little bit infrequent on "Slack".

#####Retrospection
The group feels like we hit our goals and are quite good at setting reasonable ones.\
Every deadline we have set has been met so far and everyone are contributing in some way shape and form.\
Everyone is happy with the current twice a week meetings schedule.

The project-board we think might be a bit under utilized, and we will work on improving this in the future.

#####Documentation
The current build runs and displays at first a menu with two options. Start game or exit.\
The testing we do is for the most part automatic tests, and some manual ones. \
For the automatic tests we check if the player gets initialized with the correct values\
and that it cant get more health or take more damage than possible.\
We also check that the cards works correctly in priorities.

We use manual tests to check if the movement and interactions with the board is correct.
For example walking on a laser tile, will make the player lose 1 health and a sound will be played.\
Walking off the screen or into a hole will currently only reset the position of the player.\
Health should also be updated procedurally on the HUD, so when we take damage from the laser, the HUD\
will update how many healthpoints is left.

Running the application should be smooth, and one can move around with arrowkeys.\
Q to exit
P to pause/resume music
M to mute music