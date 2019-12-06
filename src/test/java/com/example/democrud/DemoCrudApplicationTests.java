package com.example.democrud;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.democrud.model.Usuario;
import com.example.democrud.service.impl.UsuarioServiceImpl;

@SpringBootTest
class DemoCrudApplicationTests {

	@Autowired
	private UsuarioServiceImpl repo;
	@Autowired
	private BCryptPasswordEncoder bCrypt;
	
	@Test
	void crearUsuarioTest() {
		Usuario nuevoUsuario = new Usuario();
		//nuevoUsuario.setUsuario("diego");
		nuevoUsuario.setUsername("ramon");
		//nuevoUsuario.setPassword(bCrypt.encode("123"));
		nuevoUsuario.setPassword(bCrypt.encode("1234"));
		Usuario retornoUsuario = repo.guardar(nuevoUsuario);
		assertThat(retornoUsuario.getUsername().equals(nuevoUsuario.getUsername()));
	}

}
