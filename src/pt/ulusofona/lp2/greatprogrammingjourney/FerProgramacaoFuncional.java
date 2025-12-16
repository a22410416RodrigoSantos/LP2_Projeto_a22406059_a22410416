package pt.ulusofona.lp2.greatprogrammingjourney;

public class FerProgramacaoFuncional extends Ferramenta {

    public FerProgramacaoFuncional() {
        super(11, "Programação Funcional", "functional.png");
    }

    @Override
    public boolean podeNeutralizar(Abismo abismo) {
        return abismo.getId() == 6; // neutraliza Efeitos Secundários
    }
}