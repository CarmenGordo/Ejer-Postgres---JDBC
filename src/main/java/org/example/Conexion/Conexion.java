package org.example.Conexion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Conexion {

    public static String leerEnv() {
        Map<String, String> env = cargarEnv();
        String url = env.get("URL");
        String host = env.get("HOST");
        String port = env.get("PORT");
        String bd = env.get("BD");
        String user = env.get("USER");
        String password = env.get("PASSWORD");

        if (url == null || host == null || port == null || bd == null || user == null || password == null) {
            System.err.println("Error: Faltan parámetros en el archivo .env");
            return null;
        }

        //%s -> indica cada una de los param string
        return String.format("%s%s:%s/%s?user=%s&password=%s", url, host, port, bd, user, password);
    }

    private static Map<String, String> cargarEnv(){
        Map<String, String> env = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine())!= null) {
                if (line.trim().isEmpty() || line.startsWith("#")){
                    continue; // Ignorar líneas vacias o comentarios.
                }
                String[] parts = line.split("=",2);
                if(parts.length == 2){
                    env.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error: No se encontró el archivo.env."+ e.getMessage());
        }
        return env;
    }

    public static Connection getConnection() throws SQLException {
        String connectionString = leerEnv();
        if (connectionString != null) {
            return DriverManager.getConnection(connectionString); // Establece la conexión
        }
        throw new SQLException("No se pudo establecer la conexión: parámetros incorrectos.");
    }

    public static void testConexion() {
        try (Connection connection = getConnection()) {
            System.out.println("Conexión exitosa a la base de datos.");

            try (Statement stmt = connection.createStatement()) {
                stmt.executeQuery("SELECT 1");
                System.out.println("Consulta ejecutada con éxito.");
            }

        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }

    public static void cerrarConexion(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada exitosamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }



}
