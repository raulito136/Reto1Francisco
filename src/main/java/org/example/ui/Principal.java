package org.example.ui;

import org.example.context.ContextService;
import org.example.user.UserService;
import org.example.data.DataService;
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
    private int ultimoId;

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

        iniciarTabla();
    }

    private void iniciarTabla() {

        List<Pelicula> todas = ds.cargarPeliculas();
        for (Pelicula p : todas) {
            if (p.getIdUsuario().equals(ContextService.getInstance().getItem("UserId").get())) {
                peliculasUsuario.add(p);
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getTitulo(),
                        p.getDescripcion(),
                        p.getAnho()
                });
            }
        }
        ultimoId=todas.size()+1;
    }

    private void initListener() {
        tblTabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblTabla.getSelectedRow() >= 0) {
                ContextService.getInstance().addItem("PeliculaSeleccionada", peliculasUsuario.get(tblTabla.getSelectedRow()));
                new DetallePelicula(this).setVisible(true);
            }
        });

        itemAnadir.addActionListener(e -> {
            new AnhadirNuevaPelicula(this).setVisible(true);
            Pelicula peliculaAñadida= (Pelicula) ContextService.getInstance().getItem("PeliculaAnadida").get();
            peliculaAñadida.setId(String.valueOf(ultimoId));
            ds.anhadirPelicula(peliculaAñadida);
            peliculasUsuario.add(peliculaAñadida);
            modelo.addRow(new Object[]{peliculaAñadida.getId(), peliculaAñadida.getTitulo(), peliculaAñadida.getDescripcion(), peliculaAñadida.getAnho()});
            ultimoId++;
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

                        for (int i = 0; i < modelo.getRowCount(); i++) {
                            if (modelo.getValueAt(i, 0).equals(idEliminar)) {
                                modelo.removeRow(i);
                                break;
                            }
                        }

                        JOptionPane.showMessageDialog(this, "Película eliminada correctamente");

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
