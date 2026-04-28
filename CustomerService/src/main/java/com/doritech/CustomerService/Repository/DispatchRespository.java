package com.doritech.CustomerService.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.DispatchEntity;

@Repository
public interface DispatchRespository extends JpaRepository<DispatchEntity, Integer>{

	boolean existsByDeliveryChallanNo(String deliveryChallanNo);

    boolean existsByConsignmentNo(String consignmentNo);
    
    boolean existsByDeliveryChallanNoAndDispatchIdNot(String deliveryChallanNo, Integer dispatchId);

    boolean existsByConsignmentNoAndDispatchIdNot(String consignmentNo, Integer dispatchId);


	Page<DispatchEntity> findAll(Specification<DispatchEntity> filter, Pageable pageable);

}