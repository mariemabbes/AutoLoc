package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    private String nom;

    private String ville;

    private String adresse;

    private String telephone;

    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties("agence")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Vehicule> vehicules;

    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("agence")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Employe> employes = new ArrayList<>();

    // champ entier mappé
    private Integer effectif = 0;

    // symétrisation de la relation agence/employé
    public void addEmploye(Employe employe) {
        this.employes.add(employe);
        employe.setAgence(this);
    }

    public void removeEmploye(Employe employe) {
        this.employes.remove(employe);
        employe.setAgence(null);
    }
}