package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiFileNotFoundException extends Abismo {

    public abiFileNotFoundException() {
        super(3, "FileNotFoundException", "file_not_found.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        // Recua 3 casas
        int currentPos = programmer.getPosition();
        int newPos = currentPos - 3;

        // Não pode ir abaixo da posição 1
        if (newPos < 1) {
            newPos = 1;
        }

        programmer.setPosition(newPos);
    }
}