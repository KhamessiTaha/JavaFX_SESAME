package org.tahakhamessi.model;

import javafx.beans.property.*;

public class Reservation {
    private IntegerProperty id = new SimpleIntegerProperty();
    private IntegerProperty clientId = new SimpleIntegerProperty();
    private IntegerProperty vehiculeId = new SimpleIntegerProperty();
    private StringProperty dateDebut = new SimpleStringProperty();
    private StringProperty dateFin = new SimpleStringProperty();
    private IntegerProperty nombreJours = new SimpleIntegerProperty();
    private DoubleProperty prixTotal = new SimpleDoubleProperty();
    private StringProperty statut = new SimpleStringProperty();
    private StringProperty clientNom = new SimpleStringProperty();
    private StringProperty vehiculeNom = new SimpleStringProperty();
    private StringProperty options = new SimpleStringProperty();
    private DoubleProperty prixOptions = new SimpleDoubleProperty();

    public Reservation() {}

    public Reservation(int id, int clientId, int vehiculeId, String dateDebut, String dateFin,
                       int nombreJours, double prixTotal, String statut) {
        this.id.set(id);
        this.clientId.set(clientId);
        this.vehiculeId.set(vehiculeId);
        this.dateDebut.set(dateDebut);
        this.dateFin.set(dateFin);
        this.nombreJours.set(nombreJours);
        this.prixTotal.set(prixTotal);
        this.statut.set(statut);
    }

    public IntegerProperty idProperty() { return id; }
    public IntegerProperty clientIdProperty() { return clientId; }
    public IntegerProperty vehiculeIdProperty() { return vehiculeId; }
    public StringProperty dateDebutProperty() { return dateDebut; }
    public StringProperty dateFinProperty() { return dateFin; }
    public IntegerProperty nombreJoursProperty() { return nombreJours; }
    public DoubleProperty prixTotalProperty() { return prixTotal; }
    public StringProperty statutProperty() { return statut; }
    public StringProperty clientNomProperty() { return clientNom; }
    public StringProperty vehiculeNomProperty() { return vehiculeNom; }
    public StringProperty optionsProperty() { return options; }
    public DoubleProperty prixOptionsProperty() { return prixOptions; }

    public int getId() { return id.get(); }
    public int getClientId() { return clientId.get(); }
    public int getVehiculeId() { return vehiculeId.get(); }
    public String getDateDebut() { return dateDebut.get(); }
    public String getDateFin() { return dateFin.get(); }
    public int getNombreJours() { return nombreJours.get(); }
    public double getPrixTotal() { return prixTotal.get(); }
    public String getStatut() { return statut.get(); }
    public String getClientNom() { return clientNom.get(); }
    public String getVehiculeNom() { return vehiculeNom.get(); }
    public String getOptions() { return options.get(); }
    public double getPrixOptions() { return prixOptions.get(); }

    public void setId(int val) { id.set(val); }
    public void setClientId(int val) { clientId.set(val); }
    public void setVehiculeId(int val) { vehiculeId.set(val); }
    public void setDateDebut(String val) { dateDebut.set(val); }
    public void setDateFin(String val) { dateFin.set(val); }
    public void setNombreJours(int val) { nombreJours.set(val); }
    public void setPrixTotal(double val) { prixTotal.set(val); }
    public void setStatut(String val) { statut.set(val); }
    public void setClientNom(String val) { clientNom.set(val); }
    public void setVehiculeNom(String val) { vehiculeNom.set(val); }
    public void setOptions(String val) { options.set(val); }
    public void setPrixOptions(double val) { prixOptions.set(val); }

    @Override
    public String toString() {
        return "Res #" + getId() + " - " + getClientNom() + " (" + getVehiculeNom() + ")";
    }
}

