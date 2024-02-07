package org.drones.delivery;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.drones.delivery.component.reader.DroneFileReader;
import org.drones.delivery.component.runner.DeliveriesRunner;
import org.drones.delivery.model.input.FilePathInputDTO;

public class Main {
    public static void main(String... args) {
        var filePathInput = getInputFromArgs(args);
        var droneDelivery = new DroneFileReader().read(filePathInput.getPathStr());
        var deliveriesRunner = new DeliveriesRunner(droneDelivery);
        deliveriesRunner.runDeliveries();

        System.out.println(droneDelivery);
    }

    private static FilePathInputDTO getInputFromArgs(String... args) {
        try {
            var filePathInput = new FilePathInputDTO();
            JCommander.newBuilder()
                    .addObject(filePathInput)
                    .build()
                    .parse(args);

            return filePathInput;
        } catch (ParameterException e) {
            throw new RuntimeException("Invalid parameter: %s%n".formatted(e.getMessage()), e);
        }
    }
}