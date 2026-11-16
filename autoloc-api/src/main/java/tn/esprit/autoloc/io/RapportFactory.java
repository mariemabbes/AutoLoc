package tn.esprit.autoloc.io;

import org.springframework.stereotype.Component;

@Component
public class RapportFactory {

    public IRapport creer(boolean console) {
        IRapport rapport = new RapportTexte();
        if (console) {
            rapport = new RapportConsoleDecorator(rapport);
        }
        return rapport;
    }
}