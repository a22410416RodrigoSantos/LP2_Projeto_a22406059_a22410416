package pt.ulusofona.lp2.greatprogrammingjourney;

public class Slot {
    private int number;
    private Effect effect;        // Agora guarda um Effect (pode ser Abyss ou Tool)
    private boolean isStart;
    private boolean isEnd;

    public Slot(int number) {
        this.number = number;
        this.isStart = number == 1;
        this.isEnd = false;  // Será definido depois se for o fim
    }

    public int getNumber() {
        return number;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public boolean hasEffect() {
        return effect != null;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    // Usado pelo visualizador para mostrar a imagem correta da casa
    public String getImageName() {
        if (effect != null) {
            return effect.getImageName();
        }
        if (isEnd) {
            return "glory.png";
        }
        return "normal.png";
    }
}