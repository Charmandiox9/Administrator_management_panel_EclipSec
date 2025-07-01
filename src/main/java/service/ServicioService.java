package service;

import dao.ServiceDAO;
import model.Service;

import java.util.List;
import java.util.stream.Collectors;

public class ServicioService {
    private final ServiceDAO serviceDAO = new ServiceDAO();

    public List<Service> listarTodos() {
        return serviceDAO.obtenerTodos();
    }

    public List<Service> listarServicesActives() {
        return serviceDAO.obtenerTodos().stream()
                .filter(s -> s.isActive())
                .collect(Collectors.toList());
    }

    public void crearServicio(Service service) throws IllegalArgumentException {
        if (service.getName() == null || service.getPrice() >= 0) {
            throw new IllegalArgumentException("Nombre o precio inválido");
        }
        // Aquí podrías verificar que el usuario no exista antes de insertar...
        serviceDAO.insertar(service);
    }
}
