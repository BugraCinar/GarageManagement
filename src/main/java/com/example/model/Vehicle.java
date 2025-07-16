package com.example.model;

import java.util.List;

import lombok.Data;

@Data


public class Vehicle {

    private static int ticketCounter = 1;

    private int ticketId;
    private String plate;
    private String color;
    private VehicleType type;
    private int slotSize;
    private List<Integer> allocatedSlots;
    private int floor;


    public Vehicle(String plate, String color, String typeStr, List<Integer> allocatedSlots, int floor) {
        this.plate = plate;
        this.color = color;
        this.type = VehicleType.fromString(typeStr);
        this.slotSize = this.type.getSlotSize();
        this.ticketId = ticketCounter++;
        this.allocatedSlots = allocatedSlots;
        this.floor = floor;
    }


    public Vehicle(Integer ticketId, String plate, String color, String typeStr, List<Integer> allocatedSlots, int floor) {
        this.plate = plate;
        this.color = color;
        this.type = VehicleType.fromString(typeStr);
        this.slotSize = this.type.getSlotSize();
        this.ticketId = ticketId;  // ticketCounter++ yerine parametreyi kullan
        this.allocatedSlots = allocatedSlots;
        this.floor = floor;
    }


    public static void initializeCounter(int startFrom) {
        ticketCounter = startFrom+1;
    }
}