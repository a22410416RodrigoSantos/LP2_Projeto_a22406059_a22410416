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
    private int lastNrSpaces = 0; // necessário para o abismo "Erro de Lógica"

    // ==================================================================
    // createInitialBoard da Parte 1 (mantido para compatibilidade)
    // ==================================================================

    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        return createInitialBoard(playerInfo, worldSize, null);
    }

    // ==================================================================
    // Novo createInitialBoard com abismos e ferramentas
    // ==================================================================

    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }

        this.boardSize = worldSize;
        players.clear();
        slots.clear();
        totalTurns = 0;
        currentPlayerIndex = 0;
        lastNrSpaces = 0;

        // Criar slots
        for (int i = 1; i <= worldSize; i++) {
            Slot slot = new Slot(i);
            slots.add(slot);
        }

        // Marcar início e fim
        slots.get(0).setStart(true);
        slots.get(worldSize - 1).setEnd(true);

        // Processar jogadores
        for (String[] info : playerInfo) {
            if (info.length != 4) return false;

            try {
                int id = Integer.parseInt(info[0].trim());
                String name = info[1].trim();
                String languages = info[2].trim();
                String color = info[3].trim();

                if (id <= 0 || name.isEmpty() || !isValidColor(color)) return false;

                // ID duplicado?
                if (players.stream().anyMatch(p -> p.getId() == id)) return false;

                Programmer p = new Programmer();
                p.setId(id);
                p.setName(name);

                String[] langs = languages.split(";");
                for (int j = 0; j < langs.length; j++) langs[j] = langs[j].trim();
                p.setFavoriteLanguages(langs);

                p.setColor(color);
                p.setPosition(1);

                players.add(p);

            } catch (NumberFormatException e) {
                return false;
            }
        }

        if (worldSize < 2 * players.size()) return false;

        // Ordenar por ID
        players.sort((a, b) -> Integer.compare(a.getId(), b.getId()));

        // Processar abismos e ferramentas
        if (abyssesAndTools != null) {
            for (String[] item : abyssesAndTools) {
                if (item.length != 3) return false;

                try {
                    int type = Integer.parseInt(item[0].trim());
                    int position = Integer.parseInt(item[2].trim());

                    if (position < 1 || position > worldSize) return false;

                    Effect effect = createEffect(type);
                    if (effect == null) return false;

                    slots.get(position - 1).setEffect(effect);

                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        return true;
    }

    private Effect createEffect(int type) {
        return switch (type) {
            // Abismos (IDs 0–9)
            case 0  -> new abiErroSintaxe();
            case 1  -> new abiErroLogica();
            case 2  -> new abiException();
            case 3  -> new abiFileNotFoundException();
            case 4  -> new abiCrash();
            case 5  -> new abiCodigoDuplicado();
            case 6  -> new abiEfeitosSecundarios();
            case 7  -> new abiBlueScreenOfDeath();
            case 8  -> new abiCicloInfinito();
            case 9  -> new abiSegmentationFault();

            // Ferramentas (IDs 10–15)
            case 10 -> new ferHeranca();
            case 11 -> new ferProgramacaoFuncional();
            case 12 -> new ferTestesUnitarios();
            case 13 -> new ferTratamentoExcepcoes();
            case 14 -> new ferIDE();
            case 15 -> new ferAjudaDoProfessor();

            default -> null;
        };
    }

    private boolean isValidColor(String color) {
        return switch (color) {
            case "Purple", "Blue", "Green", "Brown" -> true;
            default -> false;
        };
    }

    // ==================================================================
    // Imagens e informação das slots
    // ==================================================================

    public String getImagePng(int nrSquare) {
        if (nrSquare < 1 || nrSquare > boardSize) return null;
        return slots.get(nrSquare - 1).getImageName();
    }

    public String[] getSlotInfo(int position) {
        if (position < 1 || position > boardSize) return null;

        ArrayList<Integer> ids = new ArrayList<>();
        for (Programmer p : players) {
            if (p.isInGame() && p.getPosition() == position) {
                ids.add(p.getId());
            }
        }

        ids.sort(Integer::compareTo);

        String idStr = ids.isEmpty() ? "" : ids.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        return new String[]{idStr};
    }

    // ==================================================================
    // Movimento e reação a efeitos
    // ==================================================================

    public boolean moveCurrentPlayer(int nrSpaces) {
        if (nrSpaces < 1 || nrSpaces > 6 || players.isEmpty()) return false;

        Programmer current = players.get(currentPlayerIndex);
        if (!current.isInGame()) {
            // jogador eliminado ou preso → passa turno
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            return false;
        }

        this.lastNrSpaces = nrSpaces;

        int oldPos = current.getPosition();
        int newPos = oldPos + nrSpaces;

        if (newPos > boardSize) {
            int excess = newPos - boardSize;
            newPos = boardSize - excess;
        }

        current.setPosition(newPos);
        totalTurns++;

        // Vitória imediata
        if (newPos == boardSize) {
            current.setState("Vencedor");
            return true;
        }

        // Reagir ao efeito da nova casa
        Slot slot = slots.get(newPos - 1);
        if (slot.hasEffect()) {
            Effect effect = slot.getEffect();

            if (effect instanceof Abismo abismo) {
                // Verificar se tem ferramenta que neutralize
                Ferramenta ferramenta = current.getFerramentaQueNeutraliza(abismo);
                if (ferramenta != null) {
                    current.removeFerramenta(ferramenta);
                    // efeito neutralizado → nada acontece
                } else {
                    abismo.apply(current, this);
                }
            } else if (effect instanceof Ferramenta ferramenta) {
                ferramenta.apply(current, this); // apanha a ferramenta
            }
        }

        // Passar turno se o jogador ainda estiver em jogo
        if (current.isInGame()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        return true;
    }

    public int getCurrentPlayerID() {
        if (players.isEmpty()) return -1;
        return players.get(currentPlayerIndex).getId();
    }

    // ==================================================================
    // Outros métodos obrigatórios (atualizados minimamente)
    // ==================================================================

    public String[] getProgrammerInfo(int id) {
        for (Programmer p : players) {
            if (p.getId() == id) {
                String langStr = String.join(";", p.getFavoriteLanguages());
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
                java.util.Arrays.sort(langs);
                String langStr = String.join("; ", langs);
                return p.getId() + " | " + p.getName() + " | " + p.getPosition() +
                        " | " + langStr + " | " + p.getState();
            }
        }
        return null;
    }

    public boolean gameIsOver() {
        // Alguém chegou ao fim?
        boolean someoneWon = players.stream().anyMatch(p -> p.getPosition() == boardSize);
        if (someoneWon) return true;

        // Ou só ficou 1 jogador ativo?
        long activeCount = players.stream().filter(Programmer::isInGame).count();
        return activeCount <= 1;
    }

    public ArrayList<String> getGameResults() {
        ArrayList<String> result = new ArrayList<>();
        result.add("THE GREAT PROGRAMMING JOURNEY");
        result.add("");
        result.add("NR. DE TURNOS");
        result.add(String.valueOf(totalTurns));
        result.add("");
        result.add("VENCEDOR");

        Programmer winner = players.stream()
                .filter(p -> p.getPosition() == boardSize)
                .findFirst()
                .orElse(null);

        result.add(winner != null ? winner.getName() : "N/A");
        result.add("");
        result.add("RESTANTES");

        players.stream()
                .filter(p -> winner == null || p.getId() != winner.getId())
                .sorted((a, b) -> {
                    int posCmp = Integer.compare(b.getPosition(), a.getPosition());
                    return posCmp != 0 ? posCmp : a.getName().compareTo(b.getName());
                })
                .forEach(p -> result.add(p.getName()));

        return result;
    }

    public JPanel getAuthorsPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new java.awt.Dimension(300, 300));
        panel.add(new JLabel("Nome: Marwan Ghunim"));
        panel.add(new JLabel("Número: a22406059"));
        panel.add(new JLabel("Nome: Rodrigo Santos"));
        panel.add(new JLabel("Número: a22410416"));

        return panel;
    }

    public HashMap<String, String> customizeBoard() {
        return new HashMap<>();
    }

    // ==================================================================
    // Métodos auxiliares
    // ==================================================================

    public ArrayList<Programmer> getPlayers() {
        return players;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getLastNrSpaces() {
        return lastNrSpaces;
    }
}