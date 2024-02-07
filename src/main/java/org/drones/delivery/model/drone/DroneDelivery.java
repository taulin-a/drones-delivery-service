package org.drones.delivery.model.drone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DroneDelivery {
    private static final String DATA_TEMPLATE = "[%s]";
    private static final String TRIP_TITLE_TEMPLATE = "Trip #%d";
    private static final String LINE_BREAK = "\n";
    private static final String SEPARATOR = ", ";

    private List<Drone> drones;
    @Builder.Default
    private List<Location> locations = new ArrayList<>();

    @Override
    public String toString() {
        var strBuilder = new StringBuilder();

        appendDrones(strBuilder);

        return strBuilder.toString();
    }

    private void appendDrones(StringBuilder strBuilder) {
        for (var drone : drones) {
            strBuilder.append(DATA_TEMPLATE.formatted(drone.getName()))
                    .append(LINE_BREAK);

            appendTrips(strBuilder, drone.getTrips());

            strBuilder.append(LINE_BREAK);
        }
    }

    private void appendTrips(StringBuilder strBuilder, List<Trip> trips) {
        for (var trip : trips) {
            if (Objects.isNull(trip.getLocationsDelivered()) || trip.getLocationsDelivered().isEmpty()) {
                return;
            }

            strBuilder.append(TRIP_TITLE_TEMPLATE.formatted(trip.getTripNumber()))
                    .append(LINE_BREAK);

            for (var location : trip.getLocationsDelivered()) {
                strBuilder.append(DATA_TEMPLATE.formatted(location.getName()));

                if (trip.getLocationsDelivered().indexOf(location) != (trip.getLocationsDelivered().size() - 1)) {
                    strBuilder.append(SEPARATOR);
                }
            }

            strBuilder.append(LINE_BREAK);
        }
    }
}
