package org.example.modelo;

import java.sql.*;
import java.util.Scanner;

public class DBDDL {

    // Método para crear la tabla si no existe
    public static void crearTabla(Connection conexion) {
        String creacionTabla = """
                CREATE TABLE IF NOT EXISTS Tarea (
                    id SERIAL PRIMARY KEY,
                    titulo VARCHAR(255) NOT NULL,
                    contenido VARCHAR(255),
                    fecha DATE,
                    activo BOOLEAN DEFAULT TRUE
                );
                """;
        try (Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(creacionTabla);
            System.out.println("Tabla 'Tarea' creada (si no existía).");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para insertar una nueva tarea
    public static void insertarTarea(Connection conexion, String titulo, String contenido, Date fecha, boolean activo) {
        String sql = "INSERT INTO Tarea (titulo, contenido, fecha, activo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, contenido);
            pstmt.setDate(3, fecha);
            pstmt.setBoolean(4, activo);
            pstmt.executeUpdate();
            System.out.println("Tarea insertada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener una tarea por su ID
    public static Tarea obtenerTareaPorId(Connection conexion, int id) {
        String sql = "SELECT * FROM Tarea WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Tarea(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("contenido"),
                        rs.getDate("fecha"),
                        rs.getBoolean("activo")
                );
            } else {
                System.out.println("No se encontró una tarea con el ID proporcionado.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para actualizar una tarea con opción de mantener campos existentes
    public static void actualizarTareaInteractivo(Connection conexion, int id) {
        Tarea tareaExistente = obtenerTareaPorId(conexion, id);
        if (tareaExistente == null) {
            System.out.println("No hay tareas para poder actulizarlas");
            return;
        } else {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Actualizar título (actual: " + tareaExistente.getTitulo() + "): ");
            String titulo = scanner.nextLine();
            if (titulo.isEmpty()) {
                titulo = tareaExistente.getTitulo();
            }

            System.out.println("Actualizar contenido (actual: " + tareaExistente.getContenido() + "): ");
            String contenido = scanner.nextLine();
            if (contenido.isEmpty()) {
                contenido = tareaExistente.getContenido();
            }

            System.out.println("Actualizar fecha (actual: " + tareaExistente.getFecha() + ") (YYYY-MM-DD): ");
            String fechaStr = scanner.nextLine();
            Date fecha;
            if (fechaStr.isEmpty()) {
                fecha = tareaExistente.getFecha();
            } else {
                fecha = Date.valueOf(fechaStr);
            }

            System.out.println("Actualizar activo (actual: " + tareaExistente.isActivo() + ") (true/false): ");
            String activoStr = scanner.nextLine();
            boolean activo;
            if (activoStr.isEmpty()) {
                activo = tareaExistente.isActivo();
            } else {
                activo = Boolean.parseBoolean(activoStr);
            }

            String sql = "UPDATE Tarea SET titulo = ?, contenido = ?, fecha = ?, activo = ? WHERE id = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, titulo);
                pstmt.setString(2, contenido);
                pstmt.setDate(3, fecha);
                pstmt.setBoolean(4, activo);
                pstmt.setInt(5, id);
                int filasActualizadas = pstmt.executeUpdate();
                if (filasActualizadas > 0) {
                    System.out.println("Tarea actualizada correctamente.");
                } else {
                    System.out.println("No se encontró una tarea con el ID proporcionado.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    // Método para eliminar una tarea por ID
    public static void eliminarTarea(Connection conexion, int id) {
        String sql = "DELETE FROM Tarea WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filasEliminadas = pstmt.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Tarea eliminada correctamente.");
            } else {
                System.out.println("No se encontró una tarea con el ID proporcionado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para listar todas las tareas
    public static void listarTareas(Connection conexion) {
        String sql = "SELECT * FROM Tarea";
        try (Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Lista de Tareas ---");

            boolean hayDatos = false;
            while (rs.next()) {
                hayDatos = true;
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Título: " + rs.getString("titulo"));
                System.out.println("Contenido: " + rs.getString("contenido"));
                System.out.println("Fecha: " + rs.getDate("fecha"));
                System.out.println("Activo: " + rs.getBoolean("activo"));
                System.out.println("---------------------------");
            }

            if (!hayDatos) {
                System.out.println("No hay tareas registradas.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Clase para representar una tarea
    public static class Tarea {
        private int id;
        private String titulo;
        private String contenido;
        private Date fecha;
        private boolean activo;

        public Tarea(int id, String titulo, String contenido, Date fecha, boolean activo) {
            this.id = id;
            this.titulo = titulo;
            this.contenido = contenido;
            this.fecha = fecha;
            this.activo = activo;
        }

        public int getId() {
            return id;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getContenido() {
            return contenido;
        }

        public Date getFecha() {
            return fecha;
        }

        public boolean isActivo() {
            return activo;
        }
    }
}