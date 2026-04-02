package com.doritech.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.ContractDocuments;

@Repository
public interface ContractDocumentsRepository 
        extends JpaRepository<ContractDocuments, Integer> {

}