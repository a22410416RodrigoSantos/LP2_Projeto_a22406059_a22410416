package pt.ulusofona.lp2.thegreatjourney;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {

    private ArrayList<Programmer> players = new ArrayList<>();
    private ArrayList<Slot> slots = new ArrayList<>();
    private int boardSize = 0;
    private int currentPlayerIndex = 0;
    private int totalTurns = 0; // para getGameResults

    // =========================================================================
    // 1. createInitialBoard – RETORNA boolean (Tio Bob)
    // =========================================================================
    public boolean createInitialBoard(String[][] playerInfo, int boardSize) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }

        this.boardSize = boardSize;
        players.clear();
        slots.clear();
        totalTurns = 0;

        // Criar slots
        for (int i = 1; i <= boardSize; i++) {
            Slot slot = new Slot();
            slot.setNumber(i);
            slot.setImageName("normal.png");
            slot.setStart(i == 1);
            slot.setEnd(i == boardSize);
            slots.add(slot);
        }

        // Processar jogadores
        for (String[] info : playerInfo) {
            if (info.length != 4) return false;

            try {
                int id = Integer.parseInt(info[0].trim());
                String name = info[1].trim();
                String languages = info[2].trim();
                String color = info[3].trim().toUpperCase();

                // ID único e positivo
                if (id <= 0) return false;
                for (Programmer p : players) {
                    if (p.getId() == id) return false;
                }

                // Nome não vazio
                if (name.isEmpty()) return false;

                // Cor válida
                if (!isValidColor(color)) return false;

                Programmer p = new Programmer();
                p.setId(id);
                p.setName(name);
                p.setFavoriteLanguages(languages.isEmpty() ? new String[0] : languages.split(";"));
                p.setColor(color);
                p.setPosition(1);
                players.add(p);

            } catch (NumberFormatException e) {
                return false;
            }
        }

        // boardSize >= 2 * jogadores
        if (boardSize < 2 * players.size()) return false;

        // Ordenar por ID
        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = i + 1; j < players.size(); j++) {
                if (players.get(i).getId() > players.get(j).getId()) {
                    Programmer temp = players.get(i);
                    players.set(i, players.get(j));
                    players.set(j, temp);
                }
            }
        }

        currentPlayerIndex = 0;
        return true;
    }

    private boolean isValidColor(String color) {
        return color.equals("RED") || color.equals("GREEN") ||
                color.equals("BLUE") || color.equals("YELLOW");
    }

    // =========================================================================
    // 2. getImagePng
    // =========================================================================
    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > boardSize) return null;
        if (nrSquare == boardSize) return "glory.png";
        return "normal.png";
    }

    // =========================================================================
    // 3. getProgrammerInfo
    // =========================================================================
    public String[] getProgrammerInfo(int id) {
        for (Programmer p : players) {
            if (p.getId() == id) {
                String[] langs = p.getFavoriteLanguages();
                String langStr = "";
                for (int i = 0; i < langs.length; i++) {
                    langStr += langs[i];
                    if (i < langs.length - 1) langStr += ";";
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

    // =========================================================================
    // 4. getProgrammerInfoAsStr
    // =========================================================================
    public String getProgrammerInfoAsStr(int id) {
        for (Programmer p : players) {
            if (p.getId() == id) {
                String[] langs = p.getFavoriteLanguages().clone();
                // Ordenar alfabeticamente
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
                    if (i < langs.length - 1) langStr += "; ";
                }

                String state = p.getPosition() == boardSize ? "Vencedor" : "Em Jogo";

                return p.getId() + " | " + p.getName() + " | " + p.getPosition() +
                        " | " + langStr + " | " + state;
            }
        }
        return null;
    }

    // =========================================================================
    // 5. getSlotInfo
    // =========================================================================
    public String[] getSlotInfo(int position) {
        if (position < 1 || position > boardSize) return null;

        ArrayList<Integer> ids = new ArrayList<>();
        for (Programmer p : players) {
            if (p.getPosition() == position) {
                ids.add(p.getId());
            }
        }

        // Ordenar IDs
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
            if (i < ids.size() - 1) idStr += ",";
        }

        return new String[]{idStr};
    }

    // =========================================================================
    // 6. getCurrentPlayerID
    // =========================================================================
    public int getCurrentPlayerID() {
        if (players.isEmpty()) return -1;
        return players.get(currentPlayerIndex).getId();
    }

    // =========================================================================
    // 7. moveCurrentPlayer
    // =========================================================================
    public boolean moveCurrentPlayer(int nrSpaces) {
        if (nrSpaces < 1 || nrSpaces > 6 || players.isEmpty()) return false;

        Programmer current = players.get(currentPlayerIndex);
        int newPos = current.getPosition() + nrSpaces;
        if (newPos > boardSize) newPos = boardSize;
        current.setPosition(newPos);

        totalTurns++;
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        return true;
    }

    // =========================================================================
    // 8. gameIsOver
    // =========================================================================
    public boolean gameIsOver() {
        for (Programmer p : players) {
            if (p.getPosition() == boardSize) return true;
        }
        return false;
    }

    // =========================================================================
    // 9. getGameResults
    // =========================================================================
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

        ArrayList<Programmer> remaining = new ArrayList<>(players);
        if (winner != null) {
            for (int i = 0; i < remaining.size(); i++) {
                if (remaining.get(i).getId() == winner.getId()) {
                    remaining.remove(i);
                    break;
                }
            }
        }

        // Ordenar: maior posição → nome
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

    // =========================================================================
    // 10. getAuthorsPanel
    // =========================================================================
    public JPanel getAuthorsPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new java.awt.Dimension(300, 300));
        panel.add(new JLabel("Nome: [Teu Nome]"));
        panel.add(new JLabel("Número: [Teu Número]"));
        return panel;
    }

    // =========================================================================
    // customizeBoard – STUB (Tio Bob não pediu)
    // =========================================================================
    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }
}