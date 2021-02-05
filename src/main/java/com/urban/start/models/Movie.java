package com.urban.start.models;

import java.sql.Date;
import java.time.LocalTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "movie_data_tbl", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "movie_name")
		})
public class Movie {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "movie_id")
		private Long id;
		
		@NotBlank
		@Size(max = 30)
		@Column(name = "movie_name")
		private String name;
		
		@NotBlank
		@Size(max = 15)
		@Column(name = "movie_language")
		private String language;
		
		@NotBlank
		@Size(max = 30)
		@Column(name = "movie_genre")
		private String genre;
		
		@NotBlank
		@Size(max = 150)
		@Column(name = "movie_description")
		private String description;
		
		@NotBlank
		@Column(name = "movie_date")
		private Date date;
		
		@NotBlank
		@Column(name = "movie_time")
		private LocalTime time;
		
		@NotBlank
		@Size(max = 100)
		@Column(name = "movie_image")
		private String image;
		
		@ManyToMany(fetch = FetchType.LAZY)
		@JoinTable(	name = "user_movies", 
					joinColumns = @JoinColumn(name = "movie_id"), 
					inverseJoinColumns = @JoinColumn(name = "user_id"))
		private Set<User> user;
		
		public Movie() {
			
		}

		public Movie(String name,String language,String genre,String description,Date date,
				LocalTime time,String image) {
			super();
			this.name = name;
			this.language = language;
			this.genre = genre;
			this.description = description;
			this.date = date;
			this.time = time;
			this.image = image;	
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

		public Set<User> getUser() {
			return user;
		}

		public void setUser(Set<User> user) {
			this.user = user;
		}

		

		
		
		
		
	
}
