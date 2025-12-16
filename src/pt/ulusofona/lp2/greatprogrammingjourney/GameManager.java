package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {

    private ArrayList<Programmer> players = new ArrayList<>();
    private ArrayList<Slot> slots = new ArrayList<>();
    private int boardSize = 0;
    private int currentPlayerIndex = 0;
    private int totalTurns = 0;
    private int lastNrSpaces = 0;

    // ==================================================================
    // MÉTODOS OBRIGATÓRIOS DA API - PARTE 1
    // ==================================================================

    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }

        this.boardSize = worldSize;
        players.clear();
        slots.clear();
        totalTurns = 0;

        // Criar slots
        for (int i = 1; i <= worldSize; i++) {
            Slot slot = new Slot();
            slot.setNumber(i);
            slot.setImageName("normal.png");
            if (i == 1) {
                slot.setStart(true);
            }
            if (i == worldSize) {
                slot.setEnd(true);
            }
            slots.add(slot);
        }

        // Criar jogadores
        for (String[] info : playerInfo) {
            if (info.length != 4) {
                return false;
            }

            try {
                int id = Integer.parseInt(info[0].trim());
                String name = info[1].trim();
                String languages = info[2].trim();
                String color = info[3].trim();

                if (id <= 0 || name.isEmpty()) {
                    return false;
                }

                if (!isValidColor(color)) {
                    return false;
                }

                // Verificar ID duplicado
                for (Programmer p : players) {
                    if (p.getId() == id) {
                        return false;
                    }
                }

                Programmer p = new Programmer();
                p.setId(id);
                p.setName(name);

                String[] langs = languages.split(";");
                for (int j = 0; j < langs.length; j++) {
                    langs[j] = langs[j].trim();
                }
                p.setFavoriteLanguages(langs);

                p.setColor(color);
                p.setPosition(1);
                players.add(p);

            } catch (NumberFormatException e) {
                return false;
            }
        }

        if (worldSize < 2 * players.size()) {
            return false;
        }

        // Ordenar por ID
        players.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));

        currentPlayerIndex = 0;
        return true;
    }

    // ==================================================================
    // MÉTODOS OBRIGATÓRIOS DA API - PARTE 2
    // ==================================================================

    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {
        // Chama a versão da Parte 1
        boolean success = createInitialBoard(playerInfo, worldSize);
        if (!success) return false;

        // Processa abismos e ferramentas (placeholder para compilar)
        if (abyssesAndTools != null) {
            for (String[] item : abyssesAndTools) {
                if (item.length == 3) {
                    try {
                        int type = Integer.parseInt(item[0].trim());
                        int position = Integer.parseInt(item[2].trim());
                        if (position >= 1 && position <= worldSize) {
                            // Placeholder - adiciona depois
                        }
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // MÉTODO CRÍTICO QUE FALTAVA - reactToAbyssOrTool
    public void reactToAbyssOrTool() {
        if (players.isEmpty() || currentPlayerIndex >= players.size()) return;

        Programmer current = players.get(currentPlayerIndex);
        if (!current.isInGame()) return;

        int pos = current.getPosition();
        if (pos < 1 || pos > slots.size()) return;

        Slot slot = slots.get(pos - 1);
        if (slot.hasEffect()) {
            Effect effect = slot.getEffect();
            if (effect != null) {
                effect.apply(current, this);
            }
        }
    }

    // Outros métodos obrigatórios da Parte 2
    public ArrayList<String> getProgrammersInfo() {
        ArrayList<String> info = new ArrayList<>();
        for (Programmer p : players) {
            info.add(getProgrammerInfoAsStr(p.getId()));
        }
        return info;
    }

    public void loadGame(File file) throws InvalidFileException {
        if (file == null || !file.exists()) {
            throw new InvalidFileException("Ficheiro inválido ou não encontrado");
        }
        // Implementação real depois - por agora só compila
    }

    public void saveGame(File file) {
        // Implementação real depois - por agora só compila
    }

    // ==================================================================
    // GETTER PARA RESOLVER ERRO DE PRIVATE ACCESS
    // ==================================================================

    public ArrayList<Programmer> getPlayers() {
        return players;
    }

    // ==================================================================
    // RESTO DOS MÉTODOS DA PARTE 1 (mantidos)
    // ==================================================================

    public boolean isValidColor(String color) {
        if (color.equals("Purple")) {
            return true;
        }
        if (color.equals("Blue")) {
            return true;
        }
        if (color.equals("Green")) {
            return true;
        }
        if (color.equals("Brown")) {
            return true;
        }
        return false;
    }

    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > boardSize) {
            return null;
        }
        Slot slot = slots.get(nrSquare - 1);
        return slot.getCurrentImageName(); // usa o novo método da Slot
    }

    public String[] getProgrammerInfo(int id) {
        for (Programmer p : players) {
            if (p.getId() == id) {
                String langStr = "";
                for (int i = 0; i < p.getFavoriteLanguages().length; i++) {
                    langStr += p.getFavoriteLanguages()[i];
                    if (i < p.getFavoriteLanguages().length - 1) {
                        langStr += ";";
                    }
                }
                return new String[]{
                        String.valueOf(p.getId()),
                        p.getName(),
                        langStr,
                        p.getColor(),
                        String.valueOf(p.getPosition())
                };
            }
        }
        return null;
    }

    public String getProgrammerInfoAsStr(int id) {
        for (Programmer p : players) {
            if (p.getId() == id) {
                String[] langs = p.getFavoriteLanguages().clone();

                for (int i = 0; i < langs.length - 1; i++) {
                    for (int j = i + 1; j < langs.length; j++) {
                        if (langs[i].compareTo(langs[j]) > 0) {
                            String temp = langs[i];
                            langs[i] = langs[j];
                            langs[j] = temp;
                        }
                    }
                }

                String langStr = "";
                for (int i = 0; i < langs.length; i++) {
                    langStr += langs[i];
                    if (i < langs.length - 1) {
                        langStr += "; ";
                    }
                }

                String state = p.getPosition() == boardSize ? "Derrotado" : "Em Jogo";

                return p.getId() + " | " + p.getName() + " | " + p.getPosition() +
                        " | " + langStr + " | " + state;
            }
        }
        return null;
    }

    public String[] getSlotInfo(int position) {
        if (position < 1 || position > boardSize) {
            return null;
        }

        ArrayList<Integer> ids = new ArrayList<>();
        for (Programmer p : players) {
            if (p.getPosition() == position) {
                ids.add(p.getId());
            }
        }

        for (int i = 0; i < ids.size() - 1; i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                if (ids.get(i) > ids.get(j)) {
                    int temp = ids.get(i);
                    ids.set(i, ids.get(j));
                    ids.set(j, temp);
                }
            }
        }

        String idStr = "";
        for (int i = 0; i < ids.size(); i++) {
            idStr += ids.get(i);
            if (i < ids.size() - 1) {
                idStr += ",";
            }
        }

        return new String[]{idStr};
    }

    public int getCurrentPlayerID() {
        if (players.isEmpty()) {
            return -1;
        }
        return players.get(currentPlayerIndex).getId();
    }

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (nrSpaces < 1 || nrSpaces > 6 || players.isEmpty()) {
            return false;
        }

        Programmer current = players.get(currentPlayerIndex);
        int newPos = current.getPosition() + nrSpaces;

        if (newPos > boardSize) {
            int excess = newPos - boardSize;
            newPos = boardSize - excess;
        }

        current.setPosition(newPos);
        totalTurns++;
        lastNrSpaces = nrSpaces;

        if (newPos != boardSize) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        // CHAMADA OBRIGATÓRIA PARA PARTE 2
        reactToAbyssOrTool();

        return true;
    }

    public boolean gameIsOver() {
        for (Programmer p : players) {
            if (p.getPosition() == boardSize) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getGameResults() {
        ArrayList<String> result = new ArrayList<>();
        result.add("THE GREAT PROGRAMMING JOURNEY");
        result.add("");
        result.add("NR. DE TURNOS");
        result.add(String.valueOf(totalTurns));
        result.add("");
        result.add("VENCEDOR");

        Programmer winner = null;
        for (Programmer p : players) {
            if (p.getPosition() == boardSize) {
                winner = p;
                break;
            }
        }
        result.add(winner != null ? winner.getName() : "N/A");
        result.add("");
        result.add("RESTANTES");

        ArrayList<Programmer> remaining = new ArrayList<>();
        for (Programmer p : players) {
            if (winner == null || p.getId() != winner.getId()) {
                remaining.add(p);
            }
        }

        for (int i = 0; i < remaining.size() - 1; i++) {
            for (int j = i + 1; j < remaining.size(); j++) {
                Programmer a = remaining.get(i);
                Programmer b = remaining.get(j);
                if (b.getPosition() > a.getPosition() ||
                        (b.getPosition() == a.getPosition() && b.getName().compareTo(a.getName()) < 0)) {
                    remaining.set(i, b);
                    remaining.set(j, a);
                }
            }
        }

        for (Programmer p : remaining) {
            result.add(p.getName());
        }
        return result;
    }

    public JPanel getAuthorsPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new java.awt.Dimension(300, 300));
        panel.add(new JLabel("Nome: Rodrigo Santos"));
        panel.add(new JLabel("Número: a22410416"));
        panel.add(new JLabel("Nome: Marwan Ghunim"));
        panel.add(new JLabel("Número: a22406059"));
        return panel;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }

    // ==================================================================
    // MÉTODOS AUXILIARES
    // ==================================================================

    public int getLastNrSpaces() {
        return lastNrSpaces;
    }
}