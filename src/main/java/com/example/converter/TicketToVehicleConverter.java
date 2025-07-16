package com.example.converter;

import com.example.entity.MyGarage;
import com.example.model.Vehicle;

import java.util.Arrays;

public class TicketToVehicleConverter {

    public static Vehicle convert(MyGarage ticket) {
        int ticketId = ticket.getTicketId();
        String plate = ticket.getPlate();
        String color = ticket.getColor();
        String type = ticket.getType();
        String allocatedSlotsStr = ticket.getAllocatedSlots();
        int floor = ticket.getFloor();

        var allocatedSlots = Arrays.stream(allocatedSlotsStr.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        return new Vehicle(ticketId, plate, color, type, allocatedSlots, floor);
    }
}