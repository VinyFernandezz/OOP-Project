//==================================================
// Participante.java
// br.com.escalador
//==================================================
package br.com.escalador;

/**
 * Superclasse abstrata que define o comportamento básico de um participante.
 * Atende às diretrizes: superclasse abstrata com método abstrato.
 */
public abstract class Participante {
    // PACKAGE-PRIVATE para acesso direto nas subclasses e mesmo pacote
    String nome;
    int vezesEscalado;
    
    // Construtor vazio obrigatório
    public Participante() {
        this.vezesEscalado = 0;
    }
    
    // Construtor com argumentos obrigatório
    public Participante(String nome) {
        this.nome = nome;
        this.vezesEscalado = 0;
    }
    
    // Método abstrato obrigatório
    public abstract boolean podeParticipar(String diaDaSemana, String horario);
    
    // Métodos concretos com final
    public final String getNome() { 
        return nome; 
    }
    
    public final int getVezesEscalado() { 
        return vezesEscalado; 
    }
    
    public final void incrementarEscalacao() { 
        this.vezesEscalado++; 
    }
    
    // Setter para nome (útil para edição)
    public void setNome(String nome) {
        this.nome = nome;
    }
}