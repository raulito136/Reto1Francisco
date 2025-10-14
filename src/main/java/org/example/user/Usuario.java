package org.example.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Usuario {
    private String id;
    private String email;
    private String contrasenha;
}
