package org.example.modelo;

import java.sql.*;

public class DBDDL {

    // Método para crear la tabla si no existe
    public static void crearTabla(Connection conexion) {
        String creacionTabla = """
                CREATE TABLE IF NOT EXISTS Tarea (
                    id SERIAL PRIMARY KEY,
                    titulo VARCHAR(255) NOT NULL,
                    conenido VARCHAR(255),
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

    // Método para actualizar toda una tarea por su ID
    public static void actualizarTarea(Connection conexion, int id, String titulo, String contenido, Date fecha, boolean activo) {
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

    // Método para actualizar un solo campo de una tarea
    public static void actualizarCampoTarea(Connection conexion, int id, String campo, Object valor) {
        String sql = "UPDATE Tarea SET " + campo + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            if (valor instanceof String) {
                pstmt.setString(1, (String) valor);
            } else if (valor instanceof Date) {
                pstmt.setDate(1, (Date) valor);
            } else if (valor instanceof Boolean) {
                pstmt.setBoolean(1, (Boolean) valor);
            }
            pstmt.setInt(2, id);
            int filasActualizadas = pstmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Campo actualizado correctamente.");
            } else {
                System.out.println("No se encontró una tarea con el ID proporcionado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Título: " + rs.getString("titulo"));
                System.out.println("Contenido: " + rs.getString("contenido"));
                System.out.println("Fecha: " + rs.getDate("fecha"));
                System.out.println("Activo: " + rs.getBoolean("activo"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
