package com.pacs.payments.processor.sql.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="PAYMENT_TYPE_MASTER")
@Data
@NoArgsConstructor
public class PaymentTypeMaster {

    @Id
    @Column(name="PAYMENT_TYPE")
    private String paymentType;

    @Column(name="PAYMENT_XSD")
    @Lob
    private String paymentXsd;
}
