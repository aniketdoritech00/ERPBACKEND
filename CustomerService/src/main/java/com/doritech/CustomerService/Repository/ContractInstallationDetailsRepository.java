package com.doritech.CustomerService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.ContractInstallationDetails;

@Repository
public interface ContractInstallationDetailsRepository extends JpaRepository<ContractInstallationDetails, Long> {

	Optional<ContractInstallationDetails> findByContractContractId(Integer contractId);

}