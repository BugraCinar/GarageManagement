package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "first_floor2")
public class FirstFloorTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ticket_id")
    private Integer ticketId;

    @Column(name = "plate")
    private String plate;

    @Column(name = "color")
    private String color;

    @Column(name = "type")
    private String type;

    @Column(name = "allocated_slots")
    private String allocatedSlots;

    public FirstFloorTicket() {}

    public FirstFloorTicket(Integer ticketId, String plate, String color, String type, String allocatedSlots) {
        this.ticketId = ticketId;
        this.plate = plate;
        this.color = color;
        this.type = type;
        this.allocatedSlots = allocatedSlots;
    }
}