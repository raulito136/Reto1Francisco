package org.example.data;


import org.example.session.AppSession;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvDataService implements DataService{
    String ruta;

    public CsvDataService(String ruta) {
        this.ruta = ruta;
    }

    public List<Pelicula> cargarPeliculas() {
        List<Pelicula> peliculas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] separacion = linea.split(",");
                peliculas.add(new Pelicula(
                        separacion[0], separacion[1], separacion[2],
                        separacion[3], separacion[4], separacion[5],
                        separacion[6], separacion[7]
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return peliculas;
    }

    public void anhadirPelicula(Pelicula pelicula){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(ruta,true))){
         String juego=  String.format("%d,%s,%s,%s,%s,%s,%s,%s",siguienteId(),pelicula.getTitulo(),pelicula.getAnho(),pelicula.getDirector(),pelicula.getDescripcion(),pelicula.getGenero(),pelicula.getImagen(),pelicula.getIdUsuario());
         bw.write(juego);
         bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private int siguienteId(){
        Path path=Path.of(ruta);
        try {
            List<String> peliculas= Files.readAllLines(path);
            String[] separacion=peliculas.getLast().split(",");
            int siguienteId=Integer.parseInt(separacion[0])+1;
            return siguienteId;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminarPelicula(String id) {
        Path path = Path.of(ruta);
        boolean encontrada = false;

        try {
            List<String> lineas = Files.readAllLines(path);
            List<String> nuevasLineas = new ArrayList<>();

            for (String linea : lineas) {
                String[] partes = linea.split(",");
                if (!partes[0].equals(id)) {
                    nuevasLineas.add(linea); // Mantener todas las demás líneas
                } else {
                    encontrada = true; // Solo eliminar según id, no verifica usuario
                }
            }

            Files.write(path, nuevasLineas);

        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la película", e);
        }

        return encontrada;
    }


}

