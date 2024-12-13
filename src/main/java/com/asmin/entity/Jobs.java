package com.asmin.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Jobs{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String compname;
	private String email;
	private String title;
	private String skills;
	private String salary;
	private String addr;
	private String exp;
	private boolean status;
	private String regyear;
	private String date;
	private String uemail;
}
