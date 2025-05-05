package com.pacs.payments.processor.sql.entity;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="PACS_PAYMENTS")
@Data
@NoArgsConstructor
public class PacsPayments {

    @Id
    @Column(name="MSG_ID")
    private String msgId;

    @Column(name="PAYMENT_TYPE")
    private String paymentType;

    @Column(name="PAYMNET_MSG")
    @Lob
    private String paymentMsg;

    @Column(name="IS_PROCESSED")
    private String isProcessed;

}
