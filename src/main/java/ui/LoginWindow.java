package ui;

import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class LoginWindow extends JFrame {
    // Componentes de la interfaz
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel userLabel;
    private JLabel passwordLabel;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JCheckBox rememberCheckBox;
    private JLabel statusLabel;
    private JLabel forgotPasswordLabel;
    private UserService userService = new UserService();
    private String role;

    public LoginWindow() {
        initializeComponents();
        setupLayout();
        setupEvents();
        configureWindow();
    }

    private void initializeComponents() {
        // Panel principal con fondo degradado
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Crear degradado de fondo
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(64, 128, 255),
                        0, getHeight(), new Color(120, 160, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Panel de login con borde redondeado
        loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo blanco con bordes redondeados
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Sombra sutil
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(2, 2, getWidth(), getHeight(), 20, 20);
            }
        };
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setPreferredSize(new Dimension(350, 400));

        // Título principal
        titleLabel = new JLabel("SISTEMA DE GESTIÓN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));

        // Subtítulo
        subtitleLabel = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(102, 102, 102));

        // Etiquetas
        userLabel = new JLabel("Email:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(51, 51, 51));

        passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(51, 51, 51));

        // Campos de entrada
        userField = new JTextField(30);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        userField.setPreferredSize(new Dimension(700, 40));

        passwordField = new JPasswordField(30);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        passwordField.setPreferredSize(new Dimension(700, 40));

        // Checkbox "Recordarme"
        rememberCheckBox = new JCheckBox("Recordar credenciales");
        rememberCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberCheckBox.setForeground(new Color(102, 102, 102));
        rememberCheckBox.setOpaque(false);

        // Botones
        loginButton = new JButton("INICIAR SESIÓN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(45, 100, 200));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(70, 130, 240));
                } else {
                    g2d.setColor(new Color(64, 128, 255));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(250, 45));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cancelButton = new JButton("CANCELAR");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.setForeground(new Color(102, 102, 102));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.setBorderPainted(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Label de estado
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);

        // Label "¿Olvidaste tu contraseña?"
        forgotPasswordLabel = new JLabel("<html><u>¿Olvidaste tu contraseña?</u></html>", SwingConstants.CENTER);
        forgotPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(new Color(64, 128, 255));
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();

        // Layout del panel de login
        gbc.insets = new Insets(15, 30, 5, 30);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 30, 20, 30);
        loginPanel.add(subtitleLabel, gbc);

        // Campo usuario
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 30, 5, 30);
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(userLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 30, 15, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(userField, gbc);

        // Campo contraseña
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 30, 5, 30);
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 30, 15, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(passwordField, gbc);

        // Checkbox recordar
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 30, 15, 30);
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(rememberCheckBox, gbc);

        // Label de estado
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 30, 10, 30);
        loginPanel.add(statusLabel, gbc);

        // Botón login
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(loginButton, gbc);

        // Botón cancelar
        gbc.gridy = 9;
        gbc.insets = new Insets(5, 30, 15, 30);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(cancelButton, gbc);

        // Link olvidar contraseña
        gbc.gridy = 10;
        gbc.insets = new Insets(10, 30, 20, 30);
        loginPanel.add(forgotPasswordLabel, gbc);

        // Agregar panel de login al panel principal
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginPanel, mainGbc);
    }

    private void setupEvents() {
        // Evento para el botón Login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Evento para el botón Cancelar
        cancelButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro que desea salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Evento para Enter en los campos
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };

        userField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        // Evento para limpiar mensaje de error al escribir
        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearStatusMessage();
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearStatusMessage();
            }
        });

        // Evento para "¿Olvidaste tu contraseña?"
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(
                        LoginWindow.this,
                        "Contacte al administrador del sistema para recuperar su contraseña.\n" +
                                "Email: admin@sistema.com\nTeléfono: +56 9 1234 5678",
                        "Recuperar Contraseña",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }

    private void performLogin() {
        String username = userField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validar campos vacíos
        if (username.isEmpty()) {
            showStatusMessage("Por favor ingrese su email", true);
            userField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showStatusMessage("Por favor ingrese su contraseña", true);
            passwordField.requestFocus();
            return;
        }

        // Simular proceso de autenticación
        showStatusMessage("Verificando credenciales...", false);
        loginButton.setEnabled(false);

        // Timer para simular delay de autenticación
        Timer timer = new Timer(1500, e -> {
            loginButton.setEnabled(true);

            // Credenciales de prueba (aquí conectarías con tu base de datos)
            if (isValidCredentials(username, password)) {
                showStatusMessage("¡Bienvenido " + username + "!", false);
                statusLabel.setForeground(new Color(0, 150, 0));

                // Aquí abrirías la ventana principal
                Timer successTimer = new Timer(1000, evt -> {
                    // Cerrar ventana de login y abrir ventana principal
                    dispose();
                    openMainWindow(username);
                });
                successTimer.setRepeats(false);
                successTimer.start();

            } else {
                showStatusMessage("Usuario o contraseña incorrectos", true);
                passwordField.setText("");
                passwordField.requestFocus();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }


    private boolean isValidCredentials(String username, String password) {
        // Credenciales de prueba - reemplazar con validación real

        if(userService.verificarCredenciales(username, password).equalsIgnoreCase("admin")){
            role = userService.verificarCredenciales(username, password);
        }

        return true;
    }

    private void openMainWindow(String username) {
        // Aquí abrirías tu ventana principal
        JOptionPane.showMessageDialog(
                null,
                "¡Login exitoso!\nBienvenido: " + username + "\n\n" +
                        "Aquí se abriría la ventana principal del sistema.",
                "Sistema de Gestión",
                JOptionPane.INFORMATION_MESSAGE
        );

        SwingUtilities.invokeLater(() -> {
            new MainWindow(username);
        });
    }

    private void showStatusMessage(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : new Color(102, 102, 102));
    }

    private void clearStatusMessage() {
        statusLabel.setText(" ");
    }

    private void configureWindow() {
        setTitle("Sistema de Gestión - Login");
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setResizable(false);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Ícono de la aplicación (opcional)
        // setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));

        // Foco inicial en el campo usuario
        SwingUtilities.invokeLater(() -> userField.requestFocus());
    }

    // Método main para probar la ventana
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear y mostrar la ventana en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginWindow().setVisible(true);
        });
    }
}