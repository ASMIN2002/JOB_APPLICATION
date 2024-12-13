package com.asmin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Commun{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String uemail;
	private String cemail;
	private String degree;
	private String message;
	private boolean status;
	private String respoMesg;
	private int icon;
	private String jobTitle;
	private String compName;
	private String skills;
	private String addr;
}
