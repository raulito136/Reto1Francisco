package org.example.ui;

import org.example.User.CsvUserService;
import org.example.User.UserService;
import org.example.data.DataService;
import org.example.session.AppSession;
import org.example.data.CsvDataService;
import org.example.data.Pelicula;

import javax.swing.*;
    import javax.swing.table.DefaultTableModel;
import java.awt.*;
        import java.util.ArrayList;
    import java.util.List;
import java.util.logging.Logger;

public class Principal extends JFrame {

    private JTable tblTabla;
    private JPanel panel1;
    private JPanel PanelMenu;
    private JScrollPane scrollPane;

    private ArrayList<Pelicula> peliculas;
    private DataService ds;
    private UserService us;
    Logger log=Logger.getGlobal();
    private JMenuItem itemAnadir = new JMenuItem("Añadir película");
    private JMenuItem itemEliminar = new JMenuItem("Eliminar película");
    private JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión");

    private DefaultTableModel modelo = new DefaultTableModel();

    public Principal(DataService ds, UserService userService){
        this.ds=ds;
        us=userService;
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
    public void start(){
        this.setVisible(true);
    }

    private void crearMenu() {
        // Asegurar que PanelMenu tenga un BorderLayout
        PanelMenu.setLayout(new BorderLayout());

        // Crear la barra de menú
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(200, 40)); // ancho fijo y altura

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

        // Crear etiqueta "Mis Películas"
        JLabel lblTitulo = new JLabel("Mis Películas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Agregar componentes al PanelMenu
        PanelMenu.add(menuBar, BorderLayout.WEST);   // Menú a la izquierda
        PanelMenu.add(lblTitulo, BorderLayout.CENTER); // Texto centrado
    }




    private void crearTabla() {
        // Limpiar tabla
        modelo.setColumnCount(0);
        modelo.setRowCount(0);
        modelo.addColumn("ID");
        modelo.addColumn("Titulo");
        modelo.addColumn("Descripción");
        modelo.addColumn("Año de Estreno");
        tblTabla.setModel(modelo);

        // Cargar todas las películas
        List<Pelicula> todasPeliculas = ds.cargarPeliculas();
        peliculas = new ArrayList<>();

        for (Pelicula p : todasPeliculas) {
            if (Integer.parseInt(p.getIdUsuario()) == AppSession.idUsuario) {
                peliculas.add(p); // guardamos en la lista de la clase
                // agregar fila a la tabla
                Object[] fila = new Object[]{p.getId(), p.getTitulo(), p.getDescripcion(), p.getAnho()};
                modelo.addRow(fila);
            }
        }

        tblTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initListener() {
        tblTabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblTabla.getSelectedRow() >= 0) {
                log.info("Seleccionado: " + tblTabla.getSelectedRow());
                AppSession.peliculaSeleccionada = peliculas.get(tblTabla.getSelectedRow());
                new DetallePelicula(this).setVisible(true);
            }
        });

        itemAnadir.addActionListener(e -> {
            new AnhadirNuevaPelicula(ds, this).setVisible(true);
            crearTabla();
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
                    // Cargar todas las películas del CSV
                    List<Pelicula> todasPeliculas = ds.cargarPeliculas();

                    // Buscar la película por ID
                    Pelicula peliculaAEliminar = null;
                    for (Pelicula p : todasPeliculas) {
                        if (p.getId().equals(idEliminar)) {
                            peliculaAEliminar = p;
                            break;
                        }
                    }

                    if (peliculaAEliminar == null) {
                        JOptionPane.showMessageDialog(this, "No se ha encontrado la película");
                    } else if (Integer.parseInt(peliculaAEliminar.getIdUsuario()) != AppSession.idUsuario) {
                        // Verificación: solo el propietario puede eliminar
                        JOptionPane.showMessageDialog(this, "No puedes eliminar la película de otro usuario");
                    } else {
                        // Usuario dueño -> eliminar
                        boolean resultado = ds.eliminarPelicula(idEliminar);
                        if (resultado) {
                            JOptionPane.showMessageDialog(this, "Se ha eliminado correctamente la película");
                            crearTabla(); // actualizar tabla
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al eliminar la película");
                        }
                    }

                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        itemCerrarSesion.addActionListener(e -> {
            this.dispose();
            new PantallaLogin(ds,us).setVisible(true);
        });
    }

}
