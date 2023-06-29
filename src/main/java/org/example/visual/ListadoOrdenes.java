package org.example.visual;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.excludeId;

public class ListadoOrdenes {
    private JPanel ListadoOrden;
    private JLabel lblIcon;
    private JLabel lblTitle;
    private JTextField tfSuplidor;
    private JTextField tfRango1;
    private JTextField tfRango2;
    private JButton limpiarButton;
    private JButton buscarButton;
    private JTable table1;

    private int flag;

    public ListadoOrdenes() {
        tfSuplidor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tfSuplidor.setEnabled(true);
                flag = 1;
            }
        });
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfSuplidor.setText("");
                tfSuplidor.setEnabled(false);
                tfRango1.setText("");
                tfRango2.setText("");
                tfRango1.setEnabled(false);
                tfRango2.setEnabled(false);


            }
        });
        tfRango1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tfRango1.setEnabled(true);
                tfRango2.setEnabled(true);
                flag = 2;
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tableLoadByDate();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    void tableLoadByDate() throws ParseException{

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

            MongoCollection<Document> coll = database.getCollection("Orden");

            String[] columnNames = {"numeroOrden", "codigoSuplidor", "ciudadSuplidor","fechaOrden","montoTotal"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);


            //OBTENER POR FECHA
            if (tfSuplidor.getText().equals("")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Bson[] condiciones = {Filters.gt("fechaOrden", sdf.parse(tfRango1.getText())),
                        Filters.lte("fechaOrden", sdf.parse(tfRango2.getText()))};
                Bson match = Aggregates.match(Filters.and(condiciones));
                Bson fields = Projections.fields(excludeId(), computed("numeroOrden", "$numeroOrden"),
                        computed("codigoSuplidor",
                                "$codigoSuplidor"),computed("ciudadSuplidor", "$ciudadSuplidor"),computed("fechaOrden","$fechaOrden"),computed("montoTotal","$montoTotal"));


                Bson project = Aggregates.project(fields);
                Bson[] etapas = {match, project};

                AggregateIterable<Document> aggregate = coll.aggregate(Arrays.asList(etapas));
                aggregate.forEach(documento -> {

                    System.out.println(documento);
                    String numero = String.valueOf(documento.get("numeroOrden"));
                    String codSup = String.valueOf(documento.get("codigoSuplidor"));
                    String ciudad = (String)documento.get("ciudadSuplidor");
                    String fecha = String.valueOf(documento.get("fechaOrden"));
                    String monto = String.valueOf(documento.get("montoTotal"));

                    model.addRow(new Object[] { numero, codSup, ciudad, fecha, monto });
                });

                table1.setModel(model);


            }

            //OBTENER POR SUPLIDOR

            else if (tfRango1.getText().equals("")) {

                    Bson[] condiciones = {Filters.eq("codigoSuplidor", Integer.parseInt( tfSuplidor.getText()))};
                    Bson match = Aggregates.match(Filters.and(condiciones));
                    Bson fields = Projections.fields(excludeId(), computed("numeroOrden", "$numeroOrden"),
                            computed("codigoSuplidor",
                                    "$codigoSuplidor"),computed("ciudadSuplidor", "$ciudadSuplidor"),computed("fechaOrden","$fechaOrden"),computed("montoTotal","$montoTotal"));


                    Bson project = Aggregates.project(fields);
                    Bson[] etapas = {match, project};

                    AggregateIterable<Document> aggregate = coll.aggregate(Arrays.asList(etapas));
                    aggregate.forEach(documento -> {

                        System.out.println(documento);
                        String numero = String.valueOf(documento.get("numeroOrden"));
                        String codSup = String.valueOf(documento.get("codigoSuplidor"));
                        String ciudad = (String)documento.get("ciudadSuplidor");
                        String fecha = String.valueOf(documento.get("fechaOrden"));
                        String monto = String.valueOf(documento.get("montoTotal"));

                        model.addRow(new Object[] { numero, codSup, ciudad, fecha, monto });
                    });

                    table1.setModel(model);

                //LISTADO POR FECHA Y SUPLIDOR
                }else {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Bson[] condiciones = {Filters.gt("fechaOrden", sdf.parse(tfRango1.getText())),
                        Filters.lte("fechaOrden", sdf.parse(tfRango2.getText())),Filters.eq("codigoSuplidor", Integer.parseInt( tfSuplidor.getText()))};
                Bson match = Aggregates.match(Filters.and(condiciones));
                Bson fields = Projections.fields(excludeId(), computed("numeroOrden", "$numeroOrden"),
                        computed("codigoSuplidor",
                                "$codigoSuplidor"),computed("ciudadSuplidor", "$ciudadSuplidor"),computed("fechaOrden","$fechaOrden"),computed("montoTotal","$montoTotal"));


                Bson project = Aggregates.project(fields);
                Bson[] etapas = {match, project};

                AggregateIterable<Document> aggregate = coll.aggregate(Arrays.asList(etapas));
                aggregate.forEach(documento -> {

                    System.out.println(documento);
                    String numero = String.valueOf(documento.get("numeroOrden"));
                    String codSup = String.valueOf(documento.get("codigoSuplidor"));
                    String ciudad = (String)documento.get("ciudadSuplidor");
                    String fecha = String.valueOf(documento.get("fechaOrden"));
                    String monto = String.valueOf(documento.get("montoTotal"));

                    model.addRow(new Object[] { numero, codSup, ciudad, fecha, monto });
                });

                table1.setModel(model);









            }

            }


            //match




        }




    public static void main(String[] args) {
        JFrame frame = new JFrame("ListadoOrdenes");
        frame.setContentPane(new ListadoOrdenes().ListadoOrden);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
