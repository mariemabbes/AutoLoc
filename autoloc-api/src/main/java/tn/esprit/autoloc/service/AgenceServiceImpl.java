package tn.esprit.autoloc.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long>
        implements IAgenceService, IAgenceSimulationService {

    private final IAgenceRepository agenceRepository;
    private final IEmployeService employeService;

    public AgenceServiceImpl(IAgenceRepository repository, IEmployeService employeService) {
        super(repository);
        this.agenceRepository = repository;
        this.employeService = employeService;
    }

    @Override
    protected String getEntityName() {
        return "Agence";
    }

    @Override
    protected Long getId(Agence agence) {
        return agence.getIdAgence();
    }

    @Override
    protected void setFields(Agence source, Agence target) {
        target.setNom(source.getNom());
        target.setAdresse(source.getAdresse());
        target.setTelephone(source.getTelephone());
        target.setVille(source.getVille());
    }

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom())
                && Utils.isNotEmpty(agence.getTelephone())
                && Utils.isNotEmpty(agence.getVille());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasVehicules(Long id) {
        return !findById(id).getVehicules().isEmpty();
    }

    @Override
    public String simuler(Agence agence, int nbJours, VehiculeOffre offre) {
        return "";
    }

    // Q12

    @Override
    @Transactional
    public Agence updateAvecEmployes(Long id, Agence agence) {
        // 1. mettre à jour d'abord l'agence
        Agence existante = update(id, agence);

        // 2. insérer les employés via le service des employés
        List<Employe> nouveaux = new ArrayList<>(agence.getEmployes());
        for (Employe e : nouveaux) {
            existante.addEmploye(e);       // symétrisation (Q10)
            employeService.create(e);
        }

        // 3. mettre à jour l'effectif avec la taille de la liste
        existante.setEffectif(nouveaux.size());
        return agenceRepository.save(existante);
    }
}