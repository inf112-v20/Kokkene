# Manual tests for board
We tried mocking, but we kept getting errors, making it too time consuming.\
Therefore, we settled for manual tests. You can move around using the arrow keys,\
but you cant interact with checkpoints or take damage(hp).

## Out of bounds on x-axis or y-axis
We can test player movement by moving with either the arrow keys or selecting cards.\
Moving out of bounds on the x-axis or y-axis should reset the position to the player's backup position.\
The players backup position is either the starting position, or a checkpoint.\
The checkpoint is only updated as the new backup position, if the checkpoint is visited in the correct order.

## Taking damage
You take damage from both the visible lasers on the map and the other players.\
Lasers should only damage the player, if the robot stops on top of the laser for the turn.\
Meaning, if the player moves by 1 and ends up on top of the laser or in front of another player, damage is taken.\
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
