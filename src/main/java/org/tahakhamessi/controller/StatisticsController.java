package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.tahakhamessi.dao.ReservationDAO;
import org.tahakhamessi.dao.VehiculeDAO;
import org.tahakhamessi.dao.PaiementDAO;
import org.tahakhamessi.model.Vehicule;
import java.util.HashMap;
import java.util.Map;

public class StatisticsController {
    @FXML private Label totalReservationsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label availableVehiclesLabel;
    @FXML private Label mostRentedLabel;

    private ReservationDAO reservationDAO = new ReservationDAO();
    private VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private PaiementDAO paiementDAO = new PaiementDAO();

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        int totalReservations = reservationDAO.getAll().size();
        int completedReservations = 0;
        int cancelledReservations = 0;
        double totalRevenue = 0;

        for (var reservation : reservationDAO.getAll()) {
            if ("Terminée".equals(reservation.getStatut())) {
                completedReservations++;
            } else if ("Annulée".equals(reservation.getStatut())) {
                cancelledReservations++;
            }
        }

        for (var payment : paiementDAO.getAll()) {
            totalRevenue += payment.getMontantPaye();
        }

        int availableVehicles = 0;
        int totalVehicles = 0;
        Map<String, Integer> rentedCount = new HashMap<>();

        for (var vehicle : vehiculeDAO.getAll()) {
            totalVehicles++;
            if (vehicle.getStatut().equals("Disponible")) {
                availableVehicles++;
            }
        }

        for (var res : reservationDAO.getAll()) {
            String veh = res.getVehiculeNom();
            rentedCount.put(veh, rentedCount.getOrDefault(veh, 0) + 1);
        }
        String mostRented = rentedCount.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");

        totalReservationsLabel.setText("Total Reservations: " + totalReservations);
        totalRevenueLabel.setText("Total Revenue: " + String.format("%.2f", totalRevenue) + " DH");
        availableVehiclesLabel.setText("Available Vehicles: " + availableVehicles + "/" + totalVehicles);
        mostRentedLabel.setText("Most Rented: " + mostRented);
    }
}

