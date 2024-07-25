import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CCTVPanel extends JPanel {
    private JTextArea statusArea;
    private JLabel cctvGifLabel;
    private ImageIcon normalGif;
    private ImageIcon alertGif;
    private boolean monitoringActive;

    public CCTVPanel() {
        setLayout(new BorderLayout());
        monitoringActive = false;

        // Title label
        JLabel titleLabel = new JLabel("CCTV Monitoring System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.RED);
        add(titleLabel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Load GIFs
        normalGif = new ImageIcon("normal.gif");
        alertGif = new ImageIcon("alert.gif");

        // CCTV GIF simulation
        cctvGifLabel = new JLabel(normalGif);
        contentPanel.add(cctvGifLabel, BorderLayout.CENTER);

        // Status area
        statusArea = new JTextArea(10, 30);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Arial", Font.PLAIN, 18));
        statusArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(statusArea);
        contentPanel.add(scrollPane, BorderLayout.SOUTH);

        // Add the content panel to the main layout
        add(contentPanel, BorderLayout.CENTER);

        // Activate button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton activateButton = new JButton("Activate CCTV Monitoring");
        activateButton.setFont(new Font("Arial", Font.BOLD, 18));
        activateButton.setForeground(Color.WHITE);
        activateButton.setBackground(Color.DARK_GRAY);
        activateButton.addActionListener(e -> {
            if (!monitoringActive) {
                statusArea.setText("CCTV Monitoring status: Active\n");
                monitoringActive = true;
                simulateCCTVMonitoring();
            }
        });
        buttonPanel.add(activateButton);

        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.RED);
        backButton.addActionListener(e -> {
            monitoringActive = false; // Stop monitoring
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "Dashboard");
        });
        buttonPanel.add(backButton);

        // Add the button panel to the main layout
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void simulateCCTVMonitoring() {
        // Simulate CCTV monitoring status
        new Thread(() -> {
            try {
                while (monitoringActive) {
                    Thread.sleep(3000); // Simulate every 3 seconds
                    SwingUtilities.invokeLater(() -> {
                        if (monitoringActive) {
                            statusArea.append("CCTV Monitoring: Suspicious Activity Detected\n");
                            cctvGifLabel.setIcon(alertGif); // Switch to alert GIF
                            playAlertSound(); // Play alert sound
                        }
                    });
                    Thread.sleep(5000); // Show alert GIF for 5 seconds
                    SwingUtilities.invokeLater(() -> {
                        if (monitoringActive) {
                            statusArea.append("CCTV Monitoring: Normal activity\n");
                            cctvGifLabel.setIcon(normalGif); // Switch to normal GIF
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void playAlertSound() {
        try {
            // Adjust the path to the location of your alert sound file
            File soundFile = new File("cctvalert.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CCTV Panel Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            CCTVPanel cctvPanel = new CCTVPanel();
            frame.add(cctvPanel);

            frame.setVisible(true);
        });
    }
}
