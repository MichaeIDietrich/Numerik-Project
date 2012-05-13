package numerik.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import org.scilab.forge.jlatexmath.*;

import numerik.tasks.*;

public class OutputFrame extends JFrame
{
    
    private JToolBar activeToolBar;
    
    
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
        
        initLookAndFeel();
<<<<<<< HEAD
=======
        initLatex();
>>>>>>> origin/master
        
        TabbedTaskPane tabMain = new TabbedTaskPane(this);
        tabMain.addTab("Expression", new ExpressionTask());
        tabMain.addTab("LU-Zerlegung", new LUDecomposition());
        tabMain.addTab("Newton Wurzel", new NewtonIteration());
        tabMain.addTab("Non-Lin-GS", new SolveNonLinearEquation());
        this.add(tabMain);       
        
        this.setSize(600, 720);
        this.setLocationRelativeTo(null);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    
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
    
    
    private void initLatex()
    {
        // Dummy, um die Static-Elemente zu laden
        new TeXFormula(new LatexFormula().toString()).createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
    }
    
    
    public void setJToolBar(JToolBar toolBar)
    {
        if (activeToolBar != null)
        {
            this.remove(activeToolBar);
        }
        
        activeToolBar = toolBar;
        
        if (activeToolBar != null)
        {
            this.add(activeToolBar, BorderLayout.PAGE_START);
        }
        this.repaint();
    }
}