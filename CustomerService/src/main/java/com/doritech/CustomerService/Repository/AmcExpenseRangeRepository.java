package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.doritech.CustomerService.Entity.AmcExpenseMaster;
import com.doritech.CustomerService.Entity.AmcExpenseMultiplier;

import feign.Param;

public interface AmcExpenseRangeRepository extends JpaRepository<AmcExpenseMultiplier, Integer> {

	@Query("""
			    SELECT m.multiplier
			    FROM AmcExpenseMultiplier m
			    WHERE :count BETWEEN m.minRange AND m.maxRange
			    AND m.amcExpenseMaster.amcExpenseId = :expenseId
			""")
	Integer findMultiplier(@Param("count") Integer count, @Param("expenseId") Integer expenseId);

	List<AmcExpenseMultiplier> findByAmcExpenseMaster(AmcExpenseMaster master);

}
