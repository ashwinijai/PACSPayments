package com.pacs.payments.processor.service;

import com.pacs.payments.processor.model.PaymentProcessingResponse;
import com.pacs.payments.processor.nosql.repo.MongoPacsPaymentsRepository;
import com.pacs.payments.processor.sql.entity.PacsPayments;
import com.pacs.payments.processor.sql.entity.PaymentTypeMaster;
import com.pacs.payments.processor.sql.repo.PacsPaymentsRepository;
import com.pacs.payments.processor.sql.repo.PaymentTypeMasterRepository;
import com.pacs.payments.processor.xsd.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacsPaymentsService {
    @Autowired
    PacsPaymentsRepository paymentsRepository;
    @Autowired
    MongoPacsPaymentsRepository mongoPaymentsRepository;

    @Autowired
    PaymentTypeMasterRepository paymentTypeMasterRepository;

    public List<PaymentProcessingResponse> processPayments() {
        List<PaymentProcessingResponse> responseList = new ArrayList<>();
        List<PacsPayments> pacsPaymentsList = paymentsRepository.getAllUnProcessedPayments();
        if (!CollectionUtils.isEmpty(pacsPaymentsList)) {
            responseList = pacsPaymentsList.stream().map(p -> {
                PaymentTypeMaster paymentTypeMaster = paymentTypeMasterRepository.getPaymentTypeMasterByPaymentType(p.getPaymentType());
                PaymentProcessingResponse paymentResponse = PaymentProcessingResponse.builder().msgId(p.getMsgId()).build();
                if (null != paymentTypeMaster && null != paymentTypeMaster.getPaymentXsd()) {
                    try {
                        if (validateXMLSchema(paymentTypeMaster.getPaymentXsd(), p.getPaymentMsg())) {
                            JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
                            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                            Document document = (Document) jaxbUnmarshaller.unmarshal(new StringReader(p.getPaymentMsg()));
                            System.out.println("Payment Msg id - " + document.getCstmrCdtTrfInitn().getGrpHdr().getMsgId());
                            com.pacs.payments.processor.nosql.entity.PacsPayments payment = new com.pacs.payments.processor.nosql.entity.PacsPayments();
                            payment.setPaymentMsg(document.getCstmrCdtTrfInitn().getGrpHdr().getMsgId());
                            payment.setPaymentMsg(document.toString());
                            mongoPaymentsRepository.save(payment);
                            paymentResponse.setIsProcessed("Y");
                            paymentResponse.setResponseMsg("XSD and XML validation successful. Payment msg stored to mongo");
                            p.setIsProcessed("Y");
                        }
                    } catch (Exception e) {
                        paymentResponse.setIsProcessed("N");
                        paymentResponse.setResponseMsg(e.getMessage());
                        p.setIsProcessed("N");
                    }
                } else {
                    paymentResponse.setIsProcessed("N");
                    paymentResponse.setResponseMsg("No valid XSD found for payment type");
                    p.setIsProcessed("N");
                }
                paymentsRepository.save(p);
                return paymentResponse;
            }).collect(Collectors.toList());
        }

        return responseList;

    }

    public static boolean validateXMLSchema(String xsdVal, String xmlVal) throws IOException, SAXException {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new StringReader(xsdVal)));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlVal)));
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
            throw e;
        } catch (SAXException e1) {
            System.out.println("SAX Exception: " + e1.getMessage());
            throw e1;
        }
        return true;

    }
}
