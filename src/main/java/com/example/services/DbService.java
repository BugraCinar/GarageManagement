package com.example.services;

import org.springframework.stereotype.Service;

import com.example.converter.TicketToVehicleConverter;
import com.example.entity.MyGarage;
import com.example.model.Garage;
import com.example.model.Vehicle;
import com.example.repository.MyGarageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DbService {

    private final MyGarageRepository myGarageRepo;
    private final Garage garage;

    public int getMaxTicketId() {
        return myGarageRepo.findMaxTicketId().orElse(0);
    }

    public void insertTicket(int ticketId, String plate, String color, String type, String allocatedSlots, int floor) {
        MyGarage ticket = new MyGarage(ticketId, plate, color, type, allocatedSlots, floor);
        myGarageRepo.save(ticket);
    }

    public void deleteTicket(int ticketId) {
        myGarageRepo.deleteByTicketId(ticketId);
    }

    public void loadVehiclesToGarage() {
        myGarageRepo.findAll().forEach(ticket -> {
            Vehicle vehicle = TicketToVehicleConverter.convert(ticket);
            garage.addVehicle(vehicle);
        });
    }
}