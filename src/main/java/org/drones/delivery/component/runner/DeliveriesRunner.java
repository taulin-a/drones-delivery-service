package org.drones.delivery.component.runner;

import org.drones.delivery.model.drone.Drone;
import org.drones.delivery.model.drone.DroneDelivery;
import org.drones.delivery.model.drone.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements an algorithm that simulates drones delivering packages from locations.
 */
public class DeliveriesRunner {
    private static final int NUM_OF_LOCATION = 1;

    private final DroneDelivery droneDelivery;
    private final List<Location> availableLocations;

    public DeliveriesRunner(DroneDelivery droneDelivery) {
        this.droneDelivery = droneDelivery;
        availableLocations = new ArrayList<>(droneDelivery.getLocations());
    }

    /**
     * While there are available locations, for each drone select optimal locations and visit
     * each one of them while the drone has weight capacity. When all drones have 0 capacity
     * return to base and refuel (reset current weight capacity).
     */
    public void runDeliveries() {
        while (!availableLocations.isEmpty()) {
            for (var drone : droneDelivery.getDrones()) {
                drone.startTrip();
                deliverToLocations(drone, droneKnapsack(drone));
            }
            droneDelivery.getDrones().forEach(Drone::resetCurrentWeight);
        }
    }

    /**
     * This is a simple knapsack algorithm that optimizes the number of locations visited per trip.
     * It maps all max locations visited in a matrix where each row is a location and the columns represent
     * the weight capacity of the drone.
     * After we map all values we search for the first occurrences of optimal values. the column of the
     * found value is the selected location index.
     *
     * @param drone object that represents the drone and it's trips
     * @return the list of selected locations
     */
    private List<Integer> droneKnapsack(Drone drone) {
        if (availableLocations.size() == 1) {
            return List.of(0);
        }

        var rows = availableLocations.size() + 1;
        var columns = drone.getMaximumWeight();

        int[][] droneMatrix = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == 0) {
                    droneMatrix[i][j] = 0;
                    continue;
                }

                var location = availableLocations.get(i - 1);

                if ((j - location.getPackageWeight()) < 0 || location.getPackageWeight() > j) {
                    droneMatrix[i][j] = droneMatrix[i - 1][j];
                    continue;
                }

                droneMatrix[i][j] = Math.max(droneMatrix[i - 1][j],
                        droneMatrix[i - 1][j - location.getPackageWeight()] + NUM_OF_LOCATION);
            }
        }

        return getSelectedLocations(droneMatrix, rows, columns);
    }

    /**
     * This method runs through the table and find the first occurrences of optimal values.
     *
     * @param droneMatrix matrix representing the iterations of optimal values.
     * @param rows        number of rows in the droneMatrix
     * @param columns     number of columns in the droneMatrix
     * @return list of optimal locations indexes
     */
    private List<Integer> getSelectedLocations(int[][] droneMatrix, int rows, int columns) {
        var maxNumOfLocations = droneMatrix[rows - 1][columns - 1];
        List<Integer> selectedLocationsIndexes = new ArrayList<>();
        while (maxNumOfLocations != 0) {
            var firstOccurrenceIndex = searchFirstOccurrenceOfMax(droneMatrix, rows, columns, maxNumOfLocations);

            if (firstOccurrenceIndex != -1) {
                maxNumOfLocations--;
                selectedLocationsIndexes.add(firstOccurrenceIndex);
            }
        }

        return selectedLocationsIndexes;
    }

    /**
     * Finds first occurrence of optimal value in matrix. If it fails to find the occurrence it returns -1
     *
     * @param droneMatrix matrix representing the iterations of optimal values.
     * @param rows        number of rows in the droneMatrix
     * @param columns     number of columns in the droneMatrix
     * @param max         optimal value to be found
     * @return index of the column of first occurrence of optimal value
     */
    private int searchFirstOccurrenceOfMax(int[][] droneMatrix, int rows, int columns, int max) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (droneMatrix[i][j] == max) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Get Location objects that correspond to indexes in selectedLocations, invoke visitByDrone
     * and then remove the location from the availableLocations list.
     *
     * @param drone             object that represent a drone and it's trips
     * @param selectedLocations list of the indexes of selected locations
     */
    private void deliverToLocations(Drone drone, List<Integer> selectedLocations) {
        for (var locationIndex : selectedLocations) {
            if (locationIndex >= availableLocations.size()) {
                break;
            }

            var location = availableLocations.get(locationIndex);

            if ((drone.getCurrentWeight() - location.getPackageWeight()) < 0) {
                break;
            }

            location.visitByDrone(drone);
            availableLocations.remove(location);
        }
    }
}
