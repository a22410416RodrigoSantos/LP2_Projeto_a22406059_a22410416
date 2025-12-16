package pt.ulusofona.lp2.greatprogrammingjourney;

public class ferIDE extends Ferramenta {

    public ferIDE() {
        super(14, "IDE", "ide.png");
    }

    @Override
    public boolean podeNeutralizar(Abismo abismo) {
        int id = abismo.getId();
        return id == 0 || id == 4; // neutraliza Erro de Sintaxe e Crash
    }
}