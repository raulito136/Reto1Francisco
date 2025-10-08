package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pelicula {
    private String id;
    private String titulo;
    private String anho;
    private String director;
    private String descripcion;
    private String genero;
    private String imagen;
}
