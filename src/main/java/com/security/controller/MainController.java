package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.security.model.AuthenticationRequest;
import com.security.model.AuthenticationResponse;
import com.security.service.MyUserDetailsService;
import com.security.util.JWTUtil;

@RestController
public class MainController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	MyUserDetailsService myUserDetailsService;
	@Autowired
	JWTUtil jwtUtil;
	
	@GetMapping("/homePage")
	public  String homePage() {
		System.out.println("--homePage");
	    return "home.jsp";
	}
	
	@GetMapping("/details")
	public  String details() {
		System.out.println("--details");
	    return "details";
	}
	@RequestMapping(value ="/authenticate", method=RequestMethod.POST)
	public  ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (Exception e) {
			throw new Exception("Incorrect username and password");
		}
		
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
