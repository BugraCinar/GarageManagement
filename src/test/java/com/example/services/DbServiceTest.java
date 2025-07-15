package com.example.services;

import com.example.entity.FirstFloorTicket;
import com.example.entity.SecondFloorTicket;
import com.example.model.Vehicle;
import com.example.repository.FirstFloorTicketRepository;
import com.example.repository.SecondFloorTicketRepository;
import com.example.model.Garage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DbServiceTest {

    @Mock private FirstFloorTicketRepository firstRepo;
    @Mock private SecondFloorTicketRepository secondRepo;

    private Garage garage;
    private DbService dbService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        garage = new Garage();
        dbService = new DbService(firstRepo, secondRepo, garage);
    }

    @Test
    void testGetMaxTicketId() {
        when(firstRepo.findMaxTicketId()).thenReturn(Optional.of(5));
        when(secondRepo.findMaxTicketId()).thenReturn(Optional.of(7));
        assertEquals(7, dbService.getMaxTicketId());
    }

    @Test
    void testIsPlateAlreadyParked() {
        when(firstRepo.existsByPlate("01-CNR-01")).thenReturn(false);
        when(secondRepo.existsByPlate("01-CNR-01")).thenReturn(true);
        assertTrue(dbService.isPlateAlreadyParked("01-CNR-01"));
    }

    @Test
    void testInsertFirstFloorTicket() {
        dbService.insertFirstFloorTicket(1, "34-KFN-34", "Red", "Jeep", "1,2");
        verify(firstRepo).save(any(FirstFloorTicket.class));
    }

    @Test
    void testDeleteFirstFloorTicket() {
        dbService.deleteFirstFloorTicket(1);
        verify(firstRepo).deleteByTicketId(1);
    }

    @Test
    void testLoadVehiclesToGarage() {

        FirstFloorTicket firstTicket = new FirstFloorTicket(1, "34-ABC-34", "Red", "Car", "1");
        SecondFloorTicket secondTicket = new SecondFloorTicket(2, "01-BGR-01", "Blue", "Jeep", "1,2");

        when(firstRepo.findAll()).thenReturn(List.of(firstTicket));
        when(secondRepo.findAll()).thenReturn(List.of(secondTicket));


        dbService.loadVehiclesToGarage();


        Map<Integer, Vehicle> allVehicles = garage.getAllVehicles();
        assertEquals(2, allVehicles.size());

        Vehicle v1 = allVehicles.get(1);
        assertEquals("34-ABC-34", v1.getPlate());
        assertEquals(1, v1.getFloor());

        Vehicle v2 = allVehicles.get(2);
        assertEquals("01-BGR-01", v2.getPlate());
        assertEquals(2, v2.getFloor());
    }


}
