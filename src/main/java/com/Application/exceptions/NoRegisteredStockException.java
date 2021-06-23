package com.Application.exceptions;

import com.Application.utils.MessageUtils;

public class NoRegisteredStockException extends RuntimeException{

    public NoRegisteredStockException(){
        super(MessageUtils.NO_RECORDS_FOUND);
    }


}
