package tn.esprit.autoloc.io;

public class RapportConsoleDecorator implements IRapport {

    private final IRapport rapport;

    public RapportConsoleDecorator(IRapport rapport) {
        this.rapport = rapport;
    }

    @Override
    public void ecrireEntete(String entete) {
        System.out.println(entete);   // l'en-tête va sur la console
    }

    @Override
    public void ecrireTotal(double total) {
        rapport.ecrireTotal(total);   // le total est délégué
    }

    @Override
    public String getResultat() {
        return rapport.getResultat(); // retourne uniquement le total
    }
}