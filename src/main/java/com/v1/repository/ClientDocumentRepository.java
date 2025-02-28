package com.v1.repository;

import com.v1.entity.ClientDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDocumentRepository extends JpaRepository<ClientDocuments,Long> {
}
