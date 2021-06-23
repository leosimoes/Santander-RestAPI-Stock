package com.Application.services;

import com.Application.exceptions.NoRegisteredStockException;
import com.Application.exceptions.StockNotFoundException;
import com.Application.models.dtos.StockDTO;
import com.Application.models.entities.Stock;
import com.Application.models.mappers.StockMapper;
import com.Application.repositories.StockRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;



import static org.junit.Assert.*;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    private Stock stock;

    private StockDTO stockDTO;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private StockMapper stockMapper = StockMapper.INSTANCE;

    @BeforeEach
    public void setUp() {
        stock = Stock.builder()
                .id(1L)
                .name("Test")
                .price(10D)
                .variation(0.5D)
                .date(LocalDate.of(2021,06,22))
                .build();

        stockDTO = StockDTO.builder()
                .id(1L)
                .name("Test")
                .price(10D)
                .variation(0.5D)
                .date(LocalDate.of(2021,06,22))
                .build();
    }

    @Test
    public void findOneSucess(){
        Long ID = 1L;

        when(stockRepository.findById(ID)).thenReturn(Optional.of(new Stock()));

        stockService.findById(ID);

        verify(stockRepository, times(1)).findById(ID);
    }

    @Test
    public void findOneFail(){
        Long ID = 1L;

        when(stockRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, () -> stockService.findById(ID));
        verify(stockRepository, times(1)).findById(ID);
    }

    @Test
    public void findAllSucess(){
        when(stockRepository.findAll()).thenReturn(Collections.singletonList(new Stock()));

        stockService.findAll();

        verify(stockRepository, times(1)).findAll();
    }


    @Test
    public void findAllFail(){
        when(stockRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(NoRegisteredStockException.class, () -> stockService.findAll());

        verify(stockRepository, times(1)).findAll();
    }


    @Test
    public void createSucess(){
        Long ID = 1L;

        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDTO stockDTOUpdated = stockService.createStock(stockDTO);

        assertEquals(stockDTO, stockDTOUpdated);

        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    public void updateSucess(){
        Long ID = 1L;

        when(stockRepository.existsById(ID)).thenReturn(true);
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDTO stockDTOUpdated = stockService.updateStock(stockDTO);

        assertEquals(stockDTO, stockDTOUpdated);

        verify(stockRepository, times(1)).existsById(ID);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    public void updateFail(){
        Long ID = 1L;

        when(stockRepository.existsById(ID)).thenReturn(false);

        assertThrows(StockNotFoundException.class, () -> stockService.updateStock(stockDTO));

        verify(stockRepository, times(1)).existsById(ID);
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    public void deleteSucess(){
        Long ID = 1L;

        when(stockRepository.existsById(ID)).thenReturn(true);
        when(stockRepository.findById(ID)).thenReturn(Optional.of(stock));
        doNothing().when(stockRepository).deleteById(ID);

        stockService.deleteStock(ID);

        verify(stockRepository, times(1)).existsById(ID);
        verify(stockRepository, times(1)).findById(ID);
        verify(stockRepository, times(1)).deleteById(ID);

    }

    @Test
    public void deleteFail(){
        Long ID = 1L;

        when(stockRepository.existsById(ID)).thenReturn(false);

        assertThrows(StockNotFoundException.class, () -> stockService.deleteStock(ID));

        verify(stockRepository, times(1)).existsById(ID);
        verify(stockRepository, never()).deleteById(ID);
    }
}
