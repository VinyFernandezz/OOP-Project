//==================================================
// Pessoa.java
// br.com.escalador
//==================================================
package br.com.escalador;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe que representa uma pessoa do ministério.
 * Herda de Participante e implementa Escalavel.
 */
public class Pessoa extends Participante implements Escalavel {
    // MANTÉM VISIBILIDADE PACKAGE-PRIVATE para compatibilidade
    List<String> funcoes;
    List<HorarioDisponivel> disponibilidade;
    boolean cantaEToca;

    // Construtor vazio obrigatório
    public Pessoa() {
        super();
        this.funcoes = new ArrayList<>();
        this.disponibilidade = new ArrayList<>();
        this.cantaEToca = false;
    }

    // Construtor com argumentos obrigatório
    public Pessoa(String nome) {
        super(nome);
        this.funcoes = new ArrayList<>();
        this.disponibilidade = new ArrayList<>();
        this.cantaEToca = false;
    }

    // Implementação do método abstrato da superclasse
    @Override
    public boolean podeParticipar(String diaDaSemana, String horario) {
        return disponibilidade.stream()
            .anyMatch(d -> d.diaDaSemana.equals(diaDaSemana) && 
                          d.horario.equals(horario));
    }

    // Implementação da interface Escalavel
    @Override
    public boolean isDisponivel(String dia, String horario) {
        return podeParticipar(dia, horario);
    }

    @Override
    public List<String> getFuncoesPossiveis() {
        return new ArrayList<>(funcoes);
    }

    @Override
    public boolean podeAcumularFuncoes() {
        return cantaEToca;
    }

    @Override
    public boolean temFuncao(String funcao) {
        return funcoes.contains(funcao);
    }

    // Getters e Setters ADICIONAIS (não substituem acesso direto)
    public List<String> getFuncoes() { return funcoes; }
    public void setFuncoes(List<String> funcoes) { this.funcoes = funcoes; }
    
    public List<HorarioDisponivel> getDisponibilidade() { return disponibilidade; }
    public void setDisponibilidade(List<HorarioDisponivel> disponibilidade) { 
        this.disponibilidade = disponibilidade; 
    }
    
    public boolean isCantaEToca() { return cantaEToca; }
    public void setCantaEToca(boolean cantaEToca) { this.cantaEToca = cantaEToca; }

    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(nome, pessoa.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}