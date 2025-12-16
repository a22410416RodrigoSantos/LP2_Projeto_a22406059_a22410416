package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbiErroLogica extends Abismo {

    public AbiErroLogica() {
        super(1, "Erro de Lógica", "logic_error.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        int nrSpaces = gameManager.getLastNrSpaces();
        int recuo = nrSpaces / 2;
        int currentPos = programmer.getPosition();
        int newPos = Math.max(1, currentPos - recuo);
        programmer.setPosition(newPos);
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 12; // Testes Unitários
    }
}