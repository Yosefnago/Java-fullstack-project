package com.v1.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "clients")
@Data
public class Clients {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name" , nullable = false)
    private String clientName;

    @Column(name = "business_Owner", nullable = true)
    private String businessOwner;

    @Column(name = "tax_id", nullable = false, unique = true)
    private String taxId;

    @Column(name = "phone",nullable = false,unique = true)
    private String phone;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "accountant_id")
    private AccountantUser accountant;


    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


}
