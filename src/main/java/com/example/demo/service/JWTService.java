package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JWTService {

	public String generateJwtToken(User user, Role role, List<String> permissions) {
		// TODO Auto-generated method stub
		return null;
	}

}
