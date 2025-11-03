package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {

    private ArrayList<Programmer> players = new ArrayList<>();
    private ArrayList<Slot> slots = new ArrayList<>();
    private int boardSize = 0;
    private int currentPlayerIndex = 0;
    private int totalTurns = 0;

    // =========================================================================
    // 1. createInitialBoard – LÊ JSON DO test-files/config.json
    // =========================================================================
    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        // Ignorar playerInfo e worldSize – o Tio Bob usa JSON
        players.clear();
        slots.clear();
        totalTurns = 0;

        // 1. LER JSON
        String json = readJsonFromFile("test-files/config.json");
        if (json == null || json.trim().isEmpty()) {
            return false;
        }

        // 2. PARSE JSON E CARREGAR DADOS
        return parseJsonAndInitialize(json);
    }

    // =========================================================================
    // LER JSON DO FICHEIRO
    // =========================================================================
    private String readJsonFromFile(String path) {
        try {
            java.nio.file.Path filePath = java.nio.file.Paths.get(path);
            if (!java.nio.file.Files.exists(filePath)) {
                return null;
            }
            return new String(java.nio.file.Files.readAllBytes(filePath), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    // =========================================================================
    // PARSE JSON E INICIALIZAR JOGO
    // =========================================================================
    private boolean parseJsonAndInitialize(String json) {
        try {
            String cleanJson = json.replaceAll("\\s+", "");

            // Extrair boardSize
            int boardSizeStart = cleanJson.indexOf("\"tamanho\":");
            if (boardSizeStart == -1) return false;
            int boardSizeEnd = cleanJson.indexOf(",", boardSizeStart);
            if (boardSizeEnd == -1) boardSizeEnd = cleanJson.indexOf("}", boardSizeStart);
            boardSize = Integer.parseInt(cleanJson.substring(boardSizeStart + 9, boardSizeEnd));

            if (boardSize < 4) return false;

            // Extrair jogadores
            int playersStart = cleanJson.indexOf("\"jogadores\":[");
            int playersEnd = cleanJson.indexOf("]", playersStart);
            if (playersStart == -1 || playersEnd == -1) return false;

            String playersJson = cleanJson.substring(playersStart + 12, playersEnd);
            ArrayList<Programmer> tempPlayers = new ArrayList<>();

            // Contar jogadores
            int playerCount = 0;
            int bracePos = 0;
            while ((bracePos = playersJson.indexOf("{", bracePos) + 1) > 0) {
                playerCount++;
            }
            if (playerCount < 2 || playerCount > 4) return false;

            // Parse cada jogador
            int start = 0;
            while ((start = playersJson.indexOf("{", start)) != -1) {
                int end = playersJson.indexOf("}", start);
                if (end == -1) return false;

                String playerJson = playersJson.substring(start + 1, end);
                Programmer player = parsePlayerFromJson(playerJson);

                if (player == null) return false;
                tempPlayers.add(player);

                start = end;
            }

            // Validar IDs únicos
            for (int i = 0; i < tempPlayers.size(); i++) {
                for (int j = i + 1; j < tempPlayers.size(); j++) {
                    if (tempPlayers.get(i).getId() == tempPlayers.get(j).getId()) {
                        return false;
                    }
                }
            }

            // Validar boardSize >= 2 * jogadores
            if (boardSize < 2 * tempPlayers.size()) return false;

            // Criar slots
            for (int i = 1; i <= boardSize; i++) {
                Slot slot = new Slot();
                slot.setNumber(i);
                slot.setImageName(i == boardSize ? "glory.png" : "normal.png");
                slot.setStart(i == 1);
                slot.setEnd(i == boardSize);
                slots.add(slot);
            }

            // Ordenar jogadores por ID
            for (int i = 0; i < tempPlayers.size() - 1; i++) {
                for (int j = i + 1; j < tempPlayers.size(); j++) {
                    if (tempPlayers.get(i).getId() > tempPlayers.get(j).getId()) {
                        Programmer temp = tempPlayers.get(i);
                        tempPlayers.set(i, tempPlayers.get(j));
                        tempPlayers.set(j, temp);
                    }
                }
            }

            // Atribuir posições iniciais
            for (Programmer p : tempPlayers) {
                p.setPosition(1); // REGRA-101
            }

            players = tempPlayers;
            currentPlayerIndex = 0;
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // =========================================================================
    // PARSE UM JOGADOR DO JSON
    // =========================================================================
    private Programmer parsePlayerFromJson(String playerJson) {
        Programmer p = new Programmer();

        // Extrair ID
        String idStr = extractJsonValue(playerJson, "ID");
        if (idStr == null) return null;
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0) return null;
            p.setId(id);
        } catch (NumberFormatException e) {
            return null;
        }

        // Extrair Nome
        String nome = extractJsonValue(playerJson, "Nome");
        if (nome == null || nome.trim().isEmpty()) return null;
        p.setName(nome.trim());

        // Extrair Linguagens
        String linguagens = extractJsonValue(playerJson, "Linguagens favoritas");
        if (linguagens == null) return null;
        p.setFavoriteLanguages(linguagens.isEmpty() ? new String[0] : linguagens.split(";"));

        // Extrair Cor
        String cor = extractJsonValue(playerJson, "Cor");
        if (cor == null || !isValidColor(cor.trim().toUpperCase())) return null;
        p.setColor(cor.trim().toUpperCase());

        p.setPosition(1);
        return p;
    }

    // =========================================================================
    // EXTRAI VALOR DE JSON
    // =========================================================================
    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) return null;
        start += search.length();
        int end = json.indexOf("\"", start);
        return end == -1 ? null : json.substring(start, end);
    }

    private boolean isValidColor(String color) {
        return "RED".equals(color) || "GREEN".equals(color) ||
                "BLUE".equals(color) || "YELLOW".equals(color) ||
                "BROWN".equals(color) || "PURPLE".equals(color);
    }

    // =========================================================================
    // 2. getImagePng
    // =========================================================================
    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > boardSize) return null;
        return nrSquare == boardSize ? "glory.png" : "normal.png";
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
    // 4. getProgrammerInfoAsStr – linguagens ordenadas alfabeticamente
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

        // Ricochete se ultrapassar
        if (newPos > boardSize) {
            int excess = newPos - boardSize;
            newPos = boardSize - excess;
        }

        current.setPosition(newPos);
        totalTurns++;

        // Não avança turno se chegou ao fim
        if (newPos != boardSize) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

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

        ArrayList<Programmer> remaining = new ArrayList<>();
        for (Programmer p : players) {
            if (winner == null || p.getId() != winner.getId()) {
                remaining.add(p);
            }
        }

        // Ordenar: maior posição primeiro, depois nome alfabético
        for (int i = 0; i < remaining.size() - 1; i++) {
            for (int j = i + 1; j < remaining.size(); j++) {
                Programmer a = remaining.get(i);
                Programmer b = remaining.get(j);
                if (b.getPosition() > a.getPosition() ||
                        (b.getPosition() == a.getPosition() &&
                                b.getName().compareTo(a.getName()) < 0)) {
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
    // 11. customizeBoard
    // =========================================================================
    public HashMap<String, String> customizeBoard() {
        HashMap<String, String> custom = new HashMap<>();
        // custom.put("playerRedImage", "red_player.png");
        // custom.put("boardColor", "#f0f0f0");
        return custom;
    }
}