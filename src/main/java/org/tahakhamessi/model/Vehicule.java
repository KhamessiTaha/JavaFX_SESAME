package org.tahakhamessi.model;

import javafx.beans.property.*;

public class Vehicule {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty marque = new SimpleStringProperty();
    private StringProperty modele = new SimpleStringProperty();
    private StringProperty immatriculation = new SimpleStringProperty();
    private StringProperty categorie = new SimpleStringProperty();
    private StringProperty carburant = new SimpleStringProperty();
    private StringProperty boiteVitesse = new SimpleStringProperty();
    private IntegerProperty nombrePlaces = new SimpleIntegerProperty();
    private DoubleProperty prixParJour = new SimpleDoubleProperty();
    private StringProperty statut = new SimpleStringProperty();

    public Vehicule() {}

    public Vehicule(int id, String marque, String modele, String immatriculation, String categorie,
                    String carburant, String boiteVitesse, int nombrePlaces, double prixParJour, String statut) {
        this.id.set(id);
        this.marque.set(marque);
        this.modele.set(modele);
        this.immatriculation.set(immatriculation);
        this.categorie.set(categorie);
        this.carburant.set(carburant);
        this.boiteVitesse.set(boiteVitesse);
        this.nombrePlaces.set(nombrePlaces);
        this.prixParJour.set(prixParJour);
        this.statut.set(statut);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty marqueProperty() { return marque; }
    public StringProperty modeleProperty() { return modele; }
    public StringProperty immatriculationProperty() { return immatriculation; }
    public StringProperty categorieProperty() { return categorie; }
    public StringProperty carburantProperty() { return carburant; }
    public StringProperty boiteVitesseProperty() { return boiteVitesse; }
    public IntegerProperty nombrePlacesProperty() { return nombrePlaces; }
    public DoubleProperty prixParJourProperty() { return prixParJour; }
    public StringProperty statutProperty() { return statut; }

    public int getId() { return id.get(); }
    public String getMarque() { return marque.get(); }
    public String getModele() { return modele.get(); }
    public String getImmatriculation() { return immatriculation.get(); }
    public String getCategorie() { return categorie.get(); }
    public String getCarburant() { return carburant.get(); }
    public String getBoiteVitesse() { return boiteVitesse.get(); }
    public int getNombrePlaces() { return nombrePlaces.get(); }
    public double getPrixParJour() { return prixParJour.get(); }
    public String getStatut() { return statut.get(); }

    public void setId(int val) { id.set(val); }
    public void setMarque(String val) { marque.set(val); }
    public void setModele(String val) { modele.set(val); }
    public void setImmatriculation(String val) { immatriculation.set(val); }
    public void setCategorie(String val) { categorie.set(val); }
    public void setCarburant(String val) { carburant.set(val); }
    public void setBoiteVitesse(String val) { boiteVitesse.set(val); }
    public void setNombrePlaces(int val) { nombrePlaces.set(val); }
    public void setPrixParJour(double val) { prixParJour.set(val); }
    public void setStatut(String val) { statut.set(val); }
    
    @Override
    public String toString() {
        return getMarque() + " " + getModele() + " (" + getImmatriculation() + ")";
    }
}

