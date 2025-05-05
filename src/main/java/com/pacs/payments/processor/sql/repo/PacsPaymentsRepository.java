package com.pacs.payments.processor.sql.repo;

import com.pacs.payments.processor.sql.entity.PacsPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacsPaymentsRepository extends JpaRepository<PacsPayments, String> {

    @Query("select p from PacsPayments p where p.isProcessed <> 'Y'")
    List<PacsPayments> getAllUnProcessedPayments();
}
