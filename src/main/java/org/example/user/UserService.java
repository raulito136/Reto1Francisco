package org.example.user;

import java.util.List;

public interface UserService {
     List<Usuario> listarUsuarios();
     void registrarUsuario(Usuario usuario);
}
