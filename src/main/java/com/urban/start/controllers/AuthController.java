package com.urban.start.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import com.urban.start.models.ERole;
import com.urban.start.models.Movie;
import com.urban.start.models.Role;
import com.urban.start.models.User;
import com.urban.start.payload.request.LoginRequest;
import com.urban.start.payload.request.MovieRequest;
import com.urban.start.payload.request.SignupRequest;
import com.urban.start.payload.response.JwtResponse;
import com.urban.start.payload.response.MessageResponse;
import com.urban.start.repository.MovieRepository;
import com.urban.start.repository.RoleRepository;
import com.urban.start.repository.UserRepository;
import com.urban.start.security.jwt.JwtUtils;
import com.urban.start.security.services.UserDetailsImpl;
import com.urban.start.service.FileUploadService;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	MovieRepository movieRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	FileUploadService fileUploadService;
	
	
	@PostMapping("/signin")	
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getName(),
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 userDetails.getMobileno(),
												 roles));
	}
	
		@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		
		if (userRepository.existsByMobileno(signUpRequest.getMobileno())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Mobile is already in use!"));
		}
		
		// Create new user's account
		User user = new User(signUpRequest.getName(),signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),signUpRequest.getMobileno(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MANAGER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	@PostMapping("/addmovie")
	public ResponseEntity<?> addMovie(@Valid @RequestBody MovieRequest movieRequest) {
		System.out.println(movieRequest.getImage());
		Movie movie = new Movie(movieRequest.getName(),movieRequest.getLanguage(),movieRequest.getGenre(),
			movieRequest.getDescription(),movieRequest.getDate(),movieRequest.getTime(),
			movieRequest.getImage());
		
	
		String strUser = movieRequest.getUser();	
		Set<User> users = new HashSet<>();	
		User movieUser = userRepository.findByUsername(strUser)
			.orElseThrow(() -> new RuntimeException("Error: User Not Found."));
	
		users.add(movieUser);
	
		movie.setUser(users);
		movieRepository.save(movie);	
		return ResponseEntity.ok(new MessageResponse("Movie added successfully!"));
	}
	
	@PutMapping("/updatemovie/{id}")
	public ResponseEntity<?> updatemovie(@PathVariable Long id, @RequestBody MovieRequest movieDetails){
		
		Movie movie = movieRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Movie not exist with id:" +id));
		
		movie.setDate(movieDetails.getDate());
		movie.setTime(movieDetails.getTime());
		Movie updatedMovie= movieRepository.save(movie);
		return ResponseEntity.ok(updatedMovie);
	}

	@GetMapping("/getmovies")
	public List<Movie> getAllMovies(){
		return movieRepository.findAll();
	}
	
	@GetMapping("/getmanagers")
	public ResponseEntity<List<User>> getAllManagers(){
		List<User> managers= userRepository.findAll();
		
		List<User> selectedmanagers = new ArrayList<User>();
		
		managers.forEach(data -> {
			Set<Role> roleid = data.getRoles();
			roleid.forEach(roledata -> {
				if (roledata.getId() == 2) {
					selectedmanagers.add(data);
				}
			});
		});
		return ResponseEntity.ok(selectedmanagers);
	}
	
	@GetMapping("/getusers")
	public ResponseEntity<List<User>> getUsers(){
		List<User> users= userRepository.findAll();
		
		List<User> selectedusers = new ArrayList<User>();
		
		users.forEach(data -> {
			Set<Role> roleid = data.getRoles();
			roleid.forEach(roledata -> {
				if (roledata.getId() == 1) {
					selectedusers.add(data);
				}
			});
		});
		return ResponseEntity.ok(selectedusers);
	}

	@GetMapping("/getmoviesbymanager/{id}")
	public ResponseEntity<List<Movie>> getMovieByManager(@PathVariable Long id) {
		List<Movie> movies = movieRepository.findAll();
		
		List<Movie> selectedmovies = new ArrayList<Movie>();
		
		movies.forEach(data -> {
			Set<User> usersid = data.getUser();
			usersid.forEach(userdata -> {
				if(id == userdata.getId()) {
					selectedmovies.add(data);
				}
			});	
		});
		
		return ResponseEntity.ok(selectedmovies);
	}
	
	@GetMapping("/getmoviebyid/{id}")
	public ResponseEntity<Optional<Movie>> getMovieById(@PathVariable Long id) {
		Optional<Movie> movie = movieRepository.findById(id);
		
		return ResponseEntity.ok(movie);
	}
	
	@DeleteMapping("/deletemoviebyid/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteMovieById(@PathVariable Long id){
		
		Movie movie = movieRepository.findById(id)
				.orElseThrow(() ->new RuntimeException("Error: Movie Not Found."));
		
		movieRepository.delete(movie);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);

	}
	
	@DeleteMapping("/deletemanagerwithmovies/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteManagerWithMovies(@PathVariable Long id){
		List<Movie> movies = movieRepository.findAll();
		User user = userRepository.findById(id)
				.orElseThrow(() ->new RuntimeException("Error: User Not Found."));
		
		movies.forEach(data -> {
			Set<User> usersid = data.getUser();
			usersid.forEach(userdata -> {
				if(user.getId() == userdata.getId()) {
					movieRepository.delete(data);
				}
			});	
		});
		
		userRepository.delete(user);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/deleteuserbyid/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteUserById(@PathVariable Long id){
		
		User user = userRepository.findById(id)
				.orElseThrow(() ->new RuntimeException("Error: User Not Found."));
		
		userRepository.delete(user);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);

	}
	
	@GetMapping("/lang/{language}")
	public ResponseEntity<List<Movie>> getMovieByLang(@PathVariable String language) {
		List<Movie> movies = movieRepository.findAll();
		List<Movie> selectedmovies = new ArrayList<Movie>();
		movies.forEach(data -> {
			if(language.equals(data.getLanguage())){
				System.out.println(language);
				selectedmovies.add(data);
			}
		});
		
		return ResponseEntity.ok(selectedmovies);
	}
	
	@GetMapping("/genre/{gen}")
	public ResponseEntity<List<Movie>> getMovieByGenre(@PathVariable String gen) {
		List<Movie> movies = movieRepository.findAll();
		List<Movie> selectedmovies = new ArrayList<Movie>();
		movies.forEach(data -> {
			if(gen.equals(data.getGenre())){
				System.out.println(gen);
				selectedmovies.add(data);
			}
		});
		
		return ResponseEntity.ok(selectedmovies);
	}
 
	@PostMapping("/upload/local")
	public void uploadLocal(@RequestParam("file")MultipartFile multipartFile)
	{
		fileUploadService.uploadToLocal(multipartFile);
	}
}
