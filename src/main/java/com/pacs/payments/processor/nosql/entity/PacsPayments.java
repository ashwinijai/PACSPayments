package com.pacs.payments.processor.nosql.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "PacsPayments")
@Data
public class PacsPayments {

    @Id
    private String msgId;
    private String paymentMsg;
}