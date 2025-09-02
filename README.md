---

# 📦 Inventory Management System

An **Inventory Management System** built with **Java** and **MySQL** to streamline the management of products, suppliers, stock levels, and reports. This project allows businesses to efficiently track and control inventory operations, generate reports, and manage user roles.

---

## 🚀 Features

* **User Authentication & Role-based Login** (Admin, Manager, Staff)
* **Product Management**: Add, update, delete, and search products
* **Supplier Management**: Store and manage supplier details
* **Stock Management**: Track stock levels, low-stock alerts, and out-of-stock warnings
* **Reports Module**:

  * Inventory level reports
  * Low-stock alerts
  * Supplier-wise inventory reports
  * Date-based transaction logs
* **User Management**: Manage different system users with roles
* **Database Integration** with MySQL for persistent data storage

---

## 🛠️ Tech Stack

* **Programming Language**: Java (Swing / AWT for GUI)
* **Database**: MySQL
* **IDE**: VS Code
* **Connector**: MySQL Connector

---

## 📂 Project Structure

```
InventoryManagementSystem/
│-- src/
│   │-- login/              # Login & Authentication
│   │-- inventory/          # Inventory operations (CRUD)
│   │-- suppliers/          # Supplier module
│   │-- reports/            # Reports module
│   │-- users/              # User management & roles
│   │-- utils/              # Database connection, helpers
│-- database/
│   │-- inventory_db.sql    # MySQL database schema
│-- README.md
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/InventoryManagementSystem.git
cd InventoryManagementSystem
```

### 2️⃣ Setup MySQL Database

1. Create a database named `inventory_db`.
2. Import the provided SQL file:

   ```bash
   mysql -u root -p inventory_db < database/inventory_db.sql
   ```

### 3️⃣ Configure Database Connection

Update your **DB connection file** (e.g., `DBConnection.java`) with your MySQL credentials:

```java
String url = "jdbc:mysql://localhost:3306/inventory_db";
String user = "root";  
String password = "your_password";  
```

### 4️⃣ Run the Project

* Open the project in your IDE (VS Code, IntelliJ, or Eclipse).
* Add **MySQL Connector/J** to your project’s classpath.
* Compile and run the `Main.java` file.

---

## 🖼️ Screenshots 

<table>
  <tbody>
    <tr>
      <td colspan="2">Login Page</td>
    </tr>
    <tr>
      <td>
        <img width="646" height="192" alt="image" src="https://github.com/user-attachments/assets/2f96caca-9ec6-4252-a993-f50363ac5299" />
      </td>
    </tr>
    <tr>
      <td colspan="2">Dashboard</td>
    </tr>
    <tr>
      <td>
        <img width="382" height="295" alt="image" src="https://github.com/user-attachments/assets/90d7c6de-4595-4936-ae09-9867d1b428bf" />
      </td>
    </tr>
    <tr>
      <td colspan="2">Manage Inventory</td>
    </tr>
    <tr>
      <td>
        <img width="683" height="487" alt="image" src="https://github.com/user-attachments/assets/e5caa1d0-240c-4ad0-8aee-e15e36870be2" />
      </td>
    </tr>
    <tr>
      <td colspan="2">Manage Suppliers</td>
    </tr>
    <tr>
      <td>
        <img width="631" height="380" alt="image" src="https://github.com/user-attachments/assets/93a2822f-3792-4d88-8fbb-a4580e9a6144" />
      </td>
    </tr>
    <tr>
      <td colspan="2">Inventory Reports</td>
    </tr>
    <tr>
      <td>
        <img width="380" height="284" alt="image" src="https://github.com/user-attachments/assets/1b6ad1ee-7c75-489a-a5d7-9bfc93e5d54b" />
      </td>
    </tr>
  </tbody>
</table>

---

## 👤 Author

**Pusarla Pranitha**

* 📧 Email: [pusarlapranitha2101@gmail.com](mailto:pusarlapranitha2101@gmail.com)
* 💻 GitHub: [Pranitha-18](https://github.com/Pranitha-18)

---

## 📜 License

This project is licensed under the **MIT License**.

---
