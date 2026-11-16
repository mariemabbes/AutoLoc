package tn.esprit.autoloc.io;

public interface IRapport {
    void ecrireEntete(String entete);
    void ecrireTotal(double total);
    String getResultat();
}