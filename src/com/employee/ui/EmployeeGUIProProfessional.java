package com.employee.ui;

import com.employee.dao.EmployeeDAO;
import com.employee.model.Employee;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.List;

public class EmployeeGUIProProfessional extends JFrame {
    private EmployeeDAO employeeDAO;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel totalEmployeesLabel;
    private JLabel avgSalaryLabel;
    private JLabel departmentCountLabel;

    // Input fields
    private JTextField nameField;
    private JTextField emailField;
    private JTextField departmentField;
    private JTextField salaryField;
    private JTextField searchField;
    private JTextField empIdField;

    // Modern Color Palette
    private static final Color DARK_BG = new Color(15, 23, 42);           // Very dark blue
    private static final Color CARD_BG = new Color(30, 41, 59);           // Dark card
    private static final Color ACCENT_PRIMARY = new Color(59, 130, 246);  // Blue
    private static final Color ACCENT_SUCCESS = new Color(34, 197, 94);   // Green
    private static final Color ACCENT_DANGER = new Color(239, 68, 68);    // Red
    private static final Color ACCENT_WARNING = new Color(251, 146, 60);  // Orange
    private static final Color TEXT_PRIMARY = new Color(241, 245, 250);   // Light text
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184); // Gray text
    private static final Color BORDER_COLOR = new Color(71, 85, 105);     // Border color

    public EmployeeGUIProProfessional() {
        employeeDAO = new EmployeeDAO();
        initializeGUI();
        loadEmployees();
    }

    private void initializeGUI() {
        setTitle("Employee Management System - Professional");
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(DARK_BG);

        // Header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content with sidebar
        JPanel mainContent = new JPanel(new BorderLayout(15, 0));
        mainContent.setBackground(DARK_BG);
        mainContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left sidebar (Form)
        mainContent.add(createSidebarPanel(), BorderLayout.WEST);

        // Center (Table + Stats)
        mainContent.add(createCenterPanel(), BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);

        // Footer
        add(createFooterPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ===== HEADER PANEL (Modern Dark Header) =====
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 41, 59), getWidth(), 0, new Color(15, 23, 42));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout(20, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setBackground(DARK_BG);
        headerPanel.setPreferredSize(new Dimension(0, 100));

        // Left section
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setBackground(DARK_BG);
        leftHeader.setOpaque(false);

        JLabel titleLabel = new JLabel("👥 Employee Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Professional Enterprise Solution");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        leftHeader.add(titleLabel);
        leftHeader.add(Box.createVerticalStrut(5));
        leftHeader.add(subtitleLabel);

        headerPanel.add(leftHeader, BorderLayout.WEST);

        // Right section (Stats)
        JPanel rightHeader = new JPanel(new GridLayout(1, 3, 15, 0));
        rightHeader.setBackground(DARK_BG);
        rightHeader.setOpaque(false);

        rightHeader.add(createStatCard("Total Employees", "0", ACCENT_PRIMARY, true));
        rightHeader.add(createStatCard("Avg Salary", "₹0", ACCENT_SUCCESS, false));
        rightHeader.add(createStatCard("Departments", "0", ACCENT_WARNING, false));

        headerPanel.add(rightHeader, BorderLayout.EAST);

        return headerPanel;
    }

    // ===== STAT CARD HELPER =====
    private JPanel createStatCard(String label, String value, Color accentColor, boolean isTotal) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        card.setBackground(CARD_BG);
        card.setOpaque(false);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelComp.setForeground(TEXT_SECONDARY);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueComp.setForeground(accentColor);

        if (isTotal) {
            totalEmployeesLabel = valueComp;
        } else if (label.contains("Avg")) {
            avgSalaryLabel = valueComp;
        } else {
            departmentCountLabel = valueComp;
        }

        card.add(labelComp);
        card.add(Box.createVerticalStrut(5));
        card.add(valueComp);

        return card;
    }

    // ===== SIDEBAR PANEL (Form) =====
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setBackground(CARD_BG);
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(350, 0));

        // Section Title
        JLabel sectionTitle = new JLabel("📝 Add/Update Employee");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(TEXT_PRIMARY);
        sidebar.add(sectionTitle);
        sidebar.add(Box.createVerticalStrut(20));

        // ID Field
        sidebar.add(createFormField("Employee ID", false));
        empIdField = (JTextField) sidebar.getComponent(sidebar.getComponentCount() - 1);

        // Name Field
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createFormField("Full Name", true));
        nameField = (JTextField) sidebar.getComponent(sidebar.getComponentCount() - 1);

        // Email Field
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createFormField("Email Address", true));
        emailField = (JTextField) sidebar.getComponent(sidebar.getComponentCount() - 1);

        // Department Field
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createFormField("Department", true));
        departmentField = (JTextField) sidebar.getComponent(sidebar.getComponentCount() - 1);

        // Salary Field
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createFormField("Salary", true));
        salaryField = (JTextField) sidebar.getComponent(sidebar.getComponentCount() - 1);

        sidebar.add(Box.createVerticalStrut(25));

        // Buttons
        sidebar.add(createModernButton("➕ Add Employee", ACCENT_SUCCESS, e -> addEmployee()));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createModernButton("✏️ Update", ACCENT_PRIMARY, e -> updateEmployee()));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createModernButton("🗑️ Delete", ACCENT_DANGER, e -> deleteEmployee()));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createModernButton("🔄 Clear", new Color(100, 116, 139), e -> clearFields()));

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    // ===== CENTER PANEL (Table + Search) =====
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(DARK_BG);

        // Search Panel
        centerPanel.add(createSearchPanel(), BorderLayout.NORTH);

        // Table Panel
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);

        return centerPanel;
    }

    // ===== SEARCH PANEL =====
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        searchPanel.setLayout(new BorderLayout(15, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        searchPanel.setBackground(CARD_BG);
        searchPanel.setOpaque(false);
        searchPanel.setPreferredSize(new Dimension(0, 60));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        searchField.setBackground(new Color(71, 85, 105));
        searchField.setForeground(TEXT_PRIMARY);
        searchField.setCaretColor(ACCENT_PRIMARY);

        // Placeholder text
        searchField.setText("Search employee by name...");
        searchField.setForeground(TEXT_SECONDARY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search employee by name...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_PRIMARY);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search employee by name...");
                    searchField.setForeground(TEXT_SECONDARY);
                }
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(CARD_BG);
        inputPanel.setOpaque(false);
        inputPanel.add(searchIcon, BorderLayout.WEST);
        inputPanel.add(searchField, BorderLayout.CENTER);

        searchPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(CARD_BG);
        buttonPanel.setOpaque(false);
        buttonPanel.add(createSmallButton("Search", ACCENT_PRIMARY, e -> searchEmployees()));
        buttonPanel.add(createSmallButton("Reset", ACCENT_WARNING, e -> loadEmployees()));

        searchPanel.add(buttonPanel, BorderLayout.EAST);

        return searchPanel;
    }

    // ===== TABLE PANEL =====
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(DARK_BG);

        String[] columnNames = {"ID", "Name", "Email", "Department", "Salary", "Hire Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeTable.setRowHeight(35);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setGridColor(BORDER_COLOR);
        employeeTable.setShowGrid(true);
        employeeTable.setBackground(CARD_BG);
        employeeTable.setForeground(TEXT_PRIMARY);

        // Header styling
        employeeTable.getTableHeader().setBackground(new Color(30, 41, 59));
        employeeTable.getTableHeader().setForeground(ACCENT_PRIMARY);
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(30, 41, 59));
                c.setForeground(ACCENT_PRIMARY);
                setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
                return c;
            }
        });

        // Row selection
        employeeTable.setSelectionBackground(ACCENT_PRIMARY);
        employeeTable.setSelectionForeground(Color.WHITE);

        // Cell rendering
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(CARD_BG);
                    c.setForeground(TEXT_PRIMARY);
                }
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            employeeTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        employeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = employeeTable.getSelectedRow();
                if (row >= 0) {
                    populateFields(row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBackground(DARK_BG);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Custom scroll bar styling
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BORDER_COLOR;
                this.trackColor = CARD_BG;
            }
        });

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    // ===== FOOTER PANEL =====
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(BORDER_COLOR);
                g2d.fillRect(0, 0, getWidth(), 1);
            }
        };
        footerPanel.setLayout(new BorderLayout(20, 0));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        footerPanel.setBackground(DARK_BG);

        statusLabel = new JLabel("✓ Ready to manage employees");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(ACCENT_SUCCESS);

        JLabel versionLabel = new JLabel("Version 1.0 | Enterprise Edition");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versionLabel.setForeground(TEXT_SECONDARY);

        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(versionLabel, BorderLayout.EAST);

        return footerPanel;
    }

    // ===== HELPER METHODS =====

    private JTextField createFormField(String label, boolean editable) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        label,
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.PLAIN, 10),
                        TEXT_SECONDARY
                ),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        textField.setBackground(new Color(71, 85, 105));
        textField.setForeground(TEXT_PRIMARY);
        textField.setCaretColor(ACCENT_PRIMARY);
        textField.setEditable(editable);
        return textField;
    }

    private JButton createModernButton(String text, Color color, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private JButton createSmallButton(String text, Color color, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void loadEmployees() {
        tableModel.setRowCount(0);
        List<Employee> employees = employeeDAO.getAllEmployees();

        double totalSalary = 0;
        java.util.Set<String> departments = new java.util.HashSet<>();

        for (Employee emp : employees) {
            Object[] row = {
                    emp.getEmpId(),
                    emp.getName(),
                    emp.getEmail(),
                    emp.getDepartment(),
                    String.format("₹%.2f", emp.getSalary()),
                    emp.getHireDate()
            };
            tableModel.addRow(row);
            totalSalary += emp.getSalary();
            departments.add(emp.getDepartment());
        }

        // Update stats
        totalEmployeesLabel.setText(String.valueOf(employees.size()));
        if (employees.size() > 0) {
            avgSalaryLabel.setText(String.format("₹%.0f", totalSalary / employees.size()));
        } else {
            avgSalaryLabel.setText("₹0");
        }
        departmentCountLabel.setText(String.valueOf(departments.size()));

        statusLabel.setText("✓ " + employees.size() + " employees loaded");
        clearFields();
    }

    private void searchEmployees() {
        String searchName = searchField.getText().trim();

        if (searchName.isEmpty() || searchName.equals("Search employee by name...")) {
            showError("Please enter a name to search!");
            return;
        }

        tableModel.setRowCount(0);
        List<Employee> employees = employeeDAO.searchByName(searchName);

        if (employees.isEmpty()) {
            showError("No employees found matching '" + searchName + "'");
            loadEmployees();
            return;
        }

        for (Employee emp : employees) {
            Object[] row = {
                    emp.getEmpId(),
                    emp.getName(),
                    emp.getEmail(),
                    emp.getDepartment(),
                    String.format("₹%.2f", emp.getSalary()),
                    emp.getHireDate()
            };
            tableModel.addRow(row);
        }

        statusLabel.setText("✓ Found " + employees.size() + " employee(s)");
    }

    private void addEmployee() {
        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String department = departmentField.getText().trim();
            String salaryStr = salaryField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || department.isEmpty() || salaryStr.isEmpty()) {
                showError("All fields are required!");
                return;
            }

            if (!email.contains("@")) {
                showError("Invalid email format!");
                return;
            }

            double salary = Double.parseDouble(salaryStr);
            if (salary < 0) {
                showError("Salary cannot be negative!");
                return;
            }

            Employee employee = new Employee(name, email, department, salary, LocalDate.now());
            employeeDAO.addEmployee(employee);

            showSuccess("Employee added successfully!");
            clearFields();
            loadEmployees();
        } catch (NumberFormatException ex) {
            showError("Salary must be a valid number!");
        }
    }

    private void updateEmployee() {
        try {
            int row = employeeTable.getSelectedRow();
            if (row < 0) {
                showError("Select an employee to update!");
                return;
            }

            String empIdStr = empIdField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String department = departmentField.getText().trim();
            String salaryStr = salaryField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || department.isEmpty() || salaryStr.isEmpty()) {
                showError("All fields are required!");
                return;
            }

            if (!email.contains("@")) {
                showError("Invalid email format!");
                return;
            }

            int empId = Integer.parseInt(empIdStr);
            double salary = Double.parseDouble(salaryStr);

            if (salary < 0) {
                showError("Salary cannot be negative!");
                return;
            }

            Employee employee = new Employee(empId, name, email, department, salary, LocalDate.now());
            employeeDAO.updateEmployee(employee);

            showSuccess("Employee updated successfully!");
            clearFields();
            loadEmployees();
        } catch (NumberFormatException ex) {
            showError("Invalid input!");
        }
    }

    private void deleteEmployee() {
        int row = employeeTable.getSelectedRow();
        if (row < 0) {
            showError("Select an employee to delete!");
            return;
        }

        int empId = Integer.parseInt(empIdField.getText());
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this employee? This cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            employeeDAO.deleteEmployee(empId);
            showSuccess("Employee deleted successfully!");
            clearFields();
            loadEmployees();
        }
    }

    private void populateFields(int row) {
        empIdField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        emailField.setText(tableModel.getValueAt(row, 2).toString());
        departmentField.setText(tableModel.getValueAt(row, 3).toString());
        String salaryStr = tableModel.getValueAt(row, 4).toString().replace("₹", "").trim();
        salaryField.setText(salaryStr);
        statusLabel.setText("✓ Editing: " + tableModel.getValueAt(row, 1));
    }

    private void clearFields() {
        empIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        departmentField.setText("");
        salaryField.setText("");
        if (!searchField.getText().equals("Search employee by name...")) {
            searchField.setText("Search employee by name...");
            searchField.setForeground(TEXT_SECONDARY);
        }
    }

    private void showSuccess(String message) {
        UIManager.put("OptionPane.background", DARK_BG);
        UIManager.put("Panel.background", DARK_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        JOptionPane.showMessageDialog(this, message, "✓ Success", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText("✓ " + message);
    }

    private void showError(String message) {
        UIManager.put("OptionPane.background", DARK_BG);
        UIManager.put("Panel.background", DARK_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        JOptionPane.showMessageDialog(this, message, "✗ Error", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("✗ " + message);
    }
}