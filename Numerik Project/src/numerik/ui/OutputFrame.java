package numerik.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import numerik.expression.*;
import numerik.expression.Value.ValueType;

public class OutputFrame extends JFrame implements KeyListener {
  
  private ExpressionEngine solver;
  private JTextArea text;
  private JPanel pnlOut;
  
  public OutputFrame() {
    super("Numerik");
    
    solver = new ExpressionEngine();
    
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    
    //pnlOut = new JPanel(new GridLayout(0, 1));
    pnlOut = new JPanel();
    BoxLayout box = new BoxLayout(pnlOut, BoxLayout.Y_AXIS);
    pnlOut.setLayout(box);
    pnlOut.setBackground(Color.WHITE);
    
    text = new JTextArea();
    text.setBackground(new Color(255,255, 100));
    text.addKeyListener(this);
    
    this.add(text, BorderLayout.PAGE_START);
    
    this.add(new JScrollPane(pnlOut));

    this.setSize(640, 480);
    this.setLocationRelativeTo(null);
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
    
    //pnlOut.add(new ImageComponent(new LatexFormula("\\begin{pmatrix}1&2&3\\\\4&5&6\\\\7&8&9\\end{pmatrix}").toImage()));
//    if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      pnlOut.removeAll();
      LatexFormula formula = new LatexFormula();
      
      for (String line : text.getText().split("\n")){
        System.out.println("Line: " + line);
        if (line.equals("\n")) continue;
        
//        formula.addText(line + " = ");
        formula.addText(line);
        pnlOut.add(new ImageComponent(formula.toImage(10)));
        formula.clear();
        
        Value res;
        try {
          res = solver.solve(line);
        
          if (solver.getAssignedVariable() != null) {
            formula.addText(solver.getAssignedVariable());
          }
          
          if (res.getType() != ValueType.STRING) {
          formula.addText(" = ");
          }
          
          if (res.getType() == ValueType.MATRIX) {
            formula.addMatrix(res.toMatrix());
          } else {
            formula.addText(res.toObject().toString());
          }
          pnlOut.add(new ImageComponent(formula.toImage()));
          
          if (!Recorder.getInstance().isEmpty()) {
            formula.clear();
            formula.addFormula(Recorder.getInstance().get(true));
            pnlOut.add(new ExpandButton(new ImageComponent(formula.toImage(15))));
          }
          
        } catch (InvalidExpressionException ex) {
          formula.addText(ex.getMessage());
          pnlOut.add(new ImageComponent(formula.toImage(12, Color.RED)));
        }
          
        pnlOut.add(new HorizontalLine());
        formula.clear();
          
      }
      
      this.validate();
    }
    
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
}
