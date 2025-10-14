package org.example.ui;

import org.example.session.AppSession;
import org.example.data.Pelicula;

import javax.swing.*;
import java.awt.event.*;

public class DetallePelicula extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel lblID;
    private JLabel lblTitulo;
    private JLabel lblAnho;
    private JLabel lblDirector;
    private JLabel lblDescripcion;
    private JLabel lblGenero;
    private JLabel lblImagen;

    public DetallePelicula(JFrame parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(900,900);
        setLocationRelativeTo(parent);

        Pelicula p= AppSession.peliculaSeleccionada;

        lblAnho.setText(p.getAnho());
        lblDescripcion.setText(p.getDescripcion());
        lblDirector.setText(p.getDirector());
        lblGenero.setText(p.getGenero());
        lblImagen.setText(p.getImagen());
        lblID.setText(p.getId());
        lblTitulo.setText(p.getTitulo());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }
}
