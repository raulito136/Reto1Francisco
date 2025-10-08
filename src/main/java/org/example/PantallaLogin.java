package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PantallaLogin extends JFrame{
    private JPanel panel1;
    private JTextField tfCorreo;
    private JButton btnIniciarSesion;
    private JPasswordField pfContrasenha;
    private JLabel tvInfo;
    private DataService ds;

    public PantallaLogin( DataService ds) {
        this.ds=ds;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Inicio de Sesión");
        this.setResizable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel1);
        initListener();
    }
    public void initListener(){
        btnIniciarSesion.addActionListener(e -> {
            String correo = tfCorreo.getText();
            String contrasenha = new String(pfContrasenha.getPassword());
            boolean encontrado = false;

            try (BufferedReader br = new BufferedReader(new FileReader("usuarios.csv"))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] usuario = linea.split(",");
                    if (correo.equalsIgnoreCase(usuario[1]) && contrasenha.equals(usuario[2])) {
                        encontrado = true;
                        AppSession.idUsuario= Integer.parseInt(usuario[0]);
                        break;
                    }
                }

                if (encontrado) {
                    abrirPantallaNueva();
                } else {
                    tvInfo.setText("Correo o Contraseña incorrecta");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                tvInfo.setText("Error al leer el archivo");
            }
        });
    }

    public void start(){
        this.setVisible(true);
    }
    public void abrirPantallaNueva(){
        (new Principal(ds)).start();
        dispose();
    }
}