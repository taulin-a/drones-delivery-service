package org.drones.delivery.exception;

public class DroneFileReaderException extends RuntimeException {
    public DroneFileReaderException(String message) {
        super(message);
    }

    public DroneFileReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
