# Car_Track_AI
This program implements a genetic algorithm to train bots to drive around a track

## Bots
Each bot is a circle with distance sensors at equal rotational intervals. Sensors have a fixed range. The robot feeds the sensor readings into its neural network, which then outputs a rotational action. Bots accelerate in the direction they are facing.

## Neural Network Logic
Each network has input nodes equal to its sensors and 3 output nodes. Each input node takes a sensor reading. The bot performs the action depicted by the most activated output node. The output nodes are interpreted as follows:
1. Rotate Counterclockwise
2. Stop Rotating
3. Rotate Clockwise

## Track Logic
Each generation is tested on a procedurally generated track in order to prevent over-fixing. Each track is built up of a fixed number of segments, each with a preset length. Each segment's rotation is altered by a random factor to that of the previous one.

## Genetic Algorithm
1. Simulate the neural networks to derive fitnesses
2. Sort networks based off their fitnesses in ascending order
3. Create new list of Neural networks equal in size to the previous
4. Fill the list with neural networks by randomly combining pairs from the top 10% of the previous generation
5. Mutate all weights and biases in the new neural network indiscrimanantly and randomly by a fixed factor

## Simulation
For each neural network which makes up the population, a bot is added to the simulation. If a bot leaves the bounds of the track, it is disabled and a fitness is recorded. If the simulation exceeds 2000 iterations, all live bots are immediatly disabled and fitnesses are recorded. The simulation ends when all bots are disabled.
