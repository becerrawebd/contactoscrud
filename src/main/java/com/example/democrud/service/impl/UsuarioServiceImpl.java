package com.example.democrud.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.example.democrud.commons.GenericServiceImpl;
import com.example.democrud.dao.api.UsuarioDaoAPI;
import com.example.democrud.model.Usuario;
import com.example.democrud.service.api.UsuarioServiceAPI;

@Service
public class UsuarioServiceImpl extends GenericServiceImpl<Usuario, String>
		implements UsuarioServiceAPI{

	@Autowired
	private UsuarioDaoAPI usuarioDaoApi;

	@Override
	public CrudRepository<Usuario, String> obtenerDao() {
		return usuarioDaoApi;
	}
}
