package org.example.visual;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.clases.Componente;
import org.example.clases.OrdenCompra;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.*;

public class generarOrden extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton agregarButton;

    public generarOrden() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        createTable();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

    public JTable createTable(){
        FindIterable<Document> cursor = null;

        String connectionString = "mongodb://localhost:27017/"; //CAMBIAR
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            //CAMBIAR BASE DE DATOS
            MongoDatabase database = mongoClient.getDatabase("XYZComputers");
            //Obtener objeto de un Collection

            List<OrdenCompra> ordenesCompra = new ArrayList<>();
            MongoCollection<Document> componentesCollection = database.getCollection("componentes");
            MongoCollection<Document> proveedoresCollection = database.getCollection("proveedores");
            FindIterable<Document> iterDoc = componentesCollection.find();
            Iterator<Document> it = iterDoc.iterator();
            ArrayList<Componente> componentes = null; // ARREGLAR ESTO DIOS MIO
            // Recorrer la coleccion de componentes
            for (Componente componente : componentes) {

                    Bson proveedoresFiltro = Filters.and(
                            Filters.eq("componenteId", componente.getCodigoComponente()),
                            Filters.eq("activo", true)
                    );
                    List<Document> proveedoresDocs = proveedoresCollection.find(proveedoresFiltro).into(new ArrayList<>());

                    // Ordenar los proveedores por fecha de entrega ascendente y precio ascendente
                    Collections.sort(proveedoresDocs, Comparator.comparing(d -> d.getDate("tiempoEntrega")));

                    // Obtener el proveedor con la fecha de entrega más temprana y/o menor costo
                    Document proveedorSeleccionado = proveedoresDocs.get(0);

                    // Generar la orden de compra para el proveedor seleccionado
                    OrdenCompra ordenCompra = new OrdenCompra();
                    ordenCompra.setNumeroOrden((String) proveedorSeleccionado.get("nombre"));
                    ordenCompra.setFechaOrden(new Date());
                    ordenCompra.agregarComponente(componente);
                    ordenesCompra.add(ordenCompra);
                }

        }
        return table1;
    }
    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        generarOrden dialog = new generarOrden();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
