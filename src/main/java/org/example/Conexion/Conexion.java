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

    public static Map<String, String> cargarEnv() {
        Map<String, String> env = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue; // Ignorar líneas vacías o comentarios
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    env.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error: No se encontró el archivo .env. " + e.getMessage());
        }
        return env;
    }

    public static String leerLocalConnectionString(Map<String, String> env) {
        String url = env.get("URL");
        String host = env.get("HOST");
        String port = env.get("PORT");
        String bd = env.get("BD");
        String user = env.get("USER");
        String password = env.get("PASSWORD");
        return construirConnectionString(url, host, port, bd, user, password);
    }

    public static String construirConnectionString(String url, String host, String port, String bd, String user, String password) {
        if (url == null || host == null || port == null || bd == null || user == null || password == null) {
            System.err.println("Error: Faltan parámetros de conexión.");
            return null;
        }
        return String.format("%s%s:%s/%s?user=%s&password=%s", url, host, port, bd, user, password);
    }

    public static Connection getConnection() throws SQLException {
        Map<String, String> env = cargarEnv();
        String SUPABASE = env.get("SUPABASE");
        // Intentar conexión a Supabase primero
        String localConnectionString = leerLocalConnectionString(env);

        if (SUPABASE != null) {
            try {
                System.out.println("Intentando conectar a Supabase...");
                return DriverManager.getConnection(SUPABASE);
            } catch (SQLException e) {
                System.err.println("Error al conectar a Supabase: " + e.getMessage());
            }
        } else if (localConnectionString != null) {
            System.out.println("Intentando conectar a la base de datos local...");
            return DriverManager.getConnection(localConnectionString);
        } else {
            System.err.println("No se encontraron los parámetros de conexión. Por favor verifica tu archivo.env.");
            return null;
        }

        throw new SQLException("No se pudo establecer la conexión: parámetros incorrectos o conexión fallida.");
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