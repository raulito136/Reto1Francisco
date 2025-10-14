package org.example;

import org.example.User.CsvUserService;
import org.example.data.CsvDataService;
import org.example.ui.PantallaLogin;

public class Main {
    public static void main(String[] args) {
        CsvDataService ds=new CsvDataService("peliculas.csv");
        CsvUserService us=new CsvUserService("usuarios.csv");
        (new PantallaLogin(ds,us)).start();
    }
}