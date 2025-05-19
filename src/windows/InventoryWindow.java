package windows;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import utils.Validator;
import models.Product;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Statement;



class StockStatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        int quantity = Integer.parseInt(table.getValueAt(row, 1).toString()); // Quantity is column 1
        if (quantity < 5) {
            c.setBackground(Color.PINK); // low stock
        } else {
            c.setBackground(Color.WHITE); // normal
        }

        if (isSelected) {
            c.setBackground(Color.LIGHT_GRAY); // keep selection visible
        }

        return c;
    }
}

public class InventoryWindow extends JFrame {
    private JTextField nameField, quantityField, priceField, searchField;
    private JButton addButton, deleteButton, saveButton,updateButton;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JComboBox<String> supplierComboBox; 


    private static String userRole;
    public InventoryWindow(String userRole) {
        this.userRole = userRole;
        setTitle("Manage Inventory");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top Panel - Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.add(new JLabel("Product Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);

        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        addButton = new JButton("Add Product");
        formPanel.add(addButton);

        // Empty label for alignment
        formPanel.add(new JLabel());

        // Center Panel - Table
        // tableModel = new DefaultTableModel(new String[]{"Product Name", "Quantity", "Price", "Status"}, 0);
        tableModel = new DefaultTableModel(new String[]{"Product Name", "Quantity", "Price", "Supplier", "Status"}, 0);
        inventoryTable = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        inventoryTable.setRowSorter(rowSorter);
        inventoryTable.setDefaultRenderer(Object.class, new StockStatusCellRenderer());

        inventoryTable.addMouseListener(new MouseAdapter() {  //It reads the values from that row (product name, quantity, and price) and automatically fills the text fields (nameField, quantityField, priceField) with those values.
            public void mouseClicked(MouseEvent e) {
                int selectedRow = inventoryTable.getSelectedRow();
                if (selectedRow != -1) {
                    nameField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    quantityField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    priceField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);

        // ==== Bottom Panel - Buttons ====
        JPanel buttonPanel = new JPanel(new FlowLayout());
        deleteButton = new JButton("Delete Selected Product");
        saveButton = new JButton("Save Inventory to File");
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        updateButton = new JButton("Update Selected Product");
        buttonPanel.add(updateButton);

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(searchPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        JButton lowStockButton = new JButton("Show Low Stock Only");
        buttonPanel.add(lowStockButton);
        JButton resetFilterButton = new JButton("Show All Products");
        buttonPanel.add(resetFilterButton);
        supplierComboBox = new JComboBox<>();
        formPanel.add(new JLabel("Supplier:"));
        supplierComboBox = new JComboBox<>();
        formPanel.add(supplierComboBox);
        if ("Viewer".equals(userRole)) {
            addButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            saveButton.setEnabled(false);
            searchField.setEnabled(false);
            nameField.setEnabled(false);
            quantityField.setEnabled(false);
            priceField.setEnabled(false);
        }
        JButton exportCsvButton = new JButton("Export to CSV");
        buttonPanel.add(exportCsvButton);
        exportCsvButton.addActionListener(e -> exportToCSV());
        // Layout
        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.NORTH);
        //add(searchPanel, BorderLayout.SOUTH);  // Add Search Panel to the Frame
        add(tableScrollPane, BorderLayout.CENTER);
        //add(buttonPanel, BorderLayout.SOUTH);   // Buttons at Bottom
        add(bottomPanel, BorderLayout.SOUTH);

        // Add Product Action
        // addButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         addProduct();
        //     }
        // });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    //String category = categoryField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    double price = Double.parseDouble(priceField.getText());

                    addProduct(name, quantity, price);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers for quantity and price.");
                }
            }
        });
         // Delete Selected Product Button Action
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedProduct();
            }
        });

        // Save Inventory to File Button Action
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveInventoryToFile();
            }
        });

        // Load inventory from file if exists
        loadInventory();

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }
            
            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSelectedProduct();
            }
        });

        lowStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Filter to show only rows where "Stock Status" column has "Low Stock"
                RowFilter<DefaultTableModel, Object> lowStockFilter = new RowFilter<DefaultTableModel, Object>() {
                    @Override
                    public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                        String status = entry.getStringValue(4); // 3rd index = "Stock Status" column
                        return status.equalsIgnoreCase("Low Stock");
                    }
                };
                rowSorter.setRowFilter(lowStockFilter);
            }
        });      
        
        resetFilterButton.addActionListener(e -> rowSorter.setRowFilter(null));   
        
        loadSuppliers(supplierComboBox);

    }

    // private void addProduct() {
    //     String name = nameField.getText();
    //     String quantity = quantityField.getText();
    //     String price = priceField.getText();

    //     if (name.isEmpty() || quantity.isEmpty() || price.isEmpty()) {
    //         JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
    //         return;
    //     }

    //     tableModel.addRow(new Object[]{name, quantity, price});
    //     clearForm();
    // }
    // private void addProduct() {
    //     String name = nameField.getText();
    //     String quantityStr = quantityField.getText();
    //     String priceStr = priceField.getText();

    //     // if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
    //     //     JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
    //     //     return;
    //     // }
    //     if (Validator.isEmpty(name) || Validator.isEmpty(priceStr) || Validator.isEmpty(quantityStr)) {
    //         JOptionPane.showMessageDialog(null, "All fields are required!");
    //         return;
    //     }
        
    //     try {
    //         int quantity = Integer.parseInt(quantityStr);
    //         double price = Double.parseDouble(priceStr);
    //         String supplier = (String) supplierComboBox.getSelectedItem();
    //         Product product = new Product(name, quantity, price, supplier);
            
    //         // Create a Product object

    //         // Add to table
    //         // tableModel.addRow(new Object[]{
    //         //     product.getName(), product.getQuantity(), product.getPrice()
    //         // });
    //         String status = (quantity < 5) ? "Low Stock" : "In Stock";
    //         tableModel.addRow(new Object[]{
    //             product.getName(), product.getQuantity(), product.getPrice(), supplier, status
    //         });

            
    //         clearForm();
    //     } catch (NumberFormatException e) {
    //         JOptionPane.showMessageDialog(this, "Invalid quantity or price.", "Error", JOptionPane.ERROR_MESSAGE);
    //     }
    // }
    public void addProduct(String name, int quantity, double price) {
    String query = "INSERT INTO inventory (name, quantity, price, supplier) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            //stmt.setString(2, category);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            String supplier = (String) supplierComboBox.getSelectedItem();
            stmt.setString(4, supplier); // Assuming you capture supplier from supplierComboBox
            stmt.executeUpdate();
            stmt.close();
            conn.close();

            System.out.println("Product added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 // Method to delete selected product from the table
    private void deleteSelectedProduct() {
        int selectedRow = inventoryTable.getSelectedRow(); // Get selected row index
        if (selectedRow != -1) { // If a row is selected
            tableModel.removeRow(selectedRow); // Remove the selected row
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to save inventory data to a file
    // private void saveInventoryToFile() {
    //     try {
    //         FileWriter writer = new FileWriter("inventory.txt");

    //         // Write each row from the table to file
    //         for (int i = 0; i < tableModel.getRowCount(); i++) {
    //             writer.write(
    //                 tableModel.getValueAt(i, 0) + "," +
    //                 tableModel.getValueAt(i, 1) + "," +
    //                 tableModel.getValueAt(i, 2) + "\n" 
    //             );
    //         }
    //         writer.close();
    //         JOptionPane.showMessageDialog(this, "Inventory saved successfully!");
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         JOptionPane.showMessageDialog(this, "Error saving inventory!", "Error", JOptionPane.ERROR_MESSAGE);
    //     }
    // }
    // private void saveInventoryToFile() {
    //     try (FileWriter writer = new FileWriter("inventory.txt")) {
    //         for (int i = 0; i < tableModel.getRowCount(); i++) {
    //             String name = tableModel.getValueAt(i, 0).toString();
    //             String quantity = tableModel.getValueAt(i, 1).toString();
    //             String price = tableModel.getValueAt(i, 2).toString();
    //             String supplier = tableModel.getValueAt(i, 3).toString();  // Supplier
    //             writer.write(name + "," + quantity + "," + price + "," + supplier + "\n");
    //         }
    //         JOptionPane.showMessageDialog(this, "Inventory saved successfully!");
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         JOptionPane.showMessageDialog(this, "Error saving inventory!", "Error", JOptionPane.ERROR_MESSAGE);
    //     }
    // }
    
    
    // // Method to load inventory from file at startup
    // private void loadInventoryFromFile() {
    //     File file = new File("inventory.txt");
    //     if (file.exists()) {
    //         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    //             String line;
    //             while ((line = br.readLine()) != null) {
    //                 String[] data = line.split(",");
    //                 if (data.length == 3) {
    //                     String name = data[0];
    //                     int quantity = Integer.parseInt(data[1]);
    //                     double price = Double.parseDouble(data[2]);
    //                     String supplier = data[3]; 
    //                     String status = (quantity < 5) ? "Low Stock" : "In Stock";
    //                     tableModel.addRow(new Object[]{name, quantity, price, supplier, status});
    //                 }
    //             }
    //         } catch (Exception e) {
    //             JOptionPane.showMessageDialog(this, "Error loading inventory!", "Error", JOptionPane.ERROR_MESSAGE);
    //             e.printStackTrace();
    //         }
    //     }
    // }
    // Save inventory to file
    private void saveInventoryToFile() {
        try (FileWriter writer = new FileWriter("inventory.txt")) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String name = tableModel.getValueAt(i, 0).toString();
                String quantity = tableModel.getValueAt(i, 1).toString();
                String price = tableModel.getValueAt(i, 2).toString();
                String supplier = tableModel.getValueAt(i, 3).toString();
                // Also save the status field
                String status = tableModel.getValueAt(i, 4).toString();

                writer.write(name + "," + quantity + "," + price + "," + supplier + "," + status + "\n");
            }
            JOptionPane.showMessageDialog(this, "Inventory saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving inventory!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load inventory from file at startup
    // private void loadInventoryFromFile() {
    //     File file = new File("inventory.txt");
    //     if (file.exists()) {
    //         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    //             String line;
    //             while ((line = br.readLine()) != null) {
    //                 String[] data = line.split(",");
    //                 if (data.length == 5) {
    //                     String name = data[0];
    //                     int quantity = Integer.parseInt(data[1]);
    //                     double price = Double.parseDouble(data[2]);
    //                     String supplier = data[3];
    //                     String status = data[4];

    //                     tableModel.addRow(new Object[]{name, quantity, price, supplier, status});
    //                 }
    //             }
    //         } catch (Exception e) {
    //             JOptionPane.showMessageDialog(this, "Error loading inventory!", "Error", JOptionPane.ERROR_MESSAGE);
    //             e.printStackTrace();
    //         }
    //     }
    // }
    public void loadInventory() {
        String query = "SELECT * FROM inventory";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            tableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String supplier = rs.getString("supplier");
                String status = (quantity < 5) ? "Low Stock" : "In Stock";
                System.out.println(name + " | " + quantity + " | " + price + " | " + supplier); //this below part displays added product in console only
                tableModel.addRow(new Object[]{name, quantity, price, supplier, status}); //this below code shows added product in Inventory window as table

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // private void updateSelectedProduct() {
    //     int selectedRow = inventoryTable.getSelectedRow();
    
    //     if (selectedRow == -1) {
    //         JOptionPane.showMessageDialog(this, "Please select a product to update.", "Error", JOptionPane.ERROR_MESSAGE);
    //         return;
    //     }
    
    //     String name = nameField.getText();
    //     String quantityStr = quantityField.getText();
    //     String priceStr = priceField.getText();
    //     String supplier = (String) supplierComboBox.getSelectedItem();
        
    //     if (Validator.isEmpty(name) || Validator.isEmpty(quantityStr) || Validator.isEmpty(priceStr)) {
    //         JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
    //         return;
    //     }
    
    //     try {
    //         int quantity = Integer.parseInt(quantityStr);
    //         double price = Double.parseDouble(priceStr);
    
    //         tableModel.setValueAt(name, selectedRow, 0);
    //         tableModel.setValueAt(quantity, selectedRow, 1);
    //         tableModel.setValueAt(price, selectedRow, 2);
    //         String status = (quantity < 5) ? "Low Stock" : "In Stock";
    //         tableModel.setValueAt(supplier, selectedRow, 3); // Add this
    //         tableModel.setValueAt(status, selectedRow, 4);
        
    //         clearForm();
    //         inventoryTable.clearSelection();
    //     } catch (NumberFormatException ex) {
    //         JOptionPane.showMessageDialog(this, "Invalid quantity or price format.", "Error", JOptionPane.ERROR_MESSAGE);
    //     }
    // }  
//database format of updateSelectedProduct
    private void updateSelectedProduct() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
            return;
        }

        String originalName = tableModel.getValueAt(selectedRow, 0).toString(); // Use this as identifier
        String newName = nameField.getText();
        int newQuantity = Integer.parseInt(quantityField.getText());
        double newPrice = Double.parseDouble(priceField.getText());
        String newSupplier = (String) supplierComboBox.getSelectedItem();

        String query = "UPDATE inventory SET name = ?, quantity = ?, price = ?, supplier = ? WHERE name = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newName);
            stmt.setInt(2, newQuantity);
            stmt.setDouble(3, newPrice);
            stmt.setString(4, newSupplier);
            stmt.setString(5, originalName);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product updated successfully!");

            loadInventory(); // Refresh table

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSuppliers(JComboBox<String> comboBox) {
        File file = new File("suppliers.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        comboBox.addItem(parts[0]);  // Assuming first value is supplier name
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void exportToCSV() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Specify a file to save");
    
    int userSelection = fileChooser.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();

        // Ensure .csv extension
        String filePath = fileToSave.getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".csv")) {
            fileToSave = new File(filePath + ".csv");
        }

        try (FileWriter fw = new FileWriter(fileToSave)) {
            // Write column headers
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                fw.write(tableModel.getColumnName(i));
                if (i < tableModel.getColumnCount() - 1) fw.write(",");
            }
            fw.write("\n");

            // Write rows
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    fw.write(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) fw.write(",");
                }
                fw.write("\n");
            }

            JOptionPane.showMessageDialog(this, "Inventory exported successfully to " + fileToSave.getName());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting file: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
//  private void initUI() {
//         JPanel panel = new JPanel();

//         JButton addButton = new JButton("Add Item");
//         JButton updatetButton = new JButton("Edit Item");
//         JButton deleteButton= new JButton("Delete Item");

//         // Hide buttons for Viewer
//         if (!"Viewer".equals(userRole)) {
//             panel.add(addButton);
//             panel.add(updatetButton);
//             panel.add(deleteButton);
//         }

//         add(panel);
//     }
    private void clearForm() {
        nameField.setText("");
        quantityField.setText("");
        priceField.setText("");
        supplierComboBox.setSelectedIndex(0);
    }

public static void main(String[] args) {
    InventoryWindow window = new InventoryWindow(userRole);
    window.loadInventory();  // This method prints inventory table data
}

}