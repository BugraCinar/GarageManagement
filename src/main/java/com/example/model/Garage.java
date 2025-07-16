package com.example.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.Getter;


@Getter
public class Garage {


    private final Map<Integer, Vehicle> allVehicles = new ConcurrentHashMap<>();

    public void addVehicle(Vehicle vehicle) {
        allVehicles.put(vehicle.getTicketId(), vehicle);
    }

    public void removeVehicleByTicketId(int ticketId) {
        allVehicles.remove(ticketId);
    }

    public Vehicle getVehicleByTicketId(int ticketId) {
        return allVehicles.get(ticketId);
    }

    public boolean isPlateAlreadyParked(String plate) {
        return allVehicles.values().stream()
                .anyMatch(vehicle -> vehicle.getPlate().equals(plate));
    }

    public Set<Integer> getOccupiedSlotsByFloor(int floor) {
        return allVehicles.values().stream()
                .filter(v -> v.getFloor() == floor)
                .flatMap(v -> v.getAllocatedSlots().stream())
                .collect(Collectors.toSet());
    }

    public List<Vehicle> getVehiclesByFloor(int floor) {
        return allVehicles.values().stream()
                .filter(vehicle -> vehicle.getFloor() == floor)
                .sorted(Comparator.comparingInt(Vehicle::getTicketId))
                .toList();
    }

}
