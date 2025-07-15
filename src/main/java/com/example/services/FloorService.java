package com.example.services;

import com.example.model.Garage;
import com.example.model.Vehicle;
import com.example.model.VehicleType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final Garage garage;
    private final DbService dbService;

    private static final int firstFloorSlots = 10;
    private static final int secondFloorSlots = 10;

    public String park(String plate, String color, String typeStr) {
        if (dbService.isPlateAlreadyParked(plate)) {
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

                String joinedSlots = joinSlots(freeRange);

                if (floor == 1) {
                    dbService.insertFirstFloorTicket(vehicle.getTicketId(), plate, color, typeStr, joinedSlots);
                } else {
                    dbService.insertSecondFloorTicket(vehicle.getTicketId(), plate, color, typeStr, joinedSlots);
                }

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

        if (vehicle.getFloor() == 1) {
            dbService.deleteFirstFloorTicket(ticketId);
        } else {
            dbService.deleteSecondFloorTicket(ticketId);
        }

        return "Goodbye and Drive Safe!! " + "Ticket ID was: " + ticketId;
    }

    public String status() {
        StringBuilder sb = new StringBuilder("Status:\n");

        garage.getAllVehicles().values().stream()
                .sorted(Comparator.comparingInt(Vehicle::getTicketId))
                .forEach(vehicle -> sb.append(vehicle.getPlate())
                        .append(" ")
                        .append(vehicle.getColor())
                        .append(" [")
                        .append(joinSlots(vehicle.getAllocatedSlots()))
                        .append("]\n"));

        return sb.toString().trim();
    }

    private List<Integer> findFreeSlotRange(int floor, int requiredSize) {

        Set<Integer> occupied = garage.getOccupiedSlotsByFloor(floor);
        int totalSlots = (floor == 1) ? firstFloorSlots : secondFloorSlots;


        for (int startIndex = 0; startIndex <= totalSlots - requiredSize; startIndex++) {
            if (isSlotRangeAvailable(occupied, startIndex, requiredSize, totalSlots)) {
                return generateSlotNumbers(startIndex, requiredSize);
            }
        }

        return null;
    }


    @SuppressWarnings("RedundantIfStatement")
    private boolean isSlotRangeAvailable(Set<Integer> occupied, int startIndex, int requiredSize, int totalSlots) {

        if (startIndex > 0 && occupied.contains(startIndex)) {
            return false;
        }


        for (int slotNumber = startIndex + 1; slotNumber <= startIndex + requiredSize; slotNumber++) {
            if (occupied.contains(slotNumber)) {
                return false;
            }
        }

        int nextSlot = startIndex + requiredSize + 1;
        if (nextSlot <= totalSlots && occupied.contains(nextSlot)) {
            return false;
        }

        return true;
    }

    private List<Integer> generateSlotNumbers(int startIndex, int requiredSize) {
        List<Integer> result = new ArrayList<>();
        for (int i = startIndex + 1; i <= startIndex + requiredSize; i++) {
            result.add(i);
        }
        return result;
    }

    private String joinSlots(List<Integer> slots) {
        return slots.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
























//private List<Integer> findFreeSlotRange(int floor, int requiredSize) {
//    Set<Integer> occupied = new HashSet<>();
//    for (Vehicle v : garage.getAllVehicles().values()) {
//        if (v.getFloor() == floor) {
//            occupied.addAll(v.getAllocatedSlots());
//        }
//    }
//
//    int max = (floor == 1)? firstFloorSlots : secondFloorSlots;
//
//    for (int i = 1; i <= max; i++) {
//
//        if (occupied.contains(i - 1)) continue;
//
//        List<Integer> candidate = new ArrayList<>();
//        boolean available = false;
//
//        for (int j = 0; j < requiredSize; j++) {
//            int slot = i + j;
//            if (slot > max || occupied.contains(slot)) {
//                available = true;
//                break;
//            }
//            candidate.add(slot);
//        }
//
//
//        int afterGap = i + requiredSize;
//        if (available || occupied.contains(afterGap)) {
//            continue;
//        }
//
//        return candidate;
//    }
//
//    return null;
//}