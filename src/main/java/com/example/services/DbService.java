package com.example.services;


import com.example.converter.TicketToVehicleConverter;
import com.example.model.Garage;
import com.example.model.Vehicle;
import org.springframework.stereotype.Service;

import com.example.entity.FirstFloorTicket;
import com.example.entity.SecondFloorTicket;
import com.example.repository.FirstFloorTicketRepository;
import com.example.repository.SecondFloorTicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DbService {

    private final FirstFloorTicketRepository firstFloorRepo;
    private final SecondFloorTicketRepository secondFloorRepo;
    private final Garage garage;


    public int getMaxTicketId() {
        int maxFirstFloor = firstFloorRepo.findMaxTicketId().orElse(0);
        int maxSecondFloor = secondFloorRepo.findMaxTicketId().orElse(0);
        return Math.max(maxFirstFloor, maxSecondFloor);
    }

    public boolean isPlateAlreadyParked(String plate) {
        return firstFloorRepo.existsByPlate(plate) || secondFloorRepo.existsByPlate(plate);
    }

    public void insertFirstFloorTicket(int ticketId, String plate, String color, String type, String allocatedSlots) {
        FirstFloorTicket ticket = new FirstFloorTicket(ticketId, plate, color, type, allocatedSlots);
        firstFloorRepo.save(ticket);
    }

    public void deleteFirstFloorTicket(int ticketId) {
        firstFloorRepo.deleteByTicketId(ticketId);
    }



    public void insertSecondFloorTicket(int ticketId, String plate, String color, String type, String allocatedSlots) {
        SecondFloorTicket ticket = new SecondFloorTicket(ticketId, plate, color, type, allocatedSlots);
        secondFloorRepo.save(ticket);
    }

    public void deleteSecondFloorTicket(int ticketId) {
        secondFloorRepo.deleteByTicketId(ticketId);
    }




    public void loadVehiclesToGarage() {

        firstFloorRepo.findAll().forEach(ticket -> {
            Vehicle vehicle = TicketToVehicleConverter.convert(ticket, 1);
            garage.addVehicle(vehicle);
        });

        secondFloorRepo.findAll().forEach(ticket -> {
            Vehicle vehicle = TicketToVehicleConverter.convert(ticket, 2);
            garage.addVehicle(vehicle);
        });
    }



}