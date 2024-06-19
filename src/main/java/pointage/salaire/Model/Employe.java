package pointage.salaire.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class Employe {
    private  String nom;
    private  String  prenom;
    private  String matricule;
    private LocalDate dateNaissance;
    private Instant dateEmbauche;
    private Instant dateFinContrat;
    private  double salaireBrut;
    private  Categorie categorie;
    public double getSalaireNet() {
        return salaireBrut * 0.8;
    }
}
