package dao;

import model.Service;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    public List<Service> obtenerTodos() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM servicios";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Service s = new Service(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getBoolean("activo"),
                        rs.getTimestamp("fecha_creacion")
                );
                services.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    public void insertar(Service service) {
        String sql = "INSERT INTO servicios (nombre, descripcion, precio) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, service.getName());
            pstmt.setString(2, service.getDescription());
            pstmt.setDouble(4, service.getPrice());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Service service) {
        String sql = "UPDATE servicios SET nombre = ?, descripcion = ?, precio = ?, activo = ?, fecha_creacion = ? WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, service.getName());
            pstmt.setString(2, service.getDescription());
            pstmt.setBoolean(3, service.isActive());
            pstmt.setDouble(4, service.getPrice());
            pstmt.setInt(5, service.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                System.out.println("⚠️ No se encontró ningún usuario con ID " + service.getId());
            } else {
                System.out.println("✅ Usuario actualizado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM servicios WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                System.out.println("⚠️ No se encontró ningún usuario con ID " + id);
            } else {
                System.out.println("✅ Usuario eliminado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
