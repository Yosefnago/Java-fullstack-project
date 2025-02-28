package com.v1.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_details")
@Data
public class FinancialDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private Clients client;

    @Column(name = "business_start_date", nullable = false)
    private LocalDate businessStartDate;

    @Column(name = "business_Activity" ,nullable = false)
    private String businessActivity;

    @Column(name = "bank_Number_Account", nullable = false)
    private String bankNumberAccount;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "bank_number",nullable = false)
    private String bankNumber;



}
