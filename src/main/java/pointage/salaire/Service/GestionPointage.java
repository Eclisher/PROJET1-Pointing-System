package pointage.salaire.Service;

import pointage.salaire.Model.Employe;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class GestionPointage {
    private Map<Employe, Map<Instant, Integer>> pointages;

    public GestionPointage(){
        pointages = new HashMap<>();
    }
    public void ajouterPointage(Employe employe, Instant date, int heures) {
        pointages.computeIfAbsent(employe, k -> new HashMap<>()).put(date, heures);
    }

    public int obtenirPointage(Employe employe, Instant date) {
        return pointages.getOrDefault(employe, new HashMap<>()).getOrDefault(date, 0);
    }

    public Map<Instant, Integer> obtenirPointagesPourEmploye(Employe employe) {
        return pointages.getOrDefault(employe, new HashMap<>());
    }
}
