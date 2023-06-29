package org.example.visual;

import com.mongodb.*;
import com.mongodb.client.*;

import com.mongodb.client.MongoClient;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.clases.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.excludeId;

public class RegistrarComponente extends JDialog{
    private JTextField txtCodigo;
    private JTextField txtDesc;
    private JTextField txtUnidad;
    private JButton btnComponente;
    private JTable table1;
    private JButton buscarButton;
    private JTextField txtbuscar;
    private JPanel PanelComponente;
    private JTextField txtAlmacen;
    private JButton btnLimpiar;
    private JButton volverButton;
    private JLabel lblIcon;

    CRUDModel crudModel = new CRUDModel();

    void tableLoad(){

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

            Bson unwind = Aggregates.unwind("$almacenes");
            Bson fields = Projections.fields(excludeId(), computed("codigo", "$almacenes.codigoAlmacen"),
                    computed("balance",
                            "$almacenes.balanceAlmacen"),computed("codigoComponente", "$codigoComponente"),computed("unidad","$unidad"),computed("descripcion","$descripcion"));


            Bson project = Aggregates.project(fields);
            Bson[] etapas = {unwind, project};
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


    }



    public RegistrarComponente() {

        setContentPane(PanelComponente);
        setModal(true);
        tableLoad();



        btnComponente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableLoad();
                Boolean ready = true;

                String codComponente, descripcion, unidad, almacen,balance;
                codComponente = txtCodigo.getText();
                descripcion= txtDesc.getText();
                unidad= txtUnidad.getText();
                almacen = txtAlmacen.getText();

                if(codComponente.equals("") || descripcion.equals("")|| unidad.equals("")|| almacen.equals("")){

                    JOptionPane.showMessageDialog(null,"Todos los campos deben estar completos");
                    ready = false;
                }

                if(ready){


                    List item = new ArrayList<>();
                    item.add(almacen);


                    Componente componente = new Componente();
                    componente.setUnidad(unidad);
                    componente.setAlmacenes(item);
                    componente.setDescripcion(descripcion);
                    componente.setCodigoComponente(codComponente);



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

                        crudModel.insertarDocumentoComponente(database,componente);
                        JOptionPane.showMessageDialog(null, "Componente Agregado Exitosamente");
                        tableLoad();
                        txtAlmacen.setText("");
                        txtCodigo.setText("");
                        txtDesc.setText("");
                        txtUnidad.setText("");


                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }


                }}


        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
                    Bson unwind = Aggregates.unwind("$almacenes");
                    Bson fields = Projections.fields(excludeId(), computed("codigo", "$almacenes.codigoAlmacen"),
                            computed("balance",
                                    "$almacenes.balanceAlmacen"),computed("codigoComponente", "$codigoComponente"),
                             computed("unidad","$unidad"),computed("descripcion","$descripcion"));


                    Bson project = Aggregates.project(fields);
                    Bson[] etapas = {unwind, project};
                    AggregateIterable<Document> aggregate = coll.aggregate(Arrays.asList(etapas));
                    aggregate.forEach(documento -> {


                        txtAlmacen.setText(String.valueOf(documento.get("codigo")));
                        txtCodigo.setText(String.valueOf(documento.get("codigoComponente")));
                        txtDesc.setText((String)documento.get("descripcion"));
                        txtUnidad.setText(String.valueOf(documento.get("unidad")));



                    });


                }

            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtAlmacen.setText("");
                txtCodigo.setText("");
                txtDesc.setText("");
                txtUnidad.setText("");

            }
        });
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Principal principal = new Principal();
                principal.pack();
                principal.setLocationRelativeTo(null);
                principal.setVisible(true);
                dispose();



            }
        });
    }

}
