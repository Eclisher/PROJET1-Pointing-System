package pointage.salaire.Model;

public enum Categorie {
    CADRE_SUPERIEUR("Cadre Supérieur", 40, 0),
    NORMAL("Normal", 40, 100000),
    GARDIEN("Gardien", 56, 110000),
    CHAUFFEUR("Chauffeur", 40, 90000);

    private final String nom;
    private final int heuresNormalesParSemaine;
    private final double salaireParSemaine;

    Categorie(String nom, int heuresNormalesParSemaine, double salaireParSemaine) {
        this.nom = nom;
        this.heuresNormalesParSemaine = heuresNormalesParSemaine;
        this.salaireParSemaine = salaireParSemaine;
    }

    public String getNom() { return nom; }
    public int getHeuresNormalesParSemaine() { return heuresNormalesParSemaine; }
    public double getSalaireParSemaine() { return salaireParSemaine; }
}
