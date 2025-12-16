package pt.ulusofona.lp2.greatprogrammingjourney;

public class FerHeranca extends Ferramenta {

    public FerHeranca() {
        super(10, "Herança", "inheritance.png");
    }

    @Override
    public boolean podeNeutralizar(Abismo abismo) {
        return abismo.getId() == 5; // neutraliza Código Duplicado
    }
}