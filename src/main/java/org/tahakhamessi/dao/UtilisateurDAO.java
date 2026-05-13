package org.tahakhamessi.dao;

import org.tahakhamessi.model.Utilisateur;
import org.tahakhamessi.util.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    public Utilisateur authenticate(String username, String password) {
        String sql = "SELECT * FROM utilisateurs WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Utilisateur> getAll() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Utilisateur(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

