package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiException extends Abismo {

    public abiException() {
        super(2, "Exception", "exception.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        int currentPos = programmer.getPosition();
        int newPos = Math.max(1, currentPos - 2);
        programmer.setPosition(newPos);
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 13; // Tratamento de Excepções
    }
}