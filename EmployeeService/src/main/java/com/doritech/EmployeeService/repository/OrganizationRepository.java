package com.doritech.EmployeeService.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.CustomerOrganizationEntity;


@Repository
public interface OrganizationRepository
		extends
			JpaRepository<CustomerOrganizationEntity, Integer> {

	boolean existsByOrgNameIgnoreCase(String orgName);

	@Query("""
			SELECT o FROM CustomerOrganizationEntity o
			WHERE (:orgName IS NULL OR LOWER(o.orgName) LIKE LOWER(CONCAT('%', :orgName, '%')))
			AND (:active IS NULL OR o.active = :active)
			""")
	Page<CustomerOrganizationEntity> filterOrganization(
			@Param("orgName") String orgName, @Param("active") String active,
			Pageable pageable);

	List<CustomerOrganizationEntity> findByActive(String active);

	void deleteByIdIn(List<Long> ids);

	@Modifying
	@Query("UPDATE CustomerOrganizationEntity o SET o.active='N' WHERE o.id IN :ids")
	void softDeleteByIds(@Param("ids") List<Long> ids);

	boolean existsByOrgNameIgnoreCaseAndIdNot(String orgName, Integer id);

}
