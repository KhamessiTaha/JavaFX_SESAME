package org.tahakhamessi.util;

/**
 * Utility class for Tunisian legal texts and references.
 * Provides mandatory legal disclaimers, references to Tunisian law, and required legal notices.
 */
public class TunisianLegalUtil {

    // Legal References
    public static final String CODE_OBLIGATIONS_CONTRATS = "Code des Obligations et des Contrats (COC)";
    public static final String LOI_2004_33 = "Loi n° 2004-33 sur l'organisation des transports terrestres";
    public static final String CODE_ROUTE_TUNISIEN = "Code de la route tunisien";
    public static final String LOI_TVA = "Loi n° 2004-26 relative à la TVA modifiée et complétée";

    /**
     * Get the standard legal disclaimer for rental contracts (French)
     */
    public static String getDisclaimerFrench() {
        return "CLAUSE LÉGALE - Conformément au Code des Obligations et des Contrats (articles régissant les " +
                "contrats de location) et à la Loi n° 2004-33 sur l'organisation des transports terrestres, " +
                "le locataire s'engage à respecter toutes les conditions énumérées dans le présent contrat. " +
                "Le non-respect de ces conditions peut entraîner des pénalités et des frais supplémentaires. " +
                "La responsabilité civile et tous les dommages à la propriété restent sous la responsabilité " +
                "du locataire pendant la durée de la location.";
    }

    /**
     * Get the standard legal disclaimer in Arabic (French translation note for French documents)
     */
    public static String getDisclaimerArabic() {
        return "البند القانوني - طبقاً لقانون الالتزامات والعقود والقانون عدد 2004/33 الخاص " +
                "بتنظيم النقل البري، يتعهد المستأجر بالامتثال لجميع الشروط المذكورة في هذا العقد. " +
                "عدم الامتثال لهذه الشروط قد يؤدي إلى غرامات ورسوم إضافية.";
    }

    /**
     * Get the fiscal stamp notice (Tunisian requirement)
     */
    public static String getFiscalStampNotice() {
        return "Timbre fiscal dû : Conformément aux dispositions fiscales tunisiennes, un timbre fiscal " +
                "de 1.200 TND est requis sur ce document selon le Code des Droits d'Enregistrement. " +
                "Le paiement du timbre incombe au locataire.";
    }

    /**
     * Get insurance notice
     */
    public static String getInsuranceNotice() {
        return "ASSURANCE OBLIGATOIRE : La location inclut une assurance responsabilité civile automobile " +
                "conformément à la Loi n° 94-33 du 27 avril 1994. Le locataire est couvert contre les " +
                "dommages à des tiers. Les dommages au véhicule loué restent à la charge du locataire " +
                "selon les conditions de franchise mentionnées dans ce contrat.";
    }

    /**
     * Get TVA notice
     */
    public static String getTVANotice() {
        return "TVA (19%) : Le montant indiqué HT (Hors Taxe) est soumis à une TVA de 19% selon " +
                "la Loi n° 2004-26 relative à la TVA. Le montant TTC (Toutes Taxes Comprises) " +
                "inclut cette TVA.";
    }

    /**
     * Get vehicle condition acknowledgment text
     */
    public static String getVehicleConditionAcknowledgment() {
        return "ÉTAT DU VÉHICULE RECONNU : Le locataire certifie avoir reçu le véhicule en bon état " +
                "et reconnaît avoir visité et inspecté le véhicule en sa présence. Tous les dommages " +
                "antérieurs ont été notifiés et documentés. Toute détérioration supplémentaire sera " +
                "facturée au locataire au tarif courant de réparation.";
    }

    /**
     * Get responsibility disclaimer
     */
    public static String getResponsibilityDisclaimer() {
        return "RESPONSABILITÉ : Le locataire demeure entièrement responsable du véhicule loué pendant " +
                "la période de location. Cela inclut les dommages causés par accident, vol, vandalisme, " +
                "ou mauvais usage. Le locataire s'engage également à respecter tous les " +
                "règlements du Code de la route tunisien, notamment les limitations de vitesse " +
                "et les règles de sécurité routière.";
    }

