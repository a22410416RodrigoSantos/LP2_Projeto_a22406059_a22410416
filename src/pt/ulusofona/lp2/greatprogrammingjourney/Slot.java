package pt.ulusofona.lp2.greatprogrammingjourney;

public class Slot {
    private int number;
    private String imageName;
    private boolean isStart;
    private boolean isEnd;
    private Effect effect; // para abismos e ferramentas da Parte 2

    public int getNumber() { return number; }
    public void setNumber(int n) { this.number = n; }

    public String getImageName() { return imageName; }
    public void setImageName(String name) { this.imageName = name; }

    public boolean isStart() { return isStart; }
    public void setStart(boolean b) { this.isStart = b; }

    public boolean isEnd() { return isEnd; }
    public void setEnd(boolean b) { this.isEnd = b; }

    // MÉTODOS NOVOS PARA PARTE 2
    public Effect getEffect() { return effect; }
    public void setEffect(Effect effect) { this.effect = effect; }
    public boolean hasEffect() { return effect != null; }

    // Para o visualizador mostrar a imagem correta
    public String getCurrentImageName() {
        if (effect != null && effect.getImageName() != null) {
            return effect.getImageName();
        }
        if (isEnd) {
            return "glory.png";
        }
        return imageName != null ? imageName : "normal.png";
    }
}