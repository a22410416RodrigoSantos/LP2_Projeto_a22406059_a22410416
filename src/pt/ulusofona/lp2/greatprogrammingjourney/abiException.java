package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiException extends Abismo {

    public abiException() {
        super(2, "Exception", "exception.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        // Recua 2 casas
        int currentPos = programmer.getPosition();
        int newPos = currentPos - 2;

        // Não pode ir abaixo da posição 1
        if (newPos < 1) {
            newPos = 1;
        }

        programmer.setPosition(newPos);
    }
}