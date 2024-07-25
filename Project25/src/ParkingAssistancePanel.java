import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Component interface
interface ParkingSlot {
    void enterVehicle();
    int getAvailableSlots();
    String getType();
}

// Leaf classes
class BikeSlot implements ParkingSlot {
    private int availableSlots;

    public BikeSlot(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public void enterVehicle() {
        if (availableSlots > 0) {
            availableSlots--;
        }
    }

    @Override
    public int getAvailableSlots() {
        return availableSlots;
    }

    @Override
    public String getType() {
        return "Bike";
    }
}

class CarSlot implements ParkingSlot {
    private int availableSlots;

    public CarSlot(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public void enterVehicle() {
        if (availableSlots > 0) {
            availableSlots--;
        }
    }

    @Override
    public int getAvailableSlots() {
        return availableSlots;
    }

    @Override
    public String getType() {
        return "Car";
    }
}

class HeavyVehicleSlot implements ParkingSlot {
    private int availableSlots;

    public HeavyVehicleSlot(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public void enterVehicle() {
        if (availableSlots > 0) {
            availableSlots--;
        }
    }

    @Override
    public int getAvailableSlots() {
        return availableSlots;
    }

    @Override
    public String getType() {
        return "Heavy Vehicle";
    }
}

// Composite class
class ParkingSlotsComposite implements ParkingSlot {
    private List<ParkingSlot> parkingSlots = new ArrayList<>();
    private String type;

    public ParkingSlotsComposite(String type) {
        this.type = type;
    }

    public void add(ParkingSlot parkingSlot) {
        parkingSlots.add(parkingSlot);
    }

    @Override
    public void enterVehicle() {
        // Enter vehicle in the first available slot type
        for (ParkingSlot slot : parkingSlots) {
            if (slot.getAvailableSlots() > 0) {
                slot.enterVehicle();
                break;
            }
        }
    }

    @Override
    public int getAvailableSlots() {
        int availableSlots = 0;
        for (ParkingSlot slot : parkingSlots) {
            availableSlots += slot.getAvailableSlots();
        }
        return availableSlots;
    }

    @Override
    public String getType() {
        return type;
    }
}

public class ParkingAssistancePanel extends JPanel {
    private JTextArea statusArea;
    private ParkingSlotsComposite bikeSlots;
    private ParkingSlotsComposite carSlots;
    private ParkingSlotsComposite heavyVehicleSlots;
    private JLabel vehicleImageLabel;
    private JLabel vehicleArrivalLabel;

    public ParkingAssistancePanel() {
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Parking Assistance System", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLUE);
        add(titleLabel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Status area
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Arial", Font.PLAIN, 19));
        statusArea.setForeground(Color.BLACK);
        JScrollPane statusScrollPane = new JScrollPane(statusArea);
        contentPanel.add(statusScrollPane, BorderLayout.CENTER);

        // Initialize parking slots
        bikeSlots = new ParkingSlotsComposite("Bike");
        carSlots = new ParkingSlotsComposite("Car");
        heavyVehicleSlots = new ParkingSlotsComposite("Heavy Vehicle");

        for (int i = 0; i < 20; i++) {
            bikeSlots.add(new BikeSlot(1));
            carSlots.add(new CarSlot(1));
        }
        for (int i = 0; i < 10; i++) {
            heavyVehicleSlots.add(new HeavyVehicleSlot(1));
        }

        // Vehicle arrival label
        vehicleArrivalLabel = new JLabel("", JLabel.CENTER);
        vehicleArrivalLabel.setFont(new Font("Serif", Font.BOLD, 24));
        vehicleArrivalLabel.setForeground(Color.RED);
        contentPanel.add(vehicleArrivalLabel, BorderLayout.NORTH);

        // Vehicle image label
        vehicleImageLabel = new JLabel();
        vehicleImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(vehicleImageLabel, BorderLayout.EAST);

        // Activate button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton activateButton = new JButton("Activate Parking Assistance");
        activateButton.setFont(new Font("Serif", Font.BOLD, 18));
        activateButton.setForeground(Color.WHITE);
        activateButton.setBackground(Color.BLUE);
        activateButton.addActionListener(e -> {
            statusArea.setText("Parking Assistance status: Active\n");
            displayInitialSlots();
            simulateParkingStatus();
        });
        buttonPanel.add(activateButton);

        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Serif", Font.BOLD, 18));
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

    private void displayInitialSlots() {
        statusArea.setText(String.format("Bike slots available: %d\n", bikeSlots.getAvailableSlots()));
        statusArea.append(String.format("Car slots available: %d\n", carSlots.getAvailableSlots()));
        statusArea.append(String.format("Heavy Vehicle slots available: %d\n", heavyVehicleSlots.getAvailableSlots()));
    }

    private void simulateParkingStatus() {
        // Simulate parking slot availability
        Thread simulationThread = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(2000); // Simulate every 2 seconds
                    int vehicleType = (int) (Math.random() * 3); // 0 - bike, 1 - car, 2 - heavy vehicle

                    SwingUtilities.invokeLater(() -> {
                        switch (vehicleType) {
                            case 0:
                                bikeSlots.enterVehicle();
                                statusArea.append(String.format("\nBike entered. Bike slots available: %d", bikeSlots.getAvailableSlots()));
                                displayVehicleArrival("Bike");
                                break;
                            case 1:
                                carSlots.enterVehicle();
                                statusArea.append(String.format("\nCar entered. Car slots available: %d", carSlots.getAvailableSlots()));
                                displayVehicleArrival("Car");
                                break;
                            case 2:
                                heavyVehicleSlots.enterVehicle();
                                statusArea.append(String.format("\nHeavy Vehicle entered. Heavy Vehicle slots available: %d", heavyVehicleSlots.getAvailableSlots()));
                                displayVehicleArrival("Heavy Vehicle");
                                break;
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        simulationThread.start();
    }

    private void displayVehicleArrival(String vehicleType) {
        vehicleArrivalLabel.setText(String.format("<html><b>%s Arrived</b></html>", vehicleType));
        switch (vehicleType) {
            case "Bike":
                vehicleImageLabel.setIcon(new ImageIcon("bike.png"));
                break;
            case "Car":
                vehicleImageLabel.setIcon(new ImageIcon("car.png"));
                break;
            case "Heavy Vehicle":
                vehicleImageLabel.setIcon(new ImageIcon("heavy_vehicle.png"));
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Parking Assistance Panel Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            ParkingAssistancePanel parkingAssistancePanel = new ParkingAssistancePanel();
            frame.add(parkingAssistancePanel);
            frame.setVisible(true);
        });
    }
}
