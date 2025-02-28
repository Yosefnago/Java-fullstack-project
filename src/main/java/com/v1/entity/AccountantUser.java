package com.v1.entity;




import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "accountant_users")
@Data
public class AccountantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password" , nullable = false, unique = true)
    private String password;

    @Column(name = "email" , nullable = false,unique = true)
    private String emailAddress;

    @Column(name = "phone" , nullable = false,unique = true)
    private String phone;

    @Column(name = "license_number",nullable = false,unique = true)
    private String licenseNumber;


    @OneToMany(mappedBy = "accountant", cascade = CascadeType.ALL)
    private List<Clients> client = new ArrayList<>();



}
