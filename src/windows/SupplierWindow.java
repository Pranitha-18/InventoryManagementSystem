package windows;
import models.Supplier;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import utils.Validator;


public class SupplierWindow extends JFrame {
    private JTextField nameField, contactField, emailField;
    private JButton addButton, deleteButton, saveButton;
    private JTable supplierTable;
    private DefaultTableModel tableModel;

    public SupplierWindow() {
        setTitle("Manage Suppliers");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ====== Top Form Panel ======
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Supplier Details"));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Contact Number:"));
        contactField = new JTextField();
        formPanel.add(contactField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        addButton = new JButton("Add Supplier");
        formPanel.add(addButton);

        // ====== Table Section ======
        tableModel = new DefaultTableModel(new String[]{"Name", "Contact", "Email"}, 0);
        supplierTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(supplierTable);

        // ====== Bottom Button Panel ======
        JPanel buttonPanel = new JPanel();
        deleteButton = new JButton("Delete Selected");
        saveButton = new JButton("Save to File");
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        // ====== Layout Configuration ======
        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ====== Button Actions ======
        addButton.addActionListener(e -> addSupplier());
        deleteButton.addActionListener(e -> deleteSupplier());
        saveButton.addActionListener(e -> saveToFile());

        // Load suppliers from file at startup
        loadFromFile();
    }

    private void addSupplier() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();

        if (Validator.isEmpty(name) || Validator.isEmpty(contact) || Validator.isEmpty(email)) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (!Validator.isValidPhoneNumber(contact)) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (!Validator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    

        Supplier supplier = new Supplier(name, contact, email);
        tableModel.addRow(new Object[]{supplier.getName(), supplier.getContact(), supplier.getEmail()});
        clearForm();
    }

    private void deleteSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a supplier to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToFile() {
        try (FileWriter fw = new FileWriter("suppliers.txt")) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                fw.write(
                    tableModel.getValueAt(i, 0) + "," +
                    tableModel.getValueAt(i, 1) + "," +
                    tableModel.getValueAt(i, 2) + "\n"
                );
            }
            JOptionPane.showMessageDialog(this, "Suppliers saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFromFile() {
        File file = new File("suppliers.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    tableModel.addRow(new Object[]{data[0], data[1], data[2]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        contactField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupplierWindow().setVisible(true));
    }
}