    /**
     * Get late return penalty notice
     */
    public static String getLateReturnNotice() {
        return "RETARD DE RESTITUTION : En cas de retour en retard, une pénalité sera appliquée " +
                "selon le tarif de 50 TND pour chaque heure supplémentaire ou fraction d'heure. " +
                "Après 24 heures de retard, une journée supplémentaire complète sera facturée.";
    }

    /**
     * Get driver license requirements
     */
    public static String getDriverLicenseRequirement() {
        return "PERMIS DE CONDUIRE : Le locataire doit être en possession d'un permis de conduire " +
                "valide et non expiré. La vérification du permis est obligatoire avant la location. " +
                "Le locataire accepte que le permis soit photocopié pour archivage. Un permis expiré " +
                "annule automatiquement la location.";
    }

    /**
     * Get fuel policy notice
     */
    public static String getFuelPolicyNotice() {
        return "POLITIQUE CARBURANT : Le véhicule est fourni avec un plein de carburant. " +
                "Le locataire s'engage à restituer le véhicule avec le même niveau de carburant. " +
                "En cas de non-respect, un supplément équivalent au prix du carburant manquant plus " +
                "10% de frais de manutention sera facturé.";
    }

    /**
     * Get mileage terms notice
     */
    public static String getMileageNotice() {
        return "KILOMÉTRAGE : Le nombre de kilomètres inclus dans la location est indiqué dans le contrat. " +
                "Tout dépassement sera facturé au tarif de 0.50 TND par kilomètre supplémentaire. " +
                "Le locataire accepte que le kilométrage soit relevé à la remise et à la restitution " +
                "du véhicule.";
    }

    /**
     * Get signature requirement text
     */
    public static String getSignatureRequirement() {
        return "SIGNATURE ET ACCEPTATION : En signant ci-dessous, le locataire certifie avoir " +
                "reçu un exemplaire du contrat et avoir compris toutes les conditions. Deux copies de " +
                "ce contrat seront établies : une pour le loueur et une pour le locataire.";
    }

    /**
     * Get Arabic-French legal mention (for official documents)
     */
    public static String getOfficialMention() {
        return "Document Officiel / وثيقة رسمية\n" +
                "Conforme aux normes légales tunisiennes / متوافق مع المعايير القانونية التونسية";
    }

    /**
     * Get complete legal footer for documents
     */
    public static String getCompleteLegalFooter() {
        return "Références légales : Code des Obligations et des Contrats (COC), Loi n° 2004-33, " +
                "Code de la route tunisien, Loi n° 2004-26 TVA\n" +
                "Ce document est un enregistrement légal officiel. Tous les litiges seront régis par " +
                "la loi tunisienne et soumis aux tribunaux compétents en Tunisie.";
    }

    /**
     * Get payment terms text
     */
    public static String getPaymentTerms() {
        return "CONDITIONS DE PAIEMENT : Le paiement intégral est dû à la signature du contrat ou " +
                "avant la remise du véhicule. Les paiements peuvent être effectués en espèces (TND), " +
                "par chèque bancaire ou par carte bancaire. Aucun remboursement n'est autorisé après " +
                "la signature du contrat, sauf accord écrit spécifique.";
    }

    /**
     * Get cancellation terms
     */
    public static String getCancellationTerms() {
        return "CONDITIONS D'ANNULATION : Une annulation peut être acceptée minimum 48 heures avant " +
                "la date de location prévue. Un frais d'annulation de 10% du montant total sera " +
                "retenu. Aucun remboursement ne sera effectué pour les annulations effectuées moins " +
                "de 48 heures avant la location.";
    }

    /**
     * Get maximum fuel consumption estimate
     */
    public static String getFuelConsumptionEstimate(double kmDailyEstimate) {
        return String.format("Consommation estimée : %.1f litres par jour (basée sur %.0f km/jour). " +
                "Cette estimation peut varier selon les conditions de conduite.", kmDailyEstimate * 0.08, kmDailyEstimate);
    }

    /**
     * Get accident report requirement text
     */
    public static String getAccidentReportRequirement() {
        return "RAPPORT D'ACCIDENT : En cas d'accident ou d'incident, le locataire s'engage à signaler " +
                "immédiatement à la police locale (n° d'urgence : 197) et à contacter le prestataire " +
                "dans les 2 heures suivant l'incident. Un rapport d'accident officiel doit être obtenu " +
                "et fourni au prestataire pour validation d'assurance.";
    }
}

