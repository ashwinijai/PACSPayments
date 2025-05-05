package com.pacs.payments.processor.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentProcessingResponse {
    private String msgId;
    private String isProcessed;
    private String responseMsg;
}
