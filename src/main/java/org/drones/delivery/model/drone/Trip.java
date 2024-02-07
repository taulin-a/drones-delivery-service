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
public class Trip {
    private int tripNumber;
    private List<Location> locationsDelivered;

    public Trip(int tripNumber) {
        this.tripNumber = tripNumber;
        locationsDelivered = new ArrayList<>();
    }
}
