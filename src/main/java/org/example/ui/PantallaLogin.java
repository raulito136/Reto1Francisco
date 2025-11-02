package org.example.ui;

import org.example.context.ContextService;
import org.example.user.UserService;
import org.example.user.Usuario;
import org.example.data.DataService;

import javax.swing.*;
import java.util.ArrayList;

public class PantallaLogin extends JFrame{
    private JPanel panel1;
    private JTextField tfCorreo;
    private JButton btnIniciarSesion;
    private JPasswordField pfContrasenha;
    private JLabel tvInfo;
    private DataService ds;
    private UserService us;

    public PantallaLogin(DataService ds, UserService csvDataService) {
        this.ds=ds;
        us=csvDataService;
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
            String correo = tfCorreo.getText().trim();
            String contrasenha = new String(pfContrasenha.getPassword()).trim();
            ArrayList<Usuario> usuarios= (ArrayList<Usuario>) us.listarUsuarios();

            for(Usuario u:usuarios){
                if (u.getEmail().equalsIgnoreCase(correo) && u.getContrasenha().equals(contrasenha)){
                    ContextService.getInstance().addItem("UserId",u.getId());
                    abrirPantallaNueva();
                }
            }
                tvInfo.setText("Correo o Contraseña incorrecta");
        });
    }

    public void start(){
        this.setVisible(true);
    }
    public void abrirPantallaNueva(){
        (new Principal(ds,us)).start();
        dispose();
    }
}