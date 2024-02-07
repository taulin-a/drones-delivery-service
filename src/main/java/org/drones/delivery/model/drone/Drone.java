package org.drones.delivery.model.drone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drone {
    private String name;
    private int maximumWeight;
    private int currentWeight;
    private List<Trip> trips = new ArrayList<>();
    private int currentTrip = 0;

    public Drone(String name, int maximumWeight) {
        this.name = name;
        this.maximumWeight = maximumWeight;
        currentWeight = maximumWeight;
    }

    public void resetCurrentWeight() {
        currentWeight = maximumWeight;
    }

    public synchronized void startTrip() {
        trips.add(new Trip(++currentTrip));
    }

    public Trip getCurrentTrip() {
        return trips.get(currentTrip - 1);
    }
}
