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
        
        final TabbedTaskPane tabMain = new TabbedTaskPane(this);
        tabMain.addTab("Expression", new ExpressionTask(), "Eingabebereich, um mathematische Ausdrücke auszuwerten");
        tabMain.addTab("Dezimal->Binär", new DecimalToBinary());
        tabMain.addTab("LU-Zerlegung", new LUDecomposition());
        tabMain.addTab("Eingabefehler-Abschätzung", new InputErrorEstimation());
        tabMain.addTab("Jakobi-Iteration", new JacobiIteration());
        tabMain.addTab("Newton Wurzel", new NewtonIteration());
//        tabMain.addTab("Non-Lin-GS", new SolveNonLinearEquation2());
        tabMain.addTab("Non-Lin-GS (Expr)", new SolveNonLinearEquationExpr());
        tabMain.addTab("Gauss 4P", new GaussIntegrationOrder4());
        tabMain.addTab("Gauss 4P (Expr)", new GaussIntegrationOrder4Expr());
        tabMain.addTab("Runge-Kutta-4O", new RungeKuttaOrder4());
        tabMain.addTab("Runge-Kutta-4O (Expr)", new RungeKuttaOrder4Expr());
        tabMain.addTab("Vektor-Iteration", new VectorIteration());
        tabMain.addTab("Spielwiese", new CustomTask(), "Hier kann mein seine eigenen Berechnungen etc. einfügen", "VIEL SPAẞ");
        tabMain.addTab("Optionen", new Options(), "Einstellungen vornehmen", "Matrizen / Vektoren hinzufügen oder löschen");
        
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
        
        // Möglichkeit in den Tasks zu zoomen mit STRG + Mausrad
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener()
        {
            @Override
            public void eventDispatched(AWTEvent event)
            {
                if (event instanceof MouseWheelEvent)
                {
                    MouseWheelEvent mwEvent = (MouseWheelEvent) event;
                    
                    if (mwEvent.isControlDown() && mwEvent.getWheelRotation() != 0)
                    {
                        int fontSize = Configuration.getActiveConfiguration().getFontSize();
                        if (fontSize < 100 && mwEvent.getWheelRotation() == -1 || fontSize > 1 && mwEvent.getWheelRotation() == 1)
                        {
                            Configuration.getActiveConfiguration().setFontSize(fontSize - mwEvent.getWheelRotation());
                            tabMain.runActiveTask();
                        }
                    }
                }
            }
        }, AWTEvent.MOUSE_WHEEL_EVENT_MASK);
        
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