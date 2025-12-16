package pt.ulusofona.lp2.greatprogrammingjourney;

public class ferTestesUnitarios extends Ferramenta {

    public ferTestesUnitarios() {
        super(12, "Testes Unitários", "unit_tests.png");
    }

    @Override
    public boolean podeNeutralizar(Abismo abismo) {
        return abismo.getId() == 1; // neutraliza Erro de Lógica
    }
}