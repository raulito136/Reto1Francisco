package org.example.ui;

import org.example.user.UserService;
import org.example.data.DataService;
import org.example.session.AppSession;
import org.example.data.Pelicula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Principal extends JFrame {

    private JTable tblTabla;
    private JPanel panel1;
    private JPanel PanelMenu;
    private JScrollPane scrollPane;

    private ArrayList<Pelicula> peliculasUsuario = new ArrayList<>();

    private DataService ds;
    private UserService us;

    private JMenuItem itemAnadir = new JMenuItem("Añadir película");
    private JMenuItem itemEliminar = new JMenuItem("Eliminar película");
    private JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión");

    private DefaultTableModel modelo = new DefaultTableModel();

    public Principal(DataService ds, UserService userService) {
        this.ds = ds;
        this.us = userService;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Inicio de Sesión");
        this.setResizable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel1);

        crearTabla();
        crearMenu();
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        tblTabla.setOpaque(false);
        initListener();
    }

    public void start() {
        this.setVisible(true);
    }

    private void crearMenu() {
        PanelMenu.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(200, 40));

        JMenu menu = new JMenu("Opciones");
        menu.setFont(new Font("Arial", Font.BOLD, 18));

        itemAnadir.setFont(new Font("Arial", Font.PLAIN, 16));
        itemEliminar.setFont(new Font("Arial", Font.PLAIN, 16));
        itemCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 16));

        menu.add(itemAnadir);
        menu.add(itemEliminar);
        menu.addSeparator();
        menu.add(itemCerrarSesion);
        menuBar.add(menu);

        JLabel lblTitulo = new JLabel("Mis Películas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        PanelMenu.add(menuBar, BorderLayout.WEST);
        PanelMenu.add(lblTitulo, BorderLayout.CENTER);
    }

    private void crearTabla() {
        modelo.setColumnCount(0);
        modelo.addColumn("ID");
        modelo.addColumn("Titulo");
        modelo.addColumn("Descripción");
        modelo.addColumn("Año de Estreno");

        tblTabla.setModel(modelo);
        tblTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        actualizarTabla();
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        peliculasUsuario.clear();

        List<Pelicula> todas = ds.cargarPeliculas();
        for (Pelicula p : todas) {
            if (Integer.parseInt(p.getIdUsuario()) == AppSession.idUsuario) {
                peliculasUsuario.add(p);
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getTitulo(),
                        p.getDescripcion(),
                        p.getAnho()
                });
            }
        }
    }

    private void initListener() {
        tblTabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblTabla.getSelectedRow() >= 0) {
                AppSession.peliculaSeleccionada = peliculasUsuario.get(tblTabla.getSelectedRow());
                new DetallePelicula(this).setVisible(true);
            }
        });

        itemAnadir.addActionListener(e -> {
            new AnhadirNuevaPelicula(ds, this).setVisible(true);
            actualizarTabla();
        });

        itemEliminar.addActionListener(e -> {
            var idEliminar = JOptionPane.showInputDialog(
                    this,
                    "Introduce el ID de la película a eliminar",
                    "Eliminar Película",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (idEliminar != null && !idEliminar.isBlank()) {
                try {
                    Pelicula peliculaAEliminar = null;


                    for (Pelicula p : peliculasUsuario) {
                        if (p.getId().equals(idEliminar)) {
                            peliculaAEliminar = p;
                            break;
                        }
                    }

                    if (peliculaAEliminar == null) {
                        JOptionPane.showMessageDialog(this, "No se ha encontrado la película en tu lista");
                        return;
                    }


                    boolean resultado = ds.eliminarPelicula(idEliminar);
                    if (resultado) {
                        peliculasUsuario.remove(peliculaAEliminar);
                        JOptionPane.showMessageDialog(this, "Película eliminada correctamente");
                        actualizarTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al eliminar la película");
                    }

                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        itemCerrarSesion.addActionListener(e -> {
            this.dispose();
            new PantallaLogin(ds, us).setVisible(true);
        });
    }
}
