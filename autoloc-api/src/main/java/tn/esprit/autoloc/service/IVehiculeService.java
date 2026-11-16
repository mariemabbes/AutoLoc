package tn.esprit.autoloc.service;

import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Vehicule;

public interface IVehiculeService extends IGenericService<Vehicule, Long> {
    double calculTarif (Vehicule vehicule, int nbJours , VehiculeOffre offre );

    Vehicule desaffecte(long id);
    Vehicule affecte(long id, long idAgence );


    @Transactional
    Vehicule ajouteEquipement(Long id, Long idEquipement);
}