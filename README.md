# Product Shop Application (Java Swing + MySQL)

This is a simple desktop-based Product Shop application built using **Java Swing** for the GUI and **MySQL** as the database. It allows users to:

- View available products
- Buy (decrease quantity) of a selected product
- Add new products
- Refresh the product list from the database

---

## 🖥️ Features

- View all products in a table
- Purchase a product (decrease quantity)
- Add new product via input dialog
- Real-time database refresh
- Error handling with UI popups

---

## 🛠️ Technologies Used

- Java (JDK 8 or higher)
- Swing (Java GUI Framework)
- JDBC (Java Database Connectivity)
- MySQL (Database)

---

## 🗃️ Database Setup

1. Make sure MySQL is installed and running.
2. Create a database named `shopdb`.
3. Run the following SQL to create the `products` table:

```sql
CREATE DATABASE shopdb;

USE shopdb;

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    price DOUBLE,
    quantity INT
);
