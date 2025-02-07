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

    public Noeud(String nom, Noeud filsGauche) {
        this(nom, filsGauche, null);
    }

    public Noeud(String nom) {
        this(nom, null, null);
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

    public void afficher() {
        this.affichageStructure("", true);
    }

    public void affichageStructure(String prefix, boolean isLeft) {
        System.out.println(prefix + (isLeft ? "|-- " : "|-- ") + nom);

        if (filsGauche != null) {
            filsGauche.affichageStructure(prefix + (isLeft ? "|   " : "    "), true);
        }
        if (filsDroit != null) {
            filsDroit.affichageStructure(prefix + (isLeft ? "|   " : "    "), false);
        }
    }

    public String toCodeAssembleur() {
        // Implémentation de la conversion en code assembleur
        return "";
    }

    @Override
    public String toString() {
        return nom;
    }
}
