package br.com.escalador;

import br.com.escalador.exceptions.EscalacaoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe responsável pela geração automática de escalas.
 * Agora utiliza o ValidadorEscalacao para garantir regras de negócio.
 */
public class GeradorDeEscala {

    private List<Pessoa> candidatosGerais;

    public GeradorDeEscala() {
        this.candidatosGerais = new ArrayList<>();
    }

    /**
     * Gera a escala para um determinado evento.
     * VERSÃO MELHORADA: Agora valida conflitos de funções.
     */
    public Evento gerar(Evento evento, List<Pessoa> todosOsMembros) throws EscalacaoException {
        if (todosOsMembros == null || todosOsMembros.isEmpty()) {
            throw new EscalacaoException("Lista de membros está vazia");
        }
        
        // 1. Filtra candidatos disponíveis
        this.candidatosGerais = todosOsMembros.stream()
            .filter(p -> p.podeParticipar(evento.diaDaSemana, evento.horario))
            .collect(Collectors.toList());

        Map<String, List<Pessoa>> escalaPronta = new HashMap<>();

        // 2. Processa funções em ordem de prioridade
        List<String> funcoesPrioritarias = ordenarFuncoesPorPrioridade(evento.necessidades);
        
        for (String funcao : funcoesPrioritarias) {
            int quantidadeNecessaria = evento.necessidades.get(funcao);
            
            List<Pessoa> candidatosParaFuncao = filtrarCandidatos(funcao, escalaPronta);
            List<Pessoa> escolhidos = selecionarMelhoresCandidatos(candidatosParaFuncao, quantidadeNecessaria);
            
            if (!escolhidos.isEmpty()) {
                escalaPronta.put(funcao, escolhidos);
            }
        }
        
        evento.escalaPronta = escalaPronta;
        return evento;
    }

    /**
     * Filtra candidatos válidos para uma função, considerando conflitos.
     */
    private List<Pessoa> filtrarCandidatos(String funcao, Map<String, List<Pessoa>> escalaPronta) {
        return candidatosGerais.stream()
            .filter(p -> p.temFuncao(funcao))
            .filter(p -> ValidadorEscalacao.podeSerEscalado(p, funcao, escalaPronta))
            .collect(Collectors.toList());
    }

    /**
     * Seleciona os melhores candidatos baseado em critérios de justiça.
     */
    private List<Pessoa> selecionarMelhoresCandidatos(List<Pessoa> candidatos, int quantidade) {
        if (candidatos.isEmpty()) {
            return new ArrayList<>();
        }

        // Embaralha para dar chances iguais aos que têm mesmo número de escalações
        Collections.shuffle(candidatos);
        
        // Ordena por justiça (menos escalações = maior prioridade)
        candidatos.sort(Comparator.comparingInt(Pessoa::getVezesEscalado));
        
        // Seleciona a quantidade necessária
        return candidatos.stream()
            .limit(quantidade)
            .collect(Collectors.toList());
    }

    /**
     * Define ordem de processamento das funções.
     * Funções mais críticas primeiro.
     */
    private List<String> ordenarFuncoesPorPrioridade(Map<String, Integer> necessidades) {
        return necessidades.keySet().stream()
            .sorted((f1, f2) -> {
                // Cantor tem prioridade, depois instrumentos por ordem alfabética
                if (f1.equals("Cantor")) return -1;
                if (f2.equals("Cantor")) return 1;
                return f1.compareTo(f2);
            })
            .collect(Collectors.toList());
    }

    public List<Pessoa> getCandidatosGerais() {
        return this.candidatosGerais;
    }
}