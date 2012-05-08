package numerik.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import numerik.calc.Matrix;
import numerik.expression.*;
import numerik.expression.Value.ValueType;

import numerik.tasks.GaussIntegrationOrder4;
import numerik.tasks.LUDecomposition;
import numerik.tasks.NewtonIteration;
import numerik.tasks.SolveNonLinearEquation;

public class OutputFrame extends JFrame implements KeyListener, ExpressionListener, ActionListener, MouseListener, ChangeListener
{
    
    private final String NEW_MATRIX = "NEW_MATRIX";
    private final String RUN_PAUSE = "RUN_PAUSE";
    private final String STOP = "STOP";
    
    private JTabbedPane tabMain;
    
    //private ExpressionEngine solver;
    private JTextArea txtExpressionInput;
    private JPanel pnlExpressionOutput;
    private JPanel pnlStaticCode;
    
    private JToolBar toolBar;
    private JButton btnNewMatrix;
    private JButton btnRun;
    private JButton btnStop;
    
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
        
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        btnNewMatrix = new JButton(new ImageIcon("icons/new_matrix16.png"));
        btnNewMatrix.setToolTipText("Neue Matrix erzeugen");
        btnNewMatrix.setActionCommand(NEW_MATRIX);
        btnNewMatrix.addActionListener(this);
        toolBar.add(btnNewMatrix);
        toolBar.addSeparator();
        
        btnRun = new JButton(new ImageIcon("icons/run16.png"));
        btnRun.setToolTipText("Ausdrücke auswerten");
        btnRun.setActionCommand(RUN_PAUSE);
        btnRun.addActionListener(this);
        toolBar.add(btnRun);
        
        btnStop = new JButton(new ImageIcon("icons/stop16.png"));
        btnStop.setToolTipText("Ausführung anhalten");
        btnStop.setActionCommand(STOP);
        btnStop.addActionListener(this);
        toolBar.add(btnStop);
        
        this.add(toolBar, BorderLayout.NORTH);
        
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

        NiceScrollPane lu_decompos  = new NiceScrollPane( new LUDecomposition().getFormula() ); 
        NiceScrollPane newton_iter  = new NiceScrollPane( new NewtonIteration().getFormula() ); 
        NiceScrollPane non_lin_equ  = new NiceScrollPane( new SolveNonLinearEquation().getFormula() ); 
        NiceScrollPane gauss_int4o  = new NiceScrollPane( new GaussIntegrationOrder4().getFormula() );
        NiceScrollPane rungkutta4o  = new NiceScrollPane( new GaussIntegrationOrder4().getFormula() );
        
        // --- Tab-Pane ---
        
        tabMain = new JTabbedPane(JTabbedPane.BOTTOM);
        tabMain.addTab("Expression", pnlExpression);
        tabMain.addTab("LU-Zerlegung",    lu_decompos);
        tabMain.addTab("Newton Wurzel",   newton_iter);
        tabMain.addTab("Non-Lin-GS",      non_lin_equ);
        tabMain.addTab("Gauss Int. 4",    gauss_int4o);
        tabMain.addTab("Runge Kutta 4",   rungkutta4o);
        
        tabMain.addChangeListener(changeListener);
        
        System.out.println( tabMain.getName() );
        
        this.add( tabMain );
        
        this.setSize(600, 720);
        this.setLocationRelativeTo(null);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        
        txtExpressionInput.requestFocusInWindow();
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }
    
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
                pnlExpressionOutput.add( new ImageComponent(new LatexFormula("\\text{" + data + "}").toImage(20, Color.BLUE)) );
                pnlExpressionOutput.add( new HorizontalLine() );
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
    
    ChangeListener changeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent changeEvent)
        {
            JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            
            switch (index)
            {
                case 0: {
                            btnRun.setVisible(true);
                            btnStop.setVisible(true);
                            toolBar.setVisible(true);
                        }
                
                case 1: {
                            btnRun.setVisible(false);
                            btnStop.setVisible(false);
                        }
                
                case 2: {
                    
                        }
                
                case 3: {
                    
                        }
                
                case 4: {
                    
                        }
                
                case 5: {
                    
                        }
            }
        }
      };
    
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

    @Override
    public void stateChanged(ChangeEvent arg0) {}

    @Override
    public void mouseClicked(MouseEvent arg0) {}

    @Override
    public void mouseEntered(MouseEvent arg0) {}

    @Override
    public void mouseExited(MouseEvent arg0) {}

    @Override
    public void mousePressed(MouseEvent arg0) {}

    @Override
    public void mouseReleased(MouseEvent arg0) {}
}