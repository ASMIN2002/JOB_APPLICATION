package com.asmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.asmin.entity.Company;



@Repository
public interface CompRepo extends JpaRepository<Company, Long>{
	boolean existsByEmail(String email);
	Company findByEmail(String email);
}
