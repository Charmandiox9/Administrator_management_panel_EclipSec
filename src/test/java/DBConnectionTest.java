package test;

import dao.DBConnection;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBConnectionTest {

    @Test
    public void testConexionExitosa() {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            assertNotNull("La conexión no debe ser null", conn);
            assertFalse("La conexión no debe estar cerrada", conn.isClosed());
            System.out.println("✅ Conexión establecida correctamente");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("❌ No se pudo conectar: " + e.getMessage());
        }
    }
}
