package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiErroSintaxe extends Abismo {

    public abiErroSintaxe() {
        super(0, "Erro de Sintaxe", "syntax_error.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        int currentPos = programmer.getPosition();
        int newPos = Math.max(1, currentPos - 1);
        programmer.setPosition(newPos);
    }

    @Override
    public boolean isNeutralizedBy(Ferramenta ferramenta) {
        return ferramenta.getId() == 14; // IDE
    }
}