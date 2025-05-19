import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Login extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public Login() {
        setTitle("Inventory Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // String username = usernameField.getText();
        // String password = new String(passwordField.getPassword());

        // if (username.equals("admin") && password.equals("admin123")) {
        //     JOptionPane.showMessageDialog(this, "Login successful!");
        //     dispose(); // close login window
        //     new Dashboard().setVisible(true); // open dashboard
        //     dispose();
        // } else {
        //     JOptionPane.showMessageDialog(this, "Invalid Credentials");
        // }
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        performLogin(username, password);
    }
    private void performLogin(String enteredUsername, String enteredPassword) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];

                    if (username.equals(enteredUsername) && password.equals(enteredPassword)) {
                        JOptionPane.showMessageDialog(this, "Login successful as " + role);
                        this.dispose(); // close login window
                        new Dashboard(role).setVisible(true); // pass role to Dashboard
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid username or password!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading users file.");
        }
    }


    public static void main(String[] args) {
        new Login();
    }
}
