package pointage.salaire.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import pointage.salaire.Model.Employe;
import pointage.salaire.Model.Calendrier;
public class CalculSalaire {

    private static final double HS30_RATE = 1.3;
    private static final double HS50_RATE = 1.5;

    public Map<String, Object> calculerSalaire(Employe employe, Calendrier calendrier, Map<Instant, Integer> heuresTravaillees) {
        Map<String, Object> result = new HashMap<>();

        int heuresNormales = calculerHeuresNormales(employe, calendrier, heuresTravaillees);
        int heuresSupplementaires = calculerHeuresSupplementaires(employe, heuresNormales);
        int heuresMajorees = calculerHeuresMajorees(employe, calendrier, heuresTravaillees);

        double salaireBrut = calculerSalaireBrut(employe, heuresNormales, heuresSupplementaires, heuresMajorees);
        double salaireNet = calculerSalaireNet(salaireBrut);

        result.put("heuresNormales", heuresNormales);
        result.put("heuresSupplementaires", heuresSupplementaires);
        result.put("heuresMajorees", heuresMajorees);
        result.put("salaireBrut", salaireBrut);
        result.put("salaireNet", salaireNet);

        return result;
    }

    private int calculerHeuresNormales(Employe employe, Calendrier calendrier, Map<Instant, Integer> heuresTravaillees) {
        int heuresNormales = 0;
        int joursOuvres = 0;

        for (Map.Entry<Instant, Integer> entry : heuresTravaillees.entrySet()) {
            Instant date = entry.getKey();
            int heures = entry.getValue();
            if (calendrier.estJourDeTravail(date, employe.getCategorie())) {
                heuresNormales += Math.min(heures, employe.getCategorie().getHeuresNormalesParSemaine() / 5); // 5 jours ouvrés
                joursOuvres++;
            }
        }

        return heuresNormales;
    }

    private int calculerHeuresSupplementaires(Employe employe, int heuresNormales) {
        int heuresSupplementaires = 0;
        int heuresNormalesParSemaine = employe.getCategorie().getHeuresNormalesParSemaine();
        if (heuresNormales > heuresNormalesParSemaine) {
            heuresSupplementaires = Math.min(heuresNormales - heuresNormalesParSemaine, 20);
        }
        return heuresSupplementaires;
    }

    private int calculerHeuresMajorees(Employe employe, Calendrier calendrier, Map<Instant, Integer> heuresTravaillees) {
        int heuresMajorees = 0;

        for (Map.Entry<Instant, Integer> entry : heuresTravaillees.entrySet()) {
            Instant date = entry.getKey();
            int heures = entry.getValue();
            if (calendrier.estJourFerie(date)) {
                heuresMajorees += heures;
            } else {
                ZonedDateTime zonedDateTime = date.atZone(ZoneId.systemDefault());
                if (zonedDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    heuresMajorees += heures;
                }
            }
        }

        return heuresMajorees;
    }

    private double calculerSalaireBrut(Employe employe, int heuresNormales, int heuresSupplementaires, int heuresMajorees) {
        double tauxHoraire = employe.getCategorie().getSalaireParSemaine() / employe.getCategorie().getHeuresNormalesParSemaine();
        double salaireBrut = heuresNormales * tauxHoraire;

        if (heuresSupplementaires > 0) {
            int heuresHS30 = Math.min(heuresSupplementaires, 8);
            int heuresHS50 = Math.max(0, heuresSupplementaires - 8);
            salaireBrut += heuresHS30 * tauxHoraire * HS30_RATE + heuresHS50 * tauxHoraire * HS50_RATE;
        }

        if (heuresMajorees > 0) {
            salaireBrut += heuresMajorees * tauxHoraire * HS50_RATE;
        }

        return salaireBrut;
    }

    private double calculerSalaireNet(double salaireBrut) {
        return salaireBrut * 0.8;
    }
}
