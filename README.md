# 🚗 Car Rental Management System

A modern, professional JavaFX desktop application for managing car rental operations. Built with a focus on user experience, this system provides comprehensive tools for vehicle fleet management, client handling, reservations, payments, and analytics.

## ✨ Features

### 📋 Core Management Modules

- **🚗 Vehicle Management**
  - Add, update, and delete vehicle inventory
  - Track vehicle status (Available, Maintenance, Rented)
  - Manage vehicle specifications (brand, model, registration, fuel type, seats, price per day)
  - Filter and search vehicles by any field

- **👥 Client Management**
  - Complete client information management
  - Driver license tracking with expiration dates
  - Contact information management (email, phone, address)
  - Client reservation history view
  - Email and phone validation

- **📅 Reservation Management**
  - Create and manage reservations
  - Automatic price calculation based on rental duration
  - Overlapping reservation detection
  - Reservation status tracking (Pending, Confirmed, Cancelled, Completed)
  - Date-based availability checking

- **💰 Payment Processing**
  - Track payment for each reservation
  - Support multiple payment modes (Cash, Card, Check, Bank Transfer)
  - Automatic balance calculation
  - Payment status management (Paid, Partial, Unpaid)

- **↩️ Vehicle Returns**
  - Record vehicle returns with condition assessment
  - Additional fees for damaged vehicles
  - Automatic vehicle status updates
  - Return history tracking

- **📊 Analytics Dashboard**
  - Total reservations count
  - Total revenue tracking
  - Available vehicles overview
  - Most rented vehicle identification
  - Real-time business performance metrics

### 🔐 User Management

- **Two-tier user system:**
  - **Admin**: Full access to all features
  - **Agent**: Limited access (Reservations, Clients, Returns)

### 🎨 Modern UI/UX

- **Responsive Design**: Adapts to different screen sizes
- **Modern Color Scheme**: Professional gradient backgrounds with intuitive color coding
- **Smooth Animations**: Subtle transitions and hover effects
- **Icons & Emojis**: Visual indicators for better usability
- **Dark/Light Contrast**: Easy on the eyes color combinations
- **Accessible Forms**: Clear input fields and validation messages

## 🛠️ Technology Stack

- **Language**: Java 17
- **GUI Framework**: JavaFX 21.0.1
- **Database**: SQLite (carmanagement.db)
- **Build Tool**: Maven
- **Architecture**: MVC (Model-View-Controller)

## 📦 Project Structure

```
JavaFX_SESAME/
├── src/
│   └── main/
│       ├── java/org/tahakhamessi/
│       │   ├── Main.java                 # Application entry point
│       │   ├── controller/               # FXML Controllers
│       │   │   ├── LoginController.java
│       │   │   ├── DashboardController.java
│       │   │   ├── ClientsController.java
│       │   │   ├── VehiclesController.java
│       │   │   ├── ReservationsController.java
│       │   │   ├── PaymentsController.java
│       │   │   ├── ReturnsController.java
│       │   │   └── StatisticsController.java
│       │   ├── dao/                      # Data Access Objects
│       │   │   ├── ClientDAO.java
│       │   │   ├── VehiculeDAO.java
│       │   │   ├── ReservationDAO.java
│       │   │   ├── PaiementDAO.java
│       │   │   ├── RetourDAO.java
│       │   │   └── UtilisateurDAO.java
│       │   ├── model/                    # Data Models
│       │   │   ├── Client.java
│       │   │   ├── Vehicule.java
│       │   │   ├── Reservation.java
│       │   │   ├── Paiement.java
│       │   │   ├── Retour.java
│       │   │   └── Utilisateur.java
│       │   └── util/                     # Utility Classes
│       │       ├── DatabaseManager.java
│       │       └── ValidationUtil.java
│       └── resources/
│           ├── fxml/                     # FXML UI Files
│           │   ├── Login.fxml
│           │   ├── Dashboard.fxml
│           │   ├── Clients.fxml
│           │   ├── Vehicles.fxml
│           │   ├── Reservations.fxml
│           │   ├── Payments.fxml
│           │   ├── Returns.fxml
│           │   └── Statistics.fxml
│           └── styles/                   # CSS Stylesheets
│               └── styles.css
├── pom.xml                              # Maven configuration
└── carmanagement.db                     # SQLite Database
```

## 🚀 Getting Started

### Prerequisites

