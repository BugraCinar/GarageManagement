package com.example.repository;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.FirstFloorTicket;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FirstFloorTicketRepository extends JpaRepository<FirstFloorTicket, Integer> {

    @Query("SELECT COALESCE(MAX(f.ticketId), 0) FROM FirstFloorTicket f")
    Optional<Integer> findMaxTicketId();

    boolean existsByPlate(String plate);



    void deleteByTicketId(@Param("ticketId") int ticketId);
}