import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;
import java.util.ArrayList;

// Observer interface
interface TheftAlarmObserver {
    void update(double motion);
}

// Subject interface or abstract class
interface TheftAlarmSubject {
    void registerObserver(TheftAlarmObserver observer);
    void removeObserver(TheftAlarmObserver observer);
    void notifyObservers(double motion);
}

// Concrete implementation of TheftAlarmSubject
public class TheftAlarmPanel extends JPanel implements TheftAlarmSubject {
    private static final double MOTION_THRESHOLD = 0.5; // Example motion detection threshold
    private ArrayList<TheftAlarmObserver> observers;
    private JTextArea statusArea;
    private JLabel suspiciousImageLabel;
    private Clip alarmClip;

    public TheftAlarmPanel() {
        setLayout(new BorderLayout());

        // Initialize observers list
        observers = new ArrayList<>();

        // Title label
        JLabel titleLabel = new JLabel("Theft Alarm System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.RED);
        add(titleLabel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Status area
        statusArea = new JTextArea("Theft alarm status: Inactive\n");
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Arial", Font.PLAIN, 18));
        statusArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(statusArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Suspicious activity image label
        ImageIcon suspiciousIcon = new ImageIcon("intruder.png"); // Replace with your image path
        suspiciousImageLabel = new JLabel(suspiciousIcon);
        suspiciousImageLabel.setVisible(false); // Initially hidden
        contentPanel.add(suspiciousImageLabel, BorderLayout.EAST);

        // Activate button
        JButton activateButton = new JButton("Activate Theft Alarm");
        activateButton.setFont(new Font("Arial", Font.BOLD, 18));
        activateButton.setForeground(Color.WHITE);
        activateButton.setBackground(Color.RED);
        activateButton.addActionListener(e -> {
            statusArea.setText("Theft alarm status: Active\n If the Motion intensity > 0.5 then Alarm Rings\n\n");
            statusArea.append("Monitoring: Motion Detection\n");
            simulateTheftDetection();
        });
        contentPanel.add(activateButton, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        // Load audio files
        try {
            File alarmSoundFile = new File("intruder.wav"); // Replace with your alarm sound file path
            if (alarmSoundFile.exists()) {
                AudioInputStream alarmAudioStream = AudioSystem.getAudioInputStream(alarmSoundFile);
                alarmClip = AudioSystem.getClip();
                alarmClip.open(alarmAudioStream);
            } else {
                System.err.println("Alarm sound file not found: " + alarmSoundFile.getAbsolutePath());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void simulateTheftDetection() {
        // Simulate motion detection (example)
        Thread simulationThread = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(2000); // Simulate every 2 seconds
                    double motion = Math.random()*1;

                    // Check if motion threshold is exceeded
                    if (motion > MOTION_THRESHOLD) {
                        statusArea.append(String.format("\nMotion detected: %.2f - Suspicious activity!", motion));
                        notifyObservers(motion); // Notify observers of suspicious activity
                        showSuspiciousImage(true);
                        playAlarmSound();
                    } else {
                        statusArea.append(String.format("\nMotion detected: %.2f", motion));
                        showSuspiciousImage(false);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        simulationThread.start();
    }

    private void showSuspiciousImage(boolean show) {
        SwingUtilities.invokeLater(() -> {
            suspiciousImageLabel.setVisible(show);
            revalidate(); // Ensure layout updates
        });
    }

    private void playAlarmSound() {
        if (alarmClip != null && alarmClip.isRunning()) {
            alarmClip.stop();
        }
        if (alarmClip != null) {
            try {
                alarmClip.setFramePosition(0); // Rewind to the beginning
                alarmClip.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Implement TheftAlarmSubject methods
    @Override
    public void registerObserver(TheftAlarmObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(TheftAlarmObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(double motion) {
        for (TheftAlarmObserver observer : observers) {
            observer.update(motion);
        }
    }

    // Example of using the Observer pattern
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Theft Alarm Panel Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            TheftAlarmPanel theftAlarmPanel = new TheftAlarmPanel();
            TheftMonitoringSystem monitoringSystem = new TheftMonitoringSystem();
            theftAlarmPanel.registerObserver(monitoringSystem); // Register observer

            frame.add(theftAlarmPanel);
            frame.setVisible(true);
        });
    }
}

// Example TheftAlarmObserver implementation (could be a monitoring system)
class TheftMonitoringSystem implements TheftAlarmObserver {
    @Override
    public void update(double motion) {
        // Example action when suspicious motion detected
        System.out.println("Suspicious activity detected! Motion value: " + motion);
        // Additional actions can be added here, like triggering an alarm or alerting authorities
    }
}
