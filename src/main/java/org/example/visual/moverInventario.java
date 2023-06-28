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
import org.example.clases.Componente;
import org.example.clases.MovimientoInventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.excludeId;

public class moverInventario extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable showTable;
    private JButton registrarMovimientoButton;
    private JTextField txtMovimientoTextField;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;

    CRUDModel crudModel = new CRUDModel();
    public moverInventario() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        createTable();


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
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        registrarMovimientoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //tableLoad();
                Boolean ready = true;

                String codMovimiento, fecha, codAlmacen, tipoMov;
                codMovimiento = txtMovimientoTextField.getText();
                fecha= textField2.getText();
                codAlmacen= textField3.getText();
                tipoMov = textField4.getText();


                if(codMovimiento.equals("") || codAlmacen.equals("")|| tipoMov.equals("")){

                    JOptionPane.showMessageDialog(null,"Todos los campos deben estar completos");
                    ready = false;
                }
                if(tipoMov.equals("ENTRADA")){

                }

                if(ready){


                    List item = new ArrayList<>();
                    item.add(codAlmacen);


                    MovimientoInventario aux_movimiento = new MovimientoInventario();
                    aux_movimiento.setCodigoMovimiento(codMovimiento);
                    aux_movimiento.setFechaMovimiento(fecha);
                    aux_movimiento.setCodigoAlmacen(codAlmacen);
                    aux_movimiento.setTipoMovimiento(tipoMov);

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

                        crudModel.insertarDocumentoMovimiento(database,aux_movimiento);
                        JOptionPane.showMessageDialog(null, "Componente Agregado Exitosamente");
                        createTable();
                        txtMovimientoTextField.setText("");
                        textField2.setText("");
                        textField3.setText("");
                        textField4.setText("");


                    }


                }}

        }); }


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

            String[] columnNames = {"codigoMovimiento", "Fecha Movimiento", "Codigo Almacen","Tipo Movimiento"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            Bson fields = Projections.fields(excludeId(), computed("cod", "$codigoMovimiento"),
                    computed("fecha",
                            "$fechaMovimiento"),
                    computed("codAlmacen","$codigoAlmacen"),computed("tipo","$tipoMovimiento"));


            Bson project = Aggregates.project(fields);
            Bson[] etapas = {project};
            AggregateIterable<Document> aggregate = coll.aggregate(Arrays.asList(etapas));
            aggregate.forEach(documento -> {

                System.out.println(documento);
                String cod = String.valueOf(documento.get("cod"));
                String fecha = String.valueOf(documento.get("fecha"));
                String almacen = (String)documento.get("codAlmacen");
                String tipo = String.valueOf(documento.get("tipo"));
                // String Balance = String.valueOf(documento.get("balance"));

                model.addRow(new Object[] {cod,fecha,almacen,tipo });
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