package com.asmin.entity;

import lombok.Data;

@Data
public class UserDto {
	private long id;
	private String name;
	private String email;
	private String password;
	private String education;
	private String state;
}