//==================================================
// ValidadorEscalacao.java
// br.com.escalador
//==================================================
package br.com.escalador;

import br.com.escalador.exceptions.ValidacaoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por validar as regras de escalação.
 * Centraliza toda a lógica de validação das regras de negócio.
 */
public class ValidadorEscalacao {

    /**
     * Verifica se uma pessoa pode ser escalada para uma função específica.
     */
    public static boolean podeSerEscalado(Pessoa pessoa, String novaFuncao, 
                                        Map<String, List<Pessoa>> escalaAtual) {
        try {
            validarEscalacao(pessoa, novaFuncao, escalaAtual);
            return true;
        } catch (ValidacaoException e) {
            return false;
        }
    }

    /**
     * Valida se uma escalação é possível, lançando exceção se não for.
     */
    public static void validarEscalacao(Pessoa pessoa, String novaFuncao, 
                                      Map<String, List<Pessoa>> escalaAtual) throws ValidacaoException {
        
        if (!pessoa.temFuncao(novaFuncao)) {
            throw new ValidacaoException(pessoa.getNome() + " não tem a função " + novaFuncao);
        }

        List<String> funcoesAtuais = getFuncoesAtuaisPessoa(pessoa, escalaAtual);

        // REGRA A: Se não canta e toca, máximo 1 função
        if (!pessoa.podeAcumularFuncoes() && !funcoesAtuais.isEmpty()) {
            throw new ValidacaoException(pessoa.getNome() + " já está escalado e não pode acumular funções");
        }

        // REGRA B: Não pode tocar 2 instrumentos
        boolean jaTocaInstrumento = funcoesAtuais.stream()
            .anyMatch(ConfiguracaoEscala::ehInstrumento);
        boolean novaFuncaoEhInstrumento = ConfiguracaoEscala.ehInstrumento(novaFuncao);

        if (jaTocaInstrumento && novaFuncaoEhInstrumento) {
            throw new ValidacaoException(pessoa.getNome() + " não pode tocar dois instrumentos");
        }

        // REGRA C: Máximo 2 funções
        if (funcoesAtuais.size() >= ConfiguracaoEscala.MAX_FUNCOES_SIMULTANEAS) {
            throw new ValidacaoException(pessoa.getNome() + " já atingiu o limite de funções simultâneas");
        }

        // REGRA D: Se pode cantar e tocar, só pode ter Cantor + 1 Instrumento
        if (pessoa.podeAcumularFuncoes() && funcoesAtuais.size() == 1) {
            String funcaoAtual = funcoesAtuais.get(0);
            boolean combinacaoValida = 
                (funcaoAtual.equals(ConfiguracaoEscala.FUNCAO_CANTOR) && novaFuncaoEhInstrumento) ||
                (ConfiguracaoEscala.ehInstrumento(funcaoAtual) && novaFuncao.equals(ConfiguracaoEscala.FUNCAO_CANTOR));
            
            if (!combinacaoValida) {
                throw new ValidacaoException("Combinação inválida de funções para " + pessoa.getNome());
            }
        }
    }

    /**
     * Obtém todas as funções que uma pessoa já está exercendo na escala atual.
     */
    public static List<String> getFuncoesAtuaisPessoa(Pessoa pessoa, Map<String, List<Pessoa>> escalaAtual) {
        List<String> funcoes = new ArrayList<>();
        for (Map.Entry<String, List<Pessoa>> entry : escalaAtual.entrySet()) {
            if (entry.getValue().contains(pessoa)) {
                funcoes.add(entry.getKey());
            }
        }
        return funcoes;
    }
}