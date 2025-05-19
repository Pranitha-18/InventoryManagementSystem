import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import windows.InventoryWindow;
import windows.ReportsWindow;
import windows.SupplierWindow;
import windows.UserManagementWindow;

public class Dashboard extends JFrame {
    private String userRole;
    // public Dashboard(String role) {
    //     this.userRole = role;
    //     setTitle("Dashboard - Logged in as " + userRole);
    //     //initComponents();
    //     setTitle("Inventory Management System - Dashboard");
    //     setSize(400, 300);
    //     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     setLocationRelativeTo(null);

    //     JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

    //     JButton inventoryBtn = new JButton("Manage Inventory");
    //     JButton supplierBtn  = new JButton("Manage Suppliers");
    //     supplierBtn .addActionListener(e -> new SupplierWindow().setVisible(true));
    //     panel.add(supplierBtn); // Add to the panel/layout you're using
    //     JButton usersBtn = new JButton("Manage Users");
    //     JButton reportsBtn = new JButton("View Reports");
    //     JButton logoutBtn = new JButton("Logout");
    //     // if (!userRole.equals("Admin")) {
    //     //     usersBtn.setEnabled(false); // Only Admins can manage users
    //     // }
    //     switch (userRole) {
    //         case "Admin":
    //             // All buttons stay enabled
    //             break;
    //         case "Staff":
    //             supplierBtn.setEnabled(false);
    //             usersBtn.setEnabled(false);
    //             reportsBtn.setEnabled(false);
    //             break;
    //         case "Viewer":
    //             inventoryBtn.setText("View Inventory"); // Label change for clarity
    //             supplierBtn.setEnabled(false);
    //             usersBtn.setEnabled(false);
    //             reportsBtn.setEnabled(false);
    //             break;
    //         default:
    //             JOptionPane.showMessageDialog(this, "Unknown role: " + userRole);
    //     }
        
    //     // Button actions (currently just showing messages)
    //     // inventoryBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Inventory Section Coming Soon"));
    //     // usersBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "User Management Coming Soon"));
    //     // reportsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Reports Coming Soon"));
    //     // logoutBtn.addActionListener(e -> {
    //     //     dispose();
    //     //     new Login().setVisible(true); // Back to login
    //     // });
    //     inventoryBtn.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             new InventoryWindow(userRole).setVisible(true);
    //         }
    //     });
    //     usersBtn.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             new UserManagementWindow().setVisible(true);
    //         }
    //     });
    //     reportsBtn.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             ReportsWindow reportsWindow = new ReportsWindow(); // Create ReportsWindow instance
    //             reportsWindow.setVisible(true);            
    //         }
    //     });
    //     logoutBtn.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             dispose();
    //             new Login().setVisible(true);
    //         }
    //     });
    //     panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 30, 30));
    //     panel.add(inventoryBtn);
    //     panel.add(supplierBtn);
    //     panel.add(usersBtn);
    //     panel.add(reportsBtn);
    //     panel.add(logoutBtn);

    //     add(panel);
    // }
    public Dashboard(String role) {
    this.userRole = role;
    setTitle("Inventory Management System - Dashboard (" + userRole + ")");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10)); // Adjust rows if needed

    // Always add Inventory button (label changes for Viewer)
    JButton inventoryBtn = new JButton(role.equals("Viewer") ? "View Inventory" : "Manage Inventory");
    inventoryBtn.addActionListener(e -> new InventoryWindow(userRole).setVisible(true));
    panel.add(inventoryBtn);

    // Add Supplier button only for Admin
    if (role.equals("Admin")) {
        JButton supplierBtn = new JButton("Manage Suppliers");
        supplierBtn.addActionListener(e -> new SupplierWindow().setVisible(true));
        panel.add(supplierBtn);
    }

    // Add Users button only for Admin
    if (role.equals("Admin")) {
        JButton usersBtn = new JButton("Manage Users");
        usersBtn.addActionListener(e -> new UserManagementWindow().setVisible(true));
        panel.add(usersBtn);
    }

    // Add Reports button only for Admin
    if (role.equals("Admin")) {
        JButton reportsBtn = new JButton("View Reports");
        reportsBtn.addActionListener(e -> new ReportsWindow().setVisible(true));
        panel.add(reportsBtn);
    }

    // Logout button (for all)
    JButton logoutBtn = new JButton("Logout");
    logoutBtn.addActionListener(e -> {
        dispose();
        new Login().setVisible(true);
    });
    panel.add(logoutBtn);

    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    add(panel);
}


    @Override
    public String toString() {
        return "Dashboard []";
    }
}
