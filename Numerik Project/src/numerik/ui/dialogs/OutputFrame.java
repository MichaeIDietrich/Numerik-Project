package numerik.ui.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import numerik.Configuration;
import numerik.tasks.*;
import numerik.ui.controls.TabbedTaskPane;

public final class OutputFrame extends JFrame
{
    
    private static List<Image> icons;
    
    static
    {
        icons = new ArrayList<Image>();
        icons.add(new ImageIcon("icons/app16.png").getImage());
        icons.add(new ImageIcon("icons/app32.png").getImage());
        icons.add(new ImageIcon("icons/app48.png").getImage());
        icons.add(new ImageIcon("icons/app64.png").getImage());
        icons.add(new ImageIcon("icons/app128.png").getImage());
    }
    
    
    private JToolBar activeToolBar;
    
    
    public OutputFrame()
    {
        super("Numerik");
        this.setIconImages(icons);
        
        initLookAndFeel();
        
        TabbedTaskPane tabMain = new TabbedTaskPane(this);
        tabMain.addTab("Expression", new ExpressionTask(), "Eingabebereich, um mathematische Ausdrücke auszuwerten");
        tabMain.addTab("Dezimal->Binär",    new DezimalToBinary());
        tabMain.addTab("LU-Zerlegung", new LUDecomposition());
        tabMain.addTab("Eingabefehler-Abschätzung", new InputErrorEstimation());
        tabMain.addTab("Jakobi-Iteration", new JacobiIteration());
        tabMain.addTab("Newton Wurzel", new NewtonIteration());
        tabMain.addTab("Non-Lin-GS", new SolveNonLinearEquation());
        tabMain.addTab("Gauss 4P", new GaussIntegrationOrder4());
        tabMain.addTab("Runge-Kutta-4O", new RungeKuttaOrder4());
        tabMain.addTab("Vektor-Iteration", new VectorIteration());
        tabMain.addTab("Optionen", new Options());
        
        this.add(tabMain);
        
        this.setSize(600, 720);
        this.setLocationRelativeTo(null);
        
        if (Configuration.getActiveConfiguration().isMaximized())
        {
            this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }
        
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                // Configuration sichern
                Configuration.getActiveConfiguration().save();
            }
            
            @Override
            public void windowClosing(WindowEvent e)
            {
                OutputFrame.this.dispose();
            }
        });
        
        this.setVisible(true);
    }
    
    
    private void initLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            ex.printStackTrace();
        }
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