package pt.ulusofona.lp2.greatprogrammingjourney;

public class ferTratamentoExcepcoes extends Ferramenta {

    public ferTratamentoExcepcoes() {
        super(13, "Tratamento de Excepções", "exception_handling.png");
    }

    @Override
    public boolean podeNeutralizar(Abismo abismo) {
        int id = abismo.getId();
        return id == 2 || id == 3; // neutraliza Exception e FileNotFoundException
    }
}