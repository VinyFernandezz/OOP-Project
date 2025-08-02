//==================================================
// EscalacaoService.java
// br.com.escalador.services
//==================================================
package br.com.escalador;

import br.com.escalador.exceptions.EscalacaoException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Serviço que encapsula a lógica de negócio de escalação.
 * Separa responsabilidades da UI.
 */
public class EscalacaoService {
    
    private GeradorDeEscala gerador;
    private GerenciadorMembros gerenciadorMembros;
    
    public EscalacaoService() {
        this.gerador = new GeradorDeEscala();
        this.gerenciadorMembros = new GerenciadorMembros();
    }
    
    /**
     * Cria uma nova escala baseada nos parâmetros fornecidos.
     */
    public Evento criarEscala(String diaSemana, String horario, 
                             Map<String, Integer> necessidades) throws EscalacaoException {
        
        List<Pessoa> membros = gerenciadorMembros.carregarMembros("membros.txt");
        
        Evento evento = new Evento("Culto de " + diaSemana, diaSemana, horario, necessidades);
        evento = gerador.gerar(evento, membros);
        
        // Incrementa contador dos escalados
        incrementarContadorEscalados(evento);
        
        // Salva mudanças
        gerenciadorMembros.salvarMembros("membros.txt", membros);
        
        return evento;
    }
    
    /**
     * Valida se uma pessoa pode ser escalada manualmente.
     */
    public boolean validarEscalacaoManual(Pessoa pessoa, String funcao, 
                                        Map<String, List<Pessoa>> escalaAtual) {
        return ValidadorEscalacao.podeSerEscalado(pessoa, funcao, escalaAtual);
    }
    
    /**
     * Incrementa o contador de escalações.
     */
    private void incrementarContadorEscalados(Evento evento) {
        Set<Pessoa> pessoasEscaladas = new HashSet<>();
        for (List<Pessoa> lista : evento.escalaPronta.values()) {
            pessoasEscaladas.addAll(lista);
        }
        for (Pessoa p : pessoasEscaladas) {
            p.incrementarEscalacao();
        }
    }
    
    public List<Pessoa> getCandidatosDisponiveis() {
        return gerador.getCandidatosGerais();
    }
}