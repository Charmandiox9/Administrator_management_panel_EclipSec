package dao;

import model.Hiring;
import model.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HiringDAO {

    public List<Hiring> obtenerTodos() {
        List<Hiring> hirings = new ArrayList<>();
        String sql = "SELECT * FROM contrataciones";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Hiring h = new Hiring(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getInt("servicio_id"),
                        rs.getDate("fecha_solicitud"),
                        rs.getString("comentario"),
                        rs.getString("estado")
                );
                hirings.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hirings;
    }

    public void insertar(Hiring hiring) {
        String sql = "INSERT INTO contrataciones (usuario_id, servicio_id, fecha_solicitud, comentario) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hiring.getUser_id());
            pstmt.setInt(2, hiring.getService_id());
            pstmt.setDate(4, hiring.getApplication_date());
            pstmt.setString(4, hiring.getCommentary());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Hiring hiring) {
        String sql = "UPDATE contrataciones SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hiring.getStatus());


            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                System.out.println("⚠️ No se encontró ningún usuario con ID " + hiring.getId());
            } else {
                System.out.println("✅ Usuario actualizado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM contrataciones WHERE id = ?";

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
