//==================================================
// Pessoa.java
// br.com.escalador
//==================================================
package br.com.escalador;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pessoa {
    String nome;
    List<String> funcoes;
    List<HorarioDisponivel> disponibilidade;
    int vezesEscalado;
    boolean cantaEToca;

    public Pessoa(String nome) {
        this.nome = nome;
        this.funcoes = new ArrayList<>();
        this.disponibilidade = new ArrayList<>();
        this.vezesEscalado = 0;
        this.cantaEToca = false; 
    }

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
