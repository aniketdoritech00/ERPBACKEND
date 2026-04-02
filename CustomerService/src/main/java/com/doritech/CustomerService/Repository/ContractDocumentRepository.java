package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doritech.CustomerService.Entity.ContractDocuments;

public interface ContractDocumentRepository extends JpaRepository<ContractDocuments, Integer> {

	List<ContractDocuments> findByContractId(Integer contractId);

}