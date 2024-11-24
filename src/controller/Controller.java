package controller;

import java.io.IOException;
import java.util.Scanner;
import model.logic.Modelo;
import view.View;

public class Controller<T> {

    /* Instancia del Modelo */
    private Modelo modelo;

    /* Instancia de la Vista */
    private View view;

    /* Constructor */
    public Controller() {
        view = new View();
    }

    public void run() {
        try (Scanner lector = new Scanner(System.in).useDelimiter("\n")) {
            boolean fin = false;

            while (!fin) {
                view.printMenu();
                int option = lector.nextInt();
                handleOption(option, lector);
                fin = option == 7;
            }
        }
    }

    private void handleOption(int option, Scanner lector) {
        switch (option) {
            case 1:
                handleLoadData();
                break;
            case 2:
                handleRequest1(lector);
                break;
            case 3:
                handleRequest2();
                break;
            case 4:
                handleRequest3(lector);
                break;
            case 5:
                handleRequest4();
                break;
            case 6:
                handleRequest5(lector);
                break;
            case 7:
                view.printMessage("--------- \n Hasta pronto !! \n---------");
                break;
            default:
                view.printMessage("--------- \n Opción Inválida !! \n---------");
        }
    }

    private void handleLoadData() {
        view.printMessage("--------- \nCargar datos");
        modelo = new Modelo(1);
        try {
            modelo.cargar(); // Método para cargar datos sigue siendo válido
        } catch (IOException e) {
            view.printMessage("Error al cargar los datos: " + e.getMessage());
        }
        view.printMessage("Datos cargados correctamente.");
    }

    private void handleRequest1(Scanner lector) {
        view.printMessage("--------- \nIngrese el nombre del primer punto de conexión");
        String punto1 = lector.next();
        view.printMessage("--------- \nIngrese el nombre del segundo punto de conexión");
        String punto2 = lector.next();

        view.printMessage("Funcionalidad pendiente de implementar: procesar puntos de conexión.");
        
    }

    private void handleRequest2() {
        view.printMessage("Funcionalidad pendiente de implementar: realizar una consulta de puntos de conexión.");
        
    }

    private void handleRequest3(Scanner lector) {
        view.printMessage("--------- \nIngrese el nombre del primer país");
        String pais1 = lector.next();
        view.printMessage("--------- \nIngrese el nombre del segundo país");
        String pais2 = lector.next();

        view.printMessage("Funcionalidad pendiente de implementar: procesar ruta entre países.");
        
    }

    private void handleRequest4() {
        view.printMessage("Funcionalidad pendiente de implementar: realizar un análisis de rutas.");
        
    }

    private void handleRequest5(Scanner lector) {
        view.printMessage("--------- \nIngrese el nombre del punto de conexión");
        String landing = lector.next();

        view.printMessage("Funcionalidad pendiente de implementar: analizar puntos de conexión.");
        
    }
}
