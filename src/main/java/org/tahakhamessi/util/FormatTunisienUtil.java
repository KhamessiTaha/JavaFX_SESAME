package org.tahakhamessi.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utility class for Tunisian formatting standards.
 * Handles currency (TND), date formats, TVA calculation, and other Tunisian-specific formatting.
 */
public class FormatTunisienUtil {
    
    // Company Information
    public static final String COMPANY_NAME = "SESAME CAR RENTAL";
    public static final String COMPANY_MATRICULE_FISCALE = "1234567/A/M/000";
    public static final String COMPANY_RNE = "000123456";
    public static final String COMPANY_ADDRESS = "Rue Mohamed Ali, Tunis 1000, Tunisie";
    public static final String COMPANY_PHONE = "+216 71 123 456";
    public static final String COMPANY_EMAIL = "info@sesamecarrental.com";
    public static final String COMPANY_WEB = "www.sesamecarrental.com";
    
    // TVA (Taxe sur la Valeur Ajoutée) - 19% in Tunisia
    public static final double TVA_RATE = 0.19;
    
    // Currency formatting (Tunisian Dinar)
    private static final Locale LOCALE_TN = Locale.FRANCE;
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(LOCALE_TN);
    
    static {
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
    }
    
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00", symbols);
    private static final DecimalFormat CURRENCY_NO_DECIMALS = new DecimalFormat("#,##0", symbols);
    
