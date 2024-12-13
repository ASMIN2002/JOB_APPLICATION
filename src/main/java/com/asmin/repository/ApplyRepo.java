package com.asmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.asmin.entity.AppliedJobs;

public interface ApplyRepo extends JpaRepository<AppliedJobs, Long>{
	
	@Transactional
	void deleteAllByEmail(String email);
}
