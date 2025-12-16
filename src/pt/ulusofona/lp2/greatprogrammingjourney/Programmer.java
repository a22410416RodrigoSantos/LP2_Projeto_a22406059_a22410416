package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Programmer {
    private int id;
    private String name;
    private String[] favoriteLanguages;
    private String color;
    private int position = 1;
    private boolean inGame = true;
    private String state = "Em Jogo";
    private List<Ferramenta> inventory = new ArrayList<>();
    private List<Integer> positionHistory = new ArrayList<>();

    public Programmer() {
        positionHistory.add(1);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String[] getFavoriteLanguages() {
        return favoriteLanguages;
    }
    public void setFavoriteLanguages(String[] langs) {
        this.favoriteLanguages = langs;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        if (pos < 1) {
            pos = 1;
        }
        if (this.position != pos) {
            positionHistory.add(pos);
        }
        this.position = pos;
    }

    public boolean isInGame() { return inGame; }
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
        if (!inGame) {
            this.state = "Derrotado";
        }
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public List<Ferramenta> getInventory() {
        return inventory;
    }

    public List<Ferramenta> getFerramentas() {
        return new ArrayList<>(inventory);
    }

    public void addFerramenta(Ferramenta ferramenta) {
        if (ferramenta != null) {
            inventory.add(ferramenta);
        }
    }

    public void removeFerramenta(Ferramenta ferramenta) {
        inventory.remove(ferramenta);
    }

    public Ferramenta getFerramentaQueNeutraliza(Abismo abismo) {
        for (Ferramenta f : inventory) {
            if (f.podeNeutralizar(abismo)) {
                return f;
            }
        }
        return null;
    }

    public List<Integer> getPositionHistory() {
        return new ArrayList<>(positionHistory);
    }

    public int getPositionFromHistory(int movesBack) {
        int index = positionHistory.size() - 1 - movesBack;
        if (index < 0){
            return 1;
        }
        return positionHistory.get(index);
    }

}