# Manual tests for board
We tried mocking, but we kept getting errors, making it too time consuming.\
Therefore, we settled for manual tests.

## Out of bounds on x-axis or y-axis
We can check player movement by moving with the arrow keys.\
Moving out of bounds on the x-axis or y-axis should reset the position to the player's backup position.\
The players backup position is either the starting position, or a checkpoint.\
The checkpoint is only updated as the new backup position, if the checkpoint is visited in the correct order.

## Taking damage
For now, only damage from lasers are implemented.\
Lasers should only damage the player, if the robot stops on top of the laser.\
Meaning, if movement is changed from 1 to 2 (using the card in the class RoboRally), \
and the player moves past a laser,
then no damage should be taken.\
Taking damage should reduce health by one and play the damage sound.

## Movement stops on walls
When the player is moving by either 1,2 or 3 steps per keyUp, \
if there is a wall in front of the player.\
The player should never move through the wall.

## Healthpoints and lifepoints
Healthpoints are updated procedurally in the top left corner.\
When losing over 10 health, you lose a lifepoint, and it is showed - \
on the player. To test, walk into a laser path. 