package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Principal extends JFrame {

    private JTable tblTabla;
    private JPanel panel1;

    private ArrayList<Pelicula> peliculas;
    private DataService ds;
    Logger log=Logger.getGlobal();

    public Principal(DataService ds){
        this.ds=ds;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Inicio de Sesión");
        this.setResizable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel1);

        var modelo= new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Titulo");
        modelo.addColumn("Descripción");
        tblTabla.setModel(modelo);
        peliculas= (ArrayList<Pelicula>) ds.cargarPeliculas();

        peliculas.forEach((j)-> {
            var fila=new Object[]{j.getId(), j.getTitulo(),j.getDescripcion()};
            modelo.addRow(fila);
        });

        tblTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTabla.getSelectionModel().addListSelectionListener((e)->{
            if (e.getValueIsAdjusting() && tblTabla.getSelectedRow()>=0){
                log.info("Seleccionado: "+tblTabla.getSelectedRow());

                AppSession.peliculaSeleccionada= peliculas.get(tblTabla.getSelectedRow());
                (new DetallePelicula(this)).setVisible(true);
            }
        });
    }
    public void start(){
        this.setVisible(true);
    }
}
