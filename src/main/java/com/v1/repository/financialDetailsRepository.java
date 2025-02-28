package com.v1.repository;

import com.v1.entity.FinancialDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface financialDetailsRepository extends JpaRepository<FinancialDetails,Long> {



}
