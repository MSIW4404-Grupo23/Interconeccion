package model.logic;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import model.data_structures.*;
import utils.Ordenamiento;

public class Modelo {
    private ILista datos;
    private GrafoListaAdyacencia grafo;
    private ITablaSimbolos paises;
    private ITablaSimbolos points;
    private ITablaSimbolos landingidtabla;
    private ITablaSimbolos nombrecodigo;

    public Modelo(int capacidad) {
        datos = new ArregloDinamico<>(capacidad);
    }

    public int darTamano() {
        return datos.size();
    }

    public void cargar() throws IOException {
        inicializarEstructuras();
        procesarPaises("./data/countries.csv");
        procesarLandingData("./data/landing_points.csv");
        procesarConexiones("./data/connections.csv");
    }

    private void inicializarEstructuras() {
        grafo = new GrafoListaAdyacencia(2);
        paises = new TablaHashLinearProbing(2);
        points = new TablaHashLinearProbing(2);
        landingidtabla = new TablaHashSeparteChaining(2);
        nombrecodigo = new TablaHashSeparteChaining(2);
    }

    private void procesarPaises(String filePath) throws IOException {
        try (Reader in = new FileReader(filePath)) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(in);
            for (CSVRecord record : records) {
                if (!record.get(0).isEmpty()) {
                    String countryName = record.get(0);
                    String capitalName = record.get(1);
                    double latitude = Double.parseDouble(record.get(2));
                    double longitude = Double.parseDouble(record.get(3));
                    String code = record.get(4);
                    String continentName = record.get(5);
                    float population = Float.parseFloat(record.get(6).replace(".", ""));
                    double users = Double.parseDouble(record.get(7).replace(".", ""));

                    Country pais = new Country(countryName, capitalName, latitude, longitude, code, continentName, population, users);
                    grafo.insertVertex(capitalName, pais);
                    paises.put(countryName, pais);
                }
            }
        }
    }

    private void procesarLandingData(String filePath) throws IOException {
        try (Reader in = new FileReader(filePath)) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(in);
            for (CSVRecord record : records) {
                String landingId = record.get(0);
                String id = record.get(1);
                String[] location = record.get(2).split(", ");
                String name = location[0];
                String countryName = location[location.length - 1];
                double latitude = Double.parseDouble(record.get(3));
                double longitude = Double.parseDouble(record.get(4));

                Landing landing = new Landing(landingId, id, name, countryName, latitude, longitude);
                points.put(landingId, landing);
            }
        }
    }

    private void procesarConexiones(String filePath) throws IOException {
        try (Reader in = new FileReader(filePath)) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(in);
            for (CSVRecord record : records) {
                procesarConexion(record);
            }
        }
    }

    private void procesarConexion(CSVRecord record) {
        try {
            String origin = record.get(0);
            String destination = record.get(1);
            String cableId = record.get(3);
            Landing landing1 = (Landing) points.get(origin);
            Landing landing2 = (Landing) points.get(destination);

            if (landing1 != null && landing2 != null) {
                insertarVertices(landing1, cableId);
                insertarVertices(landing2, cableId);
                agregarArco(landing1, landing2, cableId);
            }
        } catch (Exception e) {
            manejarExcepcion(e);
        }
    }

    private void insertarVertices(Landing landing, String cableId) {
        String vertexId = landing.getLandingId() + cableId;
        if (grafo.getVertex(vertexId) == null) {
            grafo.insertVertex(vertexId, landing);
        }
    }

    private void agregarArco(Landing landing1, Landing landing2, String cableId) {
        String vertexId1 = landing1.getLandingId() + cableId;
        String vertexId2 = landing2.getLandingId() + cableId;

        float distancia = distancia(landing1.getLongitude(), landing1.getLatitude(), landing2.getLongitude(), landing2.getLatitude());
        grafo.addEdge(vertexId1, vertexId2, distancia);
    }

    private void manejarExcepcion(Exception e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }

    private static float distancia(double lon1, double lat1, double lon2, double lat2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return (float) (earthRadius * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}
