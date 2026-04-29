package com.doritech.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doritech.CustomerService.Entity.AmcExpenseMaster;

public interface AmcExpenseMasterRepository extends JpaRepository<AmcExpenseMaster, Integer> {

	AmcExpenseMaster findByDistrict(String district);

}
