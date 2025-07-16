package com.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.model.Garage;
import com.example.model.Vehicle;
import com.example.model.VehicleType;
import com.example.util.VehicleStatusFormatter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final Garage garage;
    private final DbService dbService;

    private static final int FIRST_FLOOR_SLOTS = 10;
    private static final int SECOND_FLOOR_SLOTS = 10;

    @Transactional
    public String park(String plate, String color, String typeStr) {
        if (garage.isPlateAlreadyParked(plate)) {
            return "This vehicle is already parked.";
        }
        VehicleType type;
        try {
            type = VehicleType.fromString(typeStr);
        } catch (IllegalArgumentException e) {
            return "Please select a valid vehicle type! (Car, Jeep, Truck)";
        }

        int slotSize = type.getSlotSize();

        for (int floor = 1; floor <= 2; floor++) {
            List<Integer> freeRange = findFreeSlotRange(floor, slotSize);
            if (freeRange != null) {
                Vehicle vehicle = new Vehicle(plate, color, typeStr, freeRange, floor);
                garage.addVehicle(vehicle);

                String joinedSlots = VehicleStatusFormatter.joinSlots(freeRange);
                
                dbService.insertTicket(vehicle.getTicketId(), plate, color, typeStr, joinedSlots, floor);

                return "Allocated " + slotSize + " slot(s). Your Ticket ID: " + vehicle.getTicketId();
            }
        }
        return "Garage is full.";
    }

    @Transactional
    public String leave(int ticketId) {
        Vehicle vehicle = garage.getVehicleByTicketId(ticketId);
        if (vehicle == null) {
            return "Ticket ID not found.";
        }
        garage.removeVehicleByTicketId(ticketId);
        dbService.deleteTicket(ticketId);
        return "Goodbye and Drive Safe!! " + "Ticket ID was: " + ticketId;
    }

    public String status() {
        StringBuilder sb = new StringBuilder("Status:\n");

        for (int floor = 1; floor <= 2; floor++) {
            sb.append("Floor ").append(floor).append(":\n");
            
            List<Vehicle> vehiclesOnFloor = garage.getVehiclesByFloor(floor);

            if (vehiclesOnFloor.isEmpty()) {
                sb.append("  No vehicles parked.\n");
            } else {
                String floorStatus = VehicleStatusFormatter.formatVehiclesStatus(vehiclesOnFloor);
                sb.append(floorStatus.replaceAll("(?m)^", "  ")).append("\n");
            }
        }
        return sb.toString().trim();
    }

    private List<Integer> findFreeSlotRange(int floor, int requiredSize) {

        Set<Integer> occupied = garage.getOccupiedSlotsByFloor(floor);
        int totalSlots = (floor == 1) ? FIRST_FLOOR_SLOTS : SECOND_FLOOR_SLOTS;

        for (int startIndex = 0; startIndex <= totalSlots - requiredSize; startIndex++) {
            if (isSlotRangeAvailable(occupied, startIndex, requiredSize)) {
                return generateSlotNumbers(startIndex, requiredSize);
            }
        }
        return null;
    }

    private boolean isSlotRangeAvailable(Set<Integer> occupied, int startIndex, int requiredSize) {

        if (startIndex > 0 && occupied.contains(startIndex)) {
            return false;
        }

        for (int slotNumber = startIndex + 1; slotNumber <= startIndex + requiredSize; slotNumber++) {
            if (occupied.contains(slotNumber)) {
                return false;
            }
        }
        int nextSlot = startIndex + requiredSize + 1;
        return !occupied.contains(nextSlot);
    }

    private List<Integer> generateSlotNumbers(int startIndex, int requiredSize) {
        List<Integer> result = new ArrayList<>();
        for (int i = startIndex + 1; i <= startIndex + requiredSize; i++) {
            result.add(i);
        }
        return result;
    }
}
























