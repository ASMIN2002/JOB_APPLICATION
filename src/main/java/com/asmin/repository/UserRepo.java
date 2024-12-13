package com.asmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asmin.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
	boolean existsByEmail(String email);
	User findByEmail(String email);
}
