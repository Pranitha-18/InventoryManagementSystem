package windows;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;


public class ReportsWindow extends JFrame {
    public ReportsWindow() {
        setTitle("Inventory Reports");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton inventoryReportBtn = new JButton("Inventory Report");
        JButton lowStockBtn = new JButton("Low Stock Report");
        JButton supplierReportBtn = new JButton("Supplier-wise Report");
        JButton logReportBtn = new JButton("Date-based Logs");

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.add(inventoryReportBtn);
        panel.add(lowStockBtn);
        panel.add(supplierReportBtn);
        panel.add(logReportBtn);

        add(panel);

        // Add action listeners
        inventoryReportBtn.addActionListener(e -> showInventoryReport());
        lowStockBtn.addActionListener(e -> showLowStockReport());
        supplierReportBtn.addActionListener(e -> showSupplierReport());
        logReportBtn.addActionListener(e -> showLogReport()); // Optional
    }
    private List<String[]> readInventoryData() {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("inventory.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length >= 4) {
                    data.add(row); // name, qty, price, supplier
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void showInventoryReport() {
        List<String[]> data = readInventoryData();
        String[] columns = {"Product", "Quantity", "Price", "Supplier"};
        showTableDialog("Inventory Report", columns, data);
    }
    private void showLowStockReport() {
        List<String[]> data = readInventoryData();
        List<String[]> lowStock = data.stream()
            .filter(row -> Integer.parseInt(row[1]) < 5)
            .collect(Collectors.toList());
        showTableDialog("Low Stock Report", new String[]{"Product", "Quantity", "Price", "Supplier"}, lowStock);
    }
    private void showSupplierReport() {
        Map<String, List<String[]>> grouped = new HashMap<>();
        for (String[] row : readInventoryData()) {
            grouped.computeIfAbsent(row[3], k -> new ArrayList<>()).add(row);
        }

        for (String supplier : grouped.keySet()) {
            List<String[]> supplierItems = grouped.get(supplier);
            showTableDialog("Report - " + supplier, new String[]{"Product", "Quantity", "Price"}, supplierItems);
        }
    }
    private void showLogReport() {
        File logFile = new File("inventory_log.txt");
        if (!logFile.exists()) {
            JOptionPane.showMessageDialog(this, "No log data found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<String[]> logEntries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming log format: [timestamp] action details
                logEntries.add(new String[]{line});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading log file!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Convert to Object[][] for JTable
        // String[] columnNames = {"Log Entry"};
        // Object[][] rowData = new Object[logEntries.size()][1];
        // for (int i = 0; i < logEntries.size(); i++) {
        //     rowData[i][0] = logEntries.get(i)[0];
        // }

        showTableDialog("Log Report", new String[]{"Log Entry"}, logEntries);
    }

    private void showTableDialog(String title, String[] columnNames, List<String[]> rowData) {
        JDialog dialog = new JDialog(this, title, true);
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    
        for (String[] row : rowData) {
            model.addRow(row);  // each row is a String[] (one-column array in this case)
        }
    
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane);
    
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    // private void showTableDialog(String title, String[] columns, Object[][] data) {
    //     JDialog dialog = new JDialog(this, title, true);
    //     DefaultTableModel model = new DefaultTableModel(data, columns);
    //     JTable table = new JTable(model);
    //     dialog.add(new JScrollPane(table));
    //     dialog.setSize(500, 300);
    //     dialog.setLocationRelativeTo(this);
    //     dialog.setVisible(true);
    // }
    
    
}
