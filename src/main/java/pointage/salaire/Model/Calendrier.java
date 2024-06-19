package pointage.salaire.Model;

import java.time.Instant;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class Calendrier {
    private Set<Instant> joursFeries;

    public Calendrier() {
        joursFeries = new HashSet<>();
    }

    public void ajouterJourFerie(Instant instant) {
        joursFeries.add(instant);
    }

    public boolean estJourFerie(Instant instant) {
        return joursFeries.contains(instant);
    }

    public boolean estJourDeTravail(Instant instant, Categorie categorie) {
        if (estJourFerie(instant)) {
            return false;
        }

        DayOfWeek dayOfWeek = instant.atZone(ZoneId.systemDefault()).getDayOfWeek();
        if (categorie.equals(Categorie.GARDIEN)) {
            return true; // Les gardiens travaillent tous les jours
        }

        // Employés normaux : du lundi au vendredi
        return !dayOfWeek.equals(DayOfWeek.SATURDAY) && !dayOfWeek.equals(DayOfWeek.SUNDAY);
    }

    public Set<Instant> getJoursFeries() {
        return joursFeries;
    }
}
