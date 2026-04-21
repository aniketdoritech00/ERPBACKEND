package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.QuotationMaster;
@Repository
public interface QuotationMasterRepository extends JpaRepository<QuotationMaster, Integer> {

	boolean existsByQuotationCode(String quotationCode);

	boolean existsByQuotationCodeAndQuotationIdNot(String quotationCode, Integer id);

	Page<QuotationMaster> findAll(Specification<QuotationMaster> spec, Pageable pageable);
		long countByStatus(String string);

	List<QuotationMaster> findByContract_ContractId(Integer contractId);


}
