import java.awt.*;
import java.io.*;
import javax.swing.*;

public class InterfazUsuariosRB extends JFrame {
    private ArbolRB arbol;
    private PanelArbol panelArbol;
    private JTextArea salida;
    private JTextField idField, nombreField, buscarField, ceilingField;
    

    //metodo de medir tiempo a petición de avalo
    private void medirOperacion(String nombreOperacion, java.util.function.Supplier<String> operacion) {
        long inicio = System.nanoTime();
        String resultado = operacion.get();
        long fin = System.nanoTime();
        long duracionMs = (fin - inicio) / 1_000_000;
        salida.append(resultado);
        salida.append("Tiempo de " + nombreOperacion + ": " + duracionMs + " ms\n");
    }

    public InterfazUsuariosRB() {
        arbol = new ArbolRB();

        setTitle("Sistema de Usuarios Activos (Árbol Rojo-Negro)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        setContentPane(panelPrincipal);

        panelArbol = new PanelArbol(arbol);
        panelArbol.setPreferredSize(new Dimension(1000, 1000)); // un tamaño grande inicial
        JScrollPane scrollArbol = new JScrollPane(panelArbol);
        scrollArbol.setPreferredSize(new Dimension(1000, 300));
        scrollArbol.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollArbol.setBorder(BorderFactory.createTitledBorder("Visualización del Árbol"));
        panelPrincipal.add(scrollArbol, BorderLayout.NORTH);

        // Área de salida (consola de eventos)
        salida = new JTextArea(6, 70);
        salida.setEditable(false);
        JScrollPane scrollConsola = new JScrollPane(salida);
        scrollConsola.setBorder(BorderFactory.createTitledBorder("Consola de eventos"));
        panelPrincipal.add(scrollConsola, BorderLayout.CENTER);

        // Panel inferior con controles
        JPanel panelControles = new JPanel(new GridLayout(4, 2, 5, 5));
        panelPrincipal.add(panelControles, BorderLayout.SOUTH);

        idField = new JTextField();
        nombreField = new JTextField();
        buscarField = new JTextField();
        ceilingField = new JTextField();

        JButton agregarBtn = new JButton("Agregar usuario");
        JButton eliminarBtn = new JButton("Eliminar por ID");
        JButton buscarBtn = new JButton("Buscar");
        JButton mostrarBtn = new JButton("Mostrar InOrden");
        JButton buscarCeilingBtn = new JButton("Buscar Ceiling");
        JButton cargarArchivoBtn = new JButton("Cargar desde archivo");

        panelControles.add(new JLabel("ID:"));
        panelControles.add(idField);
        panelControles.add(new JLabel("Nombre:"));
        panelControles.add(nombreField);
        panelControles.add(agregarBtn);
        panelControles.add(eliminarBtn);
        panelControles.add(new JLabel("Buscar por ID:"));
        panelControles.add(buscarField);
        panelControles.add(buscarBtn);
        panelControles.add(new JLabel("Ceiling de ID:"));
        panelControles.add(ceilingField);
        panelControles.add(buscarCeilingBtn);
        panelControles.add(mostrarBtn);
        panelControles.add(cargarArchivoBtn);

        // Acciones de botones
        agregarBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String nombre = nombreField.getText();
                medirOperacion("inserción", () -> arbol.insertar(id, nombre));
                panelArbol.repaint();
            } catch (NumberFormatException ex) {
                salida.append("Error: ID inválido\n");
            }
        });

        eliminarBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                medirOperacion("eliminación", () -> arbol.eliminar(id));
                panelArbol.repaint();
            } catch (NumberFormatException ex) {
                salida.append("Error: ID inválido\n");
            }
        });


        buscarBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(buscarField.getText());
                medirOperacion("búsqueda", () -> arbol.buscarPorID(id) + "\n");
            } catch (NumberFormatException ex) {
                salida.append("Error: ID inválido\n");
            }
        });


        mostrarBtn.addActionListener(e -> {
            salida.setText(arbol.inOrden());
        });

        buscarCeilingBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(ceilingField.getText());
                NodoRB nodo = arbol.ceiling(id);
                if (nodo != null) {
                    salida.append("Ceiling encontrado: ID: " + nodo.clave + ", Nombre: " + nodo.valor + "\n");
                } else {
                    salida.append("No se encontró ceiling para ID " + id + "\n");
                }
            } catch (NumberFormatException ex) {
                salida.append("Error: ID inválido\n");
            }
        });

        cargarArchivoBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        String[] partes = linea.split(",");
                        if (partes.length == 2) {
                            int id = Integer.parseInt(partes[0].trim());
                            String nombre = partes[1].trim();
                            arbol.insertar(id, nombre);
                            salida.append("Cargado: " + id + " - " + nombre + "\n");
                        }
                    }
                    panelArbol.repaint();
                } catch (IOException | NumberFormatException ex) {
                    salida.append("Error al cargar archivo: " + ex.getMessage() + "\n");
                }
            }
        });

        setVisible(true);
    }
}
