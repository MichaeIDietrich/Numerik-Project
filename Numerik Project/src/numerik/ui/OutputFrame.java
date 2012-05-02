package numerik.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import numerik.expression.*;
import numerik.expression.Value.ValueType;

public class OutputFrame extends JFrame implements KeyListener
{
    
    //private JTabbedPane 
    
    private ExpressionEngine solver;
    private JTextArea txtExpressions;
    private JPanel pnlExpression;
    
    private JPanel pnlstaticCode;
    
    public OutputFrame()
    {
        super("Numerik");
        
        solver = new ExpressionEngine();
        
        initLookAndFeel();
        
        pnlExpression = new JPanel();
        BoxLayout box = new BoxLayout(pnlExpression, BoxLayout.Y_AXIS);
        pnlExpression.setLayout(box);
        pnlExpression.setBackground(Color.WHITE);
        
        txtExpressions = new JTextArea();
        txtExpressions.setBackground(new Color(255, 255, 100));
        txtExpressions.addKeyListener(this);
        
        this.add(txtExpressions, BorderLayout.PAGE_END);
        this.add(new JScrollPane(pnlExpression));
        
        
        
        this.setSize(640, 480);
        this.setLocationRelativeTo(null);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    @Override
    public void keyPressed(KeyEvent e) { }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            pnlExpression.removeAll();
            LatexFormula formula = new LatexFormula();
            
            for (String line : txtExpressions.getText().split("\n"))
            {
                System.out.println("Line: " + line);
                if (line.equals("\n") || line.equals(""))
                    continue;
                
                // formula.addText(line + " = ");
                formula.addText(line);
                pnlExpression.add(new ImageComponent(formula.toImage(10)));
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
                    pnlExpression.add(new ImageComponent(formula.toImage()));
                    
                    if (!Recorder.getInstance().isEmpty())
                    {
                        formula.clear();
                        formula.addFormula(Recorder.getInstance().get(true));
                        pnlExpression.add(new ExpandButton(new ImageComponent(formula.toImage(15))));
                    }
                    
                }
                catch (InvalidExpressionException ex)
                {
                    formula.addText(ex.getMessage());
                    pnlExpression.add(new ImageComponent(formula.toImage(12, Color.RED)));
                }
                
                pnlExpression.add(new HorizontalLine());
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
