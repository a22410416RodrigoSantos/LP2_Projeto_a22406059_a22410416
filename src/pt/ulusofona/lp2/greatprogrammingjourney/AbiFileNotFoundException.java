package pt.ulusofona.lp2.greatprogrammingjourney;

public class AbiFileNotFoundException extends Abismo {

    public AbiFileNotFoundException() {
        super(3, "FileNotFoundException", "file_not_found.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        int currentPos = programmer.getPosition();
        int newPos = Math.max(1, currentPos - 3);
        programmer.setPosition(newPos);
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 13; // Tratamento de Excepções
    }
}