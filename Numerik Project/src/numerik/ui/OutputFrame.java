package numerik.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import numerik.expression.*;
import numerik.expression.Value.ValueType;

public class OutputFrame extends JFrame implements KeyListener
{
    
    private JTabbedPane tabMain;
    
    private ExpressionEngine solver;
    private JTextArea txtExpressionInput;
    private JPanel pnlExpressionOutput;
    
    private JPanel pnlStaticCode;
    
    public OutputFrame()
    {
        super("Numerik");
        
        solver = new ExpressionEngine();
        
        initLookAndFeel();
        
        // --- Toolbar ---
        
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton btnNewMatrix = new JButton(new ImageIcon("icons/new_matrix16.png"));
        btnNewMatrix.setToolTipText("Neue Matrix erzeugen");
        toolBar.add(btnNewMatrix);
        
        this.add(toolBar, BorderLayout.PAGE_START);
        
        // --- Expression-Panel ---
        
        JPanel pnlExpression = new JPanel(new BorderLayout());
        
        pnlExpressionOutput = new JPanel();
        BoxLayout box = new BoxLayout(pnlExpressionOutput, BoxLayout.Y_AXIS);
        pnlExpressionOutput.setLayout(box);
        pnlExpressionOutput.setBackground(Color.WHITE);
        
        txtExpressionInput = new JTextArea();
        txtExpressionInput.setBorder(new LineBorder(Color.BLACK));
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
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            pnlExpressionOutput.removeAll();
            LatexFormula formula = new LatexFormula();
            
            for (String line : txtExpressionInput.getText().split("\n"))
            {
                System.out.println("Line: " + line);
                if (line.equals("\n") || line.equals(""))
                    continue;
                
                formula.addText(line);
                pnlExpressionOutput.add(new ImageComponent(formula.toImage(10)));
                formula.clear();
                
                Value res;
                try
                {
                    res = solver.solve(line);
                    
                    if (solver.getAssignedVariable() != null)
                    {
                        formula.addText(solver.getAssignedVariable());
                    }
                    
                    if (res.getType() != ValueType.TEXT)
                    {
                        formula.addText(" = ");
                    }
                    
                    if (res.getType() == ValueType.MATRIX)
                    {
                        formula.addMatrix(res.toMatrix());
                    }
                    else
                    {
                        formula.addText(res.toObject().toString());
                    }
                    pnlExpressionOutput.add(new ImageComponent(formula.toImage()));
                    
                    if (!Recorder.getInstance().isEmpty())
                    {
                        formula.clear();
                        formula.addFormula(Recorder.getInstance().get(true));
                        pnlExpressionOutput.add(new ExpandButton(new ImageComponent(formula.toImage(15))));
                    }
                    
                }
                catch (InvalidExpressionException ex)
                {
                    formula.addText(ex.getMessage());
                    pnlExpressionOutput.add(new ImageComponent(formula.toImage(12, Color.RED)));
                }
                
                pnlExpressionOutput.add(new HorizontalLine());
                formula.clear();
                
            }
            
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
}
