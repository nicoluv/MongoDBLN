package org.example.clases;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.*;
import static com.mongodb.client.model.Filters.eq;

public class CRUDModel {



    //METODOS PARA LOS COMPONENTES

    public Iterator obtenerComponentes(MongoDatabase database){
        MongoCollection<Document> collection = database.getCollection("Componente");
        FindIterable<Document> iterDoc = collection.find();

        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        return it;


    }
    public String obtenerComponentePorId(MongoDatabase database, String codigo){
        MongoCollection<Document> collection = database.getCollection("Componente");
        Document myDoc = collection.find(eq("codigoComponente", codigo)).first();
        if(myDoc == null){
            return null;
        }
        return myDoc.toJson();
    }

    public void insertarDocumentoComponente(MongoDatabase database, Componente cmp) throws IOException{
        MongoCollection<Document> componente = database.getCollection("Componente");

        if(obtenerComponentePorId(database, cmp.getCodigoComponente()) == null){
            // Preparar los documentos y subdocumentos que serán insertados.
            Document docComponente = new Document();
            Document docAlmacenes = new Document();

            docComponente.append("codigoComponente", cmp.getCodigoComponente());
            docComponente.append("descripcion", cmp.getDescripcion());
            docComponente.append("unidad", cmp.getUnidad());
            docComponente.append("inventarioMinimo", cmp.getInventarioMinimo());

            HashMap<String, Object> datosAlmacenes = new HashMap<>();

            datosAlmacenes.put("codigoAlmacen", cmp.getAlmacenes().get(0));
            //datosAlmacenes.put("balance", cmp.getAlmacenes().get(1));

            docAlmacenes.putAll(datosAlmacenes);

            docComponente.put("Almacenes", docAlmacenes);
            componente.insertOne(docComponente);


        }
        else {
            throw new IOException("Ya existe un componente con dicho codigo");
        }
    }

    //METODOS PARA LOS SUPLIDORES

    public Iterator obtenerSuplidores(MongoDatabase database){
        MongoCollection<Document> collection = database.getCollection("Suplidor");
        FindIterable<Document> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        return it;
    }

    public String obtenerSuplidorPorId(MongoDatabase database, String codigo){
        MongoCollection<Document> collection = database.getCollection("Suplidor");
        Document myDoc = collection.find(eq("codigoSuplidor", codigo)).first();
        if(myDoc == null){
            return null;
        }
        return myDoc.toJson();
    }


    public void insertarDocumentoSuplidor(MongoDatabase database, Suplidor sp) throws IOException{
        MongoCollection<Document> suplidor = database.getCollection("Suplidor");

        if(obtenerSuplidorPorId(database, sp.getCodigoSuplidor()) == null){
            // Preparar los documentos y subdocumentos que serán insertados.
            Document docsuplidor = new Document();


            docsuplidor.append("codigoSuplidor", sp.getCodigoSuplidor());
            docsuplidor.append("nombre", sp.getNombre());
            docsuplidor.append("rnc", sp.getRnc());
            docsuplidor.append("ciudad", sp.getCiudad());
            docsuplidor.append("direccion", sp.getDireccion());
            suplidor.insertOne(docsuplidor);

        }
        else {
            throw new IOException("Ya existe un suplidor con dicho codigo");
        }
    }


    //METODOS PARA LOS TIEMPOS DE ENTREGA

    public void insertarDocumentoTiempoEntrega(MongoDatabase database, TiempoEntrega te){
        MongoCollection<Document> tiempoEntrega = database.getCollection("TiempoEntregaSuplidor");

            // Preparar los documentos y subdocumentos que serán insertados.
            Document docTiempoEntrega = new Document();

            docTiempoEntrega.append("codigoSuplidor", te.getCodigoSuplidor());
            docTiempoEntrega.append("codigoComponente", te.getCodigoComponente());
            docTiempoEntrega.append("tiempoEntrega", te.getTiempoEntrega());
            docTiempoEntrega.append("precio", te.getTiempoEntrega());
            docTiempoEntrega.append("descuento", te.getDescuento());
            docTiempoEntrega.append("activo", te.getActivo());
            tiempoEntrega.insertOne(docTiempoEntrega);

    }

