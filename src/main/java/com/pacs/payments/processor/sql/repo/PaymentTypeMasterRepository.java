package com.pacs.payments.processor.sql.repo;

import com.pacs.payments.processor.sql.entity.PaymentTypeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypeMasterRepository extends JpaRepository<PaymentTypeMaster, String> {

    PaymentTypeMaster getPaymentTypeMasterByPaymentType(String paymentType);
}
