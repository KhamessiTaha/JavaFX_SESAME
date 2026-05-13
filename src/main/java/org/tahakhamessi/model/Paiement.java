package org.tahakhamessi.model;

import javafx.beans.property.*;

public class Paiement {
    private IntegerProperty id = new SimpleIntegerProperty();
    private IntegerProperty reservationId = new SimpleIntegerProperty();
    private DoubleProperty montantTotal = new SimpleDoubleProperty();
    private DoubleProperty montantPaye = new SimpleDoubleProperty();
    private DoubleProperty resteAPayer = new SimpleDoubleProperty();
    private StringProperty modePaiement = new SimpleStringProperty();
    private StringProperty statutPaiement = new SimpleStringProperty();

    public Paiement() {}

    public Paiement(int id, int reservationId, double montantTotal, double montantPaye,
                    double resteAPayer, String modePaiement, String statutPaiement) {
        this.id.set(id);
        this.reservationId.set(reservationId);
        this.montantTotal.set(montantTotal);
        this.montantPaye.set(montantPaye);
        this.resteAPayer.set(resteAPayer);
        this.modePaiement.set(modePaiement);
        this.statutPaiement.set(statutPaiement);
    }

    public IntegerProperty idProperty() { return id; }
    public IntegerProperty reservationIdProperty() { return reservationId; }
    public DoubleProperty montantTotalProperty() { return montantTotal; }
    public DoubleProperty montantPayeProperty() { return montantPaye; }
    public DoubleProperty resteAPayerProperty() { return resteAPayer; }
    public StringProperty modePaiementProperty() { return modePaiement; }
    public StringProperty statutPaiementProperty() { return statutPaiement; }

    public int getId() { return id.get(); }
    public int getReservationId() { return reservationId.get(); }
    public double getMontantTotal() { return montantTotal.get(); }
    public double getMontantPaye() { return montantPaye.get(); }
    public double getResteAPayer() { return resteAPayer.get(); }
    public String getModePaiement() { return modePaiement.get(); }
    public String getStatutPaiement() { return statutPaiement.get(); }

    public void setId(int val) { id.set(val); }
    public void setReservationId(int val) { reservationId.set(val); }
    public void setMontantTotal(double val) { montantTotal.set(val); }
    public void setMontantPaye(double val) { montantPaye.set(val); }
    public void setResteAPayer(double val) { resteAPayer.set(val); }
    public void setModePaiement(String val) { modePaiement.set(val); }
    public void setStatutPaiement(String val) { statutPaiement.set(val); }
}

