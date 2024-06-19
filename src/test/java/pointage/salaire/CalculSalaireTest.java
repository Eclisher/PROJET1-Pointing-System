package pointage.salaire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pointage.salaire.Model.Calendrier;
import pointage.salaire.Model.Categorie;
import pointage.salaire.Model.Employe;
import pointage.salaire.Service.CalculSalaire;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculSalaireTest {

    private Calendrier calendrierJuin;
    private Employe rakoto;
    private Employe rabe;
    private CalculSalaire calculSalaire;
    private Map<Instant, Integer> heuresTravailleesRakoto;
    private Map<Instant, Integer> heuresTravailleesRabe;

    @BeforeEach
    public void setUp() {
        // Initialisation du calendrier pour juin
        calendrierJuin = new Calendrier();
        calendrierJuin.ajouterJourFerie(Instant.parse("2024-06-17T00:00:00Z"));
        calendrierJuin.ajouterJourFerie(Instant.parse("2024-06-25T00:00:00Z"));
        calendrierJuin.ajouterJourFerie(Instant.parse("2024-06-26T00:00:00Z"));

        // Création de l'employé Rakoto
        rakoto = new Employe("Rakoto", "Jean", "MAT001",
                LocalDate.of(1985, Month.JANUARY, 15),
                Instant.parse("2024-06-01T00:00:00Z"),
                Instant.parse("2024-06-30T23:59:59Z"),
                110000.0, Categorie.GARDIEN);

        // Création de l'employé Rabe
        rabe = new Employe("Rabe", "Paul", "MAT002",
                LocalDate.of(1986, Month.FEBRUARY, 20),
                Instant.parse("2024-06-01T00:00:00Z"),
                Instant.parse("2024-06-30T23:59:59Z"),
                110000.0, Categorie.GARDIEN);
        calculSalaire = new CalculSalaire();

        heuresTravailleesRakoto = new HashMap<>();
        heuresTravailleesRabe = new HashMap<>();
        for (int i = 1; i <= 30; i++) {
            Instant jour = Instant.parse(String.format("2024-06-%02dT00:00:00Z", i));
            if (i == 25 || i == 26) {
                heuresTravailleesRakoto.put(jour, 16);
            } else if (calendrierJuin.estJourDeTravail(jour, rakoto.getCategorie())) {
                heuresTravailleesRakoto.put(jour, 8);
            }
            if (calendrierJuin.estJourDeTravail(jour, rabe.getCategorie()) && (i != 25 && i != 26)) {
                heuresTravailleesRabe.put(jour, 8);
            }
        }
    }

    @Test
    public void testConfigurerCalendrierJuin() {
        // Vérification du calendrier de travail pour juin
        assertEquals(3, calendrierJuin.getJoursFeries().size());
    }

    @Test
    public void testCalculerHeuresTravailleesRakoto() {
        Map<String, Object> resultatsRakoto = calculSalaire.calculerSalaire(rakoto, calendrierJuin, heuresTravailleesRakoto);
        assertEquals(176, resultatsRakoto.get("heuresNormales"));
    }

    @Test
    public void testCalculerHeuresTravailleesRabe() {
        Map<String, Object> resultatsRabe = calculSalaire.calculerSalaire(rabe, calendrierJuin, heuresTravailleesRabe);
        assertEquals(176, resultatsRabe.get("heuresNormales"));
    }

    @Test
    public void testCalculerSalaireBrutRakoto() {
        Map<String, Object> resultatsRakoto = calculSalaire.calculerSalaire(rakoto, calendrierJuin, heuresTravailleesRakoto);

        double salaireBrutRakoto = (double) resultatsRakoto.get("salaireBrut");
        double expectedSalaireBrutRakoto = 110000.0 / 12;
        assertEquals(expectedSalaireBrutRakoto, salaireBrutRakoto, 0.01);
    }

    @Test
    public void testCalculerSalaireNetRakoto() {
        Map<String, Object> resultatsRakoto = calculSalaire.calculerSalaire(rakoto, calendrierJuin, heuresTravailleesRakoto);

        double salaireNetRakoto = (double) resultatsRakoto.get("salaireNet");
        double expectedSalaireNetRakoto = (110000.0 / 12) * 0.8;
        assertEquals(expectedSalaireNetRakoto, salaireNetRakoto, 0.01);
    }

    @Test
    public void testCalculerSalaireBrutRabe() {
        Map<String, Object> resultatsRabe = calculSalaire.calculerSalaire(rabe, calendrierJuin, heuresTravailleesRabe);

        double salaireBrutRabe = (double) resultatsRabe.get("salaireBrut");
        double expectedSalaireBrutRabe = 110000.0 / 12;
        assertEquals(expectedSalaireBrutRabe, salaireBrutRabe, 0.01);
    }

    @Test
    public void testCalculerSalaireNetRabe() {
        Map<String, Object> resultatsRabe = calculSalaire.calculerSalaire(rabe, calendrierJuin, heuresTravailleesRabe);

        double salaireNetRabe = (double) resultatsRabe.get("salaireNet");
        double expectedSalaireNetRabe = (110000.0 / 12) * 0.8;
        assertEquals(expectedSalaireNetRabe, salaireNetRabe, 0.01);
    }
}
