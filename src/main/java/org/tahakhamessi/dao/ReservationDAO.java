package org.tahakhamessi.dao;

import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ReservationDAO {
    public void add(Reservation r) throws Exception {
        String sql = "INSERT INTO reservations (clientId, vehiculeId, dateDebut, dateFin, nombreJours, prixTotal, statut, options, prixOptions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, r.getClientId());
            stmt.setInt(2, r.getVehiculeId());
            stmt.setString(3, r.getDateDebut());
            stmt.setString(4, r.getDateFin());
            stmt.setInt(5, r.getNombreJours());
            stmt.setDouble(6, r.getPrixTotal());
            stmt.setString(7, r.getStatut());
            stmt.setString(8, r.getOptions());
            stmt.setDouble(9, r.getPrixOptions());
            stmt.executeUpdate();
        }
    }

    public void update(Reservation r) throws Exception {
        String sql = "UPDATE reservations SET clientId=?, vehiculeId=?, dateDebut=?, dateFin=?, nombreJours=?, prixTotal=?, statut=?, options=?, prixOptions=? WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, r.getClientId());
            stmt.setInt(2, r.getVehiculeId());
            stmt.setString(3, r.getDateDebut());
            stmt.setString(4, r.getDateFin());
            stmt.setInt(5, r.getNombreJours());
            stmt.setDouble(6, r.getPrixTotal());
            stmt.setString(7, r.getStatut());
            stmt.setString(8, r.getOptions());
            stmt.setDouble(9, r.getPrixOptions());
            stmt.setInt(10, r.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM reservations WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public ObservableList<Reservation> getAll() {
        ObservableList<Reservation> list = FXCollections.observableArrayList();
        String sql = "SELECT r.*, c.nom, c.prenom, v.marque, v.modele FROM reservations r "
                + "LEFT JOIN clients c ON r.clientId = c.id "
                + "LEFT JOIN vehicules v ON r.vehiculeId = v.id";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Reservation res = new Reservation(rs.getInt("id"), rs.getInt("clientId"),
                        rs.getInt("vehiculeId"), rs.getString("dateDebut"), rs.getString("dateFin"),
                        rs.getInt("nombreJours"), rs.getDouble("prixTotal"), rs.getString("statut"));
                res.setClientNom(rs.getString("nom") + " " + rs.getString("prenom"));
                res.setVehiculeNom(rs.getString("marque") + " " + rs.getString("modele"));
                list.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Reservation getById(int id) {
        String sql = "SELECT r.*, c.nom, c.prenom, v.marque, v.modele FROM reservations r "
                + "LEFT JOIN clients c ON r.clientId = c.id "
                + "LEFT JOIN vehicules v ON r.vehiculeId = v.id WHERE r.id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Reservation res = new Reservation(rs.getInt("id"), rs.getInt("clientId"),
                        rs.getInt("vehiculeId"), rs.getString("dateDebut"), rs.getString("dateFin"),
                        rs.getInt("nombreJours"), rs.getDouble("prixTotal"), rs.getString("statut"));
                res.setClientNom(rs.getString("nom") + " " + rs.getString("prenom"));
                res.setVehiculeNom(rs.getString("marque") + " " + rs.getString("modele"));
                return res;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE reservations SET statut=? WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public boolean hasOverlappingReservation(int vehiculeId, String dateDebut, String dateFin, int excludeId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE vehiculeId=? AND statut!='Annulée' AND ((dateDebut<=? AND dateFin>=?) OR (dateDebut<=? AND dateFin>=?) OR (dateDebut>=? AND dateFin<=?)) AND id!=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, vehiculeId);
            stmt.setString(2, dateFin);
            stmt.setString(3, dateDebut);
            stmt.setString(4, dateFin);
            stmt.setString(5, dateDebut);
            stmt.setString(6, dateDebut);
            stmt.setString(7, dateFin);
            stmt.setInt(8, excludeId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<Reservation> search(String query) {
        ObservableList<Reservation> list = FXCollections.observableArrayList();
        String sql = "SELECT r.*, c.nom, c.prenom, v.marque, v.modele FROM reservations r "
                + "LEFT JOIN clients c ON r.clientId = c.id "
                + "LEFT JOIN vehicules v ON r.vehiculeId = v.id "
                + "WHERE c.nom LIKE ? OR c.cin LIKE ? OR v.immatriculation LIKE ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation res = new Reservation(rs.getInt("id"), rs.getInt("clientId"),
                        rs.getInt("vehiculeId"), rs.getString("dateDebut"), rs.getString("dateFin"),
                        rs.getInt("nombreJours"), rs.getDouble("prixTotal"), rs.getString("statut"));
                res.setClientNom(rs.getString("nom") + " " + rs.getString("prenom"));
                res.setVehiculeNom(rs.getString("marque") + " " + rs.getString("modele"));
                list.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<Reservation> getByClientId(int clientId) {
        ObservableList<Reservation> list = FXCollections.observableArrayList();
        String sql = "SELECT r.*, c.nom, c.prenom, v.marque, v.modele FROM reservations r "
                + "LEFT JOIN clients c ON r.clientId = c.id "
                + "LEFT JOIN vehicules v ON r.vehiculeId = v.id WHERE r.clientId = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation(rs.getInt("id"), rs.getInt("clientId"),
                        rs.getInt("vehiculeId"), rs.getString("dateDebut"), rs.getString("dateFin"),
                        rs.getInt("nombreJours"), rs.getDouble("prixTotal"), rs.getString("statut"));
                r.setClientNom(rs.getString("nom") + " " + rs.getString("prenom"));
                r.setVehiculeNom(rs.getString("marque") + " " + rs.getString("modele"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

