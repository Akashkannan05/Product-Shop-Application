//package connectivity;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 *
 * @author kpaa
 */
public class Connectivity extends JFrame {

    /**
     * @param args the command line arguments
     */
    
    private JTable table;
    private DefaultTableModel model;
    private JButton buyButton, refreshButton, addButton;
    
    private final String URL = "db url";
    private final String USER = "root";
    private final String PASSWORD = "password"; // replace this
    
    public Connectivity() { 
        setTitle("Product Shop");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Quantity"}, 0);
        table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);

        JPanel panel = new JPanel();

        buyButton = new JButton("Buy Product");
        refreshButton = new JButton("Refresh");
        addButton = new JButton("Add Product");

        panel.add(buyButton);
        panel.add(addButton);
        panel.add(refreshButton);

        add(panel, BorderLayout.SOUTH);

        refreshProducts();

        buyButton.addActionListener(e -> buyProduct());
        refreshButton.addActionListener(e -> refreshProducts());
        addButton.addActionListener(e -> showAddProductDialog());
    }
    
    private void refreshProducts() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                });
            }
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }
    
    private void buyProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a product to buy.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        int quantity = (int) model.getValueAt(selectedRow, 3);

        if (quantity <= 0) {
            showError("Product is out of stock.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE products SET quantity = quantity - 1 WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            refreshProducts();
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void showAddProductDialog() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField qtyField = new JTextField();

        Object[] inputs = {
            "Name:", nameField,
            "Price:", priceField,
            "Quantity:", qtyField
        };

        int result = JOptionPane.showConfirmDialog(this, inputs, "Add New Product", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            double price;
            int quantity;
            try {
                price = Double.parseDouble(priceField.getText());
                quantity = Integer.parseInt(qtyField.getText());
            } catch (NumberFormatException e) {
                showError("Invalid price or quantity.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)")) {
                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setInt(3, quantity);
                stmt.executeUpdate();
                refreshProducts();
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Connectivity().setVisible(true));
        
    }
    
}
