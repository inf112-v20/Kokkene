# Manual tests for board
We tried mocking, but we kept getting errors, making it too time consuming.\
Therefore, we settled for manual tests.

## Out of bounds on x-axis or y-axis
We can test player movement by moving with either the arrow keys or selecting cards.\
Moving out of bounds on the x-axis or y-axis should reset the position to the player's backup position.\
The players backup position is either the starting position, or a checkpoint.\
The checkpoint is only updated as the new backup position, if the checkpoint is visited in the correct order.

## Taking damage
For now, only damage from lasers are implemented.\
Lasers should only damage the player, if the robot stops on top of the laser for the turn.\
Meaning, if the player moves by 1 and ends up on top of the laser, damage is taken.\
If the player moves by 2 or 3, it doesn't end its turn on top of the laser, so no damage is taken.

## Movement stops on walls
When the player is moving by either 1, 2 or 3 tiles, \
and there is a wall in front of the player,\
The player should not move through the wall, but simply stop.

## Healthpoints and lifepoints
Healthpoints are updated procedurally in the top left corner.\
When losing over 10 health, you lose a lifepoint, and it is showed - \
on the player. To test, walk into a laser path. 

## Moving on conveyors
Moving on top of the yellow conveyor belt should move the player by one, in the direction the conveyor belt displays.\
Blue conveyor belt should move the player by two. \
Both types of conveyor belts can be found on the testConveyors map

## Wrenches
The tile with only the wrench should recover one healthpoint,\
while the one with a wrench and hammer should recover two healthpoints.

## Gears
Moving on a gear should change the players direction according to the arrows on it.\
Gear with green arrows should change the direction of the player to its right.\
Gear with orange arrows should change the direction of the player to its left.

## Selecting cards
Selecting cards is done with the mouse.\
Upon clicking a card, it should indicate it has been chosen by turning green and having a number on it.\
The number represents which turn the card will be played in.

When you have selected 5 cards, the only way to do anything with the 5 cards should be either,\
clicking the confirm button on the screen, or use the keyboard shortcuts 'c' and 'r'.\
'c' should confirm the cards, and move the player accordingly, while 'r' resets the cards and allows you to select 5 cards all over again.

If damage is taken, then a card will be removed from the hand.\
However, no matter how much damage has been taken, you will always have 5 cards on hand.\
Taking damage when you only have 5 cards on hand, leads to cards starting to be locked in place.

To test the amount of cards upon taking damage, do not use the arrow keys!\
By picking the testDeck from the dropdown box in the menu,\
you get a non-shuffled deck with 9 cards, which should always be the same.\
Making it easier to get to a laser and check how the damage affects the cards on hand.


