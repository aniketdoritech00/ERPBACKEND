package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.QuotationDetail;

@Repository
public interface QuotationDetailRepository extends JpaRepository<QuotationDetail, Integer> {

	boolean existsByQuotationMasterQuotationIdAndItemIdAndSiteIdAndParentItemIdAndQuotationDetailIdNot(
			Integer quotationId, Integer itemId, Integer siteId, Integer parentItemId, Integer quotationDetailId);

	boolean existsByQuotationMasterQuotationIdAndItemIdAndSiteIdAndParentItemId(Integer quotationId, Integer itemId,
			Integer siteId, Integer parentItemId);

	boolean existsByItemId(Integer parentItemId);

	List<QuotationDetail> findByQuotationMasterQuotationIdAndParentItemId(Integer quotationId, Integer itemId);

	List<QuotationDetail> findByQuotationMasterQuotationIdAndParentItemIdIsNull(Integer quotationId);

	QuotationDetail findByItemId(Integer parentItemId);

	Page<QuotationDetail> findByParentItemIdIsNull(Pageable pageable);
}