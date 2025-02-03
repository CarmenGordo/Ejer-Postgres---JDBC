package org.example;

import org.example.Conexion.Conexion;
import org.example.modelo.DBDDL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("¡Bienvenido a tu nuevo gestor de tareas!");
        Connection conexion = Conexion.getConnection();
        DBDDL.crearTabla(conexion);
        Conexion.leerEnv();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- MENÚ DE GESTIÓN DE TAREAS ---");
            System.out.println("1. Insertar tarea");
            System.out.println("2. Actualizar tarea");
            System.out.println("3. Eliminar tarea");
            System.out.println("4. Listar tareas");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1 -> {
                    System.out.println("Has elegido insertar una tarea.");
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Contenido: ");
                    String contexto = scanner.nextLine();
                    System.out.print("Fecha (YYYY-MM-DD): ");
                    Date fecha = Date.valueOf(scanner.nextLine());
                    System.out.print("¿Activo? (true/false): ");
                    boolean activo = scanner.nextBoolean();
                    scanner.nextLine(); // Limpiar buffer
                    DBDDL.insertarTarea(conexion, titulo, contexto, fecha, activo);
                }
                case 2 -> {
                    System.out.println("Has elegido actualizar una tarea.");
                    System.out.print("ID de la tarea: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer
                    System.out.print("Nuevo Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Nuevo Contexto: ");
                    String contexto = scanner.nextLine();
                    System.out.print("Nueva Fecha (YYYY-MM-DD): ");
                    Date fecha = Date.valueOf(scanner.nextLine());
                    System.out.print("¿Activo? (true/false): ");
                    boolean activo = scanner.nextBoolean();
                    scanner.nextLine(); // Limpiar buffer
                    DBDDL.actualizarTarea(conexion, id, titulo, contexto, fecha, activo);
                }
                case 3 -> {
                    System.out.println("Has elegido eliminar una tarea.");
                    System.out.print("ID de la tarea: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer
                    DBDDL.eliminarTarea(conexion, id);
                }
                case 4 -> {
                    System.out.println("Listado de tareas:");
                    DBDDL.listarTareas(conexion);
                }
                case 5 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida. Intenta de nuevo.");
            }
        } while (opcion != 5);

        scanner.close();
        Conexion.cerrarConexion(conexion);
    }
}


    /*
    REQUISITOS:
    hacer un crud de una app
     */

    /*
    HISTORIAS DE USUARIO: (en Scrum se usa)
    Como usuario, quiero:
    Para:
     */
    //aqui se deberia d epoiner una funcion con map, para poder
    //enlazar el archivo .env, este nunca se sube

