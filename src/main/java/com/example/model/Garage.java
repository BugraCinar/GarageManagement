package com.example.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;


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


    public Set<Integer> getOccupiedSlotsByFloor(int floor) {
        Set<Integer> occupied = new HashSet<>();
        getAllVehicles().values().stream()
                .filter(v -> v.getFloor() == floor)
                .forEach(v -> occupied.addAll(v.getAllocatedSlots()));
        return occupied;
    }

}
