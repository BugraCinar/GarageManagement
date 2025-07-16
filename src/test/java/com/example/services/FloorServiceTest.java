package com.example.services;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

import com.example.model.Garage;
import com.example.model.Vehicle;
import com.example.model.VehicleType;

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

    // ===================================== yeni =========================

    @Test
    void shouldParkValidVehicleToFloor1() {
        // GIVEN
        String plate = "34-CNR-34";
        String color = "Turquoise";
        String typeStr = "Car";

        Mockito.when(garage.isPlateAlreadyParked(plate)).thenReturn(false);
        Mockito.when(garage.getOccupiedSlotsByFloor(1)).thenReturn(Set.of());

        // WHEN
        String result = floorService.park(plate, color, typeStr);

        // THEN
        assertTrue(result.contains("Allocated 1 slot(s)."));

        ArgumentCaptor<Vehicle> vehicleCaptor = ArgumentCaptor.forClass(Vehicle.class);
        Mockito.verify(garage).addVehicle(vehicleCaptor.capture());

        Vehicle capturedVehicle = vehicleCaptor.getValue();
        assertEquals(plate, capturedVehicle.getPlate());
        assertEquals(color, capturedVehicle.getColor());
        assertEquals(VehicleType.fromString(typeStr), capturedVehicle.getType());
        assertEquals(1, capturedVehicle.getFloor());
        assertEquals(List.of(1), capturedVehicle.getAllocatedSlots());
        assertTrue(capturedVehicle.getTicketId() > 0);

        Mockito.verify(dbService).insertTicket(anyInt(), eq(plate), eq(color), eq(typeStr), anyString(), eq(1));
    }

    @Test
    void shouldParkValidVehicleToFloor2WhenFloor1IsFull() {
        // GIVEN
        String plate = "01-BGR-01";
        String color = "Turquoise";
        String typeStr = "Jeep";

        Mockito.when(garage.isPlateAlreadyParked(plate)).thenReturn(false);
        Mockito.when(garage.getOccupiedSlotsByFloor(1)).thenReturn(Set.of(1,2,3,4,5,6,7,8,9,10));
        Mockito.when(garage.getOccupiedSlotsByFloor(2)).thenReturn(Set.of());

        // WHEN
        String result = floorService.park(plate, color, typeStr);

        // THEN
        assertTrue(result.contains("Allocated 2 slot(s)."));

        ArgumentCaptor<Vehicle> vehicleCaptor = ArgumentCaptor.forClass(Vehicle.class);
        Mockito.verify(garage).addVehicle(vehicleCaptor.capture());

        Vehicle capturedVehicle = vehicleCaptor.getValue();
        assertEquals(plate, capturedVehicle.getPlate());
        assertEquals(color, capturedVehicle.getColor());
        assertEquals(VehicleType.fromString(typeStr), capturedVehicle.getType());
        assertEquals(2, capturedVehicle.getFloor());
        assertEquals(List.of(1,2), capturedVehicle.getAllocatedSlots());
        assertTrue(capturedVehicle.getTicketId() > 0);

        Mockito.verify(dbService).insertTicket(anyInt(), eq(plate), eq(color), eq(typeStr), anyString(), eq(2));
    }

    @Test
    void shouldNotParkValidVehicleWhenAllFloorsAreFull() {
        // GIVEN
        String plate = "01-BGR-01";
        String color = "Turquoise";
        String typeStr = "Truck";

        Mockito.when(garage.isPlateAlreadyParked(plate)).thenReturn(false);
        Mockito.when(garage.getOccupiedSlotsByFloor(1)).thenReturn(Set.of(1,2,3,4,5,6,7,8,9,10));
        Mockito.when(garage.getOccupiedSlotsByFloor(2)).thenReturn(Set.of(1,2,3,4,5,6,7,8,9,10));

        // WHEN
        String result = floorService.park(plate, color, typeStr);

        // THEN
        assertEquals("Garage is full.",result);

        Mockito.verify(garage).isPlateAlreadyParked(plate);
        Mockito.verify(garage).getOccupiedSlotsByFloor(1);
        Mockito.verify(garage).getOccupiedSlotsByFloor(2);

        Mockito.verify(garage, never()).addVehicle(any(Vehicle.class));
        Mockito.verify(dbService, never()).insertTicket(anyInt(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    void shouldNotParkVehicleWhenPlateAlreadyParked() {
        // GIVEN
        String plate = "01-BGR-01";
        String color = "Turquoise";
        String typeStr = "Truck";

        Mockito.when(garage.isPlateAlreadyParked(plate)).thenReturn(true);

        // WHEN
        String result = floorService.park(plate, color, typeStr);

        // THEN
        assertEquals("This vehicle is already parked.",result);
        Mockito.verify(garage).isPlateAlreadyParked(plate);
        Mockito.verify(garage, never()).getOccupiedSlotsByFloor(anyInt());
        Mockito.verify(garage, never()).addVehicle(any(Vehicle.class));
        Mockito.verify(dbService, never()).insertTicket(anyInt(), eq(plate), eq(color), eq(typeStr), anyString(), eq(1));
    }
    @Test
    void shouldNotParkInvalidVehicleType() {
        // GIVEN
        String plate = "01-BGR-01";
        String color = "Turquoise";
        String typeStr = "Marti Scooter";

        Mockito.when(garage.isPlateAlreadyParked(plate)).thenReturn(false);

        // WHEN
        String result = floorService.park(plate, color, typeStr);

        // THEN
        assertEquals("Please select a valid vehicle type! (Car, Jeep, Truck)",result);
        Mockito.verify(garage).isPlateAlreadyParked(plate);
        Mockito.verify(garage, never()).getOccupiedSlotsByFloor(anyInt());
        Mockito.verify(garage, never()).addVehicle(any(Vehicle.class));
        Mockito.verify(dbService, never()).insertTicket(anyInt(), eq(plate), eq(color), eq(typeStr), anyString(), eq(1));
    }

    @Test
    void shouldLeaveVehicleWithValidId() {
        // GIVEN
        int ticketId = 1;
        Vehicle mockVehicle = new Vehicle(ticketId, "34-FB-1907", "Violet", "Car", List.of(1), 1);

        Mockito.when(garage.getVehicleByTicketId(ticketId)).thenReturn(mockVehicle);

        // WHEN
        String result = floorService.leave(ticketId);

        // THEN
        assertEquals("Goodbye and Drive Safe!! Ticket ID was: " + ticketId, result);

        ArgumentCaptor<Integer> getVehicleTicketIdCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(garage).getVehicleByTicketId(getVehicleTicketIdCaptor.capture());
        assertEquals(ticketId, getVehicleTicketIdCaptor.getValue());

        ArgumentCaptor<Integer> removeVehicleTicketIdCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(garage).removeVehicleByTicketId(removeVehicleTicketIdCaptor.capture());
        assertEquals(ticketId, removeVehicleTicketIdCaptor.getValue());

        ArgumentCaptor<Integer> deleteTicketIdCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(dbService).deleteTicket(deleteTicketIdCaptor.capture());
        assertEquals(ticketId, deleteTicketIdCaptor.getValue());
    }

    @Test
    void shouldNotLeaveVehicleWithInvalidId() {
        // GIVEN
        int ticketId = 2;

        Mockito.when(garage.getVehicleByTicketId(ticketId)).thenReturn(null);

        // WHEN
        String result = floorService.leave(ticketId);

        // THEN
        assertEquals("Ticket ID not found.", result);

        Mockito.verify(garage).getVehicleByTicketId(ticketId);
        Mockito.verify(garage, never()).removeVehicleByTicketId(anyInt());
        Mockito.verify(dbService, never()).deleteTicket(anyInt());
    }

    @Test
    void shouldReturnEmptyGarage() {
        // GIVEN
        List<Vehicle> emptyVehicleList = List.of();


        Mockito.when(garage.getVehiclesByFloor(1)).thenReturn(emptyVehicleList);
        Mockito.when(garage.getVehiclesByFloor(2)).thenReturn(emptyVehicleList);

        // WHEN
        String result = floorService.status();

        // THEN
        String expectedStatus = """
                Status:
                Floor 1:
                  No vehicles parked.
                Floor 2:
                  No vehicles parked.""";

        assertEquals(expectedStatus, result);

        Mockito.verify(garage).getVehiclesByFloor(1);
        Mockito.verify(garage).getVehiclesByFloor(2);
    }

    @Test
    void shouldReturnFloor1Only() {
        // GIVEN
        Vehicle vehicle1 = new Vehicle(1, "34-FB-1907", "Sky-Blue", "Car", List.of(1), 1);
        List<Vehicle> floor1Vehicles = List.of(vehicle1);
        List<Vehicle> floor2Vehicles = List.of();

        Mockito.when(garage.getVehiclesByFloor(1)).thenReturn(floor1Vehicles);
        Mockito.when(garage.getVehiclesByFloor(2)).thenReturn(floor2Vehicles);

        // WHEN
        String result = floorService.status();

        // THEN
        assertTrue(result.contains("Status:"));
        assertTrue(result.contains("Floor 1:"));
        assertTrue(result.contains("34-FB-1907"));
        assertTrue(result.contains("Sky-Blue"));
        assertTrue(result.contains("Floor 2:"));
        assertTrue(result.contains("No vehicles parked."));

        Mockito.verify(garage).getVehiclesByFloor(1);
        Mockito.verify(garage).getVehiclesByFloor(2);
    }

    @Test
    void shouldReturnFloor2Only() {
        // GIVEN
        Vehicle vehicle2 = new Vehicle(2, "34-FB-1907", "Blue", "Jeep", List.of(2, 3), 2);
        List<Vehicle> floor1Vehicles = List.of();
        List<Vehicle> floor2Vehicles = List.of(vehicle2);

        Mockito.when(garage.getVehiclesByFloor(1)).thenReturn(floor1Vehicles);
        Mockito.when(garage.getVehiclesByFloor(2)).thenReturn(floor2Vehicles);

        // WHEN
        String result = floorService.status();

        // THEN


        assertTrue(result.contains("Status:"));
        assertTrue(result.contains("Floor 1:"));
        assertTrue(result.contains("Floor 2:"));
        assertTrue(result.contains("34-FB-1907"));
        assertTrue(result.contains("Blue"));
        assertTrue(result.contains("No vehicles parked."));

        Mockito.verify(garage).getVehiclesByFloor(1);
        Mockito.verify(garage).getVehiclesByFloor(2);
    }


    @Test
    void shouldReturnBothFloors() {
        // GIVEN
        Vehicle vehicle1 = new Vehicle(1, "34-FB-1907", "Red", "Car", List.of(1), 1);
        Vehicle vehicle2 = new Vehicle(2, "01-CNR-01", "Blue", "Jeep", List.of(2, 3), 2);

        List<Vehicle> floor1Vehicles = List.of(vehicle1);
        List<Vehicle> floor2Vehicles = List.of(vehicle2);

        Mockito.when(garage.getVehiclesByFloor(1)).thenReturn(floor1Vehicles);
        Mockito.when(garage.getVehiclesByFloor(2)).thenReturn(floor2Vehicles);

        // WHEN
        String result = floorService.status();

        // THEN
        assertTrue(result.contains("Status:"));
        assertTrue(result.contains("Floor 1:"));
        assertTrue(result.contains("Floor 2:"));
        assertTrue(result.contains("34-FB-1907"));
        assertTrue(result.contains("01-CNR-01"));
        assertFalse(result.contains("No vehicles parked."));

        Mockito.verify(garage).getVehiclesByFloor(1);
        Mockito.verify(garage).getVehiclesByFloor(2);
    }

}