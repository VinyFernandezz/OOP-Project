//==================================================
// EscalacaoController.java
// br.com.escalador.controllers
//==================================================
package br.com.escalador;

import br.com.escalador.exceptions.EscalacaoException;
import javax.swing.JOptionPane;
import java.util.Map;
import java.util.List;

/**
 * Controller que media entre a UI e os serviços.
 * Implementa padrão MVC.
 */
public class EscalacaoController {
    
    private EscalacaoService escalacaoService;
    private EscaladorUI view;
    
    public EscalacaoController(EscaladorUI view) {
        this.view = view;
        this.escalacaoService = new EscalacaoService();
    }
    
    /**
     * Processa solicitação de criação de escala.
     */
    public void gerarNovaEscala(String diaSemana, String horario, 
                               Map<String, Integer> necessidades) {
        try {
            Evento evento = escalacaoService.criarEscala(diaSemana, horario, necessidades);
            List<Pessoa> candidatos = escalacaoService.getCandidatosDisponiveis();
            
            view.atualizarResultados(evento, candidatos);
            
        } catch (EscalacaoException e) {
            JOptionPane.showMessageDialog(view, 
                "Erro ao gerar escala: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Valida escalação manual na UI.
     */
    public boolean validarSelecaoManual(Pessoa pessoa, String funcao, 
                                       Map<String, List<Pessoa>> escalaAtual) {
        return escalacaoService.validarEscalacaoManual(pessoa, funcao, escalaAtual);
    }
}