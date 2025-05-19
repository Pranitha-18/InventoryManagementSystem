package windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.awt.event.*;
import java.io.*;
//import java.util.*;

public class UserManagementWindow extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementWindow() {
        setTitle("User Management");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadUsersFromFile();
    }

    private void initUI() {
        String[] columns = {"Username", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add User");
        JButton editButton = new JButton("Edit User");
        JButton deleteButton = new JButton("Delete User");
        JButton saveBtn = new JButton("Save Users");
        saveBtn.addActionListener(e -> saveUsersToFile());
        JButton loadBtn = new JButton("Load Users");
        loadBtn.addActionListener(e -> loadUsersFromFile());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveBtn);
        buttonPanel.add(loadBtn);
        

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> showUserDialog(null));
        editButton.addActionListener(e -> editSelectedUser());
        deleteButton.addActionListener(e -> deleteSelectedUser());
    }
    private void showUserDialog(String[] existingData) {
        JTextField usernameField = new JTextField();
        JTextField roleField = new JTextField();

        if (existingData != null) {
            usernameField.setText(existingData[0]);
            roleField.setText(existingData[1]);
        }

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Role:"));
        panel.add(roleField);

        int result = JOptionPane.showConfirmDialog(this, panel, "User Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String role = roleField.getText().trim();

            if (username.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (existingData == null) {
                tableModel.addRow(new Object[]{username, role});
            } else {
                int selectedRow = userTable.getSelectedRow();
                tableModel.setValueAt(username, selectedRow, 0);
                tableModel.setValueAt(role, selectedRow, 1);
            }

            saveUsersToFile();
        }
    }
    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = tableModel.getValueAt(selectedRow, 0).toString();
            String role = tableModel.getValueAt(selectedRow, 1).toString();
            showUserDialog(new String[]{username, role});
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            saveUsersToFile();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }
    // private void loadUsersFromFile() {
    //     File file = new File("users.txt");
    //     if (!file.exists()) return;

    //     try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    //         String line;
    //         while ((line = br.readLine()) != null) {
    //             String[] parts = line.split(",");
    //             if (parts.length == 2) {
    //                 tableModel.addRow(parts);
    //             }
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    private void loadUsersFromFile() {
        File file = new File("users.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            tableModel.setRowCount(0); // Clear old data
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    tableModel.addRow(new String[]{parts[0], parts[1]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users.");
        }
    }

    // private void saveUsersToFile() {
    //     try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"))) {
    //         for (int i = 0; i < tableModel.getRowCount(); i++) {
    //             String username = tableModel.getValueAt(i, 0).toString();
    //             String role = tableModel.getValueAt(i, 1).toString();
    //             bw.write(username + "," + role);
    //             bw.newLine();
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String username = tableModel.getValueAt(i, 0).toString();
                String role = tableModel.getValueAt(i, 1).toString();
                writer.write(username + "," + role);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Users saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving users.");
        }
    }

}
