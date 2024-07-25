import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SocietySecuritySystem extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private static final String USER_DATA_FILE = "user_data.txt";
    private static final String ACTIVITY_LOG_FILE = "activity_log.txt";
    private Map<String, String> userData;

    public SocietySecuritySystem() {
        super("Society Security System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        loadUserData();

        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "Login");

        JPanel dashboardPanel = createDashboardPanel();
        mainPanel.add(dashboardPanel, "Dashboard");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "Login");

        setVisible(true);
    }

    // Method to load user data from file
    private void loadUserData() {
        userData = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userData.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save user data to file
    private void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (Map.Entry<String, String> entry : userData.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to log activities
    private void logActivity(String activity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACTIVITY_LOG_FILE, true))) {
            writer.write(activity);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to create the login panel
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(99, 196, 204, 255));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Adding logo image
        ImageIcon logoIcon = new ImageIcon("logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        GridBagConstraints logoGbc = new GridBagConstraints();
        logoGbc.gridx = 0;
        logoGbc.gridy = 0;
        logoGbc.gridwidth = 2;
        logoGbc.anchor = GridBagConstraints.PAGE_START;
        logoGbc.insets = new Insets(0, 0, 10, 0); // Add some space below the logo
        panel.add(logoLabel, logoGbc);

        JLabel titleLabel = new JLabel("Security Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(Color.black);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        panel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        usernameLabel.setForeground(Color.black);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 10); // Add space between label and text field
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0); // Add space between label and text field
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Serif", Font.BOLD, 20));
        passwordLabel.setForeground(Color.black);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 10); // Add space between label and text field
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0); // Add space between label and text field
        panel.add(passwordField, gbc);

        // JPanel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 20px gap between buttons
        buttonPanel.setOpaque(false); // Make the panel transparent to show the background color

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 85, 92));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Serif", Font.BOLD, 16));
        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 85, 92));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Serif", Font.BOLD, 16));
        buttonPanel.add(registerButton);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0); // Add some space above the buttons
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (userData.containsKey(username) && userData.get(username).equals(password)) {
                cardLayout.show(mainPanel, "Dashboard");
                logActivity("User " + username + " logged in.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                logActivity("Failed login attempt for username: " + username);
            }
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (!userData.containsKey(username)) {
                userData.put(username, password);
                saveUserData();
                JOptionPane.showMessageDialog(this, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                logActivity("New user " + username + " registered.");
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                logActivity("Failed registration attempt for username: " + username);
            }
        });

        return panel;
    }

    // Method to create the dashboard panel with feature buttons
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(0, 85, 92));

        JButton cctvButton = createFeatureButton("CCTV Monitoring", "cctv.png");
        JButton parkingButton = createFeatureButton("Parking Assistance", "parking.png");
        JButton fireAlarmButton = createFeatureButton("Fire Alarm System", "fire_alarm.png");
        JButton theftAlarmButton = createFeatureButton("Theft Alarm System", "theft_alarm.png");

        cctvButton.addActionListener(e -> {
            showFeaturePanel(new CCTVPanel(), "CCTV Monitoring");
            logActivity("CCTV Monitoring accessed.");
        });
        parkingButton.addActionListener(e -> {
            showFeaturePanel(new ParkingAssistancePanel(), "Parking Assistance");
            logActivity("Parking Assistance accessed.");
        });
        fireAlarmButton.addActionListener(e -> {
            FireAlarmPanel fireAlarmPanel = new FireAlarmPanel.Builder()
                    .withTitle("Fire Alarm System")
                    .build();
            showFeaturePanel(fireAlarmPanel, "Fire Alarm System");
            logActivity("Fire Alarm System accessed.");
        });

        theftAlarmButton.addActionListener(e -> {
            showFeaturePanel(new TheftAlarmPanel(), "Theft Alarm System");
            logActivity("Theft Alarm System accessed.");
        });

        panel.add(cctvButton);
        panel.add(parkingButton);
        panel.add(fireAlarmButton);
        panel.add(theftAlarmButton);

        return panel;
    }

    // Method to create a styled feature button
    private JButton createFeatureButton(String text, String iconPath) {
        JButton button = new JButton(text, new ImageIcon(iconPath));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBackground(new Color(255, 255, 255));
        button.setForeground(Color.black);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        return button;
    }

    // Method to show a feature panel and integrate back button
    private void showFeaturePanel(JPanel panel, String name) {
        mainPanel.add(panel, name);
        cardLayout.show(mainPanel, name);

        // Add a back button to return to the dashboard
//        JButton backButton = new JButton("Back");
//        backButton.addActionListener(e -> {
//            cardLayout.show(mainPanel, "Dashboard");
//            mainPanel.remove(panel);
//        });
//
//        panel.add(backButton, BorderLayout.SOUTH);
//        panel.revalidate();
//        panel.repaint();
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SocietySecuritySystem::new);
    }
}

