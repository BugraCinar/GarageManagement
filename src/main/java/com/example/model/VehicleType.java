package com.example.model;

import lombok.Getter;

@Getter
public enum VehicleType {
    CAR(1),
    JEEP(2),
    TRUCK(4);

    private final int slotSize;

    VehicleType(int slotSize) {
        this.slotSize = slotSize;
    }

    public static VehicleType fromString(String type) {
        return switch (type) {
            case "Car" -> CAR;
            case "Jeep" -> JEEP;
            case "Truck" -> TRUCK;
            default -> throw new IllegalArgumentException("Please select a valid vehicle type! (Car, Jeep, Truck)");
        };
    }
}