package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.tahakhamessi.dao.ReservationDAO;
import org.tahakhamessi.dao.VehiculeDAO;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.model.Vehicule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AgentDashboardController {
    @FXML private Label availableVehiclesLabel;
    @FXML private Label todayPickupsLabel;
    @FXML private Label pendingReturnsLabel;

    @FXML private TableView<Reservation> pickupsTable;
    @FXML private TableColumn<Reservation, String> pickupClientCol;
    @FXML private TableColumn<Reservation, String> pickupVehicleCol;
    @FXML private TableColumn<Reservation, String> pickupStatusCol;

    @FXML private TableView<Reservation> returnsTable;
    @FXML private TableColumn<Reservation, String> returnClientCol;
    @FXML private TableColumn<Reservation, String> returnVehicleCol;
    @FXML private TableColumn<Reservation, String> returnStatusCol;

    private ReservationDAO reservationDAO = new ReservationDAO();
    private VehiculeDAO vehiculeDAO = new VehiculeDAO();

    @FXML
    public void initialize() {
        setupTables();
        loadData();
    }

    private void setupTables() {
        pickupClientCol.setCellValueFactory(cellData -> cellData.getValue().clientNomProperty());
        pickupVehicleCol.setCellValueFactory(cellData -> cellData.getValue().vehiculeNomProperty());
        pickupStatusCol.setCellValueFactory(cellData -> cellData.getValue().statutProperty());

        returnClientCol.setCellValueFactory(cellData -> cellData.getValue().clientNomProperty());
        returnVehicleCol.setCellValueFactory(cellData -> cellData.getValue().vehiculeNomProperty());
        returnStatusCol.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
    }

    private void loadData() {
        String today = LocalDate.now().toString();
        
        ObservableList<Reservation> allReservations = reservationDAO.getAll();
        ObservableList<Reservation> todayPickups = FXCollections.observableArrayList();
        ObservableList<Reservation> todayReturns = FXCollections.observableArrayList();

        for (Reservation res : allReservations) {
            if (today.equals(res.getDateDebut()) && !"Annulée".equals(res.getStatut())) {
                todayPickups.add(res);
            }
            if (today.equals(res.getDateFin()) && "Confirmée".equals(res.getStatut())) {
                todayReturns.add(res);
            }
        }

        pickupsTable.setItems(todayPickups);
        returnsTable.setItems(todayReturns);

        todayPickupsLabel.setText(String.valueOf(todayPickups.size()));
        pendingReturnsLabel.setText(String.valueOf(todayReturns.size()));

        long availableCount = vehiculeDAO.getAll().stream()
                .filter(v -> "Disponible".equals(v.getStatut()))
                .count();
        availableVehiclesLabel.setText(String.valueOf(availableCount));
    }
}
