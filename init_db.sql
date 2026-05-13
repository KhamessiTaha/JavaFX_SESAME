-- Database Schema for Car Management System (JavaFX_Sesame)

-- 1. Table: utilisateurs
CREATE TABLE IF NOT EXISTS utilisateurs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL,
    status TEXT DEFAULT 'EN_ATTENTE'
);

-- 2. Table: vehicules
CREATE TABLE IF NOT EXISTS vehicules (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    marque TEXT NOT NULL,
    modele TEXT NOT NULL,
    immatriculation TEXT UNIQUE NOT NULL,
    categorie TEXT NOT NULL,
    carburant TEXT NOT NULL,
    boiteVitesse TEXT NOT NULL,
    nombrePlaces INTEGER NOT NULL,
    prixParJour REAL NOT NULL,
    statut TEXT NOT NULL
);

-- 3. Table: clients
CREATE TABLE IF NOT EXISTS clients (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    prenom TEXT NOT NULL,
    cin TEXT UNIQUE NOT NULL,
    email TEXT NOT NULL,
    telephone TEXT NOT NULL,
    adresse TEXT NOT NULL,
    numeroPermis TEXT NOT NULL,
    expirationPermis TEXT NOT NULL
);

-- 4. Table: reservations
CREATE TABLE IF NOT EXISTS reservations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    clientId INTEGER NOT NULL,
    vehiculeId INTEGER NOT NULL,
    dateDebut TEXT NOT NULL,
    dateFin TEXT NOT NULL,
    nombreJours INTEGER NOT NULL,
    prixTotal REAL NOT NULL,
    statut TEXT NOT NULL,
    options TEXT,
    prixOptions REAL,
    FOREIGN KEY(clientId) REFERENCES clients(id),
    FOREIGN KEY(vehiculeId) REFERENCES vehicules(id)
);

-- 5. Table: paiements
CREATE TABLE IF NOT EXISTS paiements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reservationId INTEGER NOT NULL,
    montantTotal REAL NOT NULL,
    montantPaye REAL NOT NULL,
    resteAPayer REAL NOT NULL,
    modePaiement TEXT NOT NULL,
    statutPaiement TEXT NOT NULL,
    FOREIGN KEY(reservationId) REFERENCES reservations(id)
);

-- 6. Table: retours
CREATE TABLE IF NOT EXISTS retours (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reservationId INTEGER NOT NULL,
    dateRetour TEXT NOT NULL,
    etatVehicule TEXT NOT NULL,
    fraisSupplementaires REAL NOT NULL,
    FOREIGN KEY(reservationId) REFERENCES reservations(id)
);

-- Sample Data Seeding

-- Utilisateurs
INSERT OR IGNORE INTO utilisateurs (username, password, role, status) VALUES 
('admin', 'admin', 'admin', 'ACTIF'),
('agent', 'agent', 'agent', 'ACTIF');