    public void obtenerTiemposEntrega(MongoDatabase database){
        MongoCollection<Document> collection = database.getCollection("TiempoEntregaSuplidor");
        FindIterable<Document> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }

    //METODOS PARA LOS MOVIMIENTOS

    public void insertarDocumentoMovimiento(MongoDatabase database, MovimientoInventario mv){
        MongoCollection<Document> movimiento = database.getCollection("MovimientoInventario");

            // Preparar los documentos y subdocumentos que serán insertados.
            Document docMovimiento = new Document();
            Document docDetalles = new Document();


            docMovimiento.append("codigoMovimiento", mv.getCodigoMovimiento());
            docMovimiento.append("fechaMovimiento", mv.getFechaMovimiento());
            docMovimiento.append("codigoAlmacen", mv.getCodigoAlmacen());
            docMovimiento.append("tipoMovimiento", mv.getTipoMovimiento());

            HashMap<String, Object> datos = new HashMap<>();

            //datos.put("codigoComponente", mv.getDetalle().get(0));
            datos.put("cantidadMovimiento", mv.getDetalle().get(1));
            datos.put("unidad", mv.getDetalle().get(2));

            docDetalles.putAll(datos);

            docMovimiento.put("detalle", docDetalles);
            movimiento.insertOne(docMovimiento);

    }

    public void obtenerMovimientos(MongoDatabase database){
        MongoCollection<Document> collection = database.getCollection("MovimientoInventario");
        FindIterable<Document> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }










    private void registrarMovimiento(MongoDatabase database, MovimientoInventario movimiento){
        MongoCollection<Document> movimientosCollection = database.getCollection("Componente");

        // Creación del documento para el nuevo movimiento
        Document nuevoMovimiento = new Document()
                .append("tipo", movimiento.getCodigoMovimiento())
                .append("fecha", movimiento.getFechaMovimiento())
                .append("almacen", movimiento.getCodigoAlmacen())
                .append("componente", movimiento.getDetalle().get(0))
                .append("cantidad", movimiento.getCantidad());

        //ARREGLAR PARA AGREGAR A LA COLECCION
        //movimientosCollection.append(nuevoMovimiento);

        // Actualizar el balance del componente en la colección "componentes"
        MongoCollection<Document> componentesCollection = database.getCollection("componentes");
        Bson filtro = Filters.eq("_id", movimiento.getDetalle().get(0));
        Bson update;

        if (movimiento.getCodigoMovimiento().equals("entrada")) {
            update = Updates.inc("cantidad", movimiento.getCantidad());
        } else {
            update = Updates.inc("cantidad", -movimiento.getCantidad());
        }

        componentesCollection.updateOne(filtro, update);
    }

    public void generarOrdenCompraAutomatica(MongoDatabase database,List<Componente> componentes) {
        List<OrdenCompra> ordenesCompra = new ArrayList<>();
        MongoCollection<Document> componentesCollection = database.getCollection("Componente");
        MongoCollection<Document> suplidorCollection = database.getCollection("Suplidor");
        MongoCollection<Document> ordenCollection = database.getCollection("Ordenes");


        // Recorrer la coleccion de componentes
        for (Componente componente : componentes) {
            // Verificar si el componente tiene cantidad por debajo del nivel objetivo
            if (componente.getInventarioMinimo() < 10) {

                Bson suplidorFiltro = Filters.and(
                        Filters.eq("codigoComponente", componente.getCodigoComponente())
                );
                List<Document> suplidorDocs = suplidorCollection.find(suplidorFiltro).into(new ArrayList<>());

                // Ordenar los proveedores por fecha de entrega ascendente y precio ascendente
                //Collections.sort(suplidorDocs, Comparator.comparing(d -> d.getDate("tiempoEntrega")));

                Document suplidorSeleccionado = (Document) obtenerSuplidores(database);

                // Generar la orden de compra para el proveedor seleccionado
                OrdenCompra ordenCompra = new OrdenCompra();
                ordenCompra.setNumeroOrden((String) suplidorSeleccionado.get("nombre"));
                ordenCompra.setCodigoSuplidor((String)suplidorSeleccionado.get("codigoSuplidor"));
                ordenCompra.setCiudadSuplidor((String) suplidorSeleccionado.get("ciudad"));
                ordenCompra.setFechaOrden(new Date());
                ordenCompra.agregarComponente(componente);
                ordenesCompra.add(ordenCompra);

                // Preparar los documentos y subdocumentos que serán insertados.
                Document docOrden = new Document();

                docOrden.append("numeroOrden", ordenCompra.getNumeroOrden());
                docOrden.append("codigoSuplidor", ordenCompra.getNumeroOrden());
                docOrden.append("ciudad", ordenCompra.getNumeroOrden());
                docOrden.append("fechaOrden", ordenCompra.getFechaOrden());
                docOrden.append("componentes", ordenCompra.getComponentes());

                ordenCollection.insertOne(docOrden);
            }
        }

    }


