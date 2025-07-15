package com.example.init;

import com.example.services.DbService;
import com.example.model.Vehicle;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Loader {

    private final DbService dbService;

    @PostConstruct
    public void initializeGarageAndTicketCounter() {

        int maxTicketId = dbService.getMaxTicketId();
        Vehicle.initializeCounter(maxTicketId);
        dbService.loadVehiclesToGarage();

    }
}