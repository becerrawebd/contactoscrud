package com.example.democrud.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.democrud.model.Contacto;
import com.example.democrud.model.Usuario;
import com.example.democrud.service.api.ContactoServiceAPI;
import com.example.democrud.service.api.UsuarioServiceAPI;

@Controller
public class AppController {

	@Autowired
	private ContactoServiceAPI contactoServiceAPI;
	
	@Autowired
	private UsuarioServiceAPI usuarioServiceAPI;

	@Autowired
	private BCryptPasswordEncoder bCrypt;
	
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@GetMapping({ "/", "/login" })
	public String mostrarFormLogin(Model model) {
		return "login";
	}

	@GetMapping("/registro")
	public String mostrarFormRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registro";
	}
	
	@GetMapping("/cerrarsesion")
	public String cerrarSesion(Model model) {
		getAuthentication().setAuthenticated(false);
		return "redirect:/login";
	}
	
	@PostMapping("/registrarse")
    public String showRegistro(Usuario usuario, Model model) {
        if (!usuario.getUsername().isEmpty() && !usuario.getPassword().isEmpty()) {
        	if(usuarioServiceAPI.obtener(usuario.getUsername()) == null){
                usuario.setPassword(bCrypt.encode(usuario.getPassword()));
                usuarioServiceAPI.guardar(usuario);
                model.addAttribute("mensajeRegistro", "Registro exitoso!");
                model.addAttribute("clase", "success");
                return "login";
            }else {
            	model.addAttribute("mensajeRegistro", "Usuario ya existente!");
            	model.addAttribute("clase", "danger");
            }
        }else { 
        	model.addAttribute("mensajeRegistro", "Todos los campos deben completarse!");
        	model.addAttribute("clase", "danger");
        }
        return "registro";
    }

	@GetMapping("/contactos")
	public String contactos(Model model) {
		List<Contacto> listaFiltrada = new ArrayList<>();
		String usuarioLog = getAuthentication().getName();
		contactoServiceAPI.obtenerTodos().stream().filter(contacto -> usuarioLog.equals(contacto.getUsuario()))
				.forEach(contacto -> listaFiltrada.add(contacto));
		model.addAttribute("list", listaFiltrada);
		return "contactos";
	}

	@GetMapping("/save/{id}")
	public String showSave(@PathVariable("id") Long id, Model model) {
		String usuarioLog = getAuthentication().getName();
		if (id != null && id != 0 && usuarioLog.equals(contactoServiceAPI.obtener(id).getUsuario())) {
			model.addAttribute("contacto", contactoServiceAPI.obtener(id));
			model.addAttribute("accion", "Editar Contacto");
		} else {
			model.addAttribute("contacto", new Contacto());
			model.addAttribute("accion", "Añadir Contacto");
		}
		return "save";
	}

	@PostMapping("/save")
	public String save(Contacto contacto, Model model) {
		String usuarioLog = getAuthentication().getName();
		contacto.setUsuario(usuarioLog);
		contactoServiceAPI.guardar(contacto);
		return "redirect:/contactos";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, Model model) {
		contactoServiceAPI.eliminar(id);
		return "redirect:/contactos";
	}

}
