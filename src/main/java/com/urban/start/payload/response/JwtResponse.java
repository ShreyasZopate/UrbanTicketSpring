package com.urban.start.payload.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String name;
	private String username;
	private String email;
	private double mobileno;
	private List<String> roles;
	
	public JwtResponse(String token, Long id, String name, String username, String email, double mobileno,
			List<String> roles) {
		super();
		this.token = token;
		this.id = id;
		this.name = name;
		this.username = username;
		this.email = email;
		this.mobileno = mobileno;
		this.roles = roles;
	}
	public String getAccessToken() {
		return token;
	}
	public void setAccessToken(String token) {
		this.token = token;
	}
	
	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public List<String> getRoles() {
		return roles;
	}

}
