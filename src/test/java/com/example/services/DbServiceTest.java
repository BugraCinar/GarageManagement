package com.example.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.model.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.entity.MyGarage;
import com.example.model.Garage;
import com.example.model.Vehicle;
import com.example.repository.MyGarageRepository;

class DbServiceTest {

    @Mock
    private MyGarageRepository myGarageRepo;

    @Mock
    private Garage mockGarage;

    private Garage realGarage;
    private DbService dbService;
    private DbService dbServiceWithMockGarage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        realGarage = new Garage();
        dbService = new DbService(myGarageRepo, realGarage);
        dbServiceWithMockGarage = new DbService(myGarageRepo, mockGarage);
    }

    // =============== getMaxTicketId Tests ===============

    @Test
    void shouldReturnMaxTicketId() {
        // GIVEN
        Mockito.when(myGarageRepo.findMaxTicketId()).thenReturn(Optional.of(7));

        // WHEN
        int result = dbService.getMaxTicketId();

        // THEN
        assertEquals(7, result);
        Mockito.verify(myGarageRepo).findMaxTicketId();
    }

    @Test
    void shouldReturnZero() {
        // GIVEN
        Mockito.when(myGarageRepo.findMaxTicketId()).thenReturn(Optional.empty());

        // WHEN
        int result = dbService.getMaxTicketId();

        // THEN
        assertEquals(0, result);
        Mockito.verify(myGarageRepo).findMaxTicketId();
    }


    // =============== insertTicket Tests ===============

    @Test
    void shouldInsertTicketWithCar() {
        // GIVEN
        int ticketId = 1;
        String plate = "34-CAR-34";
        String color = "Red";
        String type = "Car";
        String allocatedSlots = "1";
        int floor = 1;

        MyGarage expectedTicket = new MyGarage(ticketId, plate, color, type, allocatedSlots, floor);

        // WHEN
        dbService.insertTicket(ticketId, plate, color, type, allocatedSlots, floor);

        // THEN
        Mockito.verify(myGarageRepo).save(any(MyGarage.class));


        ArgumentCaptor<MyGarage> ticketCaptor = ArgumentCaptor.forClass(MyGarage.class);
        Mockito.verify(myGarageRepo).save(ticketCaptor.capture());

        MyGarage actualTicket = ticketCaptor.getValue();
        assertEquals(expectedTicket.getTicketId(), actualTicket.getTicketId());
        assertEquals(expectedTicket.getPlate(), actualTicket.getPlate());
        assertEquals(expectedTicket.getColor(), actualTicket.getColor());
        assertEquals(expectedTicket.getType(), actualTicket.getType());
        assertEquals(expectedTicket.getAllocatedSlots(), actualTicket.getAllocatedSlots());
        assertEquals(expectedTicket.getFloor(), actualTicket.getFloor());
    }

    @Test
    void shouldInsertTicketWithJeep() {
        // GIVEN
        int ticketId = 2;
        String plate = "01-JEEP-01";
        String color = "Blue";
        String type = "Jeep";
        String allocatedSlots = "2,3";
        int floor = 2;
        MyGarage expectedTicket = new MyGarage(ticketId, plate, color, type, allocatedSlots, floor);

        // WHEN
        dbService.insertTicket(ticketId, plate, color, type, allocatedSlots, floor);

        // THEN
        Mockito.verify(myGarageRepo).save(any(MyGarage.class));


        ArgumentCaptor<MyGarage> ticketCaptor = ArgumentCaptor.forClass(MyGarage.class);
        Mockito.verify(myGarageRepo).save(ticketCaptor.capture());

        MyGarage actualTicket = ticketCaptor.getValue();
        assertEquals(expectedTicket.getTicketId(), actualTicket.getTicketId());
        assertEquals(expectedTicket.getPlate(), actualTicket.getPlate());
        assertEquals(expectedTicket.getColor(), actualTicket.getColor());
        assertEquals(expectedTicket.getType(), actualTicket.getType());
        assertEquals(expectedTicket.getAllocatedSlots(), actualTicket.getAllocatedSlots());
        assertEquals(expectedTicket.getFloor(), actualTicket.getFloor());
    }

    @Test
    void shouldInsertTicketWithTruck() {
        // GIVEN
        int ticketId = 3;
        String plate = "06-TRUCK-06";
        String color = "White";
        String type = "Truck";
        String allocatedSlots = "4,5,6,7";
        int floor = 1;
        MyGarage expectedTicket = new MyGarage(ticketId, plate, color, type, allocatedSlots, floor);

        // WHEN
        dbService.insertTicket(ticketId, plate, color, type, allocatedSlots, floor);

        // THEN
        Mockito.verify(myGarageRepo).save(any(MyGarage.class));

        ArgumentCaptor<MyGarage> ticketCaptor = ArgumentCaptor.forClass(MyGarage.class);
        Mockito.verify(myGarageRepo).save(ticketCaptor.capture());

        MyGarage actualTicket = ticketCaptor.getValue();
        assertEquals(expectedTicket.getTicketId(), actualTicket.getTicketId());
        assertEquals(expectedTicket.getPlate(), actualTicket.getPlate());
        assertEquals(expectedTicket.getColor(), actualTicket.getColor());
        assertEquals(expectedTicket.getType(), actualTicket.getType());
        assertEquals(expectedTicket.getAllocatedSlots(), actualTicket.getAllocatedSlots());
        assertEquals(expectedTicket.getFloor(), actualTicket.getFloor());
    }

    // =============== deleteTicket Tests ===============

    @Test
    void shouldDeleteTicketWithValidTicketId() {
        // GIVEN
        int ticketId = 9;

        // WHEN
        dbService.deleteTicket(ticketId);

        // THEN
        Mockito.verify(myGarageRepo).deleteByTicketId(ticketId);
    }


    // =============== loadVehiclesToGarage Tests ===============

    @Test
    void shouldLoadSingleVehicle() {
        // GIVEN
        MyGarage ticket = new MyGarage(1, "34-CNR-34", "Red", "Car", "1", 1);
        Mockito.when(myGarageRepo.findAll()).thenReturn(List.of(ticket));

        // WHEN
        dbServiceWithMockGarage.loadVehiclesToGarage();

        // THEN
        ArgumentCaptor<Vehicle> vehicleCaptor = ArgumentCaptor.forClass(Vehicle.class);
        Mockito.verify(mockGarage).addVehicle(vehicleCaptor.capture());
        
        Vehicle capturedVehicle = vehicleCaptor.getValue();
        assertEquals("34-LOAD-01", capturedVehicle.getPlate());
        assertEquals("Red", capturedVehicle.getColor());
        assertEquals(VehicleType.fromString("Car"), capturedVehicle.getType());
        assertEquals(1, capturedVehicle.getFloor());
        assertEquals(1, capturedVehicle.getTicketId());

        Mockito.verify(myGarageRepo).findAll();
    }

    @Test
    void shouldLoadMultipleVehicles() {
        // GIVEN
        MyGarage ticket1 = new MyGarage(1, "34-MULTI-01", "Red", "Car", "1", 1);
        MyGarage ticket2 = new MyGarage(2, "01-MULTI-02", "Blue", "Jeep", "2,3", 2);
        MyGarage ticket3 = new MyGarage(3, "35-MULTI-03", "White", "Truck", "4,5,6,7", 1);

        Mockito.when(myGarageRepo.findAll()).thenReturn(List.of(ticket1, ticket2, ticket3));

        // WHEN
        dbServiceWithMockGarage.loadVehiclesToGarage();

        // THEN

        ArgumentCaptor<Vehicle> vehicleCaptor = ArgumentCaptor.forClass(Vehicle.class);
        Mockito.verify(mockGarage, times(3)).addVehicle(vehicleCaptor.capture());
        
        List<Vehicle> capturedVehicles = vehicleCaptor.getAllValues();
        assertEquals(3, capturedVehicles.size());
        

        Vehicle capturedV1 = capturedVehicles.get(0);
        assertEquals("34-MULTI-01", capturedV1.getPlate());
        assertEquals("Red", capturedV1.getColor());
        assertEquals(VehicleType.fromString("Car"), capturedV1.getType());
        assertEquals(1, capturedV1.getFloor());
        

        Vehicle capturedV2 = capturedVehicles.get(1);
        assertEquals("01-MULTI-02", capturedV2.getPlate());
        assertEquals("Blue", capturedV2.getColor());
        assertEquals(VehicleType.fromString("Jeep"), capturedV2.getType());
        assertEquals(2, capturedV2.getFloor());
        

        Vehicle capturedV3 = capturedVehicles.get(2);
        assertEquals("35-MULTI-03", capturedV3.getPlate());
        assertEquals("White", capturedV3.getColor());
        assertEquals(VehicleType.fromString("Truck"), capturedV3.getType());
        assertEquals(1, capturedV3.getFloor());

        Mockito.verify(myGarageRepo).findAll();
    }

    @Test
    void shouldHandleEmptyData() {
        // GIVEN
        Mockito.when(myGarageRepo.findAll()).thenReturn(Collections.emptyList());

        // WHEN
        dbServiceWithMockGarage.loadVehiclesToGarage();

        // THEN
        Mockito.verify(mockGarage, never()).addVehicle(any(Vehicle.class));

        Mockito.verify(myGarageRepo).findAll();
    }

}