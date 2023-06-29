package org.example.visual;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.clases.Componente;
import org.example.clases.OrdenCompra;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Projections.*;


public class generarOrden extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton agregarButton;

    public static String consumoDiario;

    public generarOrden() throws ParseException {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
       // createTable();
        promedioConsumo();

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


    public void promedioConsumo() throws ParseException {
        


        Date tdate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(LocalDate.now()));
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(tdate);
        LocalDate today = LocalDate.parse(formattedDate);
        String endDate=today.minusMonths(2).toString();
        System.out.println(endDate);

        //
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
        String date = dateObj.format(formatter);
        String[] arrOfStr = endDate.split("-", 3);
        String finalDate = arrOfStr[0].concat("-").concat(arrOfStr[2]).concat("-").concat(arrOfStr[1]);

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

            //MATCH

            Bson[] condiciones = { Filters.gte("fechaMovimiento",  sdf.parse(finalDate)),
                    Filters.lte("fechaMovimiento",  sdf.parse(date)),
                   Filters.eq("tipoMovimiento", "SALIDA")};
            Bson match = match(Filters.and(condiciones));


            //UNWIND
            Bson unwind = Aggregates.unwind("$detalle");

            //GROUP

           // Bson group=
            Document groupId = new Document("_id", "");
            Bson group = Aggregates.group(groupId, Accumulators.sum("total", "$detalle.cantidadMovimiento"));

            // PROJECT
            Bson projection = project(fields(
                    computed("result", new Document("$round",
                            new Document("$divide", Arrays.asList("$total", 60))
                    ))
            ));


            Bson[] etapas = {match, unwind,group, projection};
            AggregateIterable<Document> aggregate = coll.aggregate(Arrays.asList(etapas));
            aggregate.forEach(documento -> {
               // System.out.println(documento);
              consumoDiario = (String.valueOf(documento.get("result")));

            });
            double doubleValue = Double.parseDouble(consumoDiario);
            int consumoDiarioInt = (int) doubleValue;




        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

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

    public static void main(String[] args) throws ParseException {
        generarOrden dialog = new generarOrden();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
