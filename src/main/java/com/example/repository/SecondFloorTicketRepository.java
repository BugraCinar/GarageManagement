package com.example.repository;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.SecondFloorTicket;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondFloorTicketRepository extends JpaRepository<SecondFloorTicket, Integer> {

    @Query("SELECT COALESCE(MAX(s.ticketId), 0) FROM SecondFloorTicket s")
    Optional<Integer> findMaxTicketId();

    boolean existsByPlate(String plate);

    @Modifying
    @Transactional
    void deleteByTicketId(@Param("ticketId") int ticketId);

}