package com.example.democrud.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.democrud.model.Usuario;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UsuarioServiceImpl usuarioServiceImpl;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario appUser = usuarioServiceImpl.obtenerDao().findById(username)
				.orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		UserDetails userDetails = new User(appUser.getUsername(), appUser.getPassword(), roles);
		return userDetails;
		
	}
}
