package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.QuotationDocument;

@Repository
public interface QuotationDocumentRepository extends JpaRepository<QuotationDocument, Integer> {

	List<QuotationDocument> findByQuotationMasterQuotationId(Integer quotationId);

	@Query("SELECT q FROM QuotationDocument q WHERE q.quotationMaster.quotationId IN :ids")
	List<QuotationDocument> findByQuotationIds(List<Integer> ids);

}