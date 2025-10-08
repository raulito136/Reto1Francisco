package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    String ruta;

    public DataService(String ruta) {
        this.ruta = ruta;
    }

    public List<Pelicula> cargarPeliculas () {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            List<Pelicula> peliculas = new ArrayList<>();
            String linea = "";
            while ((linea = br.readLine()) != null) {
                String[] separacion = linea.split(",");
                if (Integer.parseInt(separacion[7]) != AppSession.idUsuario) {
                peliculas.add(new Pelicula(separacion[0],separacion[1],separacion[2],separacion[3],separacion[4],separacion[5],separacion[6]));
                }
            }
            return peliculas;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

