package org.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.clases.*;

import java.io.IOException;
import java.util.*;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {
    MongoDatabase database;
    MongoClient mongoClient;

    public static void main(String[] args) {
        String connectionString = "mongodb://localhost:27017/";
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
            MongoCollection<org.bson.Document> collection = database.getCollection("Suplidor");
           // long countDocuments = collection.countDocuments();
            //System.out.println("\n\n\nCantidad Documentos Colección Suplidor==> " + countDocuments + "\n\n");

            List almacenes = new ArrayList<>();
            almacenes.add(3);
            almacenes.add(6000);


            Detalle det = new Detalle();
            List valores = new ArrayList<>();
            valores.add("und");
            valores.add("comp");
            valores.add(700);
            det.setValores(valores);

            Detalle det2 = new Detalle();
            List valores2 = new ArrayList<>();
            valores2.add("und2");
            valores2.add("comp2");
            valores2.add(7002);
            det2.setValores(valores2);



            ArrayList detalles = new ArrayList<Detalle>();
            detalles.add(det);
            detalles.add(det2);

           // for (int i=0 ;i<detalles.size();i++){
           //     System.out.println("el det es" + detalles.get(i).getClass() );

         //   }


            Componente cm = new Componente("532", "NUEVO", "500", almacenes, 300);
            Suplidor sp = new Suplidor("nuevo", "Spl", "873-9384", "Samana", "Calle12, 54");
            TiempoEntrega te = new TiempoEntrega("nuevo", "532", 13, 250, 15, "S");
           MovimientoInventario mi= new MovimientoInventario("mm","fecha","1","SALIDA","532");
           mi.setDetalle(detalles);

           for (int i=0 ;i<mi.getDetalle().size();i++){
               Object obj = mi.getDetalle().get(i);

               Detalle mobj = Detalle.class.cast(obj);

               System.out.println("imprime " + mobj.getValores().get(0));
               System.out.println("imprime " + mobj.getValores().get(1));
               System.out.println("imprime " + mobj.getValores().get(2));
           }

            ArrayList<Componente>componentes = new ArrayList<>();

            CRUDModel crudModel = new CRUDModel();

            crudModel.insertarDocumentoMovimiento(database,mi);
            //crudModel.generarOrdenComponenteIndividual(database,"a","10/10/23",componentes);

            // crudModel.generarOrdenCompraAutomatica(database,;


        }}


}
