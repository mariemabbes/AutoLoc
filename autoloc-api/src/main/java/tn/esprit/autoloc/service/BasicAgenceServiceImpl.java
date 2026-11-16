package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service

@RequiredArgsConstructor
public class BasicAgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) &&
                Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public Agence create(Agence agence) {
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException("identifiant non null !!!");
        }
        if (!check(agence, true)) {
            throw new IllegalArgumentException("certains champs sont invalides !");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id.toString()));
    }

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();   // corrigé : retournait List.of()
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existingAgence = findById(id);
        if (!check(agence, false)) {
            throw new IllegalArgumentException("certains champs sont invalides !");
        }
        existingAgence.setNom(agence.getNom());
        existingAgence.setVille(agence.getVille());
        existingAgence.setAdresse(agence.getAdresse());
        existingAgence.setTelephone(agence.getTelephone());
        return agenceRepository.save(existingAgence);
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id.toString());
        }
        agenceRepository.deleteById(id);
    }

    @Override
    public boolean hasVehicules(Long id) {
        return false; // à implémenter (ex: via un repository de véhicules)
    }

    // ===== MÉTHODE MANQUANTE (cause de l'erreur) =====
    @Override
    public String simuler(Agence agence, int nbJours, VehiculeOffre offre) {
        // TODO: logique de simulation
        return "";
    }

    @Override
    public Agence updateAvecEmployes(Long id, Agence agence) {
        return null;
    }
}