    public static void generarOrdenComponenteIndividual(MongoDatabase database, String codigoAlmacen, String fechaRequerida, List<Componente> componentesRequeridos) {
        List<String> codigosComponente = new ArrayList<>();
        for (Componente componente : componentesRequeridos) {
            codigosComponente.add(componente.getCodigoComponente());
        }

        List<Document> stages = Arrays.asList(
                new Document("$match", new Document("codigoComponente", new Document("$in", codigosComponente))),
                new Document("$unwind", "$almacenes"),
                new Document("$match", new Document("almacenes.codigoAlmacen", codigoAlmacen)),
                new Document("$lookup", new Document("from", "tiempoentregasuplidors")
                        .append("localField", "codigoComponente")
                        .append("foreignField", "codigoComponente")
                        .append("as", "tiempoEntregaSuplidor")),
                new Document("$unwind", "$tiempoEntregaSuplidor"),
                new Document("$project", new Document("_id", 0)
                        .append("codigoComponente", 1)
                        .append("descripcion", 1)
                        .append("unidad", 1)
                        .append("codigoAlmacen", "$almacenes.codigoAlmacen")
                        .append("balanceAlmacen", "$almacenes.balanceAlmacen")
                        .append("codigoSuplidor", "$tiempoEntregaSuplidor.codigoSuplidor")
                        .append("precio", "$tiempoEntregaSuplidor.precio")
                        .append("tiempoEntrega", "$tiempoEntregaSuplidor.tiempoEntrega")
                        .append("descuento", "$tiempoEntregaSuplidor.descuento")
                        .append("activo", "$tiempoEntregaSuplidor.activo")
                        .append("requerimientos", componentesRequeridos)
                ),
                new Document("$unwind", "$requerimientos"),
                new Document("$match", new Document("$and", Arrays.asList(
                        new Document("$expr", new Document("$eq", Arrays.asList("$activo", "S"))),
                        new Document("$expr", new Document("$eq", Arrays.asList("$requerimientos.codigoComponente", "$codigoComponente")))
                ))),
                new Document("$addFields", new Document("fechaBalance0", new Document("$dateAdd",
                        new Document("startDate", "$$NOW")
                                .append("unit", "day")
                                .append("amount", new Document("$subtract", Arrays.asList(
                                        new Document("$trunc", new Document("$divide",
                                                Arrays.asList("$balanceAlmacen", "$requerimientos.movimientoPromedio"))
                                        ),
                                        1
                                )))
                )
                ).append("fechaRequerida", new Document("$dateFromString",
                        new Document("dateString", fechaRequerida)
                ))),
                new Document("$addFields", new Document("fechaPivote", new Document("$cond",
                        new Document("if", new Document("$lt",
                                Arrays.asList("$fechaBalance0", "$fechaRequerida")
                        ))
                                .append("then", "$fechaBalance0")
                                .append("else", "$fechaRequerida")
                )).append("cantidadComprar", new Document("$round",
                        Arrays.asList(new Document("$subtract",
                                Arrays.asList("$requerimientos.cantidadRequerida",
                                        new Document("$add",
                                                Arrays.asList("$balanceAlmacen",
                                                        new Document("$multiply",
                                                                Arrays.asList(
                                                                        new Document("$dateDiff",
                                                                                new Document("startDate", "$$NOW")
                                                                                        .append("endDate", "$fechaRequerida")
                                                                                        .append("unit", "day")
                                                                        ),
                                                                        new Document("$multiply",
                                                                                Arrays.asList("$requerimientos.movimientoPromedio", -1)
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        ), 0)
                ))),
                new Document("$addFields", new Document("montoComponente", new Document("$multiply",
                        Arrays.asList(new Document("$multiply",
                                Arrays.asList("$precio", "$cantidadComprar")
                        ), new Document("$subtract",
                                Arrays.asList(1, "$descuento")
                        )))
                )).append("fechaOrden", new Document("$dateSubtract",
                        new Document("startDate", "$fechaPivote")
                                .append("amount", "$tiempoEntrega")
                                .append("unit", "day")
                )),
        new Document("$match", new Document("$expr", new Document("$gte",
                Arrays.asList("$fechaOrden",
                        new Document("$dateTrunc",
                                new Document("date", "$$NOW")
                                        .append("unit", "day")
                        )
                )
        ))),
                new Document("$sort", new Document("codigoComponente", 1)
                        .append("montoComponente", 1)),
                new Document("$group", new Document("_id", "$codigoComponente")
                        .append("informacionEntrega", new Document("$first",
                                new Document("codigoComponente", "$codigoComponente")
                                        .append("descripcion", "$descripcion")
                                        .append("unidad", "$unidad")
                                        .append("codigoAlmacen", "$codigoAlmacen")
                                        .append("balanceAlmacen", "$balanceAlmacen")
                                        .append("codigoSuplidor", "$codigoSuplidor")
                                        .append("precio", "$precio")
                                        .append("tiempoEntrega", "$tiempoEntrega")
                                        .append("descuento", "$descuento")
                                        .append("cantidadRequerida", "$requerimientos.cantidadRequerida")
                        ))
                        .append("fechaRequerida", new Document("$first", "$fechaRequerida"))
                        .append("fechaOrden", new Document("$first", "$fechaOrden"))
                        .append("importe", new Document("$first", "$montoComponente"))
                        .append("cantidad", new Document("$first", "$cantidadComprar"))
                ),
                new Document("$sort", new Document("fechaOrden", 1)),
                new Document("$group", new Document("_id", new Document("suplidor", "$informacionEntrega.codigoSuplidor"))
                        .append("componentes", new Document("$addToSet", new Document("codigoAlmacen", "$informacionEntrega.codigoAlmacen")
                                .append("codigoComponente", "$informacionEntrega.codigoComponente")
                                .append("cantidadComprada", "$cantidad")
                                .append("precioCompra", "$informacionEntrega.precio")
                                .append("unidadCompra", "$informacionEntrega.unidad")
                                .append("porcientoDescuento", "$informacionEntrega.descuento")
                                .append("montoDetalle", "$importe")
                                .append("descripcion", "$informacionEntrega.descripcion")
                        ))
                        .append("fechaOrden", new Document("$first", "$fechaOrden"))
                        .append("montoTotal", new Document("$sum", "$importe"))
                ),
                new Document("$lookup", new Document("from", "suplidor")
                        .append("localField", "_id.suplidor")
                        .append("foreignField", "codigoSuplidor")
                        .append("as", "suplidor")
                ),
                new Document("$unwind", "$suplidor"),
                new Document("$project", new Document("_id", 0)
                        .append("codigoSuplidor", "$_id.suplidor")
                        .append("nombreSuplidor", "$suplidor.nombre")
                        .append("ciudadSuplidor", "$suplidor.ciudad")
                        .append("fechaOrden", "$fechaOrden")
                        .append("montoTotal", "$montoTotal")
                        .append("detalle", "$componentes")
                ),
                new Document("$sort", new Document("fechaOrden", 1)));

        MongoCollection<Document> componenteCollection = database.getCollection("Componente");
        AggregateIterable<Document> result = componenteCollection.aggregate(stages);
        System.out.println(result);
        //return result.into(new ArrayList<>());
    }




}
