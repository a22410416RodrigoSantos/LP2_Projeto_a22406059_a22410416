package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbiException extends Abismo {

    public AbiException() {
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