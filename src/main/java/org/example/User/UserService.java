package org.example.User;

import java.util.List;

public interface UserService {
     List<Usuario> listarUsuarios();
     void registrarUsuario(Usuario usuario);
}
