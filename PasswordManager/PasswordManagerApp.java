import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class PasswordManagerApp {

    private static PasswordManager passwordManager;

    public static void main(String[] args) {
        passwordManager = new PasswordManager();

        SwingUtilities.invokeLater(() -> {
            createAndShowUI();
        });
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("Password Manager");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create a panel with a custom background image
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Replace this path with the location of your image file
                ImageIcon imageIcon = new ImageIcon("Bg22.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add buttons with transparent background and improved hover effect
        addButton(imagePanel, "Add Password", 0, 0);
        addButton(imagePanel, "Password Creator for Website", 0, 1);
        addButton(imagePanel, "Display All Password", 0, 2);
        addButton(imagePanel, "Autofill Password", 0, 3);
        addButton(imagePanel, "Retrieve Password", 0, 4);
        addButton(imagePanel, "Exit", 0, 5);

        frame.add(imagePanel, BorderLayout.CENTER);
        frame.setVisible(true);
        centerFrameOnScreen(frame);
    }

    private static void addButton(JPanel panel, String text, int x, int y) {
        JButton button = createStyledButton(text);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel.add(button, gbc);

        // Button actions
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform action based on the button clicked
                String buttonText = button.getText();
                switch (buttonText) {
                    case "Add Password":
                        addPasswordUI();
                        break;
                    case "Password Creator for Website":
                        passwordCreatorUI();
                        break;
                    case "Display All Password":
                        displayAllPasswordsUI();
                        break;
                    case "Autofill Password":
                        autofillPasswordUI();
                        break;
                    case "Retrieve Password":
                        retrievePasswordUI();
                        break;
                    case "Exit":
                        System.out.println("Exiting Password Manager. Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        });

        // Add improved hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.RED);
                button.setBorder(BorderFactory.createLineBorder(Color.RED, 1)); // Add a border for the hover effect
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.black);
                button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Reset the border
            }
        });
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return button;
    }

    private static void centerFrameOnScreen(JFrame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }

    private static void addPasswordUI() {
        String website = JOptionPane.showInputDialog("Enter website:");
        String password = JOptionPane.showInputDialog("Enter password:");
        passwordManager.addPassword(website, password);
        JOptionPane.showMessageDialog(null, "Password added successfully for " + website);
    }

    private static void passwordCreatorUI() {
        String website = JOptionPane.showInputDialog("Enter website for password creation:");
        String generatedPassword = passwordManager.createPasswordForWebsite(website);
        JOptionPane.showMessageDialog(null, "Generated Password for " + website + ": " + generatedPassword);
    }

    private static void displayAllPasswordsUI() {
        StringBuilder passwordInfo = new StringBuilder("Stored Passwords:\n");
        for (Map.Entry<String, String> entry : passwordManager.getPasswords().entrySet()) {
            passwordInfo.append("Website: ").append(entry.getKey()).append(" | Password: ").append(entry.getValue()).append("\n");
        }
        JOptionPane.showMessageDialog(null, passwordInfo.toString());
    }

    private static void autofillPasswordUI() {
        String website = JOptionPane.showInputDialog("Enter website to autofill password:");
        String password = passwordManager.autofillPassword(website);
        if (password != null) {
            JOptionPane.showMessageDialog(null, "Autofilling password for " + website + ": " + password);
        } else {
            JOptionPane.showMessageDialog(null, "Password not found for " + website);
        }
    }

    private static void retrievePasswordUI() {
        String website = JOptionPane.showInputDialog("Enter website to retrieve password:");
        String retrievedPassword = passwordManager.retrievePassword(website);
        if (retrievedPassword != null) {
            JOptionPane.showMessageDialog(null, "Retrieved Password for " + website + ": " + retrievedPassword);
        } else {
            JOptionPane.showMessageDialog(null, "Password not found for " + website);
        }
    }

    private static class PasswordManager {

        private Map<String, String> passwords;

        public PasswordManager() {
            passwords = new HashMap<>();
        }

        public Map<String, String> getPasswords() {
            return passwords;
        }

        public void addPassword(String website, String password) {
            passwords.put(website, password);
        }

        public String createPasswordForWebsite(String website) {
            String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String lowerCase = "abcdefghijklmnopqrstuvwxyz";
            String digits = "0123456789";
            String specialChars = "!@#$%^&*()-_=+";
            String allChars = upperCase + lowerCase + digits + specialChars;

            SecureRandom random = new SecureRandom();
            StringBuilder password = new StringBuilder();

            for (int i = 0; i < 10; i++) {
                int randomIndex = random.nextInt(allChars.length());
                password.append(allChars.charAt(randomIndex));
            }

            String generatedPassword = password.toString();
            addPassword(website, generatedPassword);
            return generatedPassword;
        }

        public String autofillPassword(String website) {
            return passwords.get(website);
        }

        public String retrievePassword(String website) {
            return passwords.get(website);
        }
    }
}
