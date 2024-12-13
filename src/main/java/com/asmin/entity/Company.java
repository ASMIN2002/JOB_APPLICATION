package com.asmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long cid;
	private String compname;
	
	@Column(unique = true, nullable = false)
	private String email;
	private String password;
	private String regyear;
	private String loc;
}
