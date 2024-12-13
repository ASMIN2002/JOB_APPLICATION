package com.asmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.asmin.entity.Jobs;

@Repository
public interface JobsRepo extends JpaRepository<Jobs, Long> {
	List<Jobs> findCompsByEmail(String email);
	
	@Transactional
	void deleteAllByEmail(String email);
}
