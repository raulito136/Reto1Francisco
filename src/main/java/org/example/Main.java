package org.example;

public class Main {
    public static void main(String[] args) {
        DataService ds=new DataService("peliculas.csv");
        (new PantallaLogin(ds)).start();
    }
}