- **Java 17 or higher** installed
- **Maven 3.6+** installed
- **Windows/Linux/Mac** operating system

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/JavaFX_SESAME.git
   cd JavaFX_SESAME
   ```

2. **Build the project:**
   ```bash
   mvn clean compile
   ```

3. **Run the application:**
   ```bash
   mvn javafx:run
   ```

### Alternative: Using run.bat (Windows)

Simply double-click the `run.bat` file in the project root directory.

## 🔑 Default Credentials

The application comes with two pre-configured user accounts:

| Username | Password | Role | Access |
|----------|----------|------|--------|
| admin | admin | Administrator | Full system access |
| agent | agent | Agent | Limited access (Reservations, Clients, Returns) |

**Note:** Change these credentials in production!

## 📖 Usage Guide

### Login
- Enter your credentials on the login screen
- Admin and Agent accounts have different access levels

### Dashboard
- Navigate through different modules using the menu
- Quick action cards for fast access to main features
- Logout option in the top-right corner

### Managing Vehicles
1. Go to **Management → Vehicles**
2. Fill in vehicle details in the form
3. Click **Add** to create a new vehicle
4. Use **Update** to modify selected vehicle
5. Use **Delete** to remove a vehicle
6. Use the search/filter functionality to find vehicles

### Managing Clients
1. Go to **Management → Clients**
2. Enter client information
3. Click **Add** to register a client
4. View client reservation history with the **History** button
5. Update or delete client information as needed

### Creating Reservations
1. Go to **Management → Reservations**
2. Select client and vehicle
3. Choose start and end dates
4. System calculates days and total price automatically
5. Click **Add Reservation**
6. Track reservation status

### Processing Payments
1. Go to **Operations → Payments**
2. Select reservation from dropdown
3. Enter payment amounts
4. Choose payment method and status
5. System calculates remaining balance
6. Click **Add Payment** to record transaction

### Recording Vehicle Returns
1. Go to **Operations → Returns**
2. Select reservation
3. Enter return date and vehicle condition
4. Add additional fees if applicable
5. Click **Add Return**
6. Vehicle status updates automatically

### Viewing Statistics
1. Go to **Analytics → Statistics**
2. View real-time business metrics:
   - Total reservations
   - Total revenue
   - Available vehicles
   - Most rented vehicle

## 🔒 Data Validation

The application includes comprehensive validation:

- **Email validation**: Ensures valid email format
- **Phone validation**: Checks phone number format
- **License expiration**: Alerts when driver's license is expired
- **Price validation**: Ensures positive prices
- **Date validation**: Checks date ranges and overlapping reservations
- **Required fields**: Prevents empty submissions
- **Duplicate prevention**: Avoids duplicate CINs and registrations

## 💾 Database

- **Type**: SQLite
- **File**: `carmanagement.db`
- **Location**: Project root directory
- **Auto-initialization**: Database and tables are created automatically on first run
- **Sample data**: Includes pre-loaded users

## 🎨 UI/UX Features

### Color Scheme
- **Blue/Purple Gradient** (Login, Clients)
- **Pink/Red Gradient** (Vehicles, Returns)
- **Cyan Gradient** (Reservations)
- **Green Gradient** (Payments)
- **Yellow/Pink Gradient** (Statistics)

### Responsive Elements
- ScrollPane support for scrollable content
- Adaptive table sizing
- GridPane for organized layouts
- VBox/HBox for flexible spacing

### Visual Feedback
- Success messages (Green)
- Error messages (Red)
- Important information highlights
- Hover effects on buttons and tables

## 🔧 Configuration

### Application Settings
- **Min Java Version**: 17
- **JavaFX Version**: 21.0.1
- **SQLite JDBC**: 3.43.0.0
- **Window Size**: Responsive (adaptive to content)

### CSS Customization
Edit `src/main/resources/styles/styles.css` to customize:
- Colors
- Font sizes
- Button styles
- Table appearance
- Component spacing

## 🐛 Troubleshooting

### Issue: "JavaFX runtime components are missing"
**Solution**: Use `mvn javafx:run` instead of running directly from IDE

### Issue: Database connection error
**Solution**: Ensure write permissions in the project root directory
```bash
# On Windows
attrib -R carmanagement.db
```

### Issue: Application won't start
**Solution**: Clear Maven cache and rebuild
```bash
mvn clean compile javafx:run
```

## 📝 Development

### Adding New Features
1. Create models in `model/` package
2. Create DAOs in `dao/` package
3. Create FXML in `src/main/resources/fxml/`
4. Create controller in `controller/` package
5. Add route in `DashboardController.java`

### Building Executable JAR
```bash
mvn package
java -jar target/JavaFX_Sesame-1.0-SNAPSHOT.jar
```

## 📜 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

**Taha KHAMMESSI**

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 Support

For issues, questions, or suggestions, please:
1. Check existing GitHub issues
2. Create a new issue with detailed information
3. Include screenshots or error messages

## 🎯 Roadmap

### Upcoming Features
- [ ] Multi-language support (English, French, Arabic)
- [ ] Export reports to PDF/Excel
- [ ] Email notifications for reservations
- [ ] SMS alerts for returns
- [ ] Advanced analytics with charts
- [ ] Customer rating system
- [ ] Damaged vehicle damage tracking
- [ ] Insurance claim management
- [ ] Mobile app companion
- [ ] Cloud synchronization

### Performance Improvements
- [ ] Database query optimization
- [ ] Caching mechanisms
- [ ] Async data loading
- [ ] Image compression for vehicle photos

## 📚 Documentation

See the [Wiki](https://github.com/yourusername/JavaFX_SESAME/wiki) for additional documentation:
- API Reference
- Database Schema
- User Guides
- Video Tutorials

## ✅ Checklist

- ✅ Vehicle Management CRUD
- ✅ Client Management CRUD
- ✅ Reservation System
- ✅ Payment Processing
- ✅ Vehicle Returns
- ✅ Analytics Dashboard
- ✅ User Authentication
- ✅ Modern UI/UX
- ✅ Data Validation
- ✅ Error Handling
- ✅ Responsive Design

---

**Made with ❤️ for efficient car rental management**

**Last Updated**: May 13, 2026

**Version**: 1.0-SNAPSHOT

