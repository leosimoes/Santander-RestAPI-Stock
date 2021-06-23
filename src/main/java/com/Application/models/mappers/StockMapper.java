package com.Application.models.mappers;

import com.Application.models.dtos.StockDTO;
import com.Application.models.entities.Stock;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockMapper {

    public StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    Stock toModel(StockDTO stockDTO);

    StockDTO toDTO(Stock stock);

}
