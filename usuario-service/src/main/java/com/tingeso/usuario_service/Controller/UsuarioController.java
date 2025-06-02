package com.tingeso.usuario_service.Controller;

import com.tingeso.usuario_service.DTO.UsuarioLoginDTO;
import com.tingeso.usuario_service.DTO.UsuarioRegisterDTO;
import com.tingeso.usuario_service.Entity.Usuario;
import com.tingeso.usuario_service.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*") // Habilita CORS para frontend como Vue o React
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // CRUD básico

    @GetMapping("/getAll")
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.updateUsuario(id, usuario);
        return (actualizado != null) ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable int id) {
        return usuarioService.deleteUsuario(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody UsuarioRegisterDTO dto) {
        try {
            Usuario usuario = usuarioService.register(
                    dto.getName(),
                    dto.getEmail(),
                    dto.getContrasena(),
                    dto.getBirthday()
            );
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody UsuarioLoginDTO dto) {
        try {
            Usuario usuario = usuarioService.login(dto.getEmail(), dto.getContrasena());
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
