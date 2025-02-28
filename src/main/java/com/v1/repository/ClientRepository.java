package com.v1.repository;


import com.v1.entity.Clients;
import org.hibernate.mapping.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Clients, Long> {

    boolean existsByTaxId(String taxId);

    List<Clients> findByAccountantId(Long accountantId);

}
