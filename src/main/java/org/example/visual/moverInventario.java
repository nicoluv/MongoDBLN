package org.example.visual;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.clases.CRUDModel;
import org.example.clases.MovimientoInventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.*;

import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.excludeId;

public class moverInventario extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable showTable;
    private JButton registrarMovimientoButton;
    private JTextField txtMovimientoTextField;
    private JTextField fechaTXT;
    private JTextField almacenTXT;
    private JComboBox movimientoCBX;
    private JTextField textField4;

    CRUDModel crudModel = new CRUDModel();
    public moverInventario() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        movimientoCBX.addItem("ENTRADA");
        movimientoCBX.addItem("SALIDA");
        //  createTableInsert();


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
    public JTable createTableInsert(){



        String[] columnNames = {"unidad", "Componente", "Cantidad"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        model.addRow(new Object[] {0,0,0,0 });
        showTable.setModel(model);


        return showTable;
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

            MongoCollection<Document> coll = database.getCollection("MovimientoInventario");
            FindIterable<Document> iterDoc = coll.find();
            Iterator<Document> it = iterDoc.iterator();

            String[] columnNames = {"Componente", "Cantidad", "Unidad"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            Bson unwind = Aggregates.unwind("$detalle");
            Bson fields = Projections.fields(excludeId(), computed("unidad", "$unidad"),
                    computed("codigo",
                            "$codigoMovimiento"),
                    computed("cantidad","$cantidadMovimiento"));


            Bson project = Aggregates.project(fields);
            Bson[] etapas = {unwind,project};
            AggregateIterable<Document> aggregate = coll.aggregate(Arrays.asList(etapas));
            aggregate.forEach(documento -> {

                System.out.println(documento);
                String unidad = String.valueOf(documento.get("unidad"));
                String cod = String.valueOf(documento.get("codigoComponente"));
                String cantidad = (String)documento.get("cantidad");


                model.addRow(new Object[] {unidad,cod,cantidad});
            });

            showTable.setModel(model);

        }
        return showTable;
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
        moverInventario dialog = new moverInventario();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}