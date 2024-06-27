package com.poscodx.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poscodx.mysite.repository.UserRepository;
import com.poscodx.mysite.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void join(UserVo vo) {
		vo.setPassword(passwordEncoder.encode(vo.getPassword()));
		userRepository.insert(vo);
	}

	public UserVo getUser(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	public UserVo getUser(Long no) {
		return userRepository.findByNo(no);
	}
	
	public UserVo getUser(String email) {
		return userRepository.findByEmail(email, UserVo.class);
	}

	public void update(UserVo vo) {
		vo.setPassword(vo.getPassword().equals("") ? "" : passwordEncoder.encode(vo.getPassword()));
		userRepository.update(vo);
	}
}