    // Date formatters
    private static final DateTimeFormatter DATE_FORMAT_LONG = 
        DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
    private static final DateTimeFormatter DATE_FORMAT_SHORT = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMAT_LONG =
        DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);

    /**
     * Format amount as Tunisian Dinar (TND)
     */
    public static String formatDevise(double amount) {
        return CURRENCY_FORMAT.format(amount) + " TND";
    }

    /**
     * Format amount without currency symbol
     */
    public static String formatMontant(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }

    /**
     * Format amount as integer (no decimals)
     */
    public static String formatMontantEntier(double amount) {
        return CURRENCY_NO_DECIMALS.format(amount);
    }

    /**
     * Calculate TVA (19%)
     */
    public static double calculerTVA(double montantHT) {
        return montantHT * TVA_RATE;
    }

    /**
     * Calculate total amount including TVA
     */
    public static double calculerMontantTTC(double montantHT) {
        return montantHT + calculerTVA(montantHT);
    }

    /**
     * Format date in long format: "15 mai 2026"
     */
    public static String formatDateLongue(LocalDate date) {
        return date.format(DATE_FORMAT_LONG);
    }

    /**
     * Format date in short format: "15/05/2026"
     */
    public static String formatDateCourte(LocalDate date) {
        return date.format(DATE_FORMAT_SHORT);
    }

    /**
     * Format date/time: "15/05/2026 14:30:00"
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMAT);
    }

    /**
     * Format date/time long: "15 mai 2026 à 14:30"
     */
    public static String formatDateTimeLong(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMAT_LONG);
    }

    /**
     * Format current date in long format
     */
    public static String getDateJour() {
        return formatDateLongue(LocalDate.now());
    }

    /**
     * Format current date/time
     */
    public static String getDateTimeJour() {
        return formatDateTime(LocalDateTime.now());
    }

    /**
     * Generate document reference number
     * Format: SESAME-[TYPE]-[YEAR][MONTH][DAY]-[RANDOM]
     */
    public static String genererRefDocument(String typeDocument) {
        LocalDateTime now = LocalDateTime.now();
        int random = (int) (Math.random() * 9000) + 1000;
        return String.format("%s-%s-%04d%02d%02d-%d",
            "SESAME",
            typeDocument.substring(0, Math.min(3, typeDocument.length())).toUpperCase(),
            now.getYear(),
            now.getMonthValue(),
            now.getDayOfMonth(),
            random);
    }

    /**
     * Format Tunisian license plate
     * Format: "TN [125] ABC [123]"
     */
    public static String formatPlaqueImmatriculation(String plaque) {
        if (plaque == null || plaque.isEmpty()) {
            return "";
        }
        // If already formatted, return as-is
        if (plaque.contains(" ")) {
            return plaque;
        }
        // Simple formatting
        return plaque.toUpperCase();
    }

    /**
     * Format CIN (Carte d'Identité Nationale) with leading zeros
     */
    public static String formatCIN(String cin) {
        if (cin == null || cin.isEmpty()) {
            return "";
        }
        // CIN is typically 8 digits
        return String.format("%8s", cin).replace(' ', '0');
    }

    /**
     * Format phone number for Tunisian numbers
     */
    public static String formatTelephone(String telephone) {
        if (telephone == null || telephone.isEmpty()) {
            return "";
        }
        // Tunisian phone: +216 XX XXX XXX
        telephone = telephone.replaceAll("[^0-9+]", "");
        if (!telephone.startsWith("+216")) {
            if (telephone.startsWith("216")) {
                telephone = "+" + telephone;
            } else if (telephone.startsWith("0")) {
                telephone = "+216" + telephone.substring(1);
            } else {
                telephone = "+216" + telephone;
            }
        }
        return telephone;
    }

    /**
     * Get TVA rate as percentage string
     */
    public static String getTVAPercentage() {
        return String.format("%.0f%%", TVA_RATE * 100);
    }

    /**
     * Generate invoice/receipt number
     * Format: FAC-[YEAR][MONTH][DAY]-[SEQUENCE]
     */
    public static String genererNumeroFacture(int sequence) {
        LocalDateTime now = LocalDateTime.now();
        return String.format("FAC-%04d%02d%02d-%04d",
            now.getYear(),
            now.getMonthValue(),
            now.getDayOfMonth(),
            sequence);
    }

    /**
     * Generate contract number
     * Format: CTR-[YEAR][MONTH][DAY]-[SEQUENCE]
     */
    public static String genererNumeroContrat(int sequence) {
        LocalDateTime now = LocalDateTime.now();
        return String.format("CTR-%04d%02d%02d-%04d",
            now.getYear(),
            now.getMonthValue(),
            now.getDayOfMonth(),
            sequence);
    }

    /**
     * Get current year for text
     */
    public static int getAnneeActuelle() {
        return LocalDate.now().getYear();
    }

    /**
     * Parse string date to LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    /**
     * Load configuration from environment or use defaults
     * Can be extended to load from config file
     */
    public static void initializeFromConfig() {
        // This method can be called from Main.java during application startup
        // Currently uses hardcoded values, can be extended to load from:
        // - config file
        // - environment variables
        // - database
        System.out.println("Configuration Tunisienne chargée: " + COMPANY_NAME);
    }
    
    /**
     * Update company information (useful for multi-company scenarios)
     */
    public static final class CompanyConfig {
        private static String companyName = COMPANY_NAME;
        private static String matriculeFiscale = COMPANY_MATRICULE_FISCALE;
        private static String rne = COMPANY_RNE;
        private static String address = COMPANY_ADDRESS;
        private static String phone = COMPANY_PHONE;
        private static String email = COMPANY_EMAIL;
        
        public static void setCompanyName(String name) { companyName = name; }
        public static String getCompanyName() { return companyName; }
        
        public static void setMatriculeFiscale(String mf) { matriculeFiscale = mf; }
        public static String getMatriculeFiscale() { return matriculeFiscale; }
        
        public static void setRNE(String rneValue) { rne = rneValue; }
        public static String getRNE() { return rne; }
        
        public static void setAddress(String addr) { address = addr; }
        public static String getAddress() { return address; }
        
        public static void setPhone(String tel) { phone = tel; }
        public static String getPhone() { return phone; }
        
        public static void setEmail(String emailAddr) { email = emailAddr; }
        public static String getEmail() { return email; }
    }
}
