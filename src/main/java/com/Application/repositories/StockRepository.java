package com.Application.repositories;

import com.Application.models.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findAll();

    List<Stock> findByDate(LocalDate date);

    Optional<Stock> findByNameAndDate(String name, LocalDate date);

    Optional<Stock> findByIdAndNameAndDate(Long id, String name, LocalDate date);

}
