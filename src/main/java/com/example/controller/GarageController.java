package com.example.controller;

import com.example.services.FloorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/garage")
@RequiredArgsConstructor
public class GarageController {

    private final FloorService floorService;

    @PostMapping("/park")
    public String parkVehicle(
            @RequestParam String plate,
            @RequestParam String color,
            @RequestParam String type
    ) {
        return floorService.park(plate, color, type);
    }

    @PostMapping("/leave")
    public String leaveVehicle(@RequestParam int ticketId) {
        return floorService.leave(ticketId);
    }

    @GetMapping("/status")
    public String garageStatus() {
        return floorService.status();
    }
}