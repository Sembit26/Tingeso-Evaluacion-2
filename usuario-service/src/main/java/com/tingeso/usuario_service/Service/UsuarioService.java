package com.tingeso.usuario_service.Service;

import com.tingeso.usuario_service.Entity.Usuario;
import com.tingeso.usuario_service.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // CRUD básico
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(int id) {
        return usuarioRepository.findById(id);
    }

    public Usuario updateUsuario(int id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    public boolean deleteUsuario(int id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Usuario register(String name, String email, String contrasenia, LocalDate birthday) {
        if (usuarioRepository.findByEmail(email) == null) {
            throw new RuntimeException("El cliente con el correo ya existe.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setName(name);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setContrasena(contrasenia);
        nuevoUsuario.setBirthday(birthday);
        nuevoUsuario.setNum_visitas_al_mes(0);
        nuevoUsuario.setRol("Cliente");

        return usuarioRepository.save(nuevoUsuario);
    }


    // Login: devuelve el usuario si email y contraseña coinciden
    public Usuario login(String email, String contrasena) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) throw new RuntimeException("No se encontró el cliente con el email " + email);

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }
}
