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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Iterator;

import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.excludeId;

public class ListadoComponentes extends JDialog {
    private JPanel contentPane;
    private JTable table1;
    private JButton volverButton;

    public ListadoComponentes() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(volverButton);
        createTable();


        volverButton.addActionListener(new ActionListener() {
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
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
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

            MongoCollection<Document> coll = database.getCollection("Componente");
            FindIterable<Document> iterDoc = coll.find();
            Iterator<Document> it = iterDoc.iterator();

            String[] columnNames = {"codigoComponente", "Descripcion", "Unidad","Almacen","Balance"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            Bson fields = Projections.fields(excludeId(), computed("codigo", "$almacenes.codigoAlmacen"),
                    computed("balance",
                            "$almacenes.balanceAlmacen"),computed("codigoComponente", "$codigoComponente"),computed("unidad","$unidad"),computed("descripcion","$descripcion"));


            Bson project = Aggregates.project(fields);
            Bson[] etapas = { project};
            AggregateIterable<Document> aggregate = coll.aggregate(Arrays.asList(etapas));
            aggregate.forEach(documento -> {

                System.out.println(documento);
                String Almacen = String.valueOf(documento.get("codigo"));
                String codComponente = String.valueOf(documento.get("codigoComponente"));
                String Descripcion = (String)documento.get("descripcion");
                String Unidad = String.valueOf(documento.get("unidad"));
                String Balance = String.valueOf(documento.get("balance"));

                model.addRow(new Object[] { codComponente, Descripcion, Unidad, Almacen, Balance });
            });

            table1.setModel(model);

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
        ListadoComponentes dialog = new ListadoComponentes();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}