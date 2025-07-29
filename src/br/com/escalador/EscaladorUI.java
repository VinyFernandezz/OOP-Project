package br.com.escalador;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EscaladorUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private List<Pessoa> todosOsMembros;
    private JPanel painelDeResultados;
    private GerenciadorMembros gerenciadorMembros;
    private JComboBox<String> comboDiaSemana;
    private JComboBox<String> comboHorario;
    private Map<String, JSpinner> spinnersDeFuncao;
    private Map<String, List<JComboBox<Pessoa>>> todosOsComboBoxes;
    private Map<String, List<Pessoa>> todosOsCandidatosPorFuncao;
    private JPanel painelDefinirEvento;
    private JSplitPane splitPane;

    public EscaladorUI() {
        setTitle("Escalador");
        setMinimumSize(new Dimension(800, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel painelPrincipal = new JPanel(new BorderLayout(20, 15));
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(painelPrincipal);

        gerenciadorMembros = new GerenciadorMembros();
        
        // --- Painel do Cabeçalho ---
        JPanel painelCabecalho = new JPanel(new BorderLayout());
        JLabel tituloApp = new JLabel("Escalador");
        tituloApp.setFont(new Font("SansSerif", Font.BOLD, 20));
        tituloApp.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        JButton btnGerenciar = new JButton("Gerenciar Membros");
        painelCabecalho.add(tituloApp, BorderLayout.CENTER);
        painelCabecalho.add(btnGerenciar, BorderLayout.WEST);
        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        
        // --- Painéis de Entrada e Resultado ---
        painelDefinirEvento = montarPainelDeEntrada();
        painelDeResultados = new JPanel(new GridBagLayout());
        JScrollPane painelRolagemResultados = new JScrollPane(painelDeResultados);
        painelRolagemResultados.setBorder(BorderFactory.createTitledBorder("Escala Gerada (Editável)"));
        
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelDefinirEvento, painelRolagemResultados);
        splitPane.setResizeWeight(0.45);
        splitPane.setBorder(null);
        
        painelPrincipal.add(splitPane, BorderLayout.CENTER);

        // --- Painel do Rodapé com Botão Principal ---
        JButton botaoGerar = new JButton("Gerar Escala");
        botaoGerar.setFont(new Font("SansSerif", Font.BOLD, 14));
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelRodape.add(botaoGerar);
        painelPrincipal.add(painelRodape, BorderLayout.SOUTH);
        
        // --- Listeners ---
        botaoGerar.addActionListener(e -> gerarEscalaCustomizada());
        btnGerenciar.addActionListener(e -> abrirGerenciadorMembros());
    }
    
    /**
     * Define a ordem de exibição das funções na UI.
     */
    private Comparator<String> getFuncaoComparator() {
        List<String> ordemPrioritaria = List.of(
            "Cantor", "Violão", "Teclado", "Baixo", "Guitarra", "Bateria", "Mesa de Som", "DataShow"
        );
        return Comparator.comparingInt(funcao -> {
            int index = ordemPrioritaria.indexOf(funcao);
            return index == -1 ? Integer.MAX_VALUE : index;
        });
    }
    
    /**
     * Monta o painel superior para definição do evento (dia, horário, funções).
     */
    private JPanel montarPainelDeEntrada() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        painel.setBorder(BorderFactory.createTitledBorder("Definir Evento"));
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(5, 5, 5, 20);

        // --- Painel Esquerdo: Dia e Horário ---
        JPanel painelEsquerda = new JPanel();
        painelEsquerda.setLayout(new BoxLayout(painelEsquerda, BoxLayout.Y_AXIS));
        
        painelEsquerda.add(new JLabel("Dia da Semana:"));
        String[] dias = {"Selecione o dia", "Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        comboDiaSemana = new JComboBox<>(dias);
        comboDiaSemana.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelEsquerda.add(comboDiaSemana);
        
        painelEsquerda.add(Box.createRigidArea(new Dimension(0, 15)));
        
        painelEsquerda.add(new JLabel("Horário:"));
        String[] horarios = {"--:--", "06:30", "07:30", "17:00", "17:30", "19:00"};
        comboHorario = new JComboBox<>(horarios);
        comboHorario.setEditable(true);
        comboHorario.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelEsquerda.add(comboHorario);

        painel.add(painelEsquerda, gbc);

        // --- Painel Direito: Quantidade por Função ---
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 5);

        JPanel painelDireita = new JPanel(new GridLayout(0, 4, 15, 10));
        painelDireita.setBorder(BorderFactory.createTitledBorder("Quantidades"));
        
        this.todosOsMembros = gerenciadorMembros.carregarMembros("membros.txt");
        spinnersDeFuncao = new LinkedHashMap<>();
        
        Set<String> funcoesSet = new HashSet<>();
        if (this.todosOsMembros != null) {
            for (Pessoa p : this.todosOsMembros) funcoesSet.addAll(p.funcoes);
        }
        List<String> funcoesUnicasOrdenadas = new ArrayList<>(funcoesSet);
        funcoesUnicasOrdenadas.sort(getFuncaoComparator());
        
        for (String funcao : funcoesUnicasOrdenadas) {
            painelDireita.add(new JLabel(funcao + ":"));
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
            spinnersDeFuncao.put(funcao, spinner);
            painelDireita.add(spinner);
        }
        
        painel.add(painelDireita, gbc);
        
        return painel;
    }
    
    /**
     * Abre a janela de gerenciamento de membros e atualiza a UI principal ao fechar.
     */
    private void abrirGerenciadorMembros() {
        this.todosOsMembros = gerenciadorMembros.carregarMembros("membros.txt");
        GerenciadorMembrosUI gerenciadorUI = new GerenciadorMembrosUI(this, this.todosOsMembros);
        gerenciadorUI.setVisible(true); // A execução para aqui até a janela ser fechada
        
        // Quando a janela fechar, salva os membros e atualiza a UI
        gerenciadorMembros.salvarMembros("membros.txt", this.todosOsMembros);
        JOptionPane.showMessageDialog(this, "Lista de membros salva!");
        
        // Recria o painel de entrada para refletir novas funções
        this.painelDefinirEvento = montarPainelDeEntrada();
        splitPane.setTopComponent(this.painelDefinirEvento);
        revalidate();
        repaint();
    }
    
    /**
     * Ponto de partida para a geração da escala.
     * Coleta os dados da UI, chama o GeradorDeEscala e atualiza os resultados.
     */
    private void gerarEscalaCustomizada() {
        this.todosOsMembros = gerenciadorMembros.carregarMembros("membros.txt");
        String diaSelecionado = (String) comboDiaSemana.getSelectedItem();
        String horario = (String) comboHorario.getSelectedItem();
        
        Map<String, Integer> necessidades = new LinkedHashMap<>();
        List<String> funcoesOrdenadasDaUI = new ArrayList<>(spinnersDeFuncao.keySet());
        funcoesOrdenadasDaUI.sort(getFuncaoComparator());
        
        for (String funcao : funcoesOrdenadasDaUI) {
            int qtd = (int) spinnersDeFuncao.get(funcao).getValue();
            if (qtd > 0) necessidades.put(funcao, qtd);
        }
        
        if (necessidades.isEmpty() || "Selecione o dia".equals(diaSelecionado) || "--:--".equals(horario) || horario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione dia, horário e pelo menos uma função.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Evento evento = new Evento("Culto de " + diaSelecionado, diaSelecionado, horario, necessidades);
        
        GeradorDeEscala gerador = new GeradorDeEscala();
        evento = gerador.gerar(evento, todosOsMembros);

        // REGRA D: Incrementa o contador para os escolhidos na geração automática.
        Set<Pessoa> pessoasEscaladas = new HashSet<>();
        for(List<Pessoa> lista : evento.escalaPronta.values()){
            pessoasEscaladas.addAll(lista);
        }
        for(Pessoa p : pessoasEscaladas){
            p.vezesEscalado++;
        }
        
        List<Pessoa> candidatosGerais = gerador.getCandidatosGerais();
        atualizarPainelDeResultados(evento, candidatosGerais);
    }

    /**
     * Desenha o painel de resultados com os JComboBoxes para cada vaga.
     */
    private void atualizarPainelDeResultados(Evento evento, List<Pessoa> candidatosGerais) {
        painelDeResultados.removeAll();
        this.todosOsComboBoxes = new HashMap<>();
        this.todosOsCandidatosPorFuncao = new HashMap<>();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 2, 5);
        
        List<String> funcoesOrdenadas = new ArrayList<>(evento.necessidades.keySet());
        funcoesOrdenadas.sort(getFuncaoComparator());

        for (String funcao : funcoesOrdenadas) {
            int quantidadeNecessaria = evento.necessidades.get(funcao);

            gbc.gridwidth = GridBagConstraints.REMAINDER; gbc.gridx = 0;
            painelDeResultados.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
            gbc.gridy++;
            
            JLabel tituloFuncao = new JLabel(">> " + funcao);
            tituloFuncao.setFont(new Font("SansSerif", Font.BOLD, 14));
            painelDeResultados.add(tituloFuncao, gbc);
            gbc.gridy++;
            
            JPanel painelDeVagas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            
            List<Pessoa> opcoesParaFuncao = candidatosGerais.stream()
                .filter(p -> p.funcoes.contains(funcao))
                .collect(Collectors.toList());
            todosOsCandidatosPorFuncao.put(funcao, opcoesParaFuncao);

            List<Pessoa> escalados = evento.escalaPronta.getOrDefault(funcao, new ArrayList<>());

            for (int i = 0; i < quantidadeNecessaria; i++) {
                JComboBox<Pessoa> comboBox = new JComboBox<>();
                comboBox.setRenderer(new ComboBoxRenderer());

                DefaultComboBoxModel<Pessoa> model = new DefaultComboBoxModel<>();
                model.addElement(null);
                opcoesParaFuncao.forEach(model::addElement);
                comboBox.setModel(model);

                if (i < escalados.size()) {
                    comboBox.setSelectedItem(escalados.get(i));
                }
                
                this.todosOsComboBoxes.computeIfAbsent(funcao, k -> new ArrayList<>()).add(comboBox);
                
                JPanel painelCombo = new JPanel(new BorderLayout(5, 0));
                painelCombo.add(new JLabel("Vaga " + (i + 1) + ":"), BorderLayout.WEST);
                painelCombo.add(comboBox, BorderLayout.CENTER);
                painelDeVagas.add(painelCombo);
            }
            painelDeResultados.add(painelDeVagas, gbc);
            gbc.gridy++;
        }
        
        gbc.gridy++; gbc.weighty = 1.0;
        painelDeResultados.add(new JLabel(""), gbc);

        // Adiciona os listeners que chamarão a atualização das regras a cada mudança
        for (List<JComboBox<Pessoa>> combos : this.todosOsComboBoxes.values()) {
            for (JComboBox<Pessoa> combo : combos) {
                // Cada vez que uma seleção mudar, o método de atualização é chamado.
                combo.addActionListener(e -> atualizarOpcoesDosCombos());
            }
        }
        
        // Roda a atualização pela primeira vez para definir o estado inicial correto.
        atualizarOpcoesDosCombos();

        painelDeResultados.revalidate();
        painelDeResultados.repaint();
    }

    /**
     * REGRA F: ATUALIZAÇÃO DINÂMICA DE DISPONIBILIDADE
     * Este método é o núcleo da lógica de edição manual da escala.
     * Ele é chamado sempre que uma seleção em qualquer JComboBox é alterada.
     * Sua estratégia é:
     * 1. Mapear todas as seleções atuais (quem está escalado e onde).
     * 2. Iterar sobre CADA JComboBox da tela.
     * 3. Para cada JComboBox, reconstruir sua lista de opções do zero, verificando
     * para cada membro se ele pode ser alocado naquela função, dadas as
     * outras seleções já feitas.
     * Isso garante que, se um membro é selecionado, ele some das listas incompatíveis,
     * e se ele é removido, ele reapareça nas listas onde agora é elegível.
     */
    private void atualizarOpcoesDosCombos() {
        if (todosOsComboBoxes == null || todosOsComboBoxes.isEmpty()) return;

        // 1. Mapeia quem está atualmente selecionado e em quais funções.
        Map<Pessoa, List<String>> selecoesGlobais = new HashMap<>();
        for (Map.Entry<String, List<JComboBox<Pessoa>>> entry : todosOsComboBoxes.entrySet()) {
            String funcao = entry.getKey();
            for (JComboBox<Pessoa> combo : entry.getValue()) {
                Pessoa selecionada = (Pessoa) combo.getSelectedItem();
                if (selecionada != null) {
                    selecoesGlobais.computeIfAbsent(selecionada, k -> new ArrayList<>()).add(funcao);
                }
            }
        }
        
        List<String> instrumentos = List.of("Violão", "Teclado", "Baixo", "Guitarra", "Bateria");

        // 2. Itera sobre cada combo box para revalidar suas opções.
        for (Map.Entry<String, List<JComboBox<Pessoa>>> entry : todosOsComboBoxes.entrySet()) {
            String funcaoAtualDoCombo = entry.getKey();
            List<Pessoa> todasAsOpcoesParaFuncao = this.todosOsCandidatosPorFuncao.get(funcaoAtualDoCombo);

            for (JComboBox<Pessoa> comboAlvo : entry.getValue()) {
                Pessoa selecaoAtualDoCombo = (Pessoa) comboAlvo.getSelectedItem();

                ActionListener[] listeners = comboAlvo.getActionListeners();
                for (ActionListener l : listeners) comboAlvo.removeActionListener(l);

                // 3. Reconstrói a lista de opções para este JComboBox.
                DefaultComboBoxModel<Pessoa> model = new DefaultComboBoxModel<>();
                model.addElement(null); // Opção para deixar vago

                if (todasAsOpcoesParaFuncao != null) {
                    for (Pessoa pessoaCandidata : todasAsOpcoesParaFuncao) {
                        // A pessoa que já está neste combo sempre é uma opção válida para si mesma.
                        if (pessoaCandidata.equals(selecaoAtualDoCombo)) {
                            model.addElement(pessoaCandidata);
                            continue; 
                        }

                        List<String> funcoesJaEscaladas = selecoesGlobais.get(pessoaCandidata);

                        // Se a pessoa não foi escalada em nenhum outro lugar, ela é elegível.
                        if (funcoesJaEscaladas == null || funcoesJaEscaladas.isEmpty()) {
                            model.addElement(pessoaCandidata);
                            continue;
                        }

                        // REGRA F.3 (e Regra A): Se não canta e toca, e já está escalado, não pode ser escalado de novo.
                        if (!pessoaCandidata.cantaEToca) {
                            continue; // BLOQUEADO: Pessoa já está em uma função e não pode acumular.
                        }
                        
                        // Daqui pra baixo, a pessoa PODE cantar e tocar. Vamos verificar as outras regras.
                        boolean candidatoJaTocaInstrumento = funcoesJaEscaladas.stream().anyMatch(instrumentos::contains);
                        boolean funcaoAlvoEhInstrumento = instrumentos.contains(funcaoAtualDoCombo);

                        // REGRA F.1 (e Regra B): Não pode tocar dois instrumentos.
                        if (candidatoJaTocaInstrumento && funcaoAlvoEhInstrumento) {
                            continue; // BLOQUEADO: Já toca um instrumento, não pode tocar outro.
                        }
                        
                        // REGRA GERAL DE EXCLUSIVIDADE: Permite apenas a combinação "Cantor" + 1 Instrumento.
                        boolean jaEhCantor = funcoesJaEscaladas.contains("Cantor");
                        boolean funcaoAlvoEhCantor = funcaoAtualDoCombo.equals("Cantor");

                        // Se a pessoa já está em duas funções (o máximo), bloqueia.
                        if (funcoesJaEscaladas.size() >= 2) {
                             continue; // BLOQUEADO: Já está no limite de 2 funções.
                        }
                        
                        // Bloqueia qualquer combinação que não seja Cantor + Instrumento.
                        if ( (jaEhCantor && !funcaoAlvoEhInstrumento) || (candidatoJaTocaInstrumento && !funcaoAlvoEhCantor) ){
                           continue; // BLOQUEADO: Tentativa de acumular funções incompatíveis.
                        }

                        // Se passou por todas as regras, é uma opção válida.
                        model.addElement(pessoaCandidata);
                    }
                }

                comboAlvo.setModel(model);
                comboAlvo.setSelectedItem(selecaoAtualDoCombo);

                for (ActionListener l : listeners) comboAlvo.addActionListener(l);
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Falha ao inicializar o Look and Feel.");
        }

        File f = new File("membros.txt");
        if (!f.exists()) {
            JOptionPane.showMessageDialog(null, 
                "ERRO CRÍTICO: O arquivo 'membros.txt' não foi encontrado.\n\n" +
                "Por favor, certifique-se que o arquivo 'membros.txt' está na mesma pasta que o programa.", 
                "Arquivo Não Encontrado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        SwingUtilities.invokeLater(() -> new EscaladorUI().setVisible(true));
    }
}
