package fr.usmb.m1isc.compilation.tp;

public class Noeud {
    private String nom;
    private Noeud filsGauche;
    private Noeud filsDroit;

    public Noeud(String nom, Noeud filsGauche, Noeud filsDroit) {
        this.nom = nom;
        this.filsGauche = filsGauche;
        this.filsDroit = filsDroit;
    }

    public Noeud(String nom) {
        this(nom, null, null);
    }

    public Noeud(String nom, Noeud filsGauche) {        
        this(nom, filsGauche, null);
    }

    public String getNom() {
        return nom;
    }

    public Noeud getFilsGauche() {
        return filsGauche;
    }

    public Noeud getFilsDroit() {
        return filsDroit;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setFilsGauche(Noeud filsGauche) {
        this.filsGauche = filsGauche;
    }

    public void setFilsDroit(Noeud filsDroit) {
        this.filsDroit = filsDroit;
    }

    public String toString() {
        return nom;
    }
}