-- Vehicules
INSERT OR IGNORE INTO vehicules (marque, modele, immatriculation, categorie, carburant, boiteVitesse, nombrePlaces, prixParJour, statut) VALUES 
('Toyota', 'Corolla', '123 TUN 4567', 'Economique', 'Essence', 'Manuel', 5, 120.0, 'Disponible'),
('BMW', 'X5', '200 TUN 8888', 'SUV', 'Diesel', 'Automatique', 7, 450.0, 'Disponible'),
('Mercedes', 'C-Class', '210 TUN 1234', 'Luxe', 'Essence', 'Automatique', 5, 550.0, 'Maintenance'),
('Peugeot', '308', '150 TUN 5555', 'Compacte', 'Diesel', 'Manuel', 5, 150.0, 'Disponible'),
('Audi', 'A4', '180 TUN 9999', 'Luxe', 'Essence', 'Automatique', 5, 480.0, 'Disponible'),
('Dacia', 'Duster', '220 TUN 4321', 'SUV', 'GPL', 'Manuel', 5, 130.0, 'Disponible'),
('Tesla', 'Model 3', '230 TUN 1111', 'Electrique', 'Electrique', 'Automatique', 5, 700.0, 'Disponible'),
('Renault', 'Clio', '190 TUN 7777', 'Citadine', 'Essence', 'Manuel', 5, 110.0, 'Louée'),
('Volkswagen', 'Golf 8', '205 TUN 2222', 'Compacte', 'Diesel', 'Automatique', 5, 180.0, 'Disponible'),
('Ford', 'Mustang', '215 TUN 6666', 'Sport', 'Essence', 'Automatique', 4, 850.0, 'Disponible'),
('Kia', 'Sportage', '225 TUN 3333', 'SUV', 'Diesel', 'Automatique', 5, 220.0, 'Disponible'),
('Hyundai', 'i10', '160 TUN 4444', 'Citadine', 'Essence', 'Manuel', 5, 95.0, 'Disponible'),
('Fiat', '500', '140 TUN 1234', 'Citadine', 'Essence', 'Automatique', 4, 100.0, 'Disponible'),
('Jeep', 'Wrangler', '240 TUN 5678', 'SUV', 'Essence', 'Automatique', 4, 400.0, 'Disponible'),
('Nissan', 'Qashqai', '175 TUN 9101', 'SUV', 'Diesel', 'Manuel', 5, 190.0, 'Disponible'),
('Volvo', 'XC90', '250 TUN 1122', 'Luxe', 'Hybride', 'Automatique', 7, 600.0, 'Disponible'),
('Skoda', 'Octavia', '208 TUN 3344', 'Berline', 'Diesel', 'Automatique', 5, 170.0, 'Disponible'),
('Mazda', 'CX-5', '195 TUN 5566', 'SUV', 'Essence', 'Automatique', 5, 210.0, 'Disponible'),
('Land Rover', 'Defender', '260 TUN 7788', 'SUV', 'Diesel', 'Automatique', 5, 500.0, 'Disponible'),
('Porsche', '911', '300 TUN 0911', 'Sport', 'Essence', 'Automatique', 2, 1200.0, 'Maintenance'),
('Toyota', 'Land Cruiser', '270 TUN 9999', 'SUV', 'Diesel', 'Automatique', 7, 650.0, 'Disponible'),
('Volkswagen', 'Polo', '185 TUN 1234', 'Citadine', 'Essence', 'Manuel', 5, 110.0, 'Disponible'),
('Mercedes', 'E-Class', '280 TUN 5555', 'Luxe', 'Diesel', 'Automatique', 5, 600.0, 'Disponible'),
('Hyundai', 'Tucson', '245 TUN 8888', 'SUV', 'Hybride', 'Automatique', 5, 280.0, 'Disponible'),
('Renault', 'Megane', '212 TUN 7777', 'Berline', 'Diesel', 'Manuel', 5, 160.0, 'Disponible'),
('BMW', 'M4', '310 TUN 1313', 'Sport', 'Essence', 'Automatique', 4, 950.0, 'Disponible'),
('Peugeot', '208', '165 TUN 4321', 'Citadine', 'Essence', 'Manuel', 5, 105.0, 'Disponible'),
('Ford', 'Focus', '198 TUN 6666', 'Compacte', 'Diesel', 'Automatique', 5, 155.0, 'Disponible'),
('Audi', 'Q7', '290 TUN 1111', 'SUV', 'Diesel', 'Automatique', 7, 580.0, 'Disponible'),
('Dacia', 'Sandero', '170 TUN 3333', 'Economique', 'GPL', 'Manuel', 5, 90.0, 'Disponible');

