//==================================================
// GerenciadorMembros.java
// br.com.escalador
//==================================================
package br.com.escalador;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciadorMembros {

    public List<Pessoa> carregarMembros(String nomeArquivo) {
        List<Pessoa> membros = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(nomeArquivo), StandardCharsets.UTF_8))) {
            String linha;
            reader.readLine(); // Pula o cabeçalho

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(",", 5); // Limita para evitar erros com vírgulas no nome
                if (dados.length == 5) {
                    Pessoa p = new Pessoa(dados[0].trim());

                    // Funções
                    if(!dados[1].trim().isEmpty()){
                        p.getFuncoes().addAll(Arrays.asList(dados[1].split(";")));
                    }

                    // Disponibilidade
                    if(!dados[2].trim().isEmpty()){
                        String[] dispArray = dados[2].split(";");
                        for (String disp : dispArray) {
                            String[] diaHora = disp.trim().split("-", 2);
                            if (diaHora.length == 2) {
                                p.getDisponibilidade().add(new HorarioDisponivel(diaHora[0].trim(), diaHora[1].trim()));
                            }
                        }
                    }                   
                    
                    // Vezes escalado
                    try {
                        int vezesEscalado = Integer.parseInt(dados[3].trim());
                        while(p.getVezesEscalado() < vezesEscalado) {
                            p.incrementarEscalacao();
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter vezesEscalado para " + dados[0] + ": " + e.getMessage());
                    }
                    
                    // Canta e toca
                    p.setCantaEToca(dados[4].trim().equalsIgnoreCase("S"));

                    // CORREÇÃO CRÍTICA: Adicionar a pessoa à lista DENTRO do if
                    membros.add(p);
                }
            } 
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de membros: " + e.getMessage());
        }
        return membros;
    }

    public void salvarMembros(String nomeArquivo, List<Pessoa> membros) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nomeArquivo), StandardCharsets.UTF_8))) {
            writer.write("Nome,Funcoes,Disponibilidade,VezesEscalado,CantaEToca\n"); 

            for (Pessoa p : membros) {
                String funcoes = String.join(";", p.getFuncoes());
                String disponibilidade = p.getDisponibilidade().stream()
                                      .map(d -> d.diaDaSemana + "-" + d.horario)
                                      .collect(Collectors.joining(";"));
                String cantaEToca = p.isCantaEToca() ? "S" : "N";

                writer.write(String.format("%s,%s,%s,%d,%s\n", 
                p.getNome(), funcoes, disponibilidade, p.getVezesEscalado(), cantaEToca));
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de membros: " + e.getMessage());
        }
    }
}