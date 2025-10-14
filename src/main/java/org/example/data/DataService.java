package org.example.data;

import java.util.List;

public interface DataService {
    void anhadirPelicula(Pelicula pelicula);
    boolean eliminarPelicula(String id);
    List<Pelicula> cargarPeliculas();
}
