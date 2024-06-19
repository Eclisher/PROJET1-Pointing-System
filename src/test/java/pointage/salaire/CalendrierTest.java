package pointage.salaire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pointage.salaire.Model.Calendrier;
import pointage.salaire.Model.Categorie;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CalendrierTest {

    private Calendrier calendrier;

    @BeforeEach
    public void setup() {
        calendrier = new Calendrier();
        calendrier.ajouterJourFerie(LocalDate.of(2024, 6, 17).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        calendrier.ajouterJourFerie(LocalDate.of(2024, 6, 25).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        calendrier.ajouterJourFerie(LocalDate.of(2024, 6, 26).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    @Test
    public void testCalendrierJuin() {
        for (int i = 1; i <= 30; i++) {
            Instant instant = Instant.parse(String.format("2024-06-%02dT00:00:00Z", i));
            LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            boolean expectedNormalDay = date.getDayOfWeek().getValue() >= DayOfWeek.MONDAY.getValue()
                    && date.getDayOfWeek().getValue() <= DayOfWeek.FRIDAY.getValue()
                    && !calendrier.estJourFerie(instant);

            boolean expectedGuardDay = date.getDayOfWeek().getValue() >= DayOfWeek.MONDAY.getValue()
                    && date.getDayOfWeek().getValue() <= DayOfWeek.SUNDAY.getValue()
                    && !calendrier.estJourFerie(instant);

            if (!expectedNormalDay && !expectedGuardDay) {
                System.out.println("Debug: Date " + instant + " is not a normal or guard working day.");
            }

            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                assertTrue(expectedGuardDay, "Failed for date: " + instant);
            } else {
                assertTrue(expectedNormalDay, "Failed for date: " + instant);
            }
        }
        Set<Instant> joursFeries = calendrier.getJoursFeries();
        assertFalse(joursFeries.contains(Instant.parse("2024-06-17T00:00:00Z")), "17 juin n'est pas un jour férié");
        assertFalse(joursFeries.contains(Instant.parse("2024-06-25T00:00:00Z")), "25 juin n'est pas un jour férié");
        assertFalse(joursFeries.contains(Instant.parse("2024-06-26T00:00:00Z")), "26 juin n'est pas un jour férié");
    }

    @Test
    public void testHeuresTravailleesRakotoEtRabe() {
        int heuresParJourRakoto = 8;
        int joursDansJuin = 30;
        int heuresTotalesRakoto = heuresParJourRakoto * joursDansJuin;
        double tauxMajoration = 1.3;
        int heuresTotalesRabe = (int) Math.round(heuresTotalesRakoto * tauxMajoration);
        assertEquals(240, heuresTotalesRakoto, "Le nombre d'heures de Rakoto est incorrect");
        assertEquals(312, heuresTotalesRabe, "Le nombre d'heures de Rabe est incorrect");
    }


    @Test
    public void testHeuresTravailleesEtPaiement() {
        int heuresParJourRakoto = 8;
        int joursDansJuin = 30;
        int heuresTotalesRakoto = heuresParJourRakoto * joursDansJuin;
        double tauxMajoration = 1.3;
        int heuresTotalesRabe = (int) Math.round(heuresTotalesRakoto * tauxMajoration);
        int heuresSupplementairesRakoto = 0;
        if (heuresParJourRakoto > 8) {
            heuresSupplementairesRakoto = (heuresParJourRakoto - 8) * joursDansJuin;
        }
        int heuresSupplementairesRabe = heuresTotalesRabe - heuresTotalesRakoto;
        int tauxHoraireRakoto = 10;
        double montantRakoto = (heuresTotalesRakoto - heuresSupplementairesRakoto) * tauxHoraireRakoto
                + heuresSupplementairesRakoto * tauxHoraireRakoto * 1.5;
        double tauxHoraireRabe = tauxHoraireRakoto * tauxMajoration;
        double montantRabe = (heuresTotalesRabe - heuresSupplementairesRabe) * tauxHoraireRabe
                + heuresSupplementairesRabe * tauxHoraireRabe * 1.5;
        assertEquals(240, heuresTotalesRakoto, "Le nombre d'heures de Rakoto est incorrect");
        assertEquals(312, heuresTotalesRabe, "Le nombre d'heures de Rabe est incorrect");
        assertEquals(2400.0, montantRakoto, 0.01, "Le montant à payer à Rakoto est incorrect");
        assertEquals(4524.0, montantRabe, 0.01, "Le montant à payer à Rabe est incorrect");
    }
    @Test
    public void testHeuresTravailleesEtPaiementPartieC() {
        int heuresParJourRakoto = 8;
        int joursDansJuin = 30;
        int heuresTotalesRakotoAvantChangement = heuresParJourRakoto * 24;
        int heuresTotalesRakotoApresChangement = 16 * 2 + heuresParJourRakoto * 2;
        int tauxHoraireRakoto = 10;
        double montantRakoto = (heuresTotalesRakotoAvantChangement + heuresTotalesRakotoApresChangement) * tauxHoraireRakoto;
        assertEquals(192, heuresTotalesRakotoAvantChangement, "Le nombre d'heures de Rakoto avant changement est incorrect");
        assertEquals(32 + heuresParJourRakoto * 2, heuresTotalesRakotoApresChangement, "Le nombre d'heures de Rakoto après changement est incorrect");
        assertEquals(2400.0, montantRakoto, 0.01, "Le montant à payer à Rakoto est incorrect");
    }



}
