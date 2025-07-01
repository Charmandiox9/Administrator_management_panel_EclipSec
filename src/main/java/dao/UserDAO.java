package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> obtenerTodos() {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contraseña"),
                        rs.getString("rol")
                );
                usuarios.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public void insertar(User usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, contraseña, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getName());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getPassword());
            pstmt.setString(4, usuario.getRole());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(User usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, contraseña = ?, rol = ? WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getName());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getPassword());
            pstmt.setString(4, usuario.getRole());
            pstmt.setInt(5, usuario.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                System.out.println("⚠️ No se encontró ningún usuario con ID " + usuario.getId());
            } else {
                System.out.println("✅ Usuario actualizado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

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

    public User buscarPorCorreo(String email) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contraseña"),
                        rs.getString("rol")
                );
                return u;
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }

    public User buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contraseña"),
                        rs.getString("rol")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }

        return null;
    }

}
