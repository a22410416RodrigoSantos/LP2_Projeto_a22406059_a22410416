package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiErroLogica extends Abismo {

    public abiErroLogica() {
        super(1, "Erro de Lógica", "logic_error.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        // Recua N casas, onde N = floor(valor_do_dado / 2)
        int nrSpaces = gameManager.getLastNrSpaces(); // último valor do dado
        int recuo = nrSpaces / 2; // divisão inteira = floor

        int currentPos = programmer.getPosition();
        int newPos = currentPos - recuo;

        // Não pode ir abaixo da posição 1
        if (newPos < 1) {
            newPos = 1;
        }

        programmer.setPosition(newPos);
    }
}