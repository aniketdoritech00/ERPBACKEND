package com.doritech.CustomerService.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Projection.CustomerSummaryProjection;
import com.doritech.CustomerService.Projection.CustomerWithContactProjection;

@Repository
public interface CustomerMasterRepository
		extends
		JpaRepository<CustomerMasterEntity, Integer>,
		JpaSpecificationExecutor<CustomerMasterEntity> {

	Optional<CustomerMasterEntity> findByCustomerCodeIgnoreCase(
			String customerCode);

	List<CustomerMasterEntity> findByCustomerNameIgnoreCase(
			String customerName);

	List<CustomerMasterEntity> findByIsActiveIgnoreCase(String status);

	CustomerMasterEntity findByCustomerId(Integer customerId);

	List<CustomerMasterEntity> findByCustomerNameContainingIgnoreCase(
			String customerName);

	@Query("""
			SELECT c
			FROM CustomerMasterEntity c
			WHERE c.parentId IS NULL
			AND c.isActive = 'Y'
			""")
	List<CustomerMasterEntity> findAllActiveParentCustomers();

	@Query(value = "SELECT COUNT(*) FROM customer_master", nativeQuery = true)
	long countAllCustomers();

	@Query(value = """
			SELECT
			    cm.customer_id        AS customerId,
			    cm.customer_name      AS customerName,
			    cm.customer_code      AS customerCode,
			    cm.org_id             AS orgId,
			    cm.comp_id            AS compId,
				cm.ifsc               AS ifsc,
			    cm.address            AS address,
			    cm.city               AS city,
			    cm.district			  AS district,
			    cm.state              AS state,
			    cm.country            AS country,
			    cm.postal_code        AS postalCode,
			    cm.gstin              AS gstin,
			    cm.parent_id          AS parentId,
			    cm.hierarchy_level_id AS hierarchyLevelId,
			    cm.is_active          AS isActive,
			    cm.created_by         AS createdBy,
			    cm.created_on         AS createdOn,
			    cm.modified_by        AS modifiedBy,
			    cm.modified_on        AS modifiedOn,
			    co.org_name           AS orgName,
			    comp.comp_name        AS companyName,
			    comp.comp_code        AS companyCode,
			    hl.level_name         AS hierarchyName
			FROM customer_master cm
			LEFT JOIN customer_organization co   ON co.org_id            = cm.org_id
			LEFT JOIN comp_master           comp ON comp.comp_id          = cm.comp_id
			LEFT JOIN hierarchy_level       hl   ON hl.hierarchy_level_id = cm.hierarchy_level_id
			""", countQuery = "SELECT COUNT(*) FROM customer_master", nativeQuery = true)
	List<CustomerSummaryProjection> findAllCustomersSummary(Pageable pageable);

	@Query(value = """
			SELECT
			    cm.customer_id        AS customerId,
			    cm.customer_name      AS customerName,
			    cm.customer_code      AS customerCode,
			    cm.org_id             AS orgId,
			    cm.comp_id            AS compId,
				cm.ifsc               AS ifsc,
			    cm.address            AS address,
			    cm.city               AS city,
			    cm.district			  AS district,
			    cm.state              AS state,
			    cm.country            AS country,
			    cm.postal_code        AS postalCode,
			    cm.gstin              AS gstin,
			    cm.parent_id          AS parentId,
			    cm.hierarchy_level_id AS hierarchyLevelId,
			    cm.is_active          AS isActive,
			    cm.created_by         AS createdBy,
			    cm.created_on         AS createdOn,
			    cm.modified_by        AS modifiedBy,
			    cm.modified_on        AS modifiedOn,
			    co.org_name           AS orgName,
			    comp.comp_name        AS companyName,
			    comp.comp_code        AS companyCode,
			    hl.level_name         AS hierarchyName,
			    cc.cust_cont_id       AS custContId,
			    cc.contact_person     AS contactPerson,
			    cc.email              AS email,
			    cc.phone              AS phone,
			    cc.designation        AS designation,
			    cc.role               AS role,
			    cc.department         AS department,
			    cc.is_active          AS contactIsActive,
			    cc.created_on         AS contactCreatedOn,
			    cc.modified_on        AS contactModifiedOn,
			    cc.created_by         AS contactCreatedBy,
			    cc.modified_by        AS contactModifiedBy,
			    ce.cust_entity_id     AS custEntityId,
			    ce.entity_type        AS entityType,
			    ce.is_active          AS entityTypeIsActive
			FROM customer_master cm
			LEFT JOIN customer_organization co   ON co.org_id            = cm.org_id
			LEFT JOIN comp_master           comp ON comp.comp_id          = cm.comp_id
			LEFT JOIN hierarchy_level       hl   ON hl.hierarchy_level_id = cm.hierarchy_level_id
			LEFT JOIN customer_contact      cc   ON cc.customer_id        = cm.customer_id
			LEFT JOIN customer_entity_type  ce   ON ce.customer_id        = cm.customer_id
			WHERE cm.customer_id = :customerId
			""", nativeQuery = true)
	List<CustomerWithContactProjection> findCustomerWithDetailsById(
			@Param("customerId") Integer customerId);

	@Query(value = """
			SELECT
			    cm.customer_id        AS customerId,
			    cm.customer_name      AS customerName,
			    cm.customer_code      AS customerCode,
			    cm.org_id             AS orgId,
			    cm.comp_id            AS compId,
				cm.ifsc               AS ifsc,
			    cm.address            AS address,
			    cm.city               AS city,
			    cm.district           AS district,
			    cm.state              AS state,
			    cm.country            AS country,
			    cm.postal_code        AS postalCode,
			    cm.gstin              AS gstin,
			    cm.parent_id          AS parentId,
			    cm.hierarchy_level_id AS hierarchyLevelId,
			    cm.is_active          AS isActive,
			    cm.created_by         AS createdBy,
			    cm.created_on         AS createdOn,
			    cm.modified_by        AS modifiedBy,
			    cm.modified_on        AS modifiedOn,
			    co.org_name           AS orgName,
			    comp.comp_name        AS companyName,
			    comp.comp_code        AS companyCode,
			    hl.level_name         AS hierarchyName,
			    cc.cust_cont_id       AS custContId,
			    cc.contact_person     AS contactPerson,
			    cc.email              AS email,
			    cc.phone              AS phone,
			    cc.designation        AS designation,
			    cc.role               AS role,
			    cc.department         AS department,
			    cc.is_active          AS contactIsActive,
			    cc.created_on         AS contactCreatedOn,
			    cc.modified_on        AS contactModifiedOn,
			    cc.created_by         AS contactCreatedBy,
			    cc.modified_by        AS contactModifiedBy,
			    ce.cust_entity_id     AS custEntityId,
			    ce.entity_type        AS entityType,
			    ce.is_active          AS entityTypeIsActive
			FROM customer_master cm
			LEFT JOIN customer_organization co   ON co.org_id            = cm.org_id
			LEFT JOIN comp_master           comp ON comp.comp_id          = cm.comp_id
			LEFT JOIN hierarchy_level       hl   ON hl.hierarchy_level_id = cm.hierarchy_level_id
			LEFT JOIN customer_contact      cc   ON cc.customer_id        = cm.customer_id
			LEFT JOIN customer_entity_type  ce   ON ce.customer_id        = cm.customer_id
			WHERE
			    (:name       IS NULL OR LOWER(cm.customer_name) LIKE LOWER(CONCAT('%', :name, '%')))
			    AND (:status     IS NULL OR cm.is_active   = :status)
			    AND (:entityType IS NULL OR ce.entity_type = :entityType)
			ORDER BY cm.customer_id
			""", nativeQuery = true)
	List<CustomerWithContactProjection> findCustomersWithDetailsByFilter(
			@Param("name") String name, @Param("status") String status,
			@Param("entityType") String entityType);

	@Query("""
			    SELECT c.customerId AS customerId,
			           c.customerName AS customerName,
			           c.customerCode AS customerCode
			    FROM CustomerMasterEntity  c
			    WHERE c.orgId = :orgId
			    AND c.hierarchyLevelId >= :levelId
			""")
	List<CustomerSummaryProjection> findByOrgAndHierarchy(
			@Param("orgId") Integer orgId, @Param("levelId") Integer levelId);

	long countByIsActive(String string);

	@Query(value = """
			SELECT
			    cm.customer_name,
			    cm.customer_code,
			    cm.district,
			    cm.address,
			    hl.level_name,
			    (
			        SELECT cc.email
			        FROM customer_contact cc
			        WHERE cc.customer_id = cm.customer_id
			          AND cc.is_active = 'Y'
			        ORDER BY cc.cust_cont_id ASC
			        LIMIT 1
			    ) AS first_contact_email
			FROM customer_master cm
			LEFT JOIN hierarchy_level hl
			    ON cm.hierarchy_level_id = hl.hierarchy_level_id
			WHERE cm.customer_id = :customerId
			  AND cm.is_active = 'Y'
			""", nativeQuery = true)
	List<Object[]> findCustomerDetailsByCustomerId(@Param("customerId") Integer customerId);
}