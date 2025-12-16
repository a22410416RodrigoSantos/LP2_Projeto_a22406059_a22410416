package pt.ulusofona.lp2.greatprogrammingjourney;

public class ferAjudaDoProfessor extends Ferramenta {

    public ferAjudaDoProfessor() {
        super(15, "Ajuda do Professor", "professor_help.png");
    }

    @Override
    public boolean podeNeutralizar(Abismo abismo) {
        int id = abismo.getId();
        return id == 7 || id == 8 || id == 9; // neutraliza BSOD, Ciclo Infinito e Segmentation Fault
    }
}