-- Clients
INSERT OR IGNORE INTO clients (nom, prenom, cin, email, telephone, adresse, numeroPermis, expirationPermis) VALUES 
('Ben Salah', 'Ahmed', '01234567', 'ahmed.bensalah@email.tn', '98765432', 'Tunis, Cite El Khadra', 'PERM-TN-001', '2030-01-01'),
('Trabelsi', 'Sonia', '08765432', 'sonia.trabelsi@email.tn', '22334455', 'Sousse, Khezama', 'PERM-TN-002', '2028-05-12'),
('Mahmoudi', 'Mohamed', '01122334', 'm.mahmoudi@email.tn', '55667788', 'Sfax, Route de Tunis', 'PERM-TN-003', '2032-11-20'),
('Tounsi', 'Amine', '09876543', 'amine.tounsi@email.tn', '20102030', 'Tunis, Berges du Lac', 'PERM-TN-004', '2027-03-15'),
('Gharbi', 'Leila', '07654321', 'leila.gharbi@email.tn', '99112233', 'Bizerte, Corniche', 'PERM-TN-005', '2029-08-25'),
('Hamdi', 'Youssef', '05544332', 'youssef.hamdi@email.tn', '25252525', 'Nabeul, Hammamet', 'PERM-TN-006', '2031-12-30'),
('Karray', 'Ines', '04433221', 'ines.karray@email.tn', '21212121', 'Sfax, El Jadida', 'PERM-TN-007', '2026-06-20'),
('Jaziri', 'Zied', '03322110', 'zied.jaziri@email.tn', '23232323', 'Monastir, Centre Ville', 'PERM-TN-008', '2029-01-10'),
('Chakroun', 'Mariem', '02211009', 'mariem.chakroun@email.tn', '24242424', 'Gafsa, Cite Ezzouhour', 'PERM-TN-009', '2028-09-05'),
('Ouerghi', 'Sami', '01100998', 'sami.ouerghi@email.tn', '26262626', 'Beja, Tabarka', 'PERM-TN-010', '2030-04-14'),
('Mansour', 'Hela', '00099887', 'hela.mansour@email.tn', '27272727', 'Gabes, Chenini', 'PERM-TN-011', '2032-02-28'),
('Bouaziz', 'Walid', '06655443', 'walid.bouaziz@email.tn', '28282828', 'Djerba, Houmt Souk', 'PERM-TN-012', '2027-10-10'),
('Slimane', 'Nadia', '07766554', 'nadia.slimane@email.tn', '29292929', 'Tataouine, Chenini', 'PERM-TN-013', '2029-07-22'),
('Ziedi', 'Faten', '05566778', 'faten.ziedi@email.tn', '20304050', 'Ariana, Ennasr', 'PERM-TN-014', '2031-03-12'),
('Dridi', 'Hassen', '04455667', 'hassen.dridi@email.tn', '98123456', 'Kairouan, Mansoura', 'PERM-TN-015', '2028-11-30'),
('Saidi', 'Olfa', '03344556', 'olfa.saidi@email.tn', '21000000', 'Mahdia, Zone Touristique', 'PERM-TN-016', '2032-05-15'),
('Ben Ali', 'Karim', '02233445', 'karim.benali@email.tn', '50607080', 'Tozeur, Palmeraie', 'PERM-TN-017', '2027-09-01'),
('Masmoudi', 'Rania', '01122333', 'rania.masmoudi@email.tn', '22113344', 'Sousse, Port El Kantaoui', 'PERM-TN-018', '2030-12-25'),
('Kallel', 'Omar', '08877665', 'omar.kallel@email.tn', '55442211', 'Sfax, Route de Gremda', 'PERM-TN-019', '2026-08-10'),
('Mejri', 'Yosra', '07788990', 'yosra.mejri@email.tn', '99887766', 'Jendouba, Tabarka', 'PERM-TN-020', '2029-04-18'),
('Guesmi', 'Mounir', '06677889', 'mounir.guesmi@email.tn', '23456789', 'Kasserine, Cite Ezzouhour', 'PERM-TN-021', '2031-10-05');

