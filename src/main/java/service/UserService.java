package service;

import dao.UserDAO;
import model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO usuarioDAO = new UserDAO();

    public List<User> listarTodos() {
        return usuarioDAO.obtenerTodos();
    }

    public List<User> listarAdmins() {
        return usuarioDAO.obtenerTodos().stream()
                .filter(u -> "admin".equalsIgnoreCase(u.getRole()))
                .collect(Collectors.toList());
    }

    public boolean crearUsuario(User usuario) throws IllegalArgumentException {
        if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
            throw new IllegalArgumentException("Correo inválido");
        }
        // Aquí podrías verificar que el usuario no exista antes de insertar...
        if (usuarioDAO.buscarPorCorreo(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        if (usuario.getPassword() == null || usuario.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        if (usuario.getRole() == null || (!usuario.getRole().equalsIgnoreCase("admin") && !usuario.getRole().equalsIgnoreCase("cliente"))) {
            throw new IllegalArgumentException("Rol inválido, debe ser 'admin' o 'cliente'");
        }

        // Si todas las validaciones pasan, se inserta el usuario
        usuarioDAO.insertar(usuario);
        return true;
    }

    public String verificarCredenciales(String email, String contrasena) {
        User usuario = usuarioDAO.buscarPorCorreo(email);
        if (usuario != null && usuario.getPassword().equals(contrasena)) {
            return usuario.getRole(); // "admin" o "cliente"
        }
        return null;
    }

    public boolean actualizarUsuario(User usuario) {
        if (usuario.getId() <= 0) {
            throw new IllegalArgumentException("ID de usuario inválido");
        }
        if (usuarioDAO.buscarPorId(usuario.getId()) == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        // Aquí podrías agregar más validaciones si es necesario...

        usuarioDAO.actualizar(usuario);
        return true;
    }

    public boolean eliminarUsuario(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de usuario inválido");
        }
        User usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        // Aquí podrías agregar más validaciones si es necesario...

        usuarioDAO.eliminar(id);
        return true;
    }

    // Más métodos con reglas de negocio...
}
