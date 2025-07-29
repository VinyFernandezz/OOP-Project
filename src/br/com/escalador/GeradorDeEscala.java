package br.com.escalador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeradorDeEscala {

    private List<Pessoa> candidatosGerais;

    public GeradorDeEscala() {
        this.candidatosGerais = new ArrayList<>();
    }

    /**
     * Gera a escala para um determinado evento.
     * @param evento O evento com as necessidades de funções.
     * @param todosOsMembros A lista completa de membros do ministério.
     * @return O evento com a escala preenchida.
     */
    public Evento gerar(Evento evento, List<Pessoa> todosOsMembros) {
        if (todosOsMembros == null || todosOsMembros.isEmpty()) {
            return evento; // Retorna o evento vazio se não houver membros
        }
        
        // 1. Filtra todos os membros que estão disponíveis para o dia e horário do evento.
        this.candidatosGerais = todosOsMembros.stream()
            .filter(p -> p.disponibilidade.stream()
                .anyMatch(d -> d.diaDaSemana.equals(evento.diaDaSemana) && d.horario.equals(evento.horario)))
            .collect(Collectors.toList());

        // 2. Para cada função necessária, seleciona os melhores candidatos.
        for (Map.Entry<String, Integer> necessidade : evento.necessidades.entrySet()) {
            String funcao = necessidade.getKey();
            int quantidadeNecessaria = necessidade.getValue();
            
            // Filtra os candidatos gerais para aqueles que podem exercer a função.
            List<Pessoa> candidatosParaEstaFuncao = this.candidatosGerais.stream()
                .filter(p -> p.funcoes.contains(funcao))
                .collect(Collectors.toList());

            // Embaralha para garantir que pessoas com o mesmo 'vezesEscalado' tenham chances iguais.
            Collections.shuffle(candidatosParaEstaFuncao);
            
            // Ordena pela regra de justiça (quem foi escalado menos vezes tem prioridade).
            candidatosParaEstaFuncao.sort(Comparator.comparingInt(p -> p.vezesEscalado));
                
            // Seleciona a quantidade necessária de pessoas.
            List<Pessoa> escolhidos = new ArrayList<>();
            if (!candidatosParaEstaFuncao.isEmpty()) {
                 for (int i = 0; i < quantidadeNecessaria && i < candidatosParaEstaFuncao.size(); i++) {
                    escolhidos.add(candidatosParaEstaFuncao.get(i));
                }
            }
           
            if (!escolhidos.isEmpty()) {
                evento.escalaPronta.put(funcao, escolhidos);
            }
        }
        
        return evento;
    }

    /**
     * Retorna a lista de pessoas que estavam disponíveis para o evento,
     * útil para popular os ComboBoxes na UI.
     */
    public List<Pessoa> getCandidatosGerais() {
        return this.candidatosGerais;
    }
}
