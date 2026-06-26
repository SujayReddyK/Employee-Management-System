package com.employee.dao;

import com.employee.model.Employee;
import com.employee.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // 1. ADD EMPLOYEE (INSERT)
    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (name, email, department, salary, hire_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getDepartment());
            stmt.setDouble(4, employee.getSalary());
            stmt.setDate(5, java.sql.Date.valueOf(employee.getHireDate()));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✓ Employee added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Error adding employee: " + e.getMessage());
        }
    }

    // 2. GET ALL EMPLOYEES (READ ALL)
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int empId = rs.getInt("emp_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");
                LocalDate hireDate = rs.getDate("hire_date").toLocalDate();

                Employee emp = new Employee(empId, name, email, department, salary, hireDate);
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("✗ Error retrieving employees: " + e.getMessage());
        }

        return employees;
    }

    // 3. GET EMPLOYEE BY ID (READ ONE)
    public Employee getEmployeeById(int empId) {
        String sql = "SELECT * FROM employees WHERE emp_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");
                LocalDate hireDate = rs.getDate("hire_date").toLocalDate();

                return new Employee(empId, name, email, department, salary, hireDate);
            }
        } catch (SQLException e) {
            System.out.println("✗ Error retrieving employee: " + e.getMessage());
        }

        return null;
    }

    // 4. UPDATE EMPLOYEE
    public void updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET name = ?, email = ?, department = ?, salary = ?, hire_date = ? WHERE emp_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getDepartment());
            stmt.setDouble(4, employee.getSalary());
            stmt.setDate(5, java.sql.Date.valueOf(employee.getHireDate()));
            stmt.setInt(6, employee.getEmpId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✓ Employee updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Error updating employee: " + e.getMessage());
        }
    }

    // 5. DELETE EMPLOYEE
    public void deleteEmployee(int empId) {
        String sql = "DELETE FROM employees WHERE emp_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empId);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✓ Employee deleted successfully!");
            } else {
                System.out.println("✗ Employee not found!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Error deleting employee: " + e.getMessage());
        }
    }

    // 6. SEARCH EMPLOYEE BY NAME
    public List<Employee> searchByName(String name) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE name LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int empId = rs.getInt("emp_id");
                String empName = rs.getString("name");
                String email = rs.getString("email");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");
                LocalDate hireDate = rs.getDate("hire_date").toLocalDate();

                Employee emp = new Employee(empId, empName, email, department, salary, hireDate);
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("✗ Error searching employees: " + e.getMessage());
        }

        return employees;
    }
}
