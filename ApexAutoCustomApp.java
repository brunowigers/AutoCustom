import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Random;

public class ApexAutoCustomApp extends JFrame {

    // Paleta de Cores - Tema Automotivo Dark & Performance
    private static final Color BG_DARK = new Color(18, 18, 22);
    private static final Color BG_CARD = new Color(28, 29, 36);
    private static final Color BG_HEADER = new Color(12, 12, 15);
    private static final Color ACCENT_RED = new Color(220, 38, 38);
    private static final Color ACCENT_GOLD = new Color(245, 158, 11);
    private static final Color TEXT_PRIMARY = new Color(243, 244, 246);
    private static final Color TEXT_SECONDARY = new Color(156, 163, 175);
    private static final Color BORDER_COLOR = new Color(55, 65, 81);

    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    // Componentes do formulário de orçamento
    private JTextField txtNome, txtTelefone, txtModeloCarro, txtAnoCarro;
    private JCheckBox chkEnvelopamento, chkRemap, chkEscapamento, chkRodas, chkSom, chkSuspensao;
    private JTextArea txtObservacoes, txtResumoOrcamento;
    private JLabel lblTotalEstimado;

    public ApexAutoCustomApp() {
        setTitle("APEX AUTO CUSTOM | Performance, Estética & Engenharia Automotiva");
        setSize(1150, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);

        // 1. Barra Superior (Navegação estilo Web)
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Área Central Dinâmica (Páginas do Site)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BG_DARK);

        mainContentPanel.add(createHomePanel(), "HOME");
        mainContentPanel.add(createServicesPanel(), "SERVICOS");
        mainContentPanel.add(createProjectsPanel(), "PROJETOS");
        mainContentPanel.add(createQuotePanel(), "ORCAMENTO");

        add(mainContentPanel, BorderLayout.CENTER);

