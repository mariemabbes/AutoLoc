package tn.esprit.autoloc.io;

public class RapportTexte implements IRapport {

    private final StringBuilder contenu = new StringBuilder();

    @Override
    public void ecrireEntete(String entete) {
        contenu.append(entete).append("\n");
    }

    @Override
    public void ecrireTotal(double total) {
        contenu.append("total : ").append(total);
    }

    @Override
    public String getResultat() {
        return contenu.toString();
    }
}