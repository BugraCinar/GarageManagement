package com.example.converter;

import com.example.entity.FirstFloorTicket;
import com.example.entity.SecondFloorTicket;
import com.example.model.Vehicle;

import java.util.Arrays;

public class TicketToVehicleConverter {

    public static Vehicle convert(Object ticket, int floor) {
        int ticketId;
        String plate, color, type, allocatedSlotsStr;

        if (ticket instanceof FirstFloorTicket t) {
            ticketId = t.getTicketId();
            plate = t.getPlate();
            color = t.getColor();
            type = t.getType();
            allocatedSlotsStr = t.getAllocatedSlots();
        } else if (ticket instanceof SecondFloorTicket t) {
            ticketId = t.getTicketId();
            plate = t.getPlate();
            color = t.getColor();
            type = t.getType();
            allocatedSlotsStr = t.getAllocatedSlots();
        } else {
            throw new IllegalArgumentException("Unable to convert ticket to vehicle.");
        }

        var allocatedSlots = Arrays.stream(allocatedSlotsStr.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        return new Vehicle(ticketId, plate, color, type, allocatedSlots, floor);
    }
}