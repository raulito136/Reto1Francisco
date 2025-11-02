package org.example.ui;

import org.example.context.ContextService;
import org.example.data.DataService;
import org.example.data.Pelicula;

import javax.swing.*;
import java.awt.event.*;

public class AnhadirNuevaPelicula extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextField tfTitulo;
    private JTextField tfDirector;
    private JTextField tfDescripcion;
    private JTextField tfGenero;
    private JTextField tfImagen;
    private JSpinner spnAno;
    private JLabel lblEstreno;
    private JLabel lblDirector;
    private JLabel lblDescripcion;
    private JLabel lblGenero;
    private JLabel lblImagen;
    private JLabel lblTitulo;


    public AnhadirNuevaPelicula(JFrame parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(900,900);
        setLocationRelativeTo(parent);
        spnAno.setModel(new SpinnerNumberModel(1950, 1800, 2025, 1));

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
        if (tfDescripcion.getText().isEmpty() || tfDirector.getText().isEmpty() || tfGenero.getText().isEmpty() || tfImagen.getText().isEmpty() || tfTitulo.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta completar algún dato.", "Error", JOptionPane.WARNING_MESSAGE);
        }else {

            Pelicula pelicula = new Pelicula(null, tfTitulo.getText(), spnAno.getValue().toString(), tfDirector.getText(), tfDescripcion.getText(), tfGenero.getText(), tfImagen.getText(), ContextService.getInstance().getItem("UserId").get().toString());
            ContextService.getInstance().addItem("PeliculaAnadida",pelicula);
            dispose();
        }

    }

    private void onCancel() {
        dispose();
    }
}
