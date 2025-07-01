package ui;

import model.Hiring;
import model.Service;
import model.User;
import service.HiringService;
import service.ServicioService;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame {
    private String currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private UserService userService = new UserService();
    private ServicioService servicioService = new ServicioService();
    private HiringService hiringService = new HiringService();

    // Paneles para cada sección
    private JPanel usuariosPanel;
    private JPanel serviciosPanel;
    private JPanel contratacionesPanel;
    private JPanel dashboardPanel;

    // Botones que necesitan event listeners
    private JButton logoutButton;
    private JButton addUserBtn;
    private JButton editUserBtn;
    private JButton deleteUserBtn;
    private JTable userTable;
    private DefaultTableModel userTableModel;

    public MainWindow(String username) {
        this.currentUser = username;
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Sistema de Gestión - Usuario: " + currentUser);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Configurar CardLayout para cambiar entre paneles
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Crear paneles para cada sección
        createDashboardPanel();
        createUsuariosPanel();
        createServiciosPanel();
        createContratacionesPanel();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel superior con información del usuario y logout
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panel lateral con menú de navegación
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        // Panel central con contenido
        add(contentPanel, BorderLayout.CENTER);

        // Mostrar dashboard por defecto
        cardLayout.show(contentPanel, "dashboard");
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Título
        JLabel titleLabel = new JLabel("Sistema de Gestión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        // Panel derecho con info del usuario
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Bienvenido: " + currentUser);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        logoutButton = new JButton("Cerrar Sesión"); // Asignar a la variable de instancia
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);

        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(logoutButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(44, 62, 80));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Botones del menú
        JButton dashboardBtn = createMenuButton("🏠 Dashboard", "dashboard");
        JButton usuariosBtn = createMenuButton("👥 Usuarios", "usuarios");
        JButton serviciosBtn = createMenuButton("🔧 Servicios", "servicios");
        JButton contratacionesBtn = createMenuButton("📋 Contrataciones", "contrataciones");

        sidebarPanel.add(dashboardBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(usuariosBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(serviciosBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(contratacionesBtn);
        sidebarPanel.add(Box.createVerticalGlue());

        return sidebarPanel;
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        button.addActionListener(e -> cardLayout.show(contentPanel, cardName));

        return button;
    }

    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dashboard - Resumen del Sistema");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel con estadísticas
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));

        int suma = 0;
        for(Hiring hiring: hiringService.listarTodos()) {
            for(Service service: servicioService.listarTodos()) {
                if (hiring.getService_id() == service.getId()) {
                    suma += (int) service.getPrice();
                }
            }
        }

        statsPanel.add(createStatsCard("Total Usuarios", String.valueOf(userService.listarTodos().size()), new Color(52, 152, 219)));
        statsPanel.add(createStatsCard("Servicios Activos", String.valueOf(servicioService.listarTodos().size()), new Color(46, 204, 113)));
        statsPanel.add(createStatsCard("Contrataciones", String.valueOf(hiringService.listarTodos().size()), new Color(155, 89, 182)));
        statsPanel.add(createStatsCard("Ingresos Mes", String.valueOf(suma), new Color(230, 126, 34)));

        dashboardPanel.add(titleLabel, BorderLayout.NORTH);
        dashboardPanel.add(statsPanel, BorderLayout.CENTER);

        contentPanel.add(dashboardPanel, "dashboard");
    }

    private JPanel createStatsCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void createUsuariosPanel() {
        usuariosPanel = new JPanel(new BorderLayout());
        usuariosPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Gestión de Usuarios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addUserBtn = new JButton("Agregar Usuario");
        editUserBtn = new JButton("Editar Usuario");
        deleteUserBtn = new JButton("Eliminar Usuario");

        addUserBtn.setBackground(new Color(46, 204, 113));
        addUserBtn.setForeground(Color.WHITE);
        addUserBtn.setBorderPainted(false);

        editUserBtn.setBackground(new Color(52, 152, 219));
        editUserBtn.setForeground(Color.WHITE);
        editUserBtn.setBorderPainted(false);

        deleteUserBtn.setBackground(new Color(231, 76, 60));
        deleteUserBtn.setForeground(Color.WHITE);
        deleteUserBtn.setBorderPainted(false);

        buttonPanel.add(addUserBtn);
        buttonPanel.add(editUserBtn);
        buttonPanel.add(deleteUserBtn);

        // Tabla de usuarios
        String[] columns = {"ID", "Nombre", "Email", "Passowrd", "Rol"};
        Object[][] data = cargarDatosUsuarios();

        userTableModel = new DefaultTableModel(data, columns);
        userTable = new JTable(userTableModel);
        userTable.setRowHeight(30);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(userTable);

        usuariosPanel.add(titleLabel, BorderLayout.NORTH);
        usuariosPanel.add(buttonPanel, BorderLayout.CENTER);
        usuariosPanel.add(scrollPane, BorderLayout.SOUTH);

        contentPanel.add(usuariosPanel, "usuarios");
    }

    private void createServiciosPanel() {
        serviciosPanel = new JPanel(new BorderLayout());
        serviciosPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Gestión de Servicios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addServiceBtn = new JButton("Nuevo Servicio");
        JButton editServiceBtn = new JButton("Editar Servicio");
        JButton deleteServiceBtn = new JButton("Eliminar Servicio");

        addServiceBtn.setBackground(new Color(46, 204, 113));
        addServiceBtn.setForeground(Color.WHITE);
        addServiceBtn.setBorderPainted(false);

        editServiceBtn.setBackground(new Color(52, 152, 219));
        editServiceBtn.setForeground(Color.WHITE);
        editServiceBtn.setBorderPainted(false);

        deleteServiceBtn.setBackground(new Color(231, 76, 60));
        deleteServiceBtn.setForeground(Color.WHITE);
        deleteServiceBtn.setBorderPainted(false);

        buttonPanel.add(addServiceBtn);
        buttonPanel.add(editServiceBtn);
        buttonPanel.add(deleteServiceBtn);

        // Tabla de servicios
        String[] columns = {"ID", "Nombre", "Descripción", "Precio", "Estado", "Fecha Creación"};
        Object[][] data = cargarDatosServicios();

        JTable serviceTable = new JTable(data, columns);
        serviceTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(serviceTable);

        serviciosPanel.add(titleLabel, BorderLayout.NORTH);
        serviciosPanel.add(buttonPanel, BorderLayout.CENTER);
        serviciosPanel.add(scrollPane, BorderLayout.SOUTH);

        contentPanel.add(serviciosPanel, "servicios");
    }

    private void createContratacionesPanel() {
        contratacionesPanel = new JPanel(new BorderLayout());
        contratacionesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Gestión de Contrataciones");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addContractBtn = new JButton("Nueva Contratación");
        JButton editContractBtn = new JButton("Editar Contratación");
        JButton viewContractBtn = new JButton("Ver Detalles");

        addContractBtn.setBackground(new Color(46, 204, 113));
        addContractBtn.setForeground(Color.WHITE);
        addContractBtn.setBorderPainted(false);

        editContractBtn.setBackground(new Color(52, 152, 219));
        editContractBtn.setForeground(Color.WHITE);
        editContractBtn.setBorderPainted(false);

        viewContractBtn.setBackground(new Color(155, 89, 182));
        viewContractBtn.setForeground(Color.WHITE);
        viewContractBtn.setBorderPainted(false);

        buttonPanel.add(addContractBtn);
        buttonPanel.add(editContractBtn);
        buttonPanel.add(viewContractBtn);

        // Tabla de contrataciones
        String[] columns = {"ID", "Cliente", "Servicio", "Fecha Contratación", "Comentario", "Estado"};
        Object[][] data = cargarDatosContrataciones();

        JTable contractTable = new JTable(data, columns);
        contractTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(contractTable);

        contratacionesPanel.add(titleLabel, BorderLayout.NORTH);
        contratacionesPanel.add(buttonPanel, BorderLayout.CENTER);
        contratacionesPanel.add(scrollPane, BorderLayout.SOUTH);

        contentPanel.add(contratacionesPanel, "contrataciones");
    }

    private void setupEventListeners() {
        // Event listener para el botón de logout
        logoutButton.addActionListener(e -> logout());

        // Aquí puedes agregar más event listeners para otros botones
        // Por ejemplo, si declaras más botones como variables de instancia
        // Event listeners para gestión de usuarios
        addUserBtn.addActionListener(e -> agregarUsuario());
        editUserBtn.addActionListener(e -> editarUsuario());
        deleteUserBtn.addActionListener(e -> eliminarUsuario());
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            dispose();
            // Aquí abrirías nuevamente la ventana de login
            new LoginWindow().setVisible(true);
        }
    }

    // Método para cargar datos de usuarios desde la base de datos
    private Object[][] cargarDatosUsuarios() {
        try {
            List<User> usuarios = userService.listarTodos();
            Object[][] data = new Object[usuarios.size()][5];

            for (int i = 0; i < usuarios.size(); i++) {
                User user = usuarios.get(i);
                data[i][0] = user.getId();
                data[i][1] = user.getName();
                data[i][2] = user.getEmail();
                data[i][3] = user.getPassword();
                data[i][4] = user.getRole();
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, retornar datos de ejemplo
            return new Object[][]{
                    {"Error", "No se pudieron cargar", "los datos", "de la", "base de datos"}
            };
        }
    }

    // Método para cargar datos de usuarios desde la base de datos
    private Object[][] cargarDatosServicios() {
        try {
            List<Service> servicios = servicioService.listarTodos();
            Object[][] data = new Object[servicios.size()][6];

            for (int i = 0; i < servicios.size(); i++) {
                Service service = servicios.get(i);
                data[i][0] = service.getId();
                data[i][1] = service.getName();
                data[i][2] = service.getDescription();
                data[i][3] = service.getPrice();
                data[i][4] = service.isActive();
                data[i][5] = service.getCreated_at();
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, retornar datos de ejemplo
            return new Object[][]{
                    {"Error", "No se pudieron cargar", "los datos", "de la", "base de datos"}
            };
        }
    }

    // Método para cargar datos de usuarios desde la base de datos
    private Object[][] cargarDatosContrataciones() {
        try {
            List<Hiring> contrataciones = hiringService.listarTodos();
            Object[][] data = new Object[contrataciones.size()][6];

            for (int i = 0; i < contrataciones.size(); i++) {
                Hiring contratacion = contrataciones.get(i);
                data[i][0] = contratacion.getId();
                for(User user : userService.listarTodos()) {
                    if (contratacion.getUser_id() == user.getId()) {
                        data[i][1] = user.getName();
                        break;
                    }
                }
                for(Service service : servicioService.listarTodos()) {
                    if (contratacion.getService_id() == service.getId()) {
                        data[i][2] = service.getName();
                        break;
                    }
                }
                data[i][3] = contratacion.getApplication_date();
                data[i][4] = contratacion.getCommentary();
                data[i][5] = contratacion.getStatus();
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, retornar datos de ejemplo
            return new Object[][]{
                    {"Error", "No se pudieron cargar", "los datos", "de la", "base de datos"}
            };
        }
    }

    // ========== MÉTODOS PARA GESTIÓN DE USUARIOS ==========

    private void agregarUsuario() {
        JDialog dialog = new JDialog(this, "Agregar Usuario", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campos del formulario
        JTextField nombreField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> rolCombo = new JComboBox<>(new String[]{"Usuario", "Admin"});

        // Layout del formulario
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(nombreField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(rolCombo, gbc);

        // Botones
        JPanel buttonPanel = new JPanel();
        JButton guardarBtn = new JButton("Guardar");
        JButton cancelarBtn = new JButton("Cancelar");

        guardarBtn.setBackground(new Color(46, 204, 113));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setBorderPainted(false);

        cancelarBtn.setBackground(new Color(149, 165, 166));
        cancelarBtn.setForeground(Color.WHITE);
        cancelarBtn.setBorderPainted(false);

        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        // Event listeners
        guardarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String rol = (String) rolCombo.getSelectedItem();

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userService.crearUsuario(new User(Integer.MAX_VALUE, nombre, email, password, rol))) {
                JOptionPane.showMessageDialog(dialog, "Usuario agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTablaUsuarios();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al agregar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelarBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void editarUsuario() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos del usuario seleccionado
        int id = (Integer) userTableModel.getValueAt(selectedRow, 0);
        String nombre = (String) userTableModel.getValueAt(selectedRow, 1);
        String email = (String) userTableModel.getValueAt(selectedRow, 2);
        String password = (String) userTableModel.getValueAt(selectedRow, 3);
        String rol = (String) userTableModel.getValueAt(selectedRow, 4);

        JDialog dialog = new JDialog(this, "Editar Usuario", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campos del formulario con datos actuales
        JTextField nombreField = new JTextField(nombre, 20);
        JTextField emailField = new JTextField(email, 20);
        JPasswordField passwordField = new JPasswordField(password, 20);
        JComboBox<String> rolCombo = new JComboBox<>(new String[]{"Usuario", "Admin"});
        rolCombo.setSelectedItem(rol);

        // Layout del formulario
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(nombreField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(rolCombo, gbc);

        // Botones
        JPanel buttonPanel = new JPanel();
        JButton actualizarBtn = new JButton("Actualizar");
        JButton cancelarBtn = new JButton("Cancelar");

        actualizarBtn.setBackground(new Color(52, 152, 219));
        actualizarBtn.setForeground(Color.WHITE);
        actualizarBtn.setBorderPainted(false);

        cancelarBtn.setBackground(new Color(149, 165, 166));
        cancelarBtn.setForeground(Color.WHITE);
        cancelarBtn.setBorderPainted(false);

        buttonPanel.add(actualizarBtn);
        buttonPanel.add(cancelarBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        // Event listeners
        actualizarBtn.addActionListener(e -> {
            String nuevoNombre = nombreField.getText().trim();
            String nuevoEmail = emailField.getText().trim();
            String nuevoPassword = new String(passwordField.getPassword());
            String nuevoRol = (String) rolCombo.getSelectedItem();

            if (nuevoNombre.isEmpty() || nuevoEmail.isEmpty() || nuevoPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userService.actualizarUsuario(new User(id, nuevoNombre, nuevoEmail, nuevoPassword, nuevoRol))) {
                JOptionPane.showMessageDialog(dialog, "Usuario actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTablaUsuarios();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al actualizar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelarBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void eliminarUsuario() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) userTableModel.getValueAt(selectedRow, 0);
        String nombre = (String) userTableModel.getValueAt(selectedRow, 1);

        int option = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar al usuario '" + nombre + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            if (userService.eliminarUsuario(id)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTablaUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refrescarTablaUsuarios() {
        Object[][] nuevosDatos = cargarDatosUsuarios();
        userTableModel.setDataVector(nuevosDatos, new String[]{"ID", "Nombre", "Email", "Contraseña", "Rol"});
    }
}