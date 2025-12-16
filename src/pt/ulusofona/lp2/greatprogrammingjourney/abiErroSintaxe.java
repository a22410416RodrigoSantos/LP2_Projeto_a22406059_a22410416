package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiErroSintaxe extends Abismo {

    public abiErroSintaxe() {
        super(0, "Erro de Sintaxe", "syntax_error.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        // Recua 1 casa
        int currentPos = programmer.getPosition();
        int newPos = currentPos - 1;

        // Não pode ir abaixo da posição 1
        if (newPos < 1) {
            newPos = 1;
        }

        programmer.setPosition(newPos);
    }
}