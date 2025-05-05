package com.pacs.payments.processor.nosql.repo;

import com.pacs.payments.processor.nosql.entity.PacsPayments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoPacsPaymentsRepository extends MongoRepository<PacsPayments, String> {
}
