package org.tahakhamessi.util;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:carmanagement.db";
    private static Connection connection;

    public static void initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            seedData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS utilisateurs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS vehicules (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "marque TEXT NOT NULL," +
                    "modele TEXT NOT NULL," +
                    "immatriculation TEXT UNIQUE NOT NULL," +
                    "categorie TEXT NOT NULL," +
                    "carburant TEXT NOT NULL," +
                    "boiteVitesse TEXT NOT NULL," +
                    "nombrePlaces INTEGER NOT NULL," +
                    "prixParJour REAL NOT NULL," +
                    "statut TEXT NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS clients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nom TEXT NOT NULL," +
                    "prenom TEXT NOT NULL," +
                    "cin TEXT UNIQUE NOT NULL," +
                    "email TEXT NOT NULL," +
                    "telephone TEXT NOT NULL," +
                    "adresse TEXT NOT NULL," +
                    "numeroPermis TEXT NOT NULL," +
                    "expirationPermis TEXT NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "clientId INTEGER NOT NULL," +
                    "vehiculeId INTEGER NOT NULL," +
                    "dateDebut TEXT NOT NULL," +
                    "dateFin TEXT NOT NULL," +
                    "nombreJours INTEGER NOT NULL," +
                    "prixTotal REAL NOT NULL," +
                    "statut TEXT NOT NULL," +
                    "options TEXT," +
                    "prixOptions REAL," +
                    "FOREIGN KEY(clientId) REFERENCES clients(id)," +
                    "FOREIGN KEY(vehiculeId) REFERENCES vehicules(id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS paiements (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "reservationId INTEGER NOT NULL," +
                    "montantTotal REAL NOT NULL," +
                    "montantPaye REAL NOT NULL," +
                    "resteAPayer REAL NOT NULL," +
                    "modePaiement TEXT NOT NULL," +
                    "statutPaiement TEXT NOT NULL," +
                    "FOREIGN KEY(reservationId) REFERENCES reservations(id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS retours (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "reservationId INTEGER NOT NULL," +
                    "dateRetour TEXT NOT NULL," +
                    "etatVehicule TEXT NOT NULL," +
                    "fraisSupplementaires REAL NOT NULL," +
                    "FOREIGN KEY(reservationId) REFERENCES reservations(id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seedData() {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM utilisateurs");
            if (rs.next() && rs.getInt(1) == 0) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("INSERT INTO utilisateurs (username, password, role) VALUES ('admin', 'admin', 'admin')");
                    stmt.execute("INSERT INTO utilisateurs (username, password, role) VALUES ('agent', 'agent', 'agent')");

                    stmt.execute("INSERT INTO vehicules (marque, modele, immatriculation, categorie, carburant, boiteVitesse, nombrePlaces, prixParJour, statut) VALUES " +
                            "('Toyota', 'Corolla', 'AB-123-CD', 'Economique', 'Essence', 'Manuel', 5, 50.0, 'Disponible')," +
                            "('BMW', 'X5', 'EF-456-GH', 'SUV', 'Diesel', 'Automatique', 7, 150.0, 'Disponible')," +
                            "('Mercedes', 'C-Class', 'IJ-789-KL', 'Luxe', 'Essence', 'Automatique', 5, 200.0, 'Maintenance')," +
                            "('Peugeot', '308', 'MN-012-OP', 'Compacte', 'Diesel', 'Manuel', 5, 60.0, 'Disponible')");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

