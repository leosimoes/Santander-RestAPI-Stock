package com.Application.controllers;

import com.Application.exceptions.ExceptionResponse;
import com.Application.exceptions.ExceptionsHandler;
import com.Application.exceptions.NoRegisteredStockException;
import com.Application.exceptions.StockNotFoundException;
import com.Application.models.dtos.StockDTO;
import com.Application.models.entities.Stock;
import com.Application.services.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import static com.Application.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(MockitoExtension.class)
public class StockControllerTest {

    private String URI_API = "/api/v1/stock/";

    private MockMvc mockMvc;

    private Stock stock;

    private StockDTO stockDTO;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ExceptionResponse exceptionResponseStockNotFound;

    private ExceptionResponse exceptionResponseNoRegisteredStock;

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockController stockController;

    @BeforeEach
    void setUp() {

      mockMvc = MockMvcBuilders.standaloneSetup(stockController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new ExceptionsHandler())
                .build();

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

        exceptionResponseStockNotFound = new ExceptionResponse(new StockNotFoundException().getMessage());

        exceptionResponseNoRegisteredStock = new ExceptionResponse(new NoRegisteredStockException().getMessage());

    }

    @Test
    void testGetStockOneSucess() throws Exception {
        final Long ID = 1L;

        when(stockService.findById(ID)).thenReturn(stockDTO);

        mockMvc.perform(get(URI_API + ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(stockDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(stockDTO.getName())))
                .andExpect(jsonPath("$.price", is(stockDTO.getPrice())))
                .andExpect(jsonPath("$.variation", is(stockDTO.getVariation())))
                .andExpect(jsonPath("$.date", is(stockDTO.getDate().format(formatter))));

        verify(stockService, times(1)).findById(1L);
    }

    @Test
    void testGetStockOneFail() throws Exception {
        final Long ID = 1L;

        when(stockService.findById(ID)).thenThrow(new StockNotFoundException());

        mockMvc.perform(get(URI_API + ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(exceptionResponseStockNotFound.getMessage())));

        verify(stockService, times(1)).findById(1L);
    }

    @Test
    void testGetAllStockSucess() throws Exception {
        when(stockService.findAll()).thenReturn(Arrays.asList(stockDTO));

        mockMvc.perform(get(URI_API)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(stockDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(stockDTO.getName())))
                .andExpect(jsonPath("$[0].price", is(stockDTO.getPrice())))
                .andExpect(jsonPath("$[0].variation", is(stockDTO.getVariation())))
                .andExpect(jsonPath("$[0].date", is(stockDTO.getDate().format(formatter))));

        verify(stockService, times(1)).findAll();
    }

    @Test
    void testGetAllStockFail() throws Exception {
        when(stockService.findAll()).thenThrow(new NoRegisteredStockException());

        mockMvc.perform(get(URI_API)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(stockService, times(1)).findAll();
    }


    @Test
    void testCreateStockSucess() throws Exception {
        final Long ID = 1L;

        when(stockService.createStock(stockDTO)).thenReturn(stockDTO);

        mockMvc.perform(post(URI_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(stockDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(stockDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(stockDTO.getName())))
                .andExpect(jsonPath("$.price", is(stockDTO.getPrice())))
                .andExpect(jsonPath("$.variation", is(stockDTO.getVariation())))
                .andExpect(jsonPath("$.date", is(stockDTO.getDate().format(formatter))));

        verify(stockService, times(1)).createStock(any(StockDTO.class));
    }

    @Test
    void testUpdateStockSucess() throws Exception {
        final Long ID = 1L;

        when(stockService.updateStock(stockDTO)).thenReturn(stockDTO);

        mockMvc.perform(put(URI_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(stockDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(stockDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(stockDTO.getName())))
                .andExpect(jsonPath("$.price", is(stockDTO.getPrice())))
                .andExpect(jsonPath("$.variation", is(stockDTO.getVariation())))
                .andExpect(jsonPath("$.date", is(stockDTO.getDate().format(formatter))));

        verify(stockService, times(1)).updateStock(any(StockDTO.class));
    }

    @Test
    void testUpdateStockFail() throws Exception {
        final Long ID = 1L;

        when(stockService.updateStock(stockDTO)).thenThrow(new StockNotFoundException());

        mockMvc.perform(put(URI_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(stockDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(exceptionResponseStockNotFound.getMessage())));

        verify(stockService, times(1)).updateStock(any(StockDTO.class));
    }


    @Test
    void testDeleteStockSucess() throws Exception {
        final Long ID = 1L;

        when(stockService.deleteStock(ID)).thenReturn(stockDTO);

        mockMvc.perform(delete(URI_API + ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(stockDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(stockDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(stockDTO.getName())))
                .andExpect(jsonPath("$.price", is(stockDTO.getPrice())))
                .andExpect(jsonPath("$.variation", is(stockDTO.getVariation())))
                .andExpect(jsonPath("$.date", is(stockDTO.getDate().format(formatter))));

        verify(stockService, times(1)).deleteStock(anyLong());
    }


    @Test
    void testDeleteStockFail() throws Exception {
        final Long ID = 1L;

        doThrow(new StockNotFoundException()).when(stockService).deleteStock(ID);

        mockMvc.perform(delete(URI_API + ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(exceptionResponseStockNotFound.getMessage())));

        verify(stockService, times(1)).deleteStock(anyLong());
    }

}
