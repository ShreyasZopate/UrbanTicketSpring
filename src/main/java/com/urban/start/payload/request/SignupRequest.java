package com.urban.start.payload.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.*;

 
public class SignupRequest {
	@NotBlank
	@Size(max = 20)
	private String name;
	
	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(min = 10, max = 10)
	private double mobileno;
	
	@NotBlank
	@Size(min = 6, max = 40)
	private String password;
	
	private Set<String> role;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getMobileno() {
		return mobileno;
	}

	public void setMobileno(double mobileno) {
		this.mobileno = mobileno;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRole() {
		return role;
	}

	public void setRole(String role) {
		Set<String> set = new HashSet<String>();
		set.add(role);
		this.role = set;
	}




}

