package com.Application.controllers;

import com.Application.models.dtos.StockDTO;
import com.Application.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/stock")
public class StockController {

    private StockService stockService;

    @Autowired
    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockDTO>> getAllStock(){
        return ResponseEntity.ok(this.stockService.findAll());
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> getStock(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.stockService.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> createStock(@RequestBody StockDTO stockDTO){
        StockDTO stockDTOSaved = this.stockService.createStock(stockDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(stockDTOSaved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> updateStock(@RequestBody StockDTO dto){
        return ResponseEntity.ok(this.stockService.updateStock(dto));
    }

    @DeleteMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> deleteStock(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.stockService.deleteStock(id));
    }

    @GetMapping(value="/today", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockDTO>> findByToday(){
        return ResponseEntity.ok(this.stockService.findByToday());
    }
}
