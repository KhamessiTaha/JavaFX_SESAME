package org.tahakhamessi.dao;

import org.tahakhamessi.model.Retour;
import org.tahakhamessi.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class RetourDAO {
    public void add(Retour r) throws Exception {
        String sql = "INSERT INTO retours (reservationId, dateRetour, etatVehicule, fraisSupplementaires) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, r.getReservationId());
            stmt.setString(2, r.getDateRetour());
            stmt.setString(3, r.getEtatVehicule());
            stmt.setDouble(4, r.getFraisSupplementaires());
            stmt.executeUpdate();
        }
    }

    public void update(Retour r) throws Exception {
        String sql = "UPDATE retours SET reservationId=?, dateRetour=?, etatVehicule=?, fraisSupplementaires=? WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, r.getReservationId());
            stmt.setString(2, r.getDateRetour());
            stmt.setString(3, r.getEtatVehicule());
            stmt.setDouble(4, r.getFraisSupplementaires());
            stmt.setInt(5, r.getId());
            stmt.executeUpdate();
        }
    }

    public ObservableList<Retour> getAll() {
        ObservableList<Retour> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM retours";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Retour(rs.getInt("id"), rs.getInt("reservationId"),
                        rs.getString("dateRetour"), rs.getString("etatVehicule"), rs.getDouble("fraisSupplementaires")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Retour getByReservationId(int reservationId) {
        String sql = "SELECT * FROM retours WHERE reservationId=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Retour(rs.getInt("id"), rs.getInt("reservationId"),
                        rs.getString("dateRetour"), rs.getString("etatVehicule"), rs.getDouble("fraisSupplementaires"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

