package tyss.in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminSignInGui extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox acceptConditions;
    private JButton loginButton;

    public AdminSignInGui() {
        setTitle("Admin Sign In Page");
        setSize(1600, 950);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        setContentPane(createBackgroundPanel());
        mainPanel.setOpaque(false);

        mainPanel.add(Box.createVerticalStrut(100));

        JLabel signInLabel = new JLabel("Admin Sign In Page", JLabel.CENTER);
        signInLabel.setFont(new Font("Arial", Font.BOLD, 80));
        signInLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInLabel.setForeground(Color.white);
        mainPanel.add(signInLabel);


        mainPanel.add(Box.createVerticalStrut(100));
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel uname = new JLabel("Username:");
        uname.setFont(new Font("Arial", Font.PLAIN, 24));
        usernamePanel.add(uname);
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameField.setPreferredSize(new Dimension(300, 30));
        usernamePanel.add(usernameField);
        usernamePanel.setOpaque(false);
        uname.setForeground(Color.white);
        mainPanel.add(usernamePanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        passwordPanel.add(passwordLabel);
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordPanel.add(passwordField);
        passwordPanel.setOpaque(false);
        mainPanel.add(passwordPanel);
        passwordLabel.setForeground(Color.white);

        mainPanel.add(Box.createVerticalStrut(40));
        acceptConditions = new JCheckBox("Accept Terms and Conditions");
        acceptConditions.setFont(new Font("Arial", Font.PLAIN, 20));
        acceptConditions.setAlignmentX(Component.CENTER_ALIGNMENT);
        acceptConditions.setOpaque(false);
        acceptConditions.setForeground(Color.WHITE);
        mainPanel.add(acceptConditions);

        mainPanel.add(Box.createVerticalStrut(40));
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 20));
        loginButton.setPreferredSize(new Dimension(100, 40));
        loginButton.setOpaque(false);
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);

        mainPanel.add(buttonPanel);
        buttonPanel.setOpaque(false);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (e.getSource() == loginButton) {
            if (!acceptConditions.isSelected()) {
                JOptionPane.showMessageDialog(this, "You must accept the terms and conditions", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (!validateUsername(username)) {
                JOptionPane.showMessageDialog(this, "Invalid username. Must be more than 3 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } 
            
            else {
                if (validateCredentials(username, password)) {
                    JOptionPane.showMessageDialog(this, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean validateCredentials(String username, String password) {
        try (Connection con = ConnectionJdbc.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM admin_sign_in WHERE username = ? AND password = ?")) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private JPanel createBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/logo.jpg"));
                Image image = imageIcon.getImage();
                double panelWidth = getWidth();
                double panelHeight = getHeight();
                double imageWidth = image.getWidth(this);
                double imageHeight = image.getHeight(this);
                double scale = Math.max(panelWidth / imageWidth, panelHeight / imageHeight);
                int scaledWidth = (int) (imageWidth * scale);
                int scaledHeight = (int) (imageHeight * scale);
                int x = (int) ((panelWidth - scaledWidth) / 2);
                int y = (int) ((panelHeight - scaledHeight) / 2);
                g.drawImage(image, x, y, scaledWidth, scaledHeight, this);
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    public boolean validateUsername(String username) {
        return username != null && username.length() > 3;
    }
    

}