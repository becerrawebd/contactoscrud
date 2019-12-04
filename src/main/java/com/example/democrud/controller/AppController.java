package com.example.democrud.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.democrud.model.Contacto;
import com.example.democrud.service.api.ContactoServiceAPI;

@Controller
public class AppController {
	
	@Autowired
	private ContactoServiceAPI contactoServiceAPI;
	
	@GetMapping({"/","/login"})
	public String index(Model model) {
		return "index";
	}
	
	@GetMapping("/contactos")
	public String contactos(Model model) {
		// aca obtengo el contexto del login (usuarios,password,etc).
		String usuario = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Contacto> listaFiltrada = new ArrayList<>();
		for (Contacto contacto : contactoServiceAPI.obtenerTodos()) {
			if(usuario.equals(contacto.getUsuario())) {
				listaFiltrada.add(contacto);	
			}
		}
		model.addAttribute("list", listaFiltrada);
		
		//model.addAttribute("list", contactoServiceAPI.obtenerTodos());
		return "contactos";
	}
	
	@GetMapping("/save/{id}")
	public String showSave(@PathVariable("id") Long id, Model model) {
		if(id != null && id != 0) {
			model.addAttribute("contacto", contactoServiceAPI.obtener(id));
			model.addAttribute("accion", "Editar Contacto");
		}
		else {
			model.addAttribute("contacto", new Contacto());
			model.addAttribute("accion", "Añadir Contacto");
		}
		return "save";
	}
	
	@PostMapping("/save")
	public String save(Contacto contacto, Model model) {
		contacto.setUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
		contactoServiceAPI.guardar(contacto);
		return "redirect:/contactos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, Model model) {
		contactoServiceAPI.eliminar(id);
		return "redirect:/contactos";
	}

}
