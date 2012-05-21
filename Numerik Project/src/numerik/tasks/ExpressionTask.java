package numerik.tasks;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.*;
import javax.swing.border.LineBorder;

import numerik.calc.Matrix;
import numerik.expression.*;
import numerik.expression.Value.ValueType;
import numerik.ui.controls.*;
import numerik.ui.dialogs.NewMatrixWindow;
import numerik.ui.dialogs.OutputFrame;
import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.Recorder;

public class ExpressionTask implements Task, ActionListener, KeyListener, ExpressionListener
{
    
    private final String NEW_MATRIX = "NEW_MATRIX";
    private final String RUN_PAUSE = "RUN_PAUSE";
    private final String STOP = "STOP";
    
    private OutputFrame frame;
    private TaskPane taskPane;
    
    private JTextArea txtExpressionInput;
    private JPanel pnlExpressionOutput;
    
    @Override
    public void init(OutputFrame frame, TaskPane taskPane)
    {
        this.frame = frame;
        this.taskPane = taskPane;
        
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
        
        taskPane.setJToolBar(toolBar);
        
        
        
        JPanel pnlMain = new JPanel(new BorderLayout());
        
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
        
        pnlMain.add(scrExpressionOutput);
        pnlMain.add(txtExpressionInput, BorderLayout.PAGE_END);
        
        taskPane.add(pnlMain);
    }
    
    @Override
    public void run(Value... values)
    {
        txtExpressionInput.requestFocusInWindow();
    }
    
    
    private void runExpressions()
    {
        pnlExpressionOutput.removeAll();
        Recorder.getInstance().clear();
        
        ScriptEngine engine = new ScriptEngine();
        
        engine.addExpressionListener(this);
        engine.run(txtExpressionInput.getText());
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals(NEW_MATRIX))
        {
            Matrix matrix = NewMatrixWindow.createNewMatrix(frame, MouseInfo.getPointerInfo().getLocation());
            if (matrix != null)
            {
                txtExpressionInput.insert(matrix.toString(), txtExpressionInput.getSelectionStart());
            }
        }
        else if (e.getActionCommand().equals(RUN_PAUSE))
        {
            runExpressions();
            pnlExpressionOutput.revalidate();
        }
    }
    
    
    @Override
    public void keyPressed(KeyEvent e) { }
    
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown())
        {
            runExpressions();
            
            taskPane.revalidate();
        }
    }
    
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        if (e.getKeyChar() == '*')
        {
            e.setKeyChar('⋅');
        }
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
        
        formula.addValue(result);
        
        pnlExpressionOutput.add(new ImageComponent(formula.toImage()));
        
        if (!Recorder.getInstance().isEmpty())
        {
            formula.clear();
            formula.startAlignedEquation().addAlignedEquation(Recorder.getInstance().get().toString(), "=").endAlignedEquation();
            //formula.addFormula(Recorder.getInstance().get());
            Recorder.getInstance().clear();
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
}
