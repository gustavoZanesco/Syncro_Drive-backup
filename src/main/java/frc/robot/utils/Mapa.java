package frc.robot.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Mapa {
    // Lista de pontos navegáveis (incluindo paletes e blocos)
    public Map<String, Posicao> nodes = Map.ofEntries(
        Map.entry("A", new Posicao(820, 1580, 0)),
        Map.entry("B", new Posicao(680, 790, 180)), 

        //x = 780 (esse deu certo sem a garra)
        Map.entry("C", new Posicao(830, 445, -90)),
        Map.entry("C-1", new Posicao(820, 155, -90)),
        Map.entry("C-2", new Posicao(832, 240, -90)),
        Map.entry("C-3", new Posicao(830, 445, -90)),
        Map.entry("C-4", new Posicao(830, 593, -90)),
        Map.entry("C-5", new Posicao(820, 740, -90)),
        Map.entry("C-6", new Posicao(830, 910, -90)),

        Map.entry("D", new Posicao(830, 1390, -90)),
        Map.entry("D-1", new Posicao(830, 1085, -90)),
        Map.entry("D-2", new Posicao(820, 1215, -90)),
        Map.entry("D-3", new Posicao(830, 1390, -90)),
        Map.entry("D-4", new Posicao(830, 1540, -90)),
        Map.entry("D-5", new Posicao(820, 1700, -90)),
        Map.entry("D-6", new Posicao(830, 1700, -90)),

        Map.entry("E", new Posicao(820, 500, 0)),
        Map.entry("F", new Posicao(1700, 500, 0)),

        Map.entry("G", new Posicao(2000, 1300, 0)),
        Map.entry("G-1", new Posicao(1700, 1300, 0)),
        Map.entry("G-2", new Posicao(2000, 1300, 0)),
        Map.entry("G-3", new Posicao(2450, 1300, 0)),

        Map.entry("H", new Posicao(2450, 500, 0)),
        Map.entry("I", new Posicao(3100, 500, 0)),
        Map.entry("J", new Posicao(3500, 1600, 90)),

        Map.entry("K", new Posicao(3500, 700, 0)),
        Map.entry("K-1", new Posicao(3200, 750, 180)),
        Map.entry("K-2", new Posicao(3450, 750, 180)),
        Map.entry("K-3", new Posicao(3700, 750, 180))
    );

    //Lista de 'pontes' entre cada ponto navegável
    public Map<String, List<String>> edges = Map.ofEntries(
        Map.entry("A", List.of("B", "C", "D", "E")),
        Map.entry("B", List.of("A", "C", "D", "E", "D-5")),

        Map.entry("C", List.of("A", "B", "D", "C-1", "C-2", "C-3", "C-4", "C-5", "C-6")),
        Map.entry("C-1", List.of("C", "C-2", "C-3", "C-4", "C-5", "C-6")),
        Map.entry("C-2", List.of("C", "C-1", "C-3", "C-4", "C-5", "C-6")),
        Map.entry("C-3", List.of("C", "C-1", "C-2", "C-4", "C-5", "C-6")),
        Map.entry("C-4", List.of("C", "C-1", "C-2", "C-3", "C-5", "C-6")),
        Map.entry("C-5", List.of("C", "C-1", "C-2", "C-3", "C-4", "C-6")),
        Map.entry("C-6", List.of("C", "C-1", "C-2", "C-3", "C-4", "C-5")),

        Map.entry("D", List.of("A", "B", "C", "D-1", "D-2", "D-3", "D-4", "D-5", "D-6")),
        Map.entry("D-1", List.of("D", "D-2", "D-3", "D-4", "D-5", "D-6")),
        Map.entry("D-2", List.of("D", "D-1", "D-3", "D-4", "D-5", "D-6")),
        Map.entry("D-3", List.of("D", "D-1", "D-2", "D-4", "D-5", "D-6")),
        Map.entry("D-4", List.of("D", "D-1", "D-2", "D-3", "D-5", "D-6")),
        Map.entry("D-5", List.of("D", "D-1", "D-2", "D-3", "D-4", "D-6")),
        Map.entry("D-6", List.of("D", "D-1", "D-2", "D-3", "D-4", "D-5")),

        Map.entry("E", List.of("A", "B", "C", "D", "F")),

        Map.entry("F", List.of("E", "G-1")),

        Map.entry("G", List.of("G-1", "G-2", "G-3")),
        Map.entry("G-1", List.of("F", "G", "G-2", "G-3")),
        Map.entry("G-2", List.of("G", "G-1", "G-3")),
        Map.entry("G-3", List.of("G", "G-1", "G-2", "H")),

        Map.entry("H", List.of("G-3", "I")),
        Map.entry("I", List.of("H", "K")),
        Map.entry("J", List.of("K")),
        Map.entry("K", List.of("J", "I", "K-1", "K-2", "K-3")),
        Map.entry("K-1", List.of("K", "K-2", "K-3")),
        Map.entry("K-2", List.of("K", "K-1", "K-3")),
        Map.entry("K-3", List.of("K", "K-1", "K-2"))
        );

    public Map<String, String> paletes = Map.ofEntries(
        Map.entry("1", "G-1"), 
        Map.entry("2", "G-2"), 
        Map.entry("3", "G-3"));

    public String[] blocosCanto = new String[] {"F"};

    public Mapa() {
      
    }

    public 

    String caminhoStr = "nenhum caminho";

    public List<EtapaTrajetoria> gerarEtapas(String origem, String destino, Set<String> pontosComAlinhamento) {
        
        Queue<List<String>> fila = new LinkedList<>();
        Set<String> visitado = new HashSet<>();

        fila.add(List.of(origem));

        while(!fila.isEmpty()) {
            List<String> caminho = fila.poll();
            String atual = caminho.get(caminho.size() - 1);

            if (atual.equals(destino)) {
                List<EtapaTrajetoria> etapas = new ArrayList<>();
                for (String nome : caminho) {
                    Posicao pose = nodes.get(nome);
                    boolean alinhar = pontosComAlinhamento.contains(nome);
                    etapas.add((new EtapaTrajetoria(pose, nome, alinhar)));
                }
                caminhoStr = String.join(" -> ", caminho);
                SmartDashboard.putString("caminho gerado", caminhoStr);
                return etapas;
            }

            if (!visitado.contains(atual)) {
                visitado.add(atual);
                for (String vizinho : edges.getOrDefault(atual, List.of())) {
                    if (!caminho.contains(vizinho)) {
                        List<String> novoCaminho = new ArrayList<>(caminho);
                        novoCaminho.add(vizinho);
                        fila.add(novoCaminho);
                    }
                }
            }
        }

        //SmartDashboard.putString("caminho gerado", "nenhum caminho encontrado");
        return List.of();
    }

    public String getCaminho() {
        return caminhoStr;
    }

    public void resetarCaminho() {
        caminhoStr = "null";
    }

    public Posicao getXYPonto(String ponto) {
        if (nodes.containsKey(ponto)) {
            return nodes.get(ponto);
        }

        return new Posicao(0, 0, 0);
    }
}