package numerik.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import numerik.calc.Matrix;
import numerik.expression.*;
import numerik.expression.Value.ValueType;

public class OutputFrame extends JFrame implements KeyListener, ExpressionListener, ActionListener
{
    
    private final String NEW_MATRIX = "NEW_MATRIX";
    private final String RUN_PAUSE = "RUN_PAUSE";
    private final String STOP = "STOP";
    
    private JTabbedPane tabMain;
    
    //private ExpressionEngine solver;
    private JTextArea txtExpressionInput;
    private JPanel pnlExpressionOutput;
    
    private JPanel pnlStaticCode;
    
    public OutputFrame()
    {
        super("Numerik");
        List<Image> icons = new ArrayList<Image>();
        icons.add(new ImageIcon("icons/app16.png").getImage());
        icons.add(new ImageIcon("icons/app32.png").getImage());
        icons.add(new ImageIcon("icons/app48.png").getImage());
        icons.add(new ImageIcon("icons/app64.png").getImage());
        icons.add(new ImageIcon("icons/app128.png").getImage());
        this.setIconImages(icons);
        
        //solver = new ExpressionEngine();
        
        initLookAndFeel();
        
        // --- Toolbar ---
        
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton btnNewMatrix = new JButton(new ImageIcon("icons/new_matrix16.png"));
        btnNewMatrix.setToolTipText("Neue Matrix erzeugen");
        btnNewMatrix.setActionCommand(NEW_MATRIX);
        btnNewMatrix.addActionListener(this);
        toolBar.add(btnNewMatrix);
        toolBar.addSeparator();
        JButton btnRun = new JButton(new ImageIcon("icons/run16.png"));
        btnRun.setToolTipText("Ausdrücke auswerten");
        btnRun.setActionCommand(RUN_PAUSE);
        btnRun.addActionListener(this);
        toolBar.add(btnRun);
        JButton btnStop = new JButton(new ImageIcon("icons/stop16.png"));
        btnStop.setToolTipText("Ausführung anhalten");
        btnStop.setActionCommand(STOP);
        btnStop.addActionListener(this);
        toolBar.add(btnStop);
        
        this.add(toolBar, BorderLayout.PAGE_START);
        
        // --- Expression-Panel ---
        
        JPanel pnlExpression = new JPanel(new BorderLayout());
        
        pnlExpressionOutput = new JPanel();
        BoxLayout box = new BoxLayout(pnlExpressionOutput, BoxLayout.Y_AXIS);
        pnlExpressionOutput.setLayout(box);
        pnlExpressionOutput.setBackground(Color.WHITE);
        
        txtExpressionInput = new JTextArea();
        try
        {
            StringBuilder buffer = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader("Input.txt"));
            while (br.ready())
            {
                buffer.append(br.readLine());
                buffer.append('\n');
            }
            br.close();
            txtExpressionInput.setText(buffer.toString());
            txtExpressionInput.setSelectionStart(txtExpressionInput.getText().length());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        txtExpressionInput.setBorder(new LineBorder(Color.DARK_GRAY));
        txtExpressionInput.setBackground(new Color(255, 255, 100));
        txtExpressionInput.addKeyListener(this);
        
        JScrollPane scrExpressionOutput = new JScrollPane(pnlExpressionOutput);
        scrExpressionOutput.getVerticalScrollBar().setUnitIncrement(10);
        
        pnlExpression.add(scrExpressionOutput);
        pnlExpression.add(txtExpressionInput, BorderLayout.PAGE_END);
        
        // --- Static-Code-Panel ---
        
        pnlStaticCode = new JPanel();
        
        // --- Tab-Pane ---
        
        tabMain = new JTabbedPane(JTabbedPane.BOTTOM);
        tabMain.addTab("Expression", pnlExpression);
        tabMain.addTab("Statischer Code", pnlStaticCode);
        
        this.add(tabMain);
        
        this.setSize(640, 480);
        this.setLocationRelativeTo(null);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        
        txtExpressionInput.requestFocusInWindow();
    }
    
    @Override
    public void keyPressed(KeyEvent e) { }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown())
        {
            runExpressions();
            
            this.validate();
        }
        
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }
    
    private void initLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
    }
    
    private void runExpressions()
    {
        pnlExpressionOutput.removeAll();
        
        ScriptEngine engine = new ScriptEngine();
        
        engine.addExpressionListener(this);
        engine.run(txtExpressionInput.getText());
    }
    
    @Override
    public void expressionParsed(ExpressionEngine expression, Value result)
    {
        LatexFormula formula = new LatexFormula();
        
        if (expression.getAssignedVariable() != null)
        {
            formula.addText(expression.getAssignedVariable());
        }
        
        if (result.getType() != ValueType.TEXT)
        {
            formula.addText(" = ");
        }
        
        if (result.getType() == ValueType.MATRIX)
        {
            formula.addMatrix(result.toMatrix());
        }
        else if (result.getType() == ValueType.VECTOR)
        {
            formula.addVector(result.toVector());
        }
        else
        {
            formula.addText(result.toObject().toString());
        }
        pnlExpressionOutput.add(new ImageComponent(formula.toImage()));
        
        if (!Recorder.getInstance().isEmpty())
        {
            formula.clear();
            formula.addFormula(Recorder.getInstance().get(true));
            pnlExpressionOutput.add(new ExpandButton(new ImageComponent(formula.toImage(15))));
        }
        pnlExpressionOutput.add(new HorizontalLine());
    }

    @Override
    public void actionParsed(ActionType action, String data)
    {
        switch (action)
        {
            case DO:
            case WHILE:
                pnlExpressionOutput.add(new ImageComponent(new LatexFormula("\\text{" + data + "}").toImage(20, Color.BLUE)));
                pnlExpressionOutput.add(new HorizontalLine());
                break;
            case STARTPARSING:
                pnlExpressionOutput.add(new ImageComponent(new LatexFormula().addText(data).toImage(10)));
                break;
            case BADEXPRESSION:
                pnlExpressionOutput.add(new ImageComponent(new LatexFormula().addText(data).toImage(12, Color.RED)));
                pnlExpressionOutput.add(new HorizontalLine());
                break;
            case PARSEDEXPRESSION:
                //pnlExpressionOutput.add(new ImageComponent(new LatexFormula(data).toImage(10)));
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals(NEW_MATRIX))
        {
            Matrix matrix = NewMatrixWindow.createNewMatrix(this, MouseInfo.getPointerInfo().getLocation());
            if (matrix != null)
            {
                txtExpressionInput.insert(matrix.toString(), txtExpressionInput.getSelectionStart());
            }
        }
        else if (e.getActionCommand().equals(RUN_PAUSE))
        {
            runExpressions();
            this.validate();
        }
    }
}
