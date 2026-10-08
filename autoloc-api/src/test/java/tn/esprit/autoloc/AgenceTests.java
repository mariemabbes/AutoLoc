package tn.esprit.autoloc;

import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest

public class AgenceTests {
    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;
    @Autowired
    private IAgenceRepository fullAgenceRepository;


    private void addAgence(CrudRepository<Agence,Long> crudRepository) {
        Agence agence = new Agence();
        agence.setNom("Agence ariana");
        agence.setAdresse("1 Rue Hedi");
        agence.setTelephone("71585874");
        agence.setVille("Tunis");
        int ms=(int)System.currentTimeMillis();

        Vehicule v1 = new Vehicule();
        v1.setImmatriculation("785414TU96-"+ms);
        v1.setMarque("Isuzu");
        v1.setModele("DMax");
        v1.setCategorie(CategorieVehicule.SUV);
        v1.setStatut(StatutVehicule.MAINTENANCE);
        v1.setTarifJournalier(new BigDecimal("100"));
        v1.setAgence(agence);

        Vehicule v2 = new Vehicule();
        v2.setImmatriculation("785414TU95-"+ms);
        v2.setMarque("Toyota");
        v2.setModele("Yaris");
        v2.setCategorie(CategorieVehicule.UTILITAIRE);
        v2.setStatut(StatutVehicule.DISPONIBLE);
        v2.setTarifJournalier(new BigDecimal("80"));
        v2.setAgence(agence);

        Set<Vehicule> vehicules = new HashSet<>();
        vehicules.add(v1);
        vehicules.add(v2);
        agence.setVehicules(vehicules);

        crudRepository.save(agence);


    }

    @Test
    public void basicAddAgence() {
        addAgence(basicAgenceRepository);
    }

    @Test
    public void fullAddAgence() {
        addAgence(fullAgenceRepository);
    }



    private void loadAgence(CrudRepository<Agence, Long> repository, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("Depot utilise : ").append(type).append("\n");

        for (Agence a : repository.findAll()) {
            sb.append(a.getIdAgence()).append(" | ").append(a.getNom()).append("\n");
            sb.append("Vehicules Count : ").append(a.getVehicules().size()).append("\n");
            for (Vehicule v : a.getVehicules()) {
                sb.append("=== ").append(v.getIdVehicule()).append("|")
                        .append(v.getImmatriculation()).append("\n");
            }
        }
        Assert.fail(sb.toString());
    }

    @Test

    public void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "basic");
    }

    @Test

    public void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "full");
    }
    @Test
    public void loadSortedAgences() {

        List<Agence> agences = fullAgenceRepository.findAll(Sort.by(Sort.Direction.DESC, "idAgence"));
        StringBuilder sb = new StringBuilder();
        sb.append("Agences triées par id décroissant\n");
        for (Agence a : agences) {
            sb.append(a.getIdAgence()).append(" | ")
                    .append(a.getNom()).append(" | ")
                    .append(a.getVille()).append(" | ")
                    .append(a.getAdresse()).append(" | ")
                    .append(a.getTelephone()).append("\n");
        }

        Assert.fail(sb.toString());
    }

    @Test
    public void loadPagedAgences() {
        StringBuilder sb = new StringBuilder();
        Sort sort = Sort.by(Sort.Direction.DESC, "idAgence");

        Page<Agence> premierePage = fullAgenceRepository.findAll(PageRequest.of(0, 2, sort));
        int totalPages = premierePage.getTotalPages();
        sb.append("Total pages : ").append(totalPages).append("\n");

        for (int i = 0; i < totalPages; i++) {
            Page<Agence> page = fullAgenceRepository.findAll(PageRequest.of(i, 2, sort));
            sb.append("--- Page en cours : ").append(page.getNumber()).append(" ---\n");
            for (Agence a : page.getContent()) {
                sb.append(a.getIdAgence()).append(" | ").append(a.getNom()).append("\n");
            }
        }

        Assert.fail(sb.toString());
    }



}
interface  AgenceRepositoryMock  extends CrudRepository<Agence,Long> {

}