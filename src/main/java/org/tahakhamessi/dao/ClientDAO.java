package org.tahakhamessi.dao;

import org.tahakhamessi.model.Client;
import org.tahakhamessi.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ClientDAO {
    public void add(Client c) throws Exception {
        String sql = "INSERT INTO clients (nom, prenom, cin, email, telephone, adresse, numeroPermis, expirationPermis) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setString(3, c.getCin());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getTelephone());
            stmt.setString(6, c.getAdresse());
            stmt.setString(7, c.getNumeroPermis());
            stmt.setString(8, c.getExpirationPermis());
            stmt.executeUpdate();
        }
    }

    public void update(Client c) throws Exception {
        String sql = "UPDATE clients SET nom=?, prenom=?, cin=?, email=?, telephone=?, adresse=?, numeroPermis=?, expirationPermis=? WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setString(3, c.getCin());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getTelephone());
            stmt.setString(6, c.getAdresse());
            stmt.setString(7, c.getNumeroPermis());
            stmt.setString(8, c.getExpirationPermis());
            stmt.setInt(9, c.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM clients WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public ObservableList<Client> getAll() {
        ObservableList<Client> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM clients";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"),
                        rs.getString("cin"), rs.getString("email"), rs.getString("telephone"),
                        rs.getString("adresse"), rs.getString("numeroPermis"), rs.getString("expirationPermis")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Client getById(int id) {
        String sql = "SELECT * FROM clients WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"),
                        rs.getString("cin"), rs.getString("email"), rs.getString("telephone"),
                        rs.getString("adresse"), rs.getString("numeroPermis"), rs.getString("expirationPermis"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean cinExists(String cin, int excludeId) {
        String sql = "SELECT COUNT(*) FROM clients WHERE cin=? AND id!=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, cin);
            stmt.setInt(2, excludeId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<Client> search(String query) {
        ObservableList<Client> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR prenom LIKE ? OR cin LIKE ? OR email LIKE ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);
            stmt.setString(4, searchTerm);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"),
                        rs.getString("cin"), rs.getString("email"), rs.getString("telephone"),
                        rs.getString("adresse"), rs.getString("numeroPermis"), rs.getString("expirationPermis")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

