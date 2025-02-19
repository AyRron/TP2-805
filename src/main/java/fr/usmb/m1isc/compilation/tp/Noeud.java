package fr.usmb.m1isc.compilation.tp;

import java.util.ArrayList;

public class Noeud {
    private String nom;
    private Noeud filsGauche;
    private Noeud filsDroit;
    private String type;
    private StringBuilder code = new StringBuilder();
    Counter counter = new Counter();

    public Noeud(String nom, String type, Noeud filsGauche, Noeud filsDroit) {
        this.type = type;
        this.nom = nom;
        this.filsGauche = filsGauche;
        this.filsDroit = filsDroit;
    }

    public Noeud(String nom, String type) {
        this(nom, type, null, null);
    }

    public Noeud(String nom, String type, Noeud filsGauche) {        
        this(nom, type, filsGauche, null);
    }

    public String getNom() {
        return nom;
    }

    public String getType() {
        return type;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setFilsGauche(Noeud filsGauche) {
        this.filsGauche = filsGauche;
    }

    public void setFilsDroit(Noeud filsDroit) {
        this.filsDroit = filsDroit;
    }

    public void afficherArbre(String prefix) {
        System.out.println(prefix + nom);
        if (filsGauche != null) {
            filsGauche.afficherArbre(prefix + "├── ");
        }
        if (filsDroit != null) {
            filsDroit.afficherArbre(prefix + "└── ");
        }
    }

    public void afficherArbre() {
        afficherArbre("");
    }



    public void afficherType(){
        afficherType("");
    }

    public void afficherType(String prefix){
        System.out.println(prefix + type);
        if (filsGauche != null) {
            filsGauche.afficherType(prefix + "├── ");
        }
        if (filsDroit != null) {
            filsDroit.afficherType(prefix + "└── ");
        }
    }

    private Noeud append(String s) {
        code.append(s);
        return this;
    }

    private Noeud append(StringBuilder sb) {
        code.append(sb);
        return this;
    }

    private Noeud setCounter(Counter counter) {
        this.counter = counter;
        return this;
    }

    public void generateDataDeclaration(ArrayList<String> dataList) {
        if (filsGauche != null) {
            filsGauche.generateDataDeclaration(dataList);
        }
        if (filsDroit != null) {
            filsDroit.generateDataDeclaration(dataList);
        }
        if (!dataList.contains(nom)) {
            dataList.add(nom);
        }
    }

    public StringBuilder generateAssemblyCode() {
        counter.increment();

        if (nom.equals(";")) {
            return filsGauche.generateAssemblyCode().append(filsDroit.generateAssemblyCode());
        }

        if (nom.matches("\\d+") || nom.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            append("\tmov eax, ").append(nom).append("\n");
        } else if (nom.equals("+") || nom.equals("-") || nom.equals("*") || nom.equals("/")) {
            append(filsGauche.generateAssemblyCode());
            append("\tpush eax\n");
            append(filsDroit.generateAssemblyCode());
            append("\tpop ebx\n");
            switch (nom) {
                case "+":
                    append("\tadd eax, ebx\n");
                    break;
                case "-":
                    append("\tsub ebx, eax\n");
                    append("\tmov eax, ebx\n");
                    break;
                case "*":
                    append("\tmul eax, ebx\n");
                    break;
                case "/":
                    append("\tdiv ebx, eax\n");
                    append("\tmov eax, ebx\n");
                    break;
                default:
                    break;
            }
        } else if (nom.equals("let")) {
            append(filsDroit.generateAssemblyCode());
            append("\tmov ");
            append(filsGauche.getNom());
            append(", eax\n");
        } else if (nom.equals("input")) {
            append("\tin eax\n");
        } else if (nom.equals("output")) {
            append("\tmov eax, \n");
            append(filsGauche.getNom());
            append("\n");
            append("\tout eax\n");
        } else if (nom.equals("if")) {
            append(filsGauche.generateAssemblyCode());
            append("\tjz faux_if_" + counter + "\n");
            append(filsDroit.filsGauche.generateAssemblyCode());
            append("\tjmp sortie_if_" + counter + "\n");
            append("faux_if_" + counter + ":\n");
            append(filsDroit.filsDroit.generateAssemblyCode());
            append("sortie_if_" + counter + ":\n");
            return code;
        } else if (nom.equals("while")) {
            append("debut_while_" + counter + ":\n");
            append(filsGauche.generateAssemblyCode());
            append("\tjz sortie_while_" + counter + "\n");
            append(filsDroit.generateAssemblyCode());
            append("\tjmp debut_while_" + counter + "\n");
            append("sortie_while_" + counter + ":\n");
            return code;
        } else if (nom.equals("&&")) {
            append(filsGauche.generateAssemblyCode());
            append("\tjz faux_and_" + counter + "\n");
            append(filsDroit.generateAssemblyCode());
            append("faux_and_" + counter + " :\n");
            return code;
        } else if (nom.equals("||")) {
            append(filsGauche.generateAssemblyCode());
            append("\tjnz vrai_or_" + counter + "\n");
            append(filsDroit.generateAssemblyCode());
            append("\tjnz vrai_or_" + counter + "\n");
            append("vrai_or_" + counter + " :\n");
            return code;
        } else if (nom.equals("!")) {
            append(filsGauche.generateAssemblyCode());
            append("\tjnz faux_not_" + counter + "\n");
            append("\tmov eax, 1\n");
            append("\tjmp sortie_not_" + counter + "\n");
            append("faux_not_" + counter + " :\n");
            append("\tmov eax, 0\n");
            append("sortie_not_" + counter + " :\n");
            return code;
        } else if (nom.equals("%")) {
            append(filsDroit.generateAssemblyCode());
            append("\tpush eax\n");
            append(filsGauche.generateAssemblyCode());
            append("\tpop ebx\n");
            append("\tmov ecx, eax\n");
            append("\tdiv ecx, ebx\n");
            append("\tmul ecx, ebx\n");
            append("\tsub eax, ecx\n");
            return code;
        } else if (nom.equals("==")) {
            append(filsGauche.generateAssemblyCode());
            append("\tpush eax\n");
            append(filsDroit.generateAssemblyCode());
            append("\tpop ebx\n");
            append("\tsub eax, ebx\n");
            append("\tjnz faux_egal_" + counter + "\n");
            append("\tmov eax, 1\n");
            append("\tjmp sortie_egal_" + counter + "\n");
            append("faux_egal_" + counter + " :\n");
            append("\tmov eax, 0\n");
            append("sortie_egal_" + counter + " :\n");
            return code;
        } else if (nom.equals(">")) {
            append(filsGauche.generateAssemblyCode());
            append("\tpush eax\n");
            append(filsDroit.generateAssemblyCode());
            append("\tpop ebx\n");
            append("\tsub eax, ebx\n");
            append("\tjge faux_gt_" + counter + "\n");
            append("\tmov eax, 1\n");
            append("\tjmp sortie_gt_" + counter + "\n");
            append("faux_gt_" + counter + " :\n");
            append("\tmov eax, 0\n");
            append("sortie_gt_" + counter + " :\n");
            return code;
        } else if (nom.equals(">=")) {
            append(filsGauche.generateAssemblyCode());
            append("\tpush eax\n");
            append(filsDroit.generateAssemblyCode());
            append("\tpop ebx\n");
            append("\tsub eax, ebx\n");
            append("\tjg faux_gte_" + counter + "\n");
            append("\tmov eax, 1\n");
            append("\tjmp sortie_gte_" + counter + "\n");
            append("faux_gte_" + counter + " :\n");
            append("\tmov eax, 0\n");
            append("sortie_gte_" + counter + " :\n");
            return code;
        } else if (nom.equals("<")) {
            append(filsGauche.generateAssemblyCode());
            append("\tpush eax\n");
            append(filsDroit.generateAssemblyCode());
            append("\tpop ebx\n");
            append("\tsub eax, ebx\n");
            append("\tjle faux_lt_" + counter + "\n");
            append("\tmov eax, 1\n");
            append("\tjmp sortie_lt_" + counter + "\n");
            append("faux_lt_" + counter + " :\n");
            append("\tmov eax, 0\n");
            append("sortie_lt_" + counter + " :\n");
            return code;
        } else if (nom.equals("<=")) {
            append(filsGauche.generateAssemblyCode());
            append("\tpush eax\n");
            append(filsDroit.generateAssemblyCode());
            append("\tpop ebx\n");
            append("\tsub eax, ebx\n");
            append("\tjl faux_lte_" + counter + "\n");
            append("\tmov eax, 1\n");
            append("\tjmp sortie_lte_" + counter + "\n");
            append("faux_lte_" + counter + " :\n");
            append("\tmov eax, 0\n");
            append("sortie_lte_" + counter + " :\n");
            return code;
        }
        return code;
    }

    public String compile() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("DATA SEGMENT\n");
        ArrayList<String> dataList = new ArrayList<>();
        generateDataDeclaration(dataList);
        for (String data : dataList) {
            resultat.append("\t").append(data).append(" DD\n");
        }
        resultat.append("DATA ENDS\n");
        resultat.append("CODE SEGMENT\n");
        resultat.append(generateAssemblyCode());
        resultat.append("CODE ENDS\n");
        return resultat.toString();
    }

    @Override
    public String toString() {
        return nom;
    }
}