package org.tahakhamessi.model;

import javafx.beans.property.*;

public class Retour {
    private IntegerProperty id = new SimpleIntegerProperty();
    private IntegerProperty reservationId = new SimpleIntegerProperty();
    private StringProperty dateRetour = new SimpleStringProperty();
    private StringProperty etatVehicule = new SimpleStringProperty();
    private DoubleProperty fraisSupplementaires = new SimpleDoubleProperty();

    public Retour() {}

    public Retour(int id, int reservationId, String dateRetour, String etatVehicule, double fraisSupplementaires) {
        this.id.set(id);
        this.reservationId.set(reservationId);
        this.dateRetour.set(dateRetour);
        this.etatVehicule.set(etatVehicule);
        this.fraisSupplementaires.set(fraisSupplementaires);
    }

    public IntegerProperty idProperty() { return id; }
    public IntegerProperty reservationIdProperty() { return reservationId; }
    public StringProperty dateRetourProperty() { return dateRetour; }
    public StringProperty etatVehiculeProperty() { return etatVehicule; }
    public DoubleProperty fraisSupplementairesProperty() { return fraisSupplementaires; }

    public int getId() { return id.get(); }
    public int getReservationId() { return reservationId.get(); }
    public String getDateRetour() { return dateRetour.get(); }
    public String getEtatVehicule() { return etatVehicule.get(); }
    public double getFraisSupplementaires() { return fraisSupplementaires.get(); }

    public void setId(int val) { id.set(val); }
    public void setReservationId(int val) { reservationId.set(val); }
    public void setDateRetour(String val) { dateRetour.set(val); }
    public void setEtatVehicule(String val) { etatVehicule.set(val); }
    public void setFraisSupplementaires(double val) { fraisSupplementaires.set(val); }
}

