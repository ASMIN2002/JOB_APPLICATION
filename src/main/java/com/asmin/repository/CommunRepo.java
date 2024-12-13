package com.asmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.asmin.entity.Commun;

import java.util.List;

@Repository
public interface CommunRepo extends JpaRepository<Commun, Long> {
	List<Commun> findByCemail(String cemail);
	List<Commun> findByUemail(String cemail);
	
	@Transactional
	void deleteAllByCemail(String cemail);
}