-- Reservations
INSERT OR IGNORE INTO reservations (id, clientId, vehiculeId, dateDebut, dateFin, nombreJours, prixTotal, statut, options, prixOptions) VALUES 
(1, 1, 8, '2024-05-01', '2024-05-05', 4, 440.0, 'Terminée', 'GPS', 20.0),
(2, 2, 4, '2026-05-10', '2026-05-15', 5, 750.0, 'Confirmée', 'Siège Bébé', 50.0),
(3, 3, 1, '2026-06-01', '2026-06-07', 6, 720.0, 'Confirmée', 'GPS, Assurance Plus', 60.0),
(4, 4, 2, '2026-05-20', '2026-05-25', 5, 2250.0, 'En attente', 'Aucune', 0.0),
(5, 5, 5, '2024-01-15', '2024-01-20', 5, 2400.0, 'Terminée', 'GPS', 30.0),
(6, 6, 9, '2024-03-10', '2024-03-12', 2, 360.0, 'Terminée', 'Aucune', 0.0),
(7, 7, 12, '2026-07-01', '2026-07-03', 2, 190.0, 'Confirmée', 'Aucune', 0.0),
(8, 8, 6, '2026-05-01', '2026-05-10', 9, 1170.0, 'Annulée', 'Siège Bébé', 40.0),
(9, 9, 13, '2026-08-15', '2026-08-20', 5, 500.0, 'Confirmée', 'Aucune', 0.0),
(10, 10, 15, '2024-02-01', '2024-02-05', 4, 760.0, 'Terminée', 'GPS', 20.0),
(11, 11, 10, '2026-09-01', '2026-09-10', 9, 7650.0, 'Confirmée', 'Plein Essence', 100.0),
(12, 12, 11, '2026-10-05', '2026-10-08', 3, 660.0, 'Confirmée', 'GPS', 15.0),
(13, 1, 7, '2026-11-20', '2026-11-25', 5, 3500.0, 'Confirmée', 'Tout inclus', 200.0),
(14, 14, 21, '2024-04-01', '2024-04-05', 4, 2600.0, 'Terminée', 'Aucune', 0.0),
(15, 15, 22, '2024-04-10', '2024-04-12', 2, 220.0, 'Terminée', 'Siège Bébé', 20.0),
(16, 16, 23, '2026-05-15', '2026-05-20', 5, 3000.0, 'Confirmée', 'Chauffeur', 500.0),
(17, 17, 24, '2026-06-10', '2026-06-15', 5, 1400.0, 'En attente', 'GPS', 25.0),
(18, 18, 25, '2026-07-01', '2026-07-10', 9, 1440.0, 'Confirmée', 'Aucune', 0.0),
(19, 19, 26, '2024-02-15', '2024-02-18', 3, 2850.0, 'Terminée', 'Plein Essence', 100.0),
(20, 20, 27, '2026-08-01', '2026-08-03', 2, 210.0, 'Confirmée', 'Aucune', 0.0),
(21, 21, 28, '2026-09-10', '2026-09-15', 5, 775.0, 'Confirmée', 'GPS', 20.0),
(22, 13, 29, '2026-05-25', '2026-05-30', 5, 2900.0, 'En attente', 'Assurance Plus', 150.0),
(23, 3, 30, '2024-03-20', '2024-03-22', 2, 180.0, 'Terminée', 'Aucune', 0.0);

-- Paiements
INSERT OR IGNORE INTO paiements (reservationId, montantTotal, montantPaye, resteAPayer, modePaiement, statutPaiement) VALUES 
(1, 460.0, 460.0, 0.0, 'Espèces', 'Payé'),
(2, 800.0, 200.0, 600.0, 'Carte Bancaire', 'Partiel'),
(3, 780.0, 780.0, 0.0, 'Chèque', 'Payé'),
(5, 2430.0, 2430.0, 0.0, 'Carte Bancaire', 'Payé'),
(6, 360.0, 360.0, 0.0, 'Espèces', 'Payé'),
(10, 780.0, 780.0, 0.0, 'Virement', 'Payé'),
(14, 2600.0, 2600.0, 0.0, 'Carte Bancaire', 'Payé'),
(15, 240.0, 240.0, 0.0, 'Espèces', 'Payé'),
(19, 2950.0, 2950.0, 0.0, 'Chèque', 'Payé'),
(23, 180.0, 180.0, 0.0, 'Espèces', 'Payé');

-- Retours
INSERT OR IGNORE INTO retours (reservationId, dateRetour, etatVehicule, fraisSupplementaires) VALUES 
(1, '2024-05-05', 'Excellent', 0.0),
(5, '2024-01-20', 'Bon', 0.0),
(6, '2024-03-12', 'Rayure légère', 50.0),
(10, '2024-02-05', 'Propre', 0.0),
(14, '2024-04-05', 'Excellent', 0.0),
(15, '2024-04-12', 'Bon', 0.0),
(19, '2024-02-18', 'Besoin lavage', 20.0),
(23, '2024-03-22', 'Excellent', 0.0);
