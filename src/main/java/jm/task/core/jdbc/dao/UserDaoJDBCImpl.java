package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.executor.Executor;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Executor executor;
    private Connection connection;

    public UserDaoJDBCImpl() {
        try {
            connection = Util.getJDBCConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        this.executor = new Executor(connection);
    }

    public void createUsersTable() {
        try {
            executor.execUpdate("CREATE TABLE IF NOT EXISTS users " +
                    "(id BIGINT AUTO_INCREMENT," +
                    "name VARCHAR(256) NOT NULL," +
                    "lastName VARCHAR(256) NOT NULL," +
                    "age TINYINT UNSIGNED NOT NULL," +
                    "PRIMARY KEY (id))");
        } catch (SQLException e) {
            System.out.println("Error CREATE: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        try {
            executor.execUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            System.out.println("Error DROP: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            String query = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error INSERT: " + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try {
            executor.execUpdate("DELETE FROM users WHERE id=" + id);
        } catch (SQLException e) {
            System.out.println("Error DELETE user: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            return executor.execQuery("SELECT * FROM users", result -> {
                while (result.next()) {
                    Long id = result.getLong(1);
                    String name = result.getString(2);
                    String lastName = result.getString(3);
                    Byte age = result.getByte(4);
                    User user = new User(name, lastName, age);
                    user.setId(id);
                    users.add(user);
                }
                return users;
            });
        } catch (SQLException e) {
            System.out.println("Error SELECT: " + e.getMessage());
        }
        return users;
    }

    public void cleanUsersTable() {
        try {
            executor.execUpdate("DELETE FROM users");
        } catch (SQLException e) {
            System.out.println("Error DELETE users: " + e.getMessage());
        }
    }
}
