package com.pacs.payments.processor.controller;

import com.pacs.payments.processor.model.PaymentProcessingResponse;
import com.pacs.payments.processor.service.PacsPaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pacsPayments")
public class PacsPaymentsController {

    @Autowired
    PacsPaymentsService pacsPaymentsService;

    @GetMapping("/process")
    public ResponseEntity<List<PaymentProcessingResponse>> processPayments() {
        return new ResponseEntity<>(pacsPaymentsService.processPayments(), HttpStatus.OK);
    }
}
