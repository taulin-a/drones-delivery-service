# drone-delivery-service
## Description
A squad of drones is tasked with delivering packages for a major online reseller in a world
where time and distance do not matter. Each drone can carry a specific weight and can make
multiple deliveries before returning to home base to pick up additional loads; however, the goal
is to make the fewest number of trips as each time the drone returns to home base, it is
extremely costly to refuel and reload the drone.
The purpose of the written software is to accept input which will include the name of each
drone and the maximum weight it can carry, along with a series of locations and the total weight
needed to be delivered to that specific location. The software should highlight the most efficient
deliveries for each drone to make on each trip.
Assume that time and distance to each drop off location do not matter, and that the size of
each package is also irrelevant. It is also assumed that the cost to refuel and restock each
drone is a constant and does not vary between drones. The maximum number of drones in a
squad is 100, and there is no maximum number of deliveries which are required.

## About the algorithm
The algorithm consists of a loop the runs while there are available locations with inner loop that initializes a new 
Trip object for the drones, selects optimal locations for the drone to visit using a simple implementation of KnapSack 
algorithm. After the locations are selected we add the locations to the drone's Trip nested object decrement the 
currentWeight field of drone and remove the locations from the available locations list. If we go through all the drones
and there are still available locations, we reset the drone's currentWeight field, add another trip object to drone and
run the loop for finding optimal locations again.

## Dependencies
- [JCommander](https://jcommander.org/)
- [Project Lombok](https://projectlombok.org/)
- [Maven](https://maven.apache.org/)

## How to execute the program
To run the program you only need to execute the script run.sh passing the input file location as a parameter.
Just like the following example:
```
./run.sh input-samples/Input.txt
```
