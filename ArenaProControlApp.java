import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ArenaProControlApp extends JFrame {

    // Paleta de Cores - Tema Esportivo Noturno
    private static final Color BG_DARK = new Color(15, 19, 24);
    private static final Color BG_DARKER = new Color(9, 12, 16);
    private static final Color BG_CARD = new Color(22, 28, 36);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color ACCENT_GOLD = new Color(245, 158, 11);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color TEXT_PRIMARY = new Color(243, 244, 246);
    private static final Color TEXT_SECONDARY = new Color(156, 163, 175);
    private static final Color BORDER_COLOR = new Color(38, 48, 61);

    // Navegação
    private CardLayout cardLayout;
    private JPanel mainContent;

    // Estado da Partida & Cronômetro
    private Timer matchTimer;
    private int segundosTotais = 0;
    private boolean rodando = false;
    private int acrescimos = 0;

    private int placarA = 0, placarB = 0;
    private int faltasA = 0, faltasB = 0;
    private int escanteiosA = 0, escanteiosB = 0;

    // Posse de Bola
    private boolean posseComA = true;
    private int segPosseA = 1, segPosseB = 1;

    // Scout
    private int chutesA = 0, chutesB = 0;
    private int chutesGolA = 0, chutesGolB = 0;
    private int defesasA = 0, defesasB = 0;
    private int impedimentosA = 0, impedimentosB = 0;

    // Componentes UI - Placar e Relógio
    private JTextField txtTorneio, txtTimeA, txtTimeB;
    private JComboBox<Integer> cbDuracao;
    private JComboBox<String> cbPeriodo;
    private JLabel lblPlacarA, lblPlacarB, lblTimer, lblAcrescimo, lblBadgePeriodo;
    private JLabel lblFaltasA, lblFaltasB, lblEscanteiosA, lblEscanteiosB;
    private JButton btnStartPause;
    private JTextArea txtEventLog;
    private final List<String> eventosPartida = new ArrayList<>();

    // Componentes UI - Elencos
    private final List<Jogador> elencoA = new ArrayList<>();
    private final List<Jogador> elencoB = new ArrayList<>();
    private DefaultTableModel modelElencoA, modelElencoB;
    private JTable tableElencoA, tableElencoB;
    private JLabel lblCartoesA, lblCartoesB;

    // Componentes UI - Scout & Posse
    private JProgressBar barPosse;
    private JLabel lblPosseA, lblPosseB;
    private JButton btnPosseA, btnPosseB;
    private JLabel lblChutesA, lblChutesB, lblChutesGolA, lblChutesGolB;
    private JLabel lblDefesasA, lblDefesasB, lblImpedA, lblImpedB;

    // Componentes UI - Prancheta & Súmula
    private SoccerPitchPanel pitchPanel;
    private JComboBox<String> cbFormacao;
    private JTextField txtArbitro, txtLocal;
    private JTextArea txtObsSumula, txtSumulaOutput;

    public ArenaProControlApp() {
        setTitle("ARENA PRO CONTROL | Gestão de Partidas, Cronômetro & Tática");
        setSize(1220, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);

        inicializarElencosPadrao();
        configurarTimer();

        add(createHeader(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(BG_DARK);

        mainContent.add(createPlacarPanel(), "PLACAR");
        mainContent.add(createElencoPanel(), "ELENCO");
        mainContent.add(createScoutPanel(), "SCOUT");
        mainContent.add(createPranchetaPanel(), "PRANCHETA");
        mainContent.add(createSumulaPanel(), "SUMULA");

        add(mainContent, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private void inicializarElencosPadrao() {
        elencoA.add(new Jogador(1, "Alisson", "GOL"));
        elencoA.add(new Jogador(4, "Marquinhos", "ZAG"));
        elencoA.add(new Jogador(8, "Bruno Guimarães", "MEI"));
        elencoA.add(new Jogador(10, "Vinícius Jr", "ATA"));
        elencoA.add(new Jogador(9, "Endrick", "ATA"));

        elencoB.add(new Jogador(1, "Courtois", "GOL"));
        elencoB.add(new Jogador(2, "Rüdiger", "ZAG"));
        elencoB.add(new Jogador(5, "Bellingham", "MEI"));
        elencoB.add(new Jogador(7, "Mbappé", "ATA"));
        elencoB.add(new Jogador(11, "Rodrygo", "ATA"));
    }

    private void configurarTimer() {
        matchTimer = new Timer(1000, e -> {
            segundosTotais++;
            lblTimer.setText(formatarTempo(segundosTotais));

            if (posseComA) segPosseA++;
            else segPosseB++;
            atualizarBarraPosse();

            int limiteSeg = (Integer) cbDuracao.getSelectedItem() * 60;
            if (segundosTotais >= limiteSeg) {
                lblTimer.setForeground(ACCENT_GOLD);
            } else {
                lblTimer.setForeground(ACCENT_GREEN);
            }
        });
    }

    // ==================== HEADER ====================
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARKER);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 2, 0, ACCENT_GREEN),
                new EmptyBorder(14, 28, 14, 28)
        ));

        JLabel logo = new JLabel("⚽ ARENA PRO CONTROL");
        logo.setFont(new Font("SansSerif", Font.BOLD, 21));
        logo.setForeground(TEXT_PRIMARY);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        nav.setOpaque(false);

        nav.add(createNavBtn("PLACAR & TEMPO", "PLACAR"));
        nav.add(createNavBtn("ELENCO & CARTÕES", "ELENCO"));
        nav.add(createNavBtn("SCOUT & POSSE", "SCOUT"));
        nav.add(createNavBtn("PRANCHETA TÁTICA", "PRANCHETA"));

        JButton btnSumula = createNavBtn("GERAR SÚMULA", "SUMULA");
        btnSumula.setBackground(ACCENT_GREEN);
        btnSumula.setForeground(new Color(5, 27, 17));
        nav.add(btnSumula);

        header.add(logo, BorderLayout.WEST);
        header.add(nav, BorderLayout.EAST);
        return header;
    }

    private JButton createNavBtn(String text, String card) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(BG_CARD);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 15, 9, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> cardLayout.show(mainContent, card));
        return btn;
    }

    // ==================== ABA 1: PLACAR & TEMPO ====================
    private JPanel createPlacarPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(22, 35, 22, 35));

        // Barra superior de configuração do jogo
        JPanel settingsBar = new JPanel(new GridLayout(1, 3, 15, 0));
        settingsBar.setBackground(BG_CARD);
        settingsBar.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(12, 16, 12, 16)));

        txtTorneio = createInput("Copa Arena Pro 2026 - Quartas de Final");
        cbDuracao = new JComboBox<>(new Integer[]{10, 20, 25, 30, 45});
        cbDuracao.setSelectedItem(45);
        styleCombo(cbDuracao);

        cbPeriodo = new JComboBox<>(new String[]{"1º Tempo", "Intervalo", "2º Tempo", "Prorrogação 1ºT", "Prorrogação 2ºT", "Pênaltis"});
        styleCombo(cbPeriodo);
        cbPeriodo.addActionListener(e -> {
            String p = (String) cbPeriodo.getSelectedItem();
            lblBadgePeriodo.setText(p.toUpperCase());
            registrarEvento("🔄 Período alterado para: " + p);
        });

        settingsBar.add(wrapLabeled("Competição / Torneio:", txtTorneio));
        settingsBar.add(wrapLabeled("Duração por Tempo (min):", cbDuracao));
        settingsBar.add(wrapLabeled("Período Atual:", cbPeriodo));

        // Placar Central (3 colunas)
        JPanel boardGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        boardGrid.setOpaque(false);

        // Time A
        txtTimeA = createInput("ATLÉTICO REAL FC");
        txtTimeA.setHorizontalAlignment(JTextField.CENTER);
        txtTimeA.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblPlacarA = new JLabel("0", SwingConstants.CENTER);
        lblFaltasA = new JLabel("0");
        lblEscanteiosA = new JLabel("0");
        boardGrid.add(createTeamScoreCard(txtTimeA, lblPlacarA, lblFaltasA, lblEscanteiosA, true));

        // Relógio Central
        JPanel clockCard = new JPanel();
        clockCard.setLayout(new BoxLayout(clockCard, BoxLayout.Y_AXIS));
        clockCard.setBackground(BG_DARKER);
        clockCard.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR, 2), new EmptyBorder(20, 15, 20, 15)));

        lblBadgePeriodo = new JLabel("1º TEMPO");
        lblBadgePeriodo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblBadgePeriodo.setForeground(ACCENT_GOLD);
        lblBadgePeriodo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTimer = new JLabel("00:00");
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 56));
        lblTimer.setForeground(ACCENT_GREEN);
        lblTimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel acrescPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        acrescPanel.setOpaque(false);
        lblAcrescimo = new JLabel("Acréscimos: +0 min");
        lblAcrescimo.setForeground(ACCENT_GOLD);
        lblAcrescimo.setFont(new Font("SansSerif", Font.BOLD, 13));

        JButton btnAddAcr = createMiniBtn("+1");
        btnAddAcr.addActionListener(e -> {
            acrescimos++;
            lblAcrescimo.setText("Acréscimos: +" + acrescimos + " min");
            registrarEvento("⏱️ Arbitragem sinaliza +" + acrescimos + " min de acréscimo.");
        });
        JButton btnSubAcr = createMiniBtn("-1");
        btnSubAcr.addActionListener(e -> {
            acrescimos = Math.max(0, acrescimos - 1);
            lblAcrescimo.setText("Acréscimos: +" + acrescimos + " min");
        });

        acrescPanel.add(lblAcrescimo);
        acrescPanel.add(btnAddAcr);
        acrescPanel.add(btnSubAcr);

        JPanel timerBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        timerBtns.setOpaque(false);

        btnStartPause = new JButton("▶ INICIAR");
        btnStartPause.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnStartPause.setBackground(ACCENT_GREEN);
        btnStartPause.setForeground(new Color(5, 27, 17));
        btnStartPause.setFocusPainted(false);
        btnStartPause.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnStartPause.addActionListener(e -> toggleTimer());

        JButton btnReset = new JButton("↺ ZERAR");
        btnReset.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnReset.setBackground(BG_CARD);
        btnReset.setForeground(TEXT_PRIMARY);
        btnReset.setFocusPainted(false);
        btnReset.setBorder(new EmptyBorder(10, 16, 10, 16));
        btnReset.addActionListener(e -> resetarRelogio());

        timerBtns.add(btnStartPause);
        timerBtns.add(btnReset);

        // Velocidade do simulador
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        speedPanel.setOpaque(false);
        JLabel lblSpd = new JLabel("Velocidade:");
        lblSpd.setForeground(TEXT_SECONDARY);
        lblSpd.setFont(new Font("SansSerif", Font.PLAIN, 11));
        speedPanel.add(lblSpd);
        speedPanel.add(createSpeedBtn("1x (Real)", 1000));
        speedPanel.add(createSpeedBtn("10x (Teste)", 100));
        speedPanel.add(createSpeedBtn("50x (Rápido)", 20));

        clockCard.add(lblBadgePeriodo);
        clockCard.add(Box.createRigidArea(new Dimension(0, 8)));
        clockCard.add(lblTimer);
        clockCard.add(acrescPanel);
        clockCard.add(Box.createRigidArea(new Dimension(0, 10)));
        clockCard.add(timerBtns);
        clockCard.add(speedPanel);

        boardGrid.add(clockCard);

        // Time B
        txtTimeB = createInput("SPORTING UNITED");
        txtTimeB.setHorizontalAlignment(JTextField.CENTER);
        txtTimeB.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblPlacarB = new JLabel("0", SwingConstants.CENTER);
        lblFaltasB = new JLabel("0");
        lblEscanteiosB = new JLabel("0");
        boardGrid.add(createTeamScoreCard(txtTimeB, lblPlacarB, lblFaltasB, lblEscanteiosB, false));

        // Linha do tempo inferior
        JPanel logPanel = new JPanel(new BorderLayout(0, 6));
        logPanel.setBackground(BG_DARKER);
        logPanel.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(12, 16, 12, 16)));
        logPanel.setPreferredSize(new Dimension(0, 175));

        JLabel lblLogTitle = new JLabel("⏱️ LINHA DO TEMPO DA PARTIDA (AO VIVO)");
        lblLogTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblLogTitle.setForeground(ACCENT_GOLD);

        txtEventLog = new JTextArea("Aguardando o apito inicial para registrar eventos...");
        txtEventLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtEventLog.setBackground(BG_DARKER);
        txtEventLog.setForeground(TEXT_PRIMARY);
        txtEventLog.setEditable(false);

        logPanel.add(lblLogTitle, BorderLayout.NORTH);
        logPanel.add(new JScrollPane(txtEventLog), BorderLayout.CENTER);

        panel.add(settingsBar, BorderLayout.NORTH);
        panel.add(boardGrid, BorderLayout.CENTER);
        panel.add(logPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTeamScoreCard(JTextField txtName, JLabel lblScore, JLabel lblFaltas, JLabel lblEscanteios, boolean isTeamA) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(18, 18, 18, 18)));

        lblScore.setFont(new Font("Monospaced", Font.BOLD, 68));
        lblScore.setForeground(Color.WHITE);
        lblScore.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel goalBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        goalBtns.setOpaque(false);

        JButton btnGoal = new JButton("+ GOL");
        btnGoal.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGoal.setBackground(ACCENT_GREEN);
        btnGoal.setForeground(new Color(5, 27, 17));
        btnGoal.setFocusPainted(false);
        btnGoal.setBorder(new EmptyBorder(9, 20, 9, 20));
        btnGoal.addActionListener(e -> alterarGol(isTeamA, 1, null));

        JButton btnMinus = new JButton("-1");
        btnMinus.setBackground(BG_DARKER);
        btnMinus.setForeground(TEXT_PRIMARY);
        btnMinus.setFocusPainted(false);
        btnMinus.setBorder(new EmptyBorder(9, 14, 9, 14));
        btnMinus.addActionListener(e -> alterarGol(isTeamA, -1, null));

        goalBtns.add(btnGoal);
        goalBtns.add(btnMinus);

        // Faltas e Escanteios
        JPanel extraStats = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        extraStats.setOpaque(false);

        lblFaltas.setForeground(TEXT_PRIMARY);
        lblEscanteios.setForeground(TEXT_PRIMARY);

        JButton btnAddFalta = createMiniBtn("+");
        btnAddFalta.addActionListener(e -> {
            if (isTeamA) lblFaltas.setText(String.valueOf(++faltasA));
            else lblFaltas.setText(String.valueOf(++faltasB));
        });

        JButton btnAddEsc = createMiniBtn("+");
        btnAddEsc.addActionListener(e -> {
            if (isTeamA) lblEscanteios.setText(String.valueOf(++escanteiosA));
            else lblEscanteios.setText(String.valueOf(++escanteiosB));
        });

        JLabel tF = new JLabel("Faltas:");
        tF.setForeground(TEXT_SECONDARY);
        JLabel tE = new JLabel("Escanteios:");
        tE.setForeground(TEXT_SECONDARY);

        extraStats.add(tF);
        extraStats.add(lblFaltas);
        extraStats.add(btnAddFalta);
        extraStats.add(tE);
        extraStats.add(lblEscanteios);
        extraStats.add(btnAddEsc);

        card.add(txtName);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblScore);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(goalBtns);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(extraStats);

        return card;
    }

    private JButton createSpeedBtn(String label, int delayMs) {
        JButton b = new JButton(label);
        b.setFont(new Font("SansSerif", Font.PLAIN, 11));
        b.setBackground(BG_CARD);
        b.setForeground(TEXT_SECONDARY);
        b.setFocusPainted(false);
        b.addActionListener(e -> matchTimer.setDelay(delayMs));
        return b;
    }

    // ==================== ABA 2: ELENCO, CARTÕES & SUBSTITUIÇÕES ====================
    private JPanel createElencoPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 25, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(25, 35, 25, 35));

        lblCartoesA = new JLabel("🟨 0 | 🟥 0");
        lblCartoesB = new JLabel("🟨 0 | 🟥 0");

        modelElencoA = createRosterTableModel();
        tableElencoA = createStyledTable(modelElencoA);

        modelElencoB = createRosterTableModel();
        tableElencoB = createStyledTable(modelElencoB);

        panel.add(createSingleRosterCard(true, txtTimeA.getText(), lblCartoesA, modelElencoA, tableElencoA));
        panel.add(createSingleRosterCard(false, txtTimeB.getText(), lblCartoesB, modelElencoB, tableElencoB));

        atualizarTabelasElenco();
        return panel;
    }

    private JPanel createSingleRosterCard(boolean isTeamA, String titleText, JLabel lblCartoes, DefaultTableModel model, JTable table) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(18, 18, 18, 18)));

        // Topo
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(TEXT_PRIMARY);
        lblCartoes.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblCartoes.setForeground(ACCENT_GOLD);
        top.add(title, BorderLayout.WEST);
        top.add(lblCartoes, BorderLayout.EAST);

        // Formulário adicionar jogador + Botões de ação
        JPanel controlsBox = new JPanel(new GridLayout(2, 1, 0, 8));
        controlsBox.setOpaque(false);

        JPanel addRow = new JPanel(new BorderLayout(6, 0));
        addRow.setOpaque(false);
        JTextField txtNum = createInput("");
        txtNum.setPreferredSize(new Dimension(45, 30));
        txtNum.setToolTipText("Número");
        JTextField txtNome = createInput("");
        txtNome.setToolTipText("Nome do Atleta");
        JComboBox<String> cbPos = new JComboBox<>(new String[]{"GOL", "ZAG", "LAT", "MEI", "ATA"});
        styleCombo(cbPos);

        JButton btnAdd = new JButton("+ Add");
        btnAdd.setBackground(ACCENT_GREEN);
        btnAdd.setForeground(new Color(5, 27, 17));
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnAdd.addActionListener(e -> {
            try {
                int num = Integer.parseInt(txtNum.getText().trim());
                String nome = txtNome.getText().trim();
                if (nome.isEmpty()) return;
                List<Jogador> lista = isTeamA ? elencoA : elencoB;
                lista.add(new Jogador(num, nome, (String) cbPos.getSelectedItem()));
                txtNum.setText("");
                txtNome.setText("");
                atualizarTabelasElenco();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Informe um número de camisa válido!");
            }
        });

        JPanel leftInputs = new JPanel(new BorderLayout(6, 0));
        leftInputs.setOpaque(false);
        leftInputs.add(txtNum, BorderLayout.WEST);
        leftInputs.add(txtNome, BorderLayout.CENTER);

        JPanel rightInputs = new JPanel(new BorderLayout(6, 0));
        rightInputs.setOpaque(false);
        rightInputs.add(cbPos, BorderLayout.WEST);
        rightInputs.add(btnAdd, BorderLayout.EAST);

        addRow.add(leftInputs, BorderLayout.CENTER);
        addRow.add(rightInputs, BorderLayout.EAST);

        // Barra de ações sobre o jogador selecionado na tabela
        JPanel actionRow = new JPanel(new GridLayout(1, 4, 6, 0));
        actionRow.setOpaque(false);

        JButton btnGolJog = createActionBtn("⚽ Gol", ACCENT_GREEN, new Color(5, 27, 17));
        JButton btnAmarelo = createActionBtn("🟨 Amarelo", ACCENT_GOLD, Color.BLACK);
        JButton btnVermelho = createActionBtn("🟥 Vermelho", ACCENT_RED, Color.WHITE);
        JButton btnSub = createActionBtn("🔄 Substituir", BG_DARKER, TEXT_PRIMARY);

        btnGolJog.addActionListener(e -> executarAcaoJogador(isTeamA, table, "GOL"));
        btnAmarelo.addActionListener(e -> executarAcaoJogador(isTeamA, table, "AMARELO"));
        btnVermelho.addActionListener(e -> executarAcaoJogador(isTeamA, table, "VERMELHO"));
        btnSub.addActionListener(e -> executarAcaoJogador(isTeamA, table, "SUB"));

        actionRow.add(btnGolJog);
        actionRow.add(btnAmarelo);
        actionRow.add(btnVermelho);
        actionRow.add(btnSub);

        controlsBox.add(addRow);
        controlsBox.add(actionRow);

        JPanel northWrapper = new JPanel(new BorderLayout(0, 10));
        northWrapper.setOpaque(false);
        northWrapper.add(top, BorderLayout.NORTH);
        northWrapper.add(controlsBox, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_DARKER);
        scroll.setBorder(new LineBorder(BORDER_COLOR));

        card.add(northWrapper, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private void executarAcaoJogador(boolean isTeamA, JTable table, String acao) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um jogador na tabela primeiro!");
            return;
        }
        List<Jogador> lista = isTeamA ? elencoA : elencoB;
        Jogador j = lista.get(row);
        String nomeTime = isTeamA ? txtTimeA.getText() : txtTimeB.getText();

        if (j.vermelho && !acao.equals("SUB")) {
            JOptionPane.showMessageDialog(this, "Este jogador já foi expulso!");
            return;
        }

        switch (acao) {
            case "GOL":
                j.gols++;
                alterarGol(isTeamA, 1, "#" + j.num + " " + j.nome);
                break;
            case "AMARELO":
                j.amarelo++;
                if (j.amarelo >= 2) {
                    j.vermelho = true;
                    j.emCampo = false;
                    registrarEvento("🟨🟥 EXPULSÃO (2º Amarelo)! #" + j.num + " " + j.nome + " (" + nomeTime + ")");
                } else {
                    registrarEvento("🟨 Cartão Amarelo para #" + j.num + " " + j.nome + " (" + nomeTime + ")");
                }
                break;
            case "VERMELHO":
                j.vermelho = true;
                j.emCampo = false;
                registrarEvento("🟥 CARTÃO VERMELHO DIRETO! #" + j.num + " " + j.nome + " (" + nomeTime + ")");
                break;
            case "SUB":
                j.emCampo = !j.emCampo;
                registrarEvento("🔄 Substituição (" + nomeTime + "): #" + j.num + " " + j.nome + (j.emCampo ? " ENTROU" : " SAIU"));
                break;
        }
        atualizarTabelasElenco();
    }

    private void atualizarTabelasElenco() {
        preencherModelo(elencoA, modelElencoA, lblCartoesA);
        preencherModelo(elencoB, modelElencoB, lblCartoesB);
    }

    private void preencherModelo(List<Jogador> lista, DefaultTableModel model, JLabel lblCartoes) {
        model.setRowCount(0);
        int amarelos = 0, vermelhos = 0;
        for (Jogador j : lista) {
            amarelos += j.amarelo;
            if (j.vermelho) vermelhos++;
            String status = j.vermelho ? "EXPULSO" : (j.emCampo ? "Em Campo" : "Banco");
            String cartoes = (j.amarelo > 0 ? "🟨".repeat(j.amarelo) : "") + (j.vermelho ? "🟥" : "");
            model.addRow(new Object[]{"#" + j.num, j.nome, j.pos, j.gols, cartoes, status});
        }
        lblCartoes.setText("🟨 " + amarelos + " | 🟥 " + vermelhos);
    }

    private DefaultTableModel createRosterTableModel() {
        return new DefaultTableModel(new String[]{"Nº", "Atleta", "Pos", "Gols", "Cartões", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setBackground(BG_DARKER);
        t.setForeground(TEXT_PRIMARY);
        t.setRowHeight(32);
        t.setGridColor(BORDER_COLOR);
        t.setSelectionBackground(ACCENT_BLUE);
        t.setSelectionForeground(Color.WHITE);
        t.getTableHeader().setBackground(BG_DARK);
        t.getTableHeader().setForeground(ACCENT_GOLD);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        return t;
    }

    // ==================== ABA 3: SCOUT & POSSE DE BOLA ====================
    private JPanel createScoutPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(30, 60, 30, 60));

        // Card Posse de Bola
        JPanel posseCard = new JPanel(new BorderLayout(0, 12));
        posseCard.setBackground(BG_CARD);
        posseCard.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(20, 25, 20, 25)));

        JPanel posseLabels = new JPanel(new BorderLayout());
        posseLabels.setOpaque(false);
        lblPosseA = new JLabel("Mandante: 50%");
        lblPosseA.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPosseA.setForeground(ACCENT_GREEN);

        JLabel lblCenter = new JLabel("POSSE DE BOLA EM TEMPO REAL", SwingConstants.CENTER);
        lblCenter.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblCenter.setForeground(TEXT_PRIMARY);

        lblPosseB = new JLabel("Visitante: 50%");
        lblPosseB.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPosseB.setForeground(ACCENT_BLUE);

        posseLabels.add(lblPosseA, BorderLayout.WEST);
        posseLabels.add(lblCenter, BorderLayout.CENTER);
        posseLabels.add(lblPosseB, BorderLayout.EAST);

        barPosse = new JProgressBar(0, 100);
        barPosse.setValue(50);
        barPosse.setForeground(ACCENT_GREEN);
        barPosse.setBackground(ACCENT_BLUE);
        barPosse.setPreferredSize(new Dimension(0, 20));
        barPosse.setBorderPainted(false);

        JPanel posseBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        posseBtns.setOpaque(false);
        btnPosseA = new JButton("⚽ Posse com Mandante");
        btnPosseB = new JButton("⚽ Posse com Visitante");
        stylePosseButtons();

        btnPosseA.addActionListener(e -> { posseComA = true; stylePosseButtons(); });
        btnPosseB.addActionListener(e -> { posseComA = false; stylePosseButtons(); });

        posseBtns.add(btnPosseA);
        posseBtns.add(btnPosseB);

        posseCard.add(posseLabels, BorderLayout.NORTH);
        posseCard.add(barPosse, BorderLayout.CENTER);
        posseCard.add(posseBtns, BorderLayout.SOUTH);

        // Grid de Estatísticas Comparativas
        JPanel scoutGrid = new JPanel(new GridLayout(4, 1, 0, 14));
        scoutGrid.setOpaque(false);

        lblChutesA = new JLabel("0"); lblChutesB = new JLabel("0");
        lblChutesGolA = new JLabel("0"); lblChutesGolB = new JLabel("0");
        lblDefesasA = new JLabel("0"); lblDefesasB = new JLabel("0");
        lblImpedA = new JLabel("0"); lblImpedB = new JLabel("0");

        scoutGrid.add(createScoutRow("Finalizações Totais", lblChutesA, lblChutesB, () -> chutesA++, () -> chutesA = Math.max(0, chutesA - 1), () -> chutesB++, () -> chutesB = Math.max(0, chutesB - 1)));
        scoutGrid.add(createScoutRow("Chutes no Alvo (Gol)", lblChutesGolA, lblChutesGolB, () -> chutesGolA++, () -> chutesGolA = Math.max(0, chutesGolA - 1), () -> chutesGolB++, () -> chutesGolB = Math.max(0, chutesGolB - 1)));
        scoutGrid.add(createScoutRow("Defesas do Goleiro", lblDefesasA, lblDefesasB, () -> defesasA++, () -> defesasA = Math.max(0, defesasA - 1), () -> defesasB++, () -> defesasB = Math.max(0, defesasB - 1)));
        scoutGrid.add(createScoutRow("Impedimentos", lblImpedA, lblImpedB, () -> impedimentosA++, () -> impedimentosA = Math.max(0, impedimentosA - 1), () -> impedimentosB++, () -> impedimentosB = Math.max(0, impedimentosB - 1)));

        panel.add(posseCard, BorderLayout.NORTH);
        panel.add(scoutGrid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createScoutRow(String title, JLabel lblA, JLabel lblB, Runnable incA, Runnable decA, Runnable incB, Runnable decB) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG_CARD);
        row.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(12, 25, 12, 25)));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setForeground(TEXT_PRIMARY);

        row.add(createCounterBox(lblA, incA, decA), BorderLayout.WEST);
        row.add(lblTitle, BorderLayout.CENTER);
        row.add(createCounterBox(lblB, incB, decB), BorderLayout.EAST);
        return row;
    }

    private JPanel createCounterBox(JLabel valLabel, Runnable onInc, Runnable onDec) {
        JPanel box = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        box.setOpaque(false);
        valLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        valLabel.setForeground(ACCENT_GOLD);

        JButton bMinus = createMiniBtn("-");
        bMinus.addActionListener(e -> {
            onDec.run();
            valLabel.setText(String.valueOf(Math.max(0, Integer.parseInt(valLabel.getText()) - 1)));
        });

        JButton bPlus = createMiniBtn("+");
        bPlus.addActionListener(e -> {
            onInc.run();
            valLabel.setText(String.valueOf(Integer.parseInt(valLabel.getText()) + 1));
        });

        box.add(bMinus);
        box.add(valLabel);
        box.add(bPlus);
        return box;
    }

    private void stylePosseButtons() {
        btnPosseA.setBackground(posseComA ? ACCENT_GREEN : BG_DARKER);
        btnPosseA.setForeground(posseComA ? new Color(5, 27, 17) : TEXT_SECONDARY);
        btnPosseB.setBackground(!posseComA ? ACCENT_BLUE : BG_DARKER);
        btnPosseB.setForeground(!posseComA ? Color.WHITE : TEXT_SECONDARY);
    }

    private void atualizarBarraPosse() {
        int total = segPosseA + segPosseB;
        int pctA = (int) Math.round((segPosseA * 100.0) / total);
        int pctB = 100 - pctA;
        barPosse.setValue(pctA);
        lblPosseA.setText(txtTimeA.getText() + ": " + pctA + "%");
        lblPosseB.setText(txtTimeB.getText() + ": " + pctB + "%");
    }

    // ==================== ABA 4: PRANCHETA TÁTICA ====================
    private JPanel createPranchetaPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topBar.setOpaque(false);

        JLabel lbl = new JLabel("Formação Tática:");
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        cbFormacao = new JComboBox<>(new String[]{"4-3-3", "4-4-2", "3-5-2", "4-2-3-1"});
        styleCombo(cbFormacao);
        cbFormacao.addActionListener(e -> pitchPanel.setFormacao((String) cbFormacao.getSelectedItem()));

        JLabel hint = new JLabel("💡 Dica: Clique em qualquer ponto do gramado para movimentar a bola!");
        hint.setForeground(ACCENT_GOLD);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 13));

        topBar.add(lbl);
        topBar.add(cbFormacao);
        topBar.add(hint);

        pitchPanel = new SoccerPitchPanel();

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(pitchPanel, BorderLayout.CENTER);
        return panel;
    }

    // ==================== ABA 5: SÚMULA ELETRÔNICA ====================
    private JPanel createSumulaPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 25, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(25, 40, 25, 40));

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(BG_CARD);
        formCard.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(25, 25, 25, 25)));

        JLabel title = new JLabel("DADOS OFICIAIS DA ARBITRAGEM");
        title.setFont(new Font("SansSerif", Font.BOLD, 17));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtArbitro = createInput("Anderson Daronco");
        txtLocal = createInput("Arena Pro - Campo Principal");
        txtObsSumula = new JTextArea("Partida transcorreu dentro da normalidade disciplinar.");
        txtObsSumula.setBackground(BG_DARKER);
        txtObsSumula.setForeground(TEXT_PRIMARY);
        txtObsSumula.setCaretColor(ACCENT_GREEN);
        txtObsSumula.setLineWrap(true);
        txtObsSumula.setWrapStyleWord(true);
        txtObsSumula.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(8, 10, 8, 10)));

        JButton btnGerar = new JButton("📋 COMPILAR SÚMULA OFICIAL");
        btnGerar.setBackground(ACCENT_GREEN);
        btnGerar.setForeground(new Color(5, 27, 17));
        btnGerar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGerar.setFocusPainted(false);
        btnGerar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGerar.addActionListener(e -> gerarSumula());

        formCard.add(title);
        formCard.add(Box.createRigidArea(new Dimension(0, 18)));
        formCard.add(wrapLabeled("Árbitro Principal:", txtArbitro));
        formCard.add(Box.createRigidArea(new Dimension(0, 12)));
        formCard.add(wrapLabeled("Local / Estádio / Quadra:", txtLocal));
        formCard.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel lblObs = new JLabel("Observações Disciplinares / Relatório:");
        lblObs.setForeground(TEXT_SECONDARY);
        lblObs.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblObs.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lblObs);
        formCard.add(Box.createRigidArea(new Dimension(0, 5)));
        formCard.add(new JScrollPane(txtObsSumula));
        formCard.add(Box.createRigidArea(new Dimension(0, 18)));
        formCard.add(btnGerar);

        // Terminal de saída
        JPanel outCard = new JPanel(new BorderLayout(0, 10));
        outCard.setBackground(BG_CARD);
        outCard.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(20, 20, 20, 20)));

        JLabel outTitle = new JLabel("DOCUMENTO OFICIAL DE PARTIDA");
        outTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        outTitle.setForeground(ACCENT_GOLD);

        txtSumulaOutput = new JTextArea(
                "====================================================\n" +
                "         ARENA PRO - SÚMULA DIGITAL OFICIAL\n" +
                "====================================================\n\n" +
                "Clique em \"COMPILAR SÚMULA OFICIAL\" ao lado para\n" +
                "consolidar o placar, tempo jogado, cartões,\n" +
                "artilheiros e estatísticas completas da partida."
        );
        txtSumulaOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtSumulaOutput.setBackground(BG_DARKER);
        txtSumulaOutput.setForeground(ACCENT_GREEN);
        txtSumulaOutput.setEditable(false);
        txtSumulaOutput.setBorder(new EmptyBorder(15, 15, 15, 15));

        outCard.add(outTitle, BorderLayout.NORTH);
        outCard.add(new JScrollPane(txtSumulaOutput), BorderLayout.CENTER);

        panel.add(formCard);
        panel.add(outCard);
        return panel;
    }

    private void gerarSumula() {
        int totalPosse = segPosseA + segPosseB;
        int pctA = (int) Math.round((segPosseA * 100.0) / totalPosse);
        int pctB = 100 - pctA;

        StringBuilder hist = new StringBuilder();
        if (eventosPartida.isEmpty()) {
            hist.append("Nenhum evento registrado.\n");
        } else {
            for (int i = eventosPartida.size() - 1; i >= 0; i--) {
                hist.append(eventosPartida.get(i)).append("\n");
            }
        }

        String sumula =
                "====================================================\n" +
                "         ARENA PRO - SÚMULA DIGITAL OFICIAL\n" +
                "====================================================\n" +
                "COMPETIÇÃO:   " + txtTorneio.getText() + "\n" +
                "LOCAL:        " + txtLocal.getText() + "\n" +
                "ÁRBITRO:      " + txtArbitro.getText() + "\n" +
                "TEMPO JOGADO: " + formatarTempo(segundosTotais) + " (+" + acrescimos + " min acréscimos)\n" +
                "----------------------------------------------------\n" +
                "PLACAR FINAL:\n" +
                String.format("  %-22s %d x %d   %s\n", txtTimeA.getText(), placarA, placarB, txtTimeB.getText()) +
                "----------------------------------------------------\n" +
                "ESTATÍSTICAS COMPARATIVAS:\n" +
                String.format("  Posse de Bola:      %d%%   |   %d%%\n", pctA, pctB) +
                String.format("  Finalizações:       %d     |   %d\n", chutesA, chutesB) +
                String.format("  Chutes no Gol:      %d     |   %d\n", chutesGolA, chutesGolB) +
                String.format("  Defesas Goleiro:    %d     |   %d\n", defesasA, defesasB) +
                String.format("  Escanteios:         %d     |   %d\n", escanteiosA, escanteiosB) +
                String.format("  Faltas Cometidas:   %d     |   %d\n", faltasA, faltasB) +
                String.format("  Impedimentos:       %d     |   %d\n", impedimentosA, impedimentosB) +
                "----------------------------------------------------\n" +
                "CRONOLOGIA DOS EVENTOS:\n" +
                hist +
                "----------------------------------------------------\n" +
                "RELATÓRIO DA ARBITRAGEM:\n" +
                txtObsSumula.getText() + "\n" +
                "====================================================\n" +
                "Documento autenticado pelo Sistema Arena Pro Control.";

        txtSumulaOutput.setText(sumula);
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private void toggleTimer() {
        if (rodando) {
            matchTimer.stop();
            rodando = false;
            btnStartPause.setText("▶ CONTINUAR");
            btnStartPause.setBackground(ACCENT_GREEN);
        } else {
            matchTimer.start();
            rodando = true;
            btnStartPause.setText("⏸ PAUSAR");
            btnStartPause.setBackground(ACCENT_GOLD);
            if (segundosTotais == 0 && eventosPartida.isEmpty()) {
                registrarEvento("📣 Apito inicial! Começa a partida.");
            }
        }
    }

    private void resetarRelogio() {
        matchTimer.stop();
        rodando = false;
        segundosTotais = 0;
        acrescimos = 0;
        lblTimer.setText("00:00");
        lblTimer.setForeground(ACCENT_GREEN);
        lblAcrescimo.setText("Acréscimos: +0 min");
        btnStartPause.setText("▶ INICIAR");
        btnStartPause.setBackground(ACCENT_GREEN);
    }

    private void alterarGol(boolean isTeamA, int delta, String nomeJogador) {
        if (isTeamA) {
            placarA = Math.max(0, placarA + delta);
            lblPlacarA.setText(String.valueOf(placarA));
        } else {
            placarB = Math.max(0, placarB + delta);
            lblPlacarB.setText(String.valueOf(placarB));
        }
        String time = isTeamA ? txtTimeA.getText() : txtTimeB.getText();
        int min = (segundosTotais / 60) + 1;
        if (delta > 0) {
            String autor = nomeJogador != null ? " (" + nomeJogador + ")" : "";
            registrarEvento("⚽ GOOOOOL! " + time + autor + " marca aos " + min + "'!");
        } else {
            registrarEvento("❌ Gol anulado (VAR / Arbitragem) para " + time + " aos " + min + "'.");
        }
    }

    private void registrarEvento(String msg) {
        int min = (segundosTotais / 60) + 1;
        String periodo = (String) cbPeriodo.getSelectedItem();
        eventosPartida.add(0, "[" + min + "' - " + periodo + "] " + msg);
        txtEventLog.setText(String.join("\n", eventosPartida));
        txtEventLog.setCaretPosition(0);
    }

    private String formatarTempo(int seg) {
        return String.format("%02d:%02d", seg / 60, seg % 60);
    }

    private JTextField createInput(String defaultText) {
        JTextField tf = new JTextField(defaultText);
        tf.setBackground(BG_DARKER);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT_GREEN);
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(7, 10, 7, 10)));
        return tf;
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setBackground(BG_DARKER);
        cb.setForeground(TEXT_PRIMARY);
    }

    private JPanel wrapLabeled(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(600, 58));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel l = new JLabel(label);
        l.setForeground(TEXT_SECONDARY);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JButton createMiniBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(BG_DARKER);
        b.setForeground(TEXT_PRIMARY);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(4, 9, 4, 9));
        return b;
    }

    private JButton createActionBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("SansSerif", Font.BOLD, 11));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(6, 6, 6, 6));
        return b;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_DARKER);
        footer.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, BORDER_COLOR), new EmptyBorder(8, 28, 8, 28)));
        JLabel l = new JLabel("© 2026 ARENA PRO CONTROL - Sistema Oficial de Gestão & Cronometragem Esportiva");
        l.setForeground(TEXT_SECONDARY);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.add(l, BorderLayout.WEST);
        return footer;
    }

    // ==================== CLASSES INTERNAS ====================
    static class Jogador {
        int num;
        String nome;
        String pos;
        int gols = 0;
        int amarelo = 0;
        boolean vermelho = false;
        boolean emCampo = true;

        Jogador(int num, String nome, String pos) {
            this.num = num;
            this.nome = nome;
            this.pos = pos;
        }
    }

    // Painel Gráfico Customizado da Prancheta Tática (Campo de Futebol)
    static class SoccerPitchPanel extends JPanel {
        private double ballX = 0.5, ballY = 0.5;
        private double[][] posicoes;
        private int[] numeros;

        SoccerPitchPanel() {
            setFormacao("4-3-3");
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ballX = (double) e.getX() / getWidth();
                    ballY = (double) e.getY() / getHeight();
                    repaint();
                }
            });
        }

        void setFormacao(String esquema) {
            switch (esquema) {
                case "4-4-2":
                    numeros = new int[]{1, 2, 3, 4, 6, 7, 5, 8, 11, 9, 10};
                    posicoes = new double[][]{{0.07,0.5},{0.24,0.15},{0.22,0.38},{0.22,0.62},{0.24,0.85},{0.50,0.18},{0.48,0.40},{0.48,0.60},{0.50,0.82},{0.76,0.36},{0.76,0.64}};
                    break;
                case "3-5-2":
                    numeros = new int[]{1, 3, 4, 5, 2, 8, 10, 7, 6, 9, 11};
                    posicoes = new double[][]{{0.07,0.5},{0.22,0.25},{0.20,0.50},{0.22,0.75},{0.48,0.12},{0.44,0.35},{0.55,0.50},{0.44,0.65},{0.48,0.88},{0.78,0.36},{0.78,0.64}};
                    break;
                case "4-2-3-1":
                    numeros = new int[]{1, 2, 3, 4, 6, 5, 8, 7, 10, 11, 9};
                    posicoes = new double[][]{{0.07,0.5},{0.24,0.15},{0.22,0.38},{0.22,0.62},{0.24,0.85},{0.40,0.36},{0.40,0.64},{0.60,0.20},{0.62,0.50},{0.60,0.80},{0.82,0.50}};
                    break;
                default: // 4-3-3
                    numeros = new int[]{1, 2, 3, 4, 6, 5, 8, 10, 7, 9, 11};
                    posicoes = new double[][]{{0.07,0.5},{0.25,0.15},{0.22,0.38},{0.22,0.62},{0.25,0.85},{0.42,0.50},{0.52,0.30},{0.52,0.70},{0.75,0.18},{0.82,0.50},{0.75,0.82}};
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Faixas do gramado
            int stripes = 10;
            int stripeW = w / stripes;
            for (int i = 0; i < stripes; i++) {
                g2.setColor(i % 2 == 0 ? new Color(21, 87, 52) : new Color(25, 102, 61));
                g2.fillRect(i * stripeW, 0, stripeW + 2, h);
            }

            // Linhas do campo
            g2.setColor(new Color(255, 255, 255, 190));
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(10, 10, w - 20, h - 20);
            g2.drawLine(w / 2, 10, w / 2, h - 10);
            g2.drawOval(w / 2 - 60, h / 2 - 60, 120, 120);
            g2.drawRect(10, (int) (h * 0.22), (int) (w * 0.14), (int) (h * 0.56));
            g2.drawRect(w - 10 - (int) (w * 0.14), (int) (h * 0.22), (int) (w * 0.14), (int) (h * 0.56));

            // Jogadores
            for (int i = 0; i < posicoes.length; i++) {
                int px = (int) (posicoes[i][0] * w);
                int py = (int) (posicoes[i][1] * h);

                g2.setColor(ACCENT_GOLD);
                g2.fillOval(px - 18, py - 18, 36, 36);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(px - 18, py - 18, 36, 36);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                String numStr = String.valueOf(numeros[i]);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(numStr, px - fm.stringWidth(numStr) / 2, py + 5);
            }

            // Bola
            int bx = (int) (ballX * w);
            int by = (int) (ballY * h);
            g2.setColor(Color.WHITE);
            g2.fillOval(bx - 10, by - 10, 20, 20);
            g2.setColor(Color.BLACK);
            g2.drawOval(bx - 10, by - 10, 20, 20);
        }
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new ArenaProControlApp().setVisible(true));
    }
}