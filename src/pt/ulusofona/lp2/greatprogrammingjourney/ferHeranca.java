package pt.ulusofona.lp2.greatprogrammingjourney;

public class ferHeranca extends Ferramenta {

    public ferHeranca() {
        super(10, "Herança", "inheritance.png");
    }

    @Override
    public boolean podeNeutralizar(Abismo abismo) {
        return abismo.getId() == 5; // neutraliza Código Duplicado
    }
}