        // 3. Rodapé (Footer)
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    // ==================== HEADER / MENU ====================
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_HEADER);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 2, 0, ACCENT_RED),
                new EmptyBorder(15, 30, 15, 30)
        ));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);
        JLabel lblLogoIcon = new JLabel("⚡");
        lblLogoIcon.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblLogoIcon.setForeground(ACCENT_RED);

        JLabel lblLogoText = new JLabel("APEX AUTO CUSTOM");
        lblLogoText.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogoText.setForeground(TEXT_PRIMARY);

        logoPanel.add(lblLogoIcon);
        logoPanel.add(lblLogoText);

        // Botões de Navegação
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        navPanel.setOpaque(false);

        navPanel.add(createNavButton("INÍCIO", "HOME"));
        navPanel.add(createNavButton("SERVIÇOS & TABELA", "SERVICOS"));
        navPanel.add(createNavButton("PROJETOS REALIZADOS", "PROJETOS"));

        JButton btnQuote = createNavButton("SOLICITAR ORÇAMENTO", "ORCAMENTO");
        btnQuote.setBackground(ACCENT_RED);
        btnQuote.setForeground(Color.WHITE);
        navPanel.add(btnQuote);

        header.add(logoPanel, BorderLayout.WEST);
        header.add(navPanel, BorderLayout.EAST);

        return header;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(BG_CARD);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> cardLayout.show(mainContentPanel, cardName));
        return btn;
    }

    // ==================== PÁGINA 1: HOME ====================
    private JScrollPane createHomePanel() {
        JPanel home = new JPanel();
        home.setLayout(new BoxLayout(home, BoxLayout.Y_AXIS));
        home.setBackground(BG_DARK);
        home.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Banner Principal (Hero Section)
        JPanel heroBanner = new JPanel(new BorderLayout());
        heroBanner.setBackground(BG_CARD);
        heroBanner.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(35, 35, 35, 35)
        ));
        heroBanner.setMaximumSize(new Dimension(1100, 260));

        JPanel heroText = new JPanel();
        heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));
        heroText.setOpaque(false);

        JLabel lblTag = new JLabel("ENGENHARIA DE PERFORMANCE & ESTÉTICA AUTOMOTIVA");
        lblTag.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTag.setForeground(ACCENT_GOLD);

        JLabel lblHeadline = new JLabel("<html>Transformamos seu carro em uma<br>máquina única nas ruas e pistas.</html>");
        lblHeadline.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblHeadline.setForeground(TEXT_PRIMARY);
        lblHeadline.setBorder(new EmptyBorder(10, 0, 15, 0));

        JLabel lblSub = new JLabel("<html>Especialistas em Remapeamento ECU, Envelopamento Líquido e Vinil Premium, Suspensão Coilover,<br>Escapamentos em Inox sob medida e Projetos de Áudio Hi-Fi.</html>");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblSub.setForeground(TEXT_SECONDARY);

        heroText.add(lblTag);
        heroText.add(lblHeadline);
        heroText.add(lblSub);

        JButton btnCta = new JButton("MONTE SEU PROJETO AGORA ➔");
        btnCta.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCta.setBackground(ACCENT_RED);
        btnCta.setForeground(Color.WHITE);
        btnCta.setFocusPainted(false);
        btnCta.setBorder(new EmptyBorder(15, 25, 15, 25));
        btnCta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCta.addActionListener(e -> cardLayout.show(mainContentPanel, "ORCAMENTO"));

        JPanel btnWrapper = new JPanel(new GridBagLayout());
        btnWrapper.setOpaque(false);
        btnWrapper.add(btnCta);

        heroBanner.add(heroText, BorderLayout.CENTER);
        heroBanner.add(btnWrapper, BorderLayout.EAST);

        home.add(heroBanner);
        home.add(Box.createRigidArea(new Dimension(0, 30)));

        // Estatísticas / Números da Oficina
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.setMaximumSize(new Dimension(1100, 110));

        statsGrid.add(createStatCard("+480", "Projetos Entregues"));
        statsGrid.add(createStatCard("12 Anos", "De Experiência no Setor"));
        statsGrid.add(createStatCard("Dinamômetro", "4x4 Próprio na Oficina"));
        statsGrid.add(createStatCard("100%", "Garantia nas Peças e Mão de Obra"));

        home.add(statsGrid);
        home.add(Box.createRigidArea(new Dimension(0, 30)));

        // Seção Diferenciais
        JLabel lblSecTitle = new JLabel("POR QUE ESCOLHER A APEX AUTO CUSTOM?");
        lblSecTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblSecTitle.setForeground(TEXT_PRIMARY);
        lblSecTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        home.add(lblSecTitle);
        home.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel featuresGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        featuresGrid.setOpaque(false);
        featuresGrid.setMaximumSize(new Dimension(1100, 200));

        featuresGrid.add(createInfoCard("🔧 Peças Importadas & Homologadas",
                "Trabalhamos diretamente com marcas líderes mundiais como Brembo, Bilstein, Akrapovič, K&N, FuelTech e 3M."));
        featuresGrid.add(createInfoCard("💻 Telemetria e Diagnóstico Digital",
                "Todo aumento de potência é aferido antes e depois em dinamômetro, garantindo curva de torque linear e segurança ao motor."));
        featuresGrid.add(createInfoCard("🛡️ Estúdio Climatizado Livre de Poeira",
                "Cabine exclusiva para aplicação de PPF (Paint Protection Film), vitrificação cerâmica 9H e envelopamento automotivo."));

        home.add(featuresGrid);

        JScrollPane scroll = new JScrollPane(home);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createStatCard(String bigNumber, String label) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
                new MatteBorder(3, 0, 0, 0, ACCENT_RED),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JLabel lblNum = new JLabel(bigNumber);
        lblNum.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblNum.setForeground(ACCENT_GOLD);

        JLabel lblDesc = new JLabel(label);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDesc.setForeground(TEXT_SECONDARY);

        card.add(lblNum);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblDesc);
        return card;
    }

    private JPanel createInfoCard(String title, String description) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_PRIMARY);

        JTextArea txtDesc = new JTextArea(description);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDesc.setForeground(TEXT_SECONDARY);
        txtDesc.setBackground(BG_CARD);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(txtDesc, BorderLayout.CENTER);
        return card;
    }

    // ==================== PÁGINA 2: SERVIÇOS & TABELA ====================
    private JPanel createServicesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topInfo = new JPanel(new BorderLayout());
        topInfo.setOpaque(false);

        JLabel title = new JLabel("CATÁLOGO DE SERVIÇOS E VALORES BASE");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Valores sujeitos a variação conforme modelo, ano e motorização do veículo.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);

        topInfo.add(title, BorderLayout.NORTH);
        topInfo.add(subtitle, BorderLayout.SOUTH);

        String[] columns = {"Categoria", "Serviço Especializado", "Especificação Técnica", "Prazo Médio", "A partir de"};
        Object[][] data = {
                {"Performance", "Remap ECU Stage 1", "Otimização de mapa de injeção/ignição (+15% a +30% cv)", "1 dia", "R$ 2.200,00"},
                {"Performance", "Remap ECU Stage 2 + Downpipe", "Downpipe em Inox 304 + Filtro Inbox + Acerto em Dinamômetro", "2 dias", "R$ 4.800,00"},
                {"Escapamento", "Catback Full Inox com Difusor", "Solda TIG, ponteiras em carbono e controle via válvula", "3 dias", "R$ 5.500,00"},
                {"Estética", "Envelopamento Full Car Premium", "Vinil importado (Oracal / 3M / Avery) com desmontagem técnica", "5 dias", "R$ 6.500,00"},
                {"Proteção", "PPF Frontal (Paint Protection)", "Película regenerativa contra pedras e riscos (Capô, para-choque, faróis)", "3 dias", "R$ 4.200,00"},
                {"Estética", "Vitrificação Cerâmica 9H + Polimento", "Correção de verniz em 3 etapas + proteção hidrofóbica de 3 anos", "2 dias", "R$ 1.800,00"},
                {"Suspensão", "Instalação Coilover / Suspensão a Ar", "Geometria completa, cambagem e regulagem de carga", "2 dias", "R$ 3.900,00"},
                {"Rodas & Freios", "Pintura Eletrostática + Pinças", "Pintura em estufa de rodas e pinças de freio em alta temperatura", "2 dias", "R$ 1.400,00"},
                {"Áudio & Tech", "Sistema de Som Hi-Fi / Multimídia", "Tratamento acústico nas portas, amplificador DSP e subwoofer slim", "3 dias", "R$ 3.500,00"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(42);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(ACCENT_RED);
        table.setSelectionForeground(Color.WHITE);

        // Estilo do Cabeçalho da Tabela
        table.getTableHeader().setBackground(BG_HEADER);
        table.getTableHeader().setForeground(ACCENT_GOLD);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Ajuste de largura das colunas
        table.getColumnModel().getColumn(0).setPreferredWidth(110);
        table.getColumnModel().getColumn(1).setPreferredWidth(230);
        table.getColumnModel().getColumn(2).setPreferredWidth(380);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);

        // Centralizar prazo e preço
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_CARD);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1));

        panel.add(topInfo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ==================== PÁGINA 3: PROJETOS REALIZADOS ====================
    private JScrollPane createProjectsPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(BG_DARK);
        container.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("PORTFÓLIO: PROJETOS EM DESTAQUE");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel grid = new JPanel(new GridLayout(2, 2, 25, 25));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(1100, 520));

        grid.add(createProjectCard(
                "FORD MUSTANG GT V8 - STAGE 3",
                "Potência: 585 cv | Torque: 68 kgfm",
                "• Supercharger Whipple + Coletor Dimensionado em Inox\n" +
                "• Envelopamento Satin Black com faixas Vermelho Racing\n" +
                "• Rodas Forjadas Aro 20\" + Pneus Michelin Pilot Sport 4S\n" +
                "• Freios Brembo de 6 pistões",
                "INVESTIMENTO: R$ 84.000,00"
        ));

        grid.add(createProjectCard(
                "VW GOLF GTI MK7.5 - TRACK DAY",
                "Potência: 360 cv | Torque: 51 kgfm",
                "• Turbina IS38 + Intercooler Frontal de Alumínio\n" +
                "• Remap Motor + Câmbio DSG Stage 3\n" +
                "• Suspensão Coilover Bilstein B16 com regulagem de carga\n" +
                "• Interior com bancos concha e volante em Alcântara",
                "INVESTIMENTO: R$ 42.500,00"
        ));

        grid.add(createProjectCard(
                "BMW M3 F80 - WIDEBODY & CARBON",
                "Potência: 530 cv | Torque: 72 kgfm",
                "• Kit aerodinâmico completo em Fibra de Carbono real\n" +
                "• Escapamento Full Titânio com ponteiras queimadas\n" +
                "• Aplicação de PPF Full Body Fosco (Stealth)\n" +
                "• Sistema de som Harman Kardon customizado",
                "INVESTIMENTO: R$ 68.000,00"
        ));

        grid.add(createProjectCard(
                "HONDA CIVIC SI K24 - ASPIRADO",
                "Potência: 265 cv (9.000 RPM)",
                "• Comandos de válvula Skunk2 Stage 2 + Injeção FuelTech FT550\n" +
                "• Pintura customizada Azul Estoril Perolizado\n" +
                "• Rodas Enkei RPF1 originais + Barras anti-torção\n" +
                "• Coletor 4x2x1 em inox polido",
                "INVESTIMENTO: R$ 35.000,00"
        ));

        container.add(grid);

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        return scroll;
    }

    private JPanel createProjectCard(String carName, String specsHighlight, String modList, String priceTag) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 22, 20, 22)
        ));

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setOpaque(false);

        JLabel lblCar = new JLabel(carName);
        lblCar.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblCar.setForeground(TEXT_PRIMARY);

        JLabel lblHighlight = new JLabel(specsHighlight);
        lblHighlight.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblHighlight.setForeground(ACCENT_RED);

        top.add(lblCar);
        top.add(lblHighlight);

        JTextArea txtMods = new JTextArea(modList);
        txtMods.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtMods.setForeground(TEXT_SECONDARY);
        txtMods.setBackground(BG_CARD);
        txtMods.setEditable(false);
        txtMods.setLineWrap(true);
        txtMods.setWrapStyleWord(true);

        JLabel lblPrice = new JLabel(priceTag);
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPrice.setForeground(ACCENT_GOLD);

        card.add(top, BorderLayout.NORTH);
        card.add(txtMods, BorderLayout.CENTER);
        card.add(lblPrice, BorderLayout.SOUTH);

        return card;
    }

    // ==================== PÁGINA 4: ORÇAMENTO INTERATIVO ====================
    private JPanel createQuotePanel() {
        JPanel main = new JPanel(new GridLayout(1, 2, 30, 0));
        main.setBackground(BG_DARK);
        main.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Coluna Esquerda: Formulário
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BG_CARD);
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblFormTitle = new JLabel("MONTE SEU ORÇAMENTO PERSONALIZADO");
        lblFormTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblFormTitle.setForeground(TEXT_PRIMARY);
        formPanel.add(lblFormTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        txtNome = createStyledTextField("Seu Nome Completo");
        txtTelefone = createStyledTextField("WhatsApp / Telefone");
        txtModeloCarro = createStyledTextField("Marca e Modelo do Veículo (Ex: BMW 320i)");
        txtAnoCarro = createStyledTextField("Ano / Motorização (Ex: 2022 2.0 Turbo)");

        formPanel.add(createFieldGroup("Nome do Cliente:", txtNome));
        formPanel.add(createFieldGroup("Contato (WhatsApp):", txtTelefone));
        formPanel.add(createFieldGroup("Veículo:", txtModeloCarro));
        formPanel.add(createFieldGroup("Ano / Motor:", txtAnoCarro));

        JLabel lblServ = new JLabel("Selecione os Serviços Desejados:");
        lblServ.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblServ.setForeground(ACCENT_GOLD);
        lblServ.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(lblServ);

        JPanel checksGrid = new JPanel(new GridLayout(3, 2, 5, 5));
        checksGrid.setOpaque(false);
        checksGrid.setMaximumSize(new Dimension(500, 100));
        checksGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        chkRemap = createStyledCheckBox("Remap ECU (R$ 2.200)", 2200);
        chkEscapamento = createStyledCheckBox("Escapamento Inox (R$ 5.500)", 5500);
        chkEnvelopamento = createStyledCheckBox("Envelopamento (R$ 6.500)", 6500);
        chkSuspensao = createStyledCheckBox("Suspensão Coilover (R$ 3.900)", 3900);
        chkRodas = createStyledCheckBox("Pintura Rodas/Pinças (R$ 1.400)", 1400);
        chkSom = createStyledCheckBox("Som Hi-Fi / DSP (R$ 3.500)", 3500);

        checksGrid.add(chkRemap);
        checksGrid.add(chkEscapamento);
        checksGrid.add(chkEnvelopamento);
        checksGrid.add(chkSuspensao);
        checksGrid.add(chkRodas);
        checksGrid.add(chkSom);

        formPanel.add(checksGrid);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        lblTotalEstimado = new JLabel("INVESTIMENTO ESTIMADO: R$ 0,00");
        lblTotalEstimado.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotalEstimado.setForeground(ACCENT_GOLD);
        lblTotalEstimado.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblTotalEstimado);

        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnGerar = new JButton("GERAR PROTOCOLO DE ORÇAMENTO");
        btnGerar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGerar.setBackground(ACCENT_RED);
        btnGerar.setForeground(Color.WHITE);
        btnGerar.setFocusPainted(false);
        btnGerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGerar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGerar.addActionListener(e -> gerarResumoOrcamento());

        formPanel.add(btnGerar);

        // Coluna Direita: Resumo / Protocolo Gerado
        JPanel summaryPanel = new JPanel(new BorderLayout(0, 15));
        summaryPanel.setBackground(BG_CARD);
        summaryPanel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblSummaryTitle = new JLabel("FICHA TÉCNICA DO PROJETO / PROTOCOLO");
        lblSummaryTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblSummaryTitle.setForeground(TEXT_PRIMARY);

        txtResumoOrcamento = new JTextArea();
        txtResumoOrcamento.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtResumoOrcamento.setBackground(BG_HEADER);
        txtResumoOrcamento.setForeground(new Color(16, 185, 129)); // Verde estilo terminal/telemetria
        txtResumoOrcamento.setEditable(false);
        txtResumoOrcamento.setBorder(new EmptyBorder(15, 15, 15, 15));
        txtResumoOrcamento.setText(
                "==================================================\n" +
                "        AGUARDANDO DADOS DO SEU PROJETO...\n" +
                "==================================================\n\n" +
                "Preencha os dados ao lado e selecione os upgrades\n" +
                "desejados para calcular o valor estimado e gerar\n" +
                "seu código de atendimento prioritário na oficina."
        );

        summaryPanel.add(lblSummaryTitle, BorderLayout.NORTH);
        summaryPanel.add(new JScrollPane(txtResumoOrcamento), BorderLayout.CENTER);

        main.add(formPanel);
        main.add(summaryPanel);

        return main;
    }

    private JPanel createFieldGroup(String labelText, JTextField field) {
        JPanel group = new JPanel(new BorderLayout(0, 4));
        group.setOpaque(false);
        group.setMaximumSize(new Dimension(500, 55));
        group.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(TEXT_SECONDARY);

        group.add(lbl, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        return group;
    }

    private JTextField createStyledTextField(String tooltip) {
        JTextField tf = new JTextField();
        tf.setBackground(BG_HEADER);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT_RED);
        tf.setToolTipText(tooltip);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    private JCheckBox createStyledCheckBox(String text, double price) {
        JCheckBox chk = new JCheckBox(text);
        chk.setOpaque(false);
        chk.setForeground(TEXT_PRIMARY);
        chk.setFont(new Font("SansSerif", Font.PLAIN, 12));
        chk.addActionListener(e -> atualizarValorEstimado());
        return chk;
    }

    private double calcularTotal() {
        double total = 0;
        if (chkRemap.isSelected()) total += 2200;
        if (chkEscapamento.isSelected()) total += 5500;
        if (chkEnvelopamento.isSelected()) total += 6500;
        if (chkSuspensao.isSelected()) total += 3900;
        if (chkRodas.isSelected()) total += 1400;
        if (chkSom.isSelected()) total += 3500;
        return total;
    }

    private void atualizarValorEstimado() {
        DecimalFormat df = new DecimalFormat("R$ #,##0.00");
        lblTotalEstimado.setText("INVESTIMENTO ESTIMADO: " + df.format(calcularTotal()));
    }

    private void gerarResumoOrcamento() {
        String nome = txtNome.getText().trim();
        String carro = txtModeloCarro.getText().trim();

        if (nome.isEmpty() || carro.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha pelo menos seu Nome e o Modelo do Veículo!",
                    "Dados Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = calcularTotal();
        if (total == 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione pelo menos um serviço para gerar o orçamento.",
                    "Nenhum Serviço Selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int protocolo = 10000 + new Random().nextInt(90000);
        DecimalFormat df = new DecimalFormat("R$ #,##0.00");

        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("       APEX AUTO CUSTOM - ORÇAMENTO OFICIAL       \n");
        sb.append("==================================================\n");
        sb.append("PROTOCOLO: #APX-").append(protocolo).append("\n");
        sb.append("CLIENTE:   ").append(nome.toUpperCase()).append("\n");
        sb.append("CONTATO:   ").append(txtTelefone.getText()).append("\n");
        sb.append("VEÍCULO:   ").append(carro.toUpperCase()).append(" (").append(txtAnoCarro.getText()).append(")\n");
        sb.append("--------------------------------------------------\n");
        sb.append("SERVIÇOS SELECIONADOS:\n");

        if (chkRemap.isSelected()) sb.append(" [✔] Remap ECU Stage 1 ................. R$ 2.200,00\n");
        if (chkEscapamento.isSelected()) sb.append(" [✔] Escapamento Full Inox ........... R$ 5.500,00\n");
        if (chkEnvelopamento.isSelected()) sb.append(" [✔] Envelopamento Full Car .......... R$ 6.500,00\n");
        if (chkSuspensao.isSelected()) sb.append(" [✔] Suspensão Coilover .............. R$ 3.900,00\n");
        if (chkRodas.isSelected()) sb.append(" [✔] Pintura de Rodas e Pinças ....... R$ 1.400,00\n");
        if (chkSom.isSelected()) sb.append(" [✔] Sistema de Som Hi-Fi / DSP ...... R$ 3.500,00\n");

        sb.append("--------------------------------------------------\n");
        sb.append("TOTAL ESTIMADO: ").append(df.format(total)).append("\n");
        sb.append("CONDIÇÃO: Até 12x no cartão ou 8% OFF no PIX\n");
        sb.append("==================================================\n");
        sb.append("Apresente este protocolo na recepção da oficina\n");
        sb.append("para agendar sua avaliação técnica gratuita.");

        txtResumoOrcamento.setText(sb.toString());
    }

    // ==================== FOOTER ====================
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_HEADER);
        footer.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
                new EmptyBorder(10, 30, 10, 30)
        ));

        JLabel left = new JLabel("© 2026 APEX AUTO CUSTOM - Todos os direitos reservados.");
        left.setFont(new Font("SansSerif", Font.PLAIN, 12));
        left.setForeground(TEXT_SECONDARY);

        JLabel right = new JLabel("📍 Av. das Nações, 1500 - Distrito Industrial | Seg a Sáb: 08h às 18h");
        right.setFont(new Font("SansSerif", Font.PLAIN, 12));
        right.setForeground(TEXT_SECONDARY);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    public static void main(String[] args) {
        // Garantir renderização suave de fontes
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            ApexAutoCustomApp app = new ApexAutoCustomApp();
            app.setVisible(true);
        });
    }
}