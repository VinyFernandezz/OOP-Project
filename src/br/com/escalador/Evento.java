//==================================================
// Evento.java
// br.com.escalador
//==================================================
package br.com.escalador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa um evento a ser escalado.
 */
public class Evento {
    // TODOS OS ATRIBUTOS PACKAGE-PRIVATE para compatibilidade total
    String descricao;
    String diaDaSemana;
    String horario;
    Map<String, Integer> necessidades;
    Map<String, List<Pessoa>> escalaPronta;

    // Construtor vazio obrigatório (para diretrizes)
    public Evento() {
        this.necessidades = new HashMap<>();
        this.escalaPronta = new HashMap<>();
    }

    // Construtor com argumentos
    public Evento(String descricao, String diaDaSemana, String horario, Map<String, Integer> necessidades) {
        this.descricao = descricao;
        this.diaDaSemana = diaDaSemana;
        this.horario = horario;
        this.necessidades = necessidades;
        this.escalaPronta = new HashMap<>();
    }

    // Getters públicos (boa prática, mas não obrigatórios para funcionamento)
    public String getDescricao() { return descricao; }
    public String getDiaDaSemana() { return diaDaSemana; }
    public String getHorario() { return horario; }
    public Map<String, Integer> getNecessidades() { return necessidades; }
    public Map<String, List<Pessoa>> getEscalaPronta() { return escalaPronta; }

    // Setters públicos
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setDiaDaSemana(String diaDaSemana) { this.diaDaSemana = diaDaSemana; }
    public void setHorario(String horario) { this.horario = horario; }
    public void setNecessidades(Map<String, Integer> necessidades) { this.necessidades = necessidades; }
    public void setEscalaPronta(Map<String, List<Pessoa>> escalaPronta) { this.escalaPronta = escalaPronta; }
}