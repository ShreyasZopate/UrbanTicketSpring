package com.urban.start.payload.request;

import java.sql.Date;
import java.time.LocalTime;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MovieRequest {
	
	@NotBlank
	@Size(max = 30)
	private String name;
	
	@NotBlank
	@Size(max = 15)
	private String language;
	
	@NotBlank
	@Size(max = 30)
	private String genre;
	
	@NotBlank
	@Size(max = 150)
	private String description;
	
	@NotBlank
	private Date date;
	
	@NotBlank
	private LocalTime time;
	
	@NotBlank
	@Size(max = 100)
	private String image;
	
	private String user;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	
}
