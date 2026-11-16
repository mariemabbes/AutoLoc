package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Service
public class VehiculeServiceImpl extends GenericServiceImpl<Vehicule, Long> implements IVehiculeService {

    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    public VehiculeServiceImpl(IVehiculeRepository repository,
                               IAgenceRepository agenceRepository,
                               IEquipementRepository equipementRepository) {
        super(repository);
        this.agenceRepository = agenceRepository;
        this.equipementRepository = equipementRepository;
    }

    @Override
    protected String getEntityName() {
        return "Vehicule";
    }

    @Override
    protected Long getId(Vehicule vehicule) {
        return vehicule.getIdVehicule();
    }

    @Override
    protected void setFields(Vehicule source, Vehicule target) {
        target.setImmatriculation(source.getImmatriculation());
        target.setMarque(source.getMarque());
        target.setModele(source.getModele());
        target.setCategorie(source.getCategorie());
        target.setTarifJournalier(source.getTarifJournalier());
        target.setStatut(source.getStatut());
    }

    @Override
    public boolean check(Vehicule vehicule, boolean isNew) {
        return Utils.isNotEmpty(vehicule.getImmatriculation())
                && Utils.isNotEmpty(vehicule.getMarque())
                && Utils.isNotEmpty(vehicule.getModele())
                && vehicule.getCategorie() != null
                && vehicule.getStatut() != null
                && vehicule.getTarifJournalier() != null;
    }

    @Override
    public double calculTarif(Vehicule vehicule, int nbJours, VehiculeOffre offre) {
        double tarifJournalier = vehicule.getTarifJournalier().doubleValue();

        if (offre == VehiculeOffre.WEEKEND) {
            tarifJournalier *= 0.8;
        }
        if (offre == VehiculeOffre.LONGUE_DUREE) {
            tarifJournalier *= 0.75;
        }

        double total = tarifJournalier * nbJours;

        if (offre == VehiculeOffre.LONGUE_DUREE && total > 2000) {
            total *= 0.95;
        }
        return total;
    }

    @Override
    public Vehicule desaffecte(long id) {
        Vehicule vehicule = findById(id);
        vehicule.setAgence(null);
        return vehicule;
    }

    @Override
    public Vehicule affecte(long id, long idAgence) {
        Vehicule vehicule = findById(id);
        vehicule.setAgence(null);
        return vehicule;
    }



    @Transactional
    @Override
    public Vehicule ajouteEquipement(Long id, Long idEquipement) {
        Vehicule vehicule = findById(id);
        var equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement.toString()));
        vehicule.getEquipements().add(equipement);
        return vehicule;
    }
}