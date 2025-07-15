package com.example.converter;

import com.example.entity.FirstFloorTicket;
import com.example.entity.SecondFloorTicket;
import com.example.model.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketToVehicleConverterTest {

    @Test
    void testConvertFirstFloorTicket() {
        FirstFloorTicket ticket = new FirstFloorTicket(1, "01-CNR-01", "Turquoise", "Car", "1");
        Vehicle vehicle = TicketToVehicleConverter.convert(ticket, 1);

        assertEquals(1, vehicle.getTicketId());
        assertEquals("01-CNR-01", vehicle.getPlate());
        assertEquals(1, vehicle.getAllocatedSlots().size());
        assertEquals(1, vehicle.getFloor());
    }

    @Test
    void testConvertSecondFloorTicket() {
        SecondFloorTicket ticket = new SecondFloorTicket(2, "34-FB-1907", "Dark-Blue", "Truck", "3,4,5,6");
        Vehicle vehicle = TicketToVehicleConverter.convert(ticket, 2);

        assertEquals("34-FB-1907", vehicle.getPlate());
        assertEquals(4, vehicle.getAllocatedSlots().size());
        assertEquals(2, vehicle.getFloor());

    }

    @Test
    void testConvertInvalidObject() {
        assertThrows(IllegalArgumentException.class, () -> {
            TicketToVehicleConverter.convert("tenet", 1);
        });
    }
}