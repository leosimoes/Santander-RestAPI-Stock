package com.Application.services;

import com.Application.exceptions.NoRegisteredStockException;
import com.Application.exceptions.StockNotFoundException;
import com.Application.models.dtos.StockDTO;
import com.Application.models.entities.Stock;
import com.Application.models.mappers.StockMapper;
import com.Application.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockMapper stockMapper = StockMapper.INSTANCE;

    private StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    @Transactional(readOnly = true)
    public List<StockDTO> findAll() {
        List<Stock> stockList = this.stockRepository.findAll();

        if(stockList.isEmpty()){
            throw new NoRegisteredStockException();
        }

        List<StockDTO> stockDTOList = stockList
                .stream()
                .map(stockMapper::toDTO)
                .collect(Collectors.toList());

        return stockDTOList;
    }

    @Transactional(readOnly = true)
    public StockDTO findById(Long id) {
        Optional<Stock> stockOpt = this.stockRepository.findById(id);

        if(stockOpt.isPresent()){
            StockDTO stockDTO = stockMapper.toDTO(stockOpt.get());
            return stockDTO;
        }

        throw new StockNotFoundException();
    }

    @Transactional
    public StockDTO createStock(StockDTO stockDTO){
        Stock stock = stockMapper.toModel(stockDTO);
        Stock stockSaved = this.stockRepository.save(stock);

        return stockMapper.toDTO(stockSaved);
    }

    @Transactional
    public StockDTO updateStock(StockDTO stockDTO) {
        verifyIfExistsById(stockDTO.getId());

        Stock stockToUpdate = stockMapper.toModel(stockDTO);
        Stock stockUpdated = this.stockRepository.save(stockToUpdate);

        return stockMapper.toDTO(stockUpdated);
    }

    @Transactional
    public StockDTO deleteStock(Long id) {
        verifyIfExistsById(id);

        Stock stockDeleted = this.stockRepository.findById(id).get();
        StockDTO stockDTODeleted = stockMapper.toDTO(stockDeleted);
        this.stockRepository.deleteById(id);

        return stockDTODeleted;
    }

    @Transactional(readOnly = true)
    public List<StockDTO> findByToday() {
        List<Stock> stockList = this.stockRepository.findByDate(LocalDate.now());

        if(stockList.isEmpty()){
            throw new NoRegisteredStockException();
        }

        List<StockDTO> stockDTOList = stockList
                .stream()
                .map(stockMapper::toDTO)
                .collect(Collectors.toList());

        return stockDTOList;
    }

    private void verifyIfExistsById(Long id){
        if(!this.stockRepository.existsById(id)) {
            throw new StockNotFoundException();
        }
    }

}
