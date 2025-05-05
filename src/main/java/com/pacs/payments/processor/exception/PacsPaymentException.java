package com.pacs.payments.processor.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PacsPaymentException extends RuntimeException{

    private int errorCode;
    private String errorMessage;
}
