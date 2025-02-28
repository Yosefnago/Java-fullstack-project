package com.v1.service;


import com.v1.entity.ClientDocuments;
import com.v1.entity.Clients;
import com.v1.entity.FinancialDetails;
import com.v1.repository.ClientDocumentRepository;
import com.v1.repository.ClientRepository;
import com.v1.repository.financialDetailsRepository;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientsService {


    @Autowired
    ClientRepository clientRepository;

    @Autowired
    financialDetailsRepository fdRepository;

    @Autowired
    ClientDocumentRepository cdRepository;

    public Clients saveClient(Clients client) {

        if (clientRepository.existsByTaxId(client.getTaxId())) {
            Notification.show("תיק קיים על מספר זהות זה", 3000, Notification.Position.TOP_CENTER);
            return null;
        }

        return clientRepository.save(client);
    }
    public FinancialDetails saveFinancialDetails(FinancialDetails financialDetails) {
        return fdRepository.save(financialDetails);
    }
    public ClientDocuments saveClientDocuments(ClientDocuments clientDocuments) {
        return cdRepository.save(clientDocuments);
    }
    public List<Clients> getClientsByAccountantId(Long accountantId) {
        return clientRepository.findByAccountantId(accountantId);
    }
}
