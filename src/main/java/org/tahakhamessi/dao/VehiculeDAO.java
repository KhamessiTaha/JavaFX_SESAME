package org.tahakhamessi.dao;

import org.tahakhamessi.model.Vehicule;
import org.tahakhamessi.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class VehiculeDAO {
    public void add(Vehicule v) throws Exception {
        String sql = "INSERT INTO vehicules (marque, modele, immatriculation, categorie, carburant, boiteVitesse, nombrePlaces, prixParJour, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, v.getMarque());
            stmt.setString(2, v.getModele());
            stmt.setString(3, v.getImmatriculation());
            stmt.setString(4, v.getCategorie());
            stmt.setString(5, v.getCarburant());
            stmt.setString(6, v.getBoiteVitesse());
            stmt.setInt(7, v.getNombrePlaces());
            stmt.setDouble(8, v.getPrixParJour());
            stmt.setString(9, v.getStatut());
            stmt.executeUpdate();
        }
    }

    public void update(Vehicule v) throws Exception {
        String sql = "UPDATE vehicules SET marque=?, modele=?, immatriculation=?, categorie=?, carburant=?, boiteVitesse=?, nombrePlaces=?, prixParJour=?, statut=? WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, v.getMarque());
            stmt.setString(2, v.getModele());
            stmt.setString(3, v.getImmatriculation());
            stmt.setString(4, v.getCategorie());
            stmt.setString(5, v.getCarburant());
            stmt.setString(6, v.getBoiteVitesse());
            stmt.setInt(7, v.getNombrePlaces());
            stmt.setDouble(8, v.getPrixParJour());
            stmt.setString(9, v.getStatut());
            stmt.setInt(10, v.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM vehicules WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public ObservableList<Vehicule> getAll() {
        ObservableList<Vehicule> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM vehicules";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Vehicule(rs.getInt("id"), rs.getString("marque"), rs.getString("modele"),
                        rs.getString("immatriculation"), rs.getString("categorie"), rs.getString("carburant"),
                        rs.getString("boiteVitesse"), rs.getInt("nombrePlaces"), rs.getDouble("prixParJour"),
                        rs.getString("statut")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Vehicule getById(int id) {
        String sql = "SELECT * FROM vehicules WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vehicule(rs.getInt("id"), rs.getString("marque"), rs.getString("modele"),
                        rs.getString("immatriculation"), rs.getString("categorie"), rs.getString("carburant"),
                        rs.getString("boiteVitesse"), rs.getInt("nombrePlaces"), rs.getDouble("prixParJour"),
                        rs.getString("statut"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean immatriculationExists(String immatriculation, int excludeId) {
        String sql = "SELECT COUNT(*) FROM vehicules WHERE immatriculation=? AND id!=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, immatriculation);
            stmt.setInt(2, excludeId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE vehicules SET statut=? WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public ObservableList<Vehicule> getFiltered(String query, String filter) {
        ObservableList<Vehicule> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM vehicules WHERE (LOWER(marque) LIKE ? OR LOWER(modele) LIKE ? OR LOWER(immatriculation) LIKE ? OR LOWER(categorie) LIKE ?) AND (statut = ? OR ? = 'Tous')";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            stmt.setString(3, "%" + query + "%");
            stmt.setString(4, "%" + query + "%");
            stmt.setString(5, filter);
            stmt.setString(6, filter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Vehicule(rs.getInt("id"), rs.getString("marque"), rs.getString("modele"),
                        rs.getString("immatriculation"), rs.getString("categorie"), rs.getString("carburant"),
                        rs.getString("boiteVitesse"), rs.getInt("nombrePlaces"), rs.getDouble("prixParJour"),
                        rs.getString("statut")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

