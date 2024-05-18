package org.drones.delivery.component.reader;

import org.drones.delivery.exception.DroneFileReaderException;
import org.drones.delivery.model.drone.Drone;
import org.drones.delivery.model.drone.DroneDelivery;
import org.drones.delivery.model.drone.Location;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DroneFileReader {
    private static final String DRONE_OBJECT_PREFIX = "Drone";

    private final Pattern dataPattern;

    public DroneFileReader() {
        dataPattern = Pattern.compile("\\[([^]]+)],\\s*\\[([^]]+)]");
    }

    public DroneDelivery read(String pathStr) {
        try (var linesStream = Files.lines(getPathFromString(pathStr))) {
            return getDroneDeliveryFromLineStream(linesStream);
        } catch (Exception e) {
            throw new DroneFileReaderException("Error while reading file %s: %s".formatted(pathStr, e.getMessage()), e);
        }
    }

    private Path getPathFromString(String pathStr) {
        var filePath = Paths.get(pathStr);
        if (!Files.exists(filePath)) {
            throw new DroneFileReaderException("Drone file %s could not be found".formatted(pathStr));
        }

        return filePath;
    }

    @SuppressWarnings("unchecked")
    private DroneDelivery getDroneDeliveryFromLineStream(Stream<String> lines) throws IOException {
        Map<Boolean, List<Object>> dronesObjectsMap = lines.map(l -> isDroneLine(l)
                        ? getDroneStrListFromLine(l)
                        : getLocationFromLine(l))
                .collect(Collectors.partitioningBy(o -> o instanceof Location));

        List<Drone> droneList = dronesObjectsMap.get(Boolean.FALSE).stream()
                .findFirst()
                .map(o -> (List<Drone>) o)
                .orElseThrow(() -> new DroneFileReaderException("Drone section not found in file"));

        List<Location> locationList = dronesObjectsMap.get(Boolean.TRUE).stream()
                .map(o -> (Location) o)
                .sorted()
                .toList();

        return DroneDelivery.builder()
                .drones(droneList)
                .locations(locationList)
                .build();
    }

    private boolean isDroneLine(String line) {
        return line.contains(DRONE_OBJECT_PREFIX);
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
