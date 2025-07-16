package com.example.converter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.entity.MyGarage;
import com.example.model.Vehicle;
import com.example.model.VehicleType;

class TicketToVehicleConverterTest {

    @Mock
    private MyGarage mockTicket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test

    void shouldConvertCar() {
        // GIVEN
        MyGarage ticket = new MyGarage(1, "34-CAR-34", "Red", "Car", "1", 1);

        // WHEN
        Vehicle vehicle = TicketToVehicleConverter.convert(ticket);

        // THEN
        assertNotNull(vehicle);
        assertEquals(1, vehicle.getTicketId());
        assertEquals("34-CAR-34", vehicle.getPlate());
        assertEquals("Red", vehicle.getColor());
        assertEquals(VehicleType.fromString("Car"), vehicle.getType());
        assertEquals(List.of(1), vehicle.getAllocatedSlots());
        assertEquals(1, vehicle.getFloor());
    }

    @Test
    void shouldConvertJeep() {
        // GIVEN
        MyGarage ticket = new MyGarage(2, "01-JEEP-01", "Blue", "Jeep", "2,3", 2);

        // WHEN
        Vehicle vehicle = TicketToVehicleConverter.convert(ticket);

        // THEN
        assertNotNull(vehicle);
        assertEquals(2, vehicle.getTicketId());
        assertEquals("01-JEEP-01", vehicle.getPlate());
        assertEquals("Blue", vehicle.getColor());
        assertEquals(VehicleType.fromString("Jeep"), vehicle.getType());
        assertEquals(List.of(2, 3), vehicle.getAllocatedSlots());
        assertEquals(2, vehicle.getFloor());
    }

    @Test

    void shouldConvertTruck() {
        // GIVEN
        MyGarage ticket = new MyGarage(3, "06-TRUCK-06", "White", "Truck", "4,5,6,7", 1);

        // WHEN
        Vehicle vehicle = TicketToVehicleConverter.convert(ticket);

        // THEN
        assertNotNull(vehicle);
        assertEquals(3, vehicle.getTicketId());
        assertEquals("06-TRUCK-06", vehicle.getPlate());
        assertEquals("White", vehicle.getColor());
        assertEquals(VehicleType.fromString("Truck"), vehicle.getType());
        assertEquals(List.of(4, 5, 6, 7), vehicle.getAllocatedSlots());
        assertEquals(1, vehicle.getFloor());
    }




    @Test

    void shouldConvertUsingMock() {
        // GIVEN
        Mockito.when(mockTicket.getTicketId()).thenReturn(1);
        Mockito.when(mockTicket.getPlate()).thenReturn("01-FB-1907");
        Mockito.when(mockTicket.getColor()).thenReturn("Turquoise");
        Mockito.when(mockTicket.getType()).thenReturn("Car");
        Mockito.when(mockTicket.getAllocatedSlots()).thenReturn("1");
        Mockito.when(mockTicket.getFloor()).thenReturn(1);

        // WHEN
        Vehicle vehicle = TicketToVehicleConverter.convert(mockTicket);

        // THEN
        assertNotNull(vehicle);
        assertEquals(1, vehicle.getTicketId());
        assertEquals("01-FB-1907", vehicle.getPlate());
        assertEquals("Turquoise", vehicle.getColor());
        assertEquals(VehicleType.fromString("Car"), vehicle.getType());
        assertEquals(List.of(1), vehicle.getAllocatedSlots());
        assertEquals(1, vehicle.getFloor());


        Mockito.verify(mockTicket).getTicketId();
        Mockito.verify(mockTicket).getPlate();
        Mockito.verify(mockTicket).getColor();
        Mockito.verify(mockTicket).getType();
        Mockito.verify(mockTicket).getAllocatedSlots();
        Mockito.verify(mockTicket).getFloor();
    }

}