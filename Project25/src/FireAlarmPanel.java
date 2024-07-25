import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FireAlarmPanel extends JPanel {

    private static final double TEMPERATURE_THRESHOLD = 65.0; // Example threshold values
    private static final double SMOKE_LEVEL_THRESHOLD = 50.0;

    private JTextArea statusArea;
    private JLabel imageLabel;

    private FireAlarmPanel(String title) {
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.RED);
        add(titleLabel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Status area
        statusArea = new JTextArea("Fire alarm status: Inactive\n");
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Arial", Font.PLAIN, 18));
        statusArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(statusArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Image label
        imageLabel = new JLabel(new ImageIcon("firealm.png"));
        imageLabel.setVisible(false); // Initially hidden
        contentPanel.add(imageLabel, BorderLayout.EAST);

        // Activate button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton activateButton = new JButton("Activate Fire Alarm");
        activateButton.setFont(new Font("Arial", Font.BOLD, 18));
        activateButton.setForeground(Color.WHITE);
        activateButton.setBackground(Color.RED);
        activateButton.addActionListener(e -> {
            statusArea.setText("Fire alarm status: Active\n");
            statusArea.append("Monitoring: Temperature, Smoke Level\n");
            simulateFireDetection();
        });
        buttonPanel.add(activateButton);

        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.RED);
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "Dashboard");
        });
        buttonPanel.add(backButton);

        // Add the button panel to the main layout
        add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void simulateFireDetection() {
        // Simulate fire detection (example)
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(2000); // Simulate every 2 seconds
                    double temperature = Math.random() * 70;
                    double smokeLevel = Math.random() * 60;

                    // Check if thresholds are exceeded
                    if (temperature > TEMPERATURE_THRESHOLD || smokeLevel > SMOKE_LEVEL_THRESHOLD) {
                        statusArea.append(String.format("\nTemperature: %.2f°C, Smoke Level: %.2f - Fire Detected!", temperature, smokeLevel));
                        imageLabel.setVisible(true); // Show the image when alarm is triggered
                        playAlertSound(); // Play alert sound
                    } else {
                        statusArea.append(String.format("\nTemperature: %.2f°C, Smoke Level: %.2f - No Fire Detected.", temperature, smokeLevel));
                        imageLabel.setVisible(false); // Hide the image when no alarm
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void playAlertSound() {
        try {
            // Adjust the path to the location of your alert sound file
            File soundFile = new File("fire.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static class Builder {
        private String title;

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public FireAlarmPanel build() {
            return new FireAlarmPanel(title);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Fire Alarm Panel Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            FireAlarmPanel fireAlarmPanel = new FireAlarmPanel.Builder()
                    .withTitle("Fire Alarm System")
                    .build();

            frame.add(fireAlarmPanel);
            frame.setVisible(true);
        });
    }
}
