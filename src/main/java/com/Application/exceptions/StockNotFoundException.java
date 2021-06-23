package com.Application.exceptions;

import com.Application.utils.MessageUtils;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(){
        super(MessageUtils.STOCK_ALREADY_EXISTS);
    }
}
