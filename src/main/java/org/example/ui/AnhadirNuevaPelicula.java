package org.example.ui;

import org.example.data.CsvDataService;
import org.example.data.DataService;
import org.example.data.Pelicula;
import org.example.session.AppSession;

import javax.swing.*;
import java.awt.event.*;

public class AnhadirNuevaPelicula extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel lblTitulo;
    private JLabel lblEstreno;
    private JLabel lblDirector;
    private JLabel lblDescripcion;
    private JLabel lblGenero;
    private JLabel lblImagen;
    private JTextField tfTitulo;
    private JTextField tfAnho;
    private JTextField tfDirector;
    private JTextField tfDescripcion;
    private JTextField tfGenero;
    private JTextField tfImagen;

    private DataService ds;
    public AnhadirNuevaPelicula(DataService ds, JFrame parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(900,900);
        setLocationRelativeTo(parent);
        this.ds=ds;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        //Añadimos la pelicula
        if (tfAnho.getText().isEmpty() || tfDescripcion.getText().isEmpty() || tfDirector.getText().isEmpty() || tfGenero.getText().isEmpty() || tfImagen.getText().isEmpty() || tfTitulo.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta completar algún dato.", "Error", JOptionPane.WARNING_MESSAGE);
        }else {
            Pelicula pelicula = new Pelicula(null, tfTitulo.getText(), tfAnho.getText(), tfDirector.getText(), tfDescripcion.getText(), tfGenero.getText(), tfImagen.getText(), String.valueOf(AppSession.idUsuario));
            ds.anhadirPelicula(pelicula);
            dispose();
        }

    }

    private void onCancel() {
        dispose();
    }
}
