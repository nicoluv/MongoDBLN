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

            List detalles = new ArrayList<>();
            detalles.add("532");
            detalles.add(70);
            detalles.add(600);

            Componente cm = new Componente("532", "NUEVO", "500", almacenes, 300);
            Suplidor sp = new Suplidor("nuevo", "Spl", "873-9384", "Samana", "Calle12, 54");
            TiempoEntrega te = new TiempoEntrega("nuevo", "532", 13, 250, 15, "S");
           // MovimientoInventario mi= new MovimientoInventario("nuevoMov",new String(),"1","SALIDA",detalles);

            ArrayList<Componente>componentes = new ArrayList<>();

            CRUDModel crudModel = new CRUDModel();
            //crudModel.insertarDocumentoMovimiento(database,mi);
            //crudModel.generarOrdenComponenteIndividual(database,"a","10/10/23",componentes);

            // crudModel.generarOrdenCompraAutomatica(database,;


        }}


}
