package com.urban.start.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "user_data_tbl", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "user_username"),
			@UniqueConstraint(columnNames = "user_email") ,
			@UniqueConstraint(columnNames = "user_phoneNo")
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;
	
	@NotBlank
	@Size(max = 20)
	@Column(name = "user_name")
	private String name;
	
	@NotBlank
	@Size(max = 20)
	@Column(name = "user_username")
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	@Column(name = "user_email")
	private String email;

	@NotBlank
	@Size(min = 10, max = 10)
	@Column(name = "user_phoneNo")
	private double mobileno;
	
	@NotBlank
	@Size(max = 120)
	@Column(name = "user_password")
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(String name,String username,String email,double mobileno,String password) {
		super();
		this.name = name;
		this.username = username;
		this.email = email;
		this.mobileno = mobileno;
		this.password = password;
	}
	
	public Long getId() {
		return id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}

