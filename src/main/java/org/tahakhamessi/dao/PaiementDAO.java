package org.tahakhamessi.dao;

import org.tahakhamessi.model.Paiement;
import org.tahakhamessi.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class PaiementDAO {
    public void add(Paiement p) throws Exception {
        String sql = "INSERT INTO paiements (reservationId, montantTotal, montantPaye, resteAPayer, modePaiement, statutPaiement) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, p.getReservationId());
            stmt.setDouble(2, p.getMontantTotal());
            stmt.setDouble(3, p.getMontantPaye());
            stmt.setDouble(4, p.getResteAPayer());
            stmt.setString(5, p.getModePaiement());
            stmt.setString(6, p.getStatutPaiement());
            stmt.executeUpdate();
        }
    }

    public void update(Paiement p) throws Exception {
        String sql = "UPDATE paiements SET reservationId=?, montantTotal=?, montantPaye=?, resteAPayer=?, modePaiement=?, statutPaiement=? WHERE id=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, p.getReservationId());
            stmt.setDouble(2, p.getMontantTotal());
            stmt.setDouble(3, p.getMontantPaye());
            stmt.setDouble(4, p.getResteAPayer());
            stmt.setString(5, p.getModePaiement());
            stmt.setString(6, p.getStatutPaiement());
            stmt.setInt(7, p.getId());
            stmt.executeUpdate();
        }
    }

    public ObservableList<Paiement> getAll() {
        ObservableList<Paiement> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM paiements";
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Paiement(rs.getInt("id"), rs.getInt("reservationId"),
                        rs.getDouble("montantTotal"), rs.getDouble("montantPaye"),
                        rs.getDouble("resteAPayer"), rs.getString("modePaiement"), rs.getString("statutPaiement")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Paiement getByReservationId(int reservationId) {
        String sql = "SELECT * FROM paiements WHERE reservationId=?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Paiement(rs.getInt("id"), rs.getInt("reservationId"),
                            rs.getDouble("montantTotal"), rs.getDouble("montantPaye"),
                            rs.getDouble("resteAPayer"), rs.getString("modePaiement"), rs.getString("statutPaiement"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

