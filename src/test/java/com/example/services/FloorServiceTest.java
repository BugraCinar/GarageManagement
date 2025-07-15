package com.example.services;

import com.example.model.Garage;
import com.example.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FloorServiceTest {

    @Mock
    private Garage garage;

    @Mock
    private DbService dbService;


    private FloorService floorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        floorService = new FloorService(garage, dbService);
    }

    @Test
    void testPark_VehicleAlreadyParked() {
        when(dbService.isPlateAlreadyParked("34-ABC-34")).thenReturn(true);
        String result = floorService.park("34-ABC-34", "Red", "Car");
        assertEquals("This vehicle is already parked.", result);
    }

    @Test
    void testPark_InvalidVehicleType() {
        String result = floorService.park("34-ABC-34", "Red", "Jet-Ski");
        assertEquals("Please select a valid vehicle type! (Car, Jeep, Truck)", result);
    }


    @Test
    void testVehiclesAreParkedWithGapBetweenThem() {

        String result1 = floorService.park("34-ABC-01", "Red", "Car");
        assertTrue(result1.contains("Allocated 1 slot"));

        Vehicle firstVehicle = garage.getAllVehicles().values().iterator().next();
        int firstSlot = firstVehicle.getAllocatedSlots().getFirst();


        String result2 = floorService.park("34-ABC-02", "Blue", "Car");
        assertTrue(result2.contains("Allocated 1 slot"));


        Vehicle secondVehicle = garage.getAllVehicles().values().stream()
                .filter(v -> v.getPlate().equals("34-ABC-02"))
                .findFirst()
                .orElse(null);

        assertNotNull(secondVehicle);

        int secondSlot = secondVehicle.getAllocatedSlots().getFirst();


        int gap = Math.abs(secondSlot - firstSlot);
        assertTrue(gap >= 2, "There must be a gap between the vehicles!! ");
    }

    @Test
    void testPark_ValidVehicleType() {
         // any?
        String plate = "34-ABC-34";
        String color = "Red";
        String type = "Car";
         //any mi olsaydı, park atayacak zaten

        when(dbService.isPlateAlreadyParked(plate)).thenReturn(false);
        when(garage.getAllVehicles()).thenReturn(new HashMap<>());

        String result = floorService.park(plate, color, type);

        assertTrue(result.contains("Allocated 1 slot(s)"));
        assertTrue(result.contains("Your Ticket ID:"));

        verify(garage).addVehicle(any(Vehicle.class));
        verify(dbService).insertFirstFloorTicket(anyInt(), eq(plate), eq(color), eq(type), anyString() );
    }



    @Test
    void testLeave_TicketNotFound() {
        when(garage.getVehicleByTicketId(1)).thenReturn(null);
        String result = floorService.leave(1);
        assertEquals("Ticket ID not found.", result);
    }

    @Test
    void testLeave_ValidTicketId() {
        Vehicle vehicle = new Vehicle(1, "34-ABC-34", "Red", "Car", List.of(1), 1);
        when(garage.getVehicleByTicketId(1)).thenReturn(vehicle);

        String result = floorService.leave(1);

        verify(garage).removeVehicleByTicketId(1);
        verify(dbService).deleteFirstFloorTicket(1);
        assertEquals("Goodbye and Drive Safe!! Ticket ID was: 1", result);
    }

    @Test
    void testStatus() {
        Vehicle vehicle = new Vehicle(1, "34-ABC-34", "Red", "Car", List.of(1), 1);
        Map<Integer, Vehicle> map = new HashMap<>();
        map.put(1, vehicle);

        when(garage.getAllVehicles()).thenReturn(map);

        String status = floorService.status();
        assertTrue(status.contains("34-ABC-34"));
        assertTrue(status.contains("Red"));
    }
}