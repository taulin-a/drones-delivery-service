package org.drones.delivery.component.reader;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DroneFileReaderTest {
    private static final String FILE_PATH = "input-samples/Input.txt";
    private static final int EXPECTED_DRONE_LIST_SIZE = 3;
    private static final String EXPECTED_DRONE1_NAME = "DroneA";
    private static final int EXPECTED_DRONE1_MAX_WEIGHT = 200;
    private static final int EXPECTED_LOCATION_LIST_SIZE = 16;
    private static final String EXPECTED_LOCATION1_NAME = "LocationL";
    private static final int EXPECTED_LOCATION1_PACKAGE_WEIGHT = 20;

    @DisplayName("Method should return expected drone object")
    @Test
    public void shouldReturnExpectedObject() {
        var target = new DroneFileReader();

        var droneDelivery = target.read(FILE_PATH);

        assertNotNull(droneDelivery);
        assertEquals(EXPECTED_DRONE_LIST_SIZE, droneDelivery.getDrones().size());

        var drone1 = droneDelivery.getDrones().get(0);
        assertEquals(EXPECTED_DRONE1_NAME, drone1.getName());
        assertEquals(EXPECTED_DRONE1_MAX_WEIGHT, drone1.getMaximumWeight());

        assertEquals(EXPECTED_LOCATION_LIST_SIZE, droneDelivery.getLocations().size());

        var location1 = droneDelivery.getLocations().get(0);
        assertEquals(EXPECTED_LOCATION1_NAME, location1.getName());
        assertEquals(EXPECTED_LOCATION1_PACKAGE_WEIGHT, location1.getPackageWeight());
    }
}
