package org.drones.delivery.model.drone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location implements Comparable<Location> {
    private String name;
    private int packageWeight;
    @Builder.Default
    private boolean delivered = false;

    public Location(String name, int packageWeight) {
        this.name = name;
        this.packageWeight = packageWeight;
    }

    @Override
    public int compareTo(Location otherLocation) {
        return packageWeight - otherLocation.getPackageWeight();
    }

    public void visitByDrone(Drone drone) {
        if (delivered) return;

        drone.getCurrentTrip().getLocationsDelivered().add(this);
        drone.setCurrentWeight(drone.getCurrentWeight() - packageWeight);
        delivered = true;
    }
}
