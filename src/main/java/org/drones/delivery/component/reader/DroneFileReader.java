package org.drones.delivery.component.reader;

import org.drones.delivery.exception.DroneFileReaderException;
import org.drones.delivery.model.drone.Drone;
import org.drones.delivery.model.drone.DroneDelivery;
import org.drones.delivery.model.drone.Location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class DroneFileReader {
    private final Pattern dataPattern;

    public DroneFileReader() {
        dataPattern = Pattern.compile("\\[([^]]+)],\\s*\\[([^]]+)]");
    }

    public DroneDelivery read(String pathStr) {
        try (var inputStream = new FileInputStream(readFile(pathStr));
             var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return getDroneDeliveryFromReader(reader);
        } catch (Exception e) {
            throw new DroneFileReaderException("Error while reading file %s: %s".formatted(pathStr, e.getMessage()), e);
        }
    }

    private File readFile(String pathStr) {
        var file = new File(pathStr);
        if (!file.exists()) {
            throw new DroneFileReaderException("Drone file %s could not be found".formatted(pathStr));
        }

        return file;
    }

    private DroneDelivery getDroneDeliveryFromReader(BufferedReader reader) throws IOException {
        final var droneDelivery = new DroneDelivery();
        boolean isFirstLine = true;
        var line = reader.readLine();
        while (Objects.nonNull(line)) {
            if (isFirstLine) {
                droneDelivery.setDrones(getDroneStrListFromLine(line));
                isFirstLine = false;
            } else {
                droneDelivery.getLocations().add(getLocationFromLine(line));
            }
            line = reader.readLine();
        }

        sortLocationsByWeightDesc(droneDelivery.getLocations());

        return droneDelivery;
    }

    private void sortLocationsByWeightDesc(List<Location> locations) {
        Collections.sort(locations);
    }

    private List<Drone> getDroneStrListFromLine(String line) {
        List<Drone> droneList = new ArrayList<>();
        var matcher = dataPattern.matcher(line);
        while (matcher.find()) {
            droneList.add(new Drone(matcher.group(1), Integer.parseInt(matcher.group(2))));
        }

        return droneList;
    }

    private Location getLocationFromLine(String line) {
        var matcher = dataPattern.matcher(line);
        return matcher.find()
                ? new Location(matcher.group(1), Integer.parseInt(matcher.group(2)))
                : null;
    }
}
