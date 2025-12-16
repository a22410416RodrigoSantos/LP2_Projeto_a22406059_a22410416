package pt.ulusofona.lp2.greatprogrammingjourney;

public class abiCicloInfinito extends Abismo {

    public abiCicloInfinito() {
        super(8, "Ciclo Infinito", "infinite_loop.png");
    }

    @Override
    public void apply(Programmer programmer, GameManager gameManager) {
        int position = programmer.getPosition();

        // Procura se já existe algum programador preso nesta casa
        Programmer presoAtual = null;
        for (Programmer p : gameManager.getPlayers()) {
            if (p.isInGame() && p.getPosition() == position && "Preso".equals(p.getState())) {
                presoAtual = p;
                break;
            }
        }

        // Se houver alguém preso, liberta-o
        if (presoAtual != null) {
            presoAtual.setState("Em Jogo");
        }

        // O novo jogador que chegou fica preso (só se não foi neutralizado pela ferramenta)
        // Nota: a neutralização já foi verificada no GameManager antes de chamar apply()
        programmer.setState("Preso");
    }
}