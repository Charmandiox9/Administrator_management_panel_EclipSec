package service;

import dao.HiringDAO;
import model.Hiring;

import java.util.List;
import java.util.stream.Collectors;

public class HiringService {
    private final HiringDAO hiringDAO = new HiringDAO();

    public List<Hiring> listarTodos() {
        return hiringDAO.obtenerTodos();
    }

    public List<Hiring> listarHiringsPendients() {
        return hiringDAO.obtenerTodos().stream()
                .filter(h -> "pendiente".equalsIgnoreCase(h.getStatus()))
                .collect(Collectors.toList());
    }

    public void crearServicio(Hiring hiring) throws IllegalArgumentException {
        if (hiring.getUser_id() >= 0 || hiring.getService_id() >= 0) {
            throw new IllegalArgumentException("Usuario o servicio inválido");
        }
        // Aquí podrías verificar que el usuario no exista antes de insertar...
        hiringDAO.insertar(hiring);
    }
}
