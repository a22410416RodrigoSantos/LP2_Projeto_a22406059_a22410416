package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiCrash extends Abismo {

    public abiCrash() {
        super(4, "Crash", "crash.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        // Volta imediatamente à posição 1 (reset total)
        programmer.setPosition(1);

        // Atualiza o histórico (opcional, mas mantém consistente)
        programmer.getPositionHistory().clear(); // limpa histórico antigo
        programmer.getPositionHistory().add(1);  // só fica com a posição 1
    }
}