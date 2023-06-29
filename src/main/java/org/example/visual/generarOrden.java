package org.example.visual;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.clases.Componente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
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
    private JTable showTable;
    private JTextField fechatxt;
    private JButton agregarButton;
    private JComboBox componenteCBX;
    private JSpinner cantidadSpinner;
    private JSpinner cantSpinner;
    private JComboBox suplidorCBX;
    private  JTable table1;
    ArrayList<Componente> misComponentes = new ArrayList<>();

    public static String consumoDiario;

    public generarOrden() throws ParseException {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        tableLoad();

        componenteCBX.addItem("as");

        componenteCBX.addItem("MOUSE");

        componenteCBX.addItem("PROCESADOR");


        String connectionString = "mongodb://localhost:27017/"; //CAMBIAR
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();




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

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (MongoClient mongoClient = MongoClients.create(settings)) {
                    //CAMBIAR BASE DE DATOS
                    MongoDatabase database = mongoClient.getDatabase("XYZComputers");
                    String codigoComponente = (String) componenteCBX.getSelectedItem();


                    MongoCollection<Document> componenteCollection = database.getCollection("Componente");

                    // Valor seleccionado del ComboBox
                    String valorSeleccionado = (String) componenteCBX.getSelectedItem(); // Reemplaza con el valor real seleccionado

                    // Construir el filtro de búsqueda
                    Bson filtro = new Document("descripcion", valorSeleccionado);

                    // Realizar la consulta con el filtro
                    FindIterable<Document> result = componenteCollection.find(filtro);
                    DefaultTableModel modelo = (DefaultTableModel)table1.getModel();

                    for (Document documento : result) {
                        // Obtener los valores de cada campo y crear un arreglo de objetos
                        Object[] fila = new Object[4]; // Cambiar el tamaño según el número de columnas

                        fila[0] = documento.get("codigoComponente");
                        fila[1] = documento.get("descripcion");
                        int value = (Integer) cantidadSpinner.getValue();
                        fila[2] = value;
                        fila[3] = documento.get("unidad");
                        // Asignar los valores de más campos si es necesario

                        // Agregar la fila al modelo de la tabla
                        modelo.addRow(fila);
                    }


                    for (Document documento : result) {
                        // Obtener las claves (nombres de los campos)
                        Set<String> campos = documento.keySet();


                        // Imprimir los valores de cada campo
                        for (String campo : campos) {
                            Object valor = documento.get(campo);

                            System.out.println(campo + ": " + valor);
                        }
                        System.out.println("-------------------");
                    }
                }

                   // addTable(codigoComponente);
            }
        });
    }

    void addTable(String codigoComponente){
        DefaultTableModel model = (DefaultTableModel)table1.getModel();


        model.addRow(new Object[] {codigoComponente,"HOLA TODOS","UND","ALMACEN","200"});


        table1.setModel(model);
    }
    void tableLoad(){


            String[] columnNames = {"codigoComponente", "Componente", "Cantidad","Unidad"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);


            //model.addRow(new Object[] { codComponente, Descripcion, Unidad, Almacen});



            table1.setModel(model);

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

