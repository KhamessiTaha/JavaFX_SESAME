package org.tahakhamessi.model;

import javafx.beans.property.*;

public class Client {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nom = new SimpleStringProperty();
    private StringProperty prenom = new SimpleStringProperty();
    private StringProperty cin = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty telephone = new SimpleStringProperty();
    private StringProperty adresse = new SimpleStringProperty();
    private StringProperty numeroPermis = new SimpleStringProperty();
    private StringProperty expirationPermis = new SimpleStringProperty();

    public Client() {}

    public Client(int id, String nom, String prenom, String cin, String email, String telephone,
                  String adresse, String numeroPermis, String expirationPermis) {
        this.id.set(id);
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.cin.set(cin);
        this.email.set(email);
        this.telephone.set(telephone);
        this.adresse.set(adresse);
        this.numeroPermis.set(numeroPermis);
        this.expirationPermis.set(expirationPermis);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public StringProperty cinProperty() { return cin; }
    public StringProperty emailProperty() { return email; }
    public StringProperty telephoneProperty() { return telephone; }
    public StringProperty adresseProperty() { return adresse; }
    public StringProperty numeroPermisProperty() { return numeroPermis; }
    public StringProperty expirationPermisProperty() { return expirationPermis; }

    public int getId() { return id.get(); }
    public String getNom() { return nom.get(); }
    public String getPrenom() { return prenom.get(); }
    public String getCin() { return cin.get(); }
    public String getEmail() { return email.get(); }
    public String getTelephone() { return telephone.get(); }
    public String getAdresse() { return adresse.get(); }
    public String getNumeroPermis() { return numeroPermis.get(); }
    public String getExpirationPermis() { return expirationPermis.get(); }

    public void setId(int val) { id.set(val); }
    public void setNom(String val) { nom.set(val); }
    public void setPrenom(String val) { prenom.set(val); }
    public void setCin(String val) { cin.set(val); }
    public void setEmail(String val) { email.set(val); }
    public void setTelephone(String val) { telephone.set(val); }
    public void setAdresse(String val) { adresse.set(val); }
    public void setNumeroPermis(String val) { numeroPermis.set(val); }
    public void setExpirationPermis(String val) { expirationPermis.set(val); }

    @Override
    public String toString() {
        return getNom() + " " + getPrenom() + " (" + getCin() + ")";
    }
}

