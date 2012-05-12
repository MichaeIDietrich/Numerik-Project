package numerik.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

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
        
        /*TaskPane lu_decompos = new TaskPane( new LUDecomposition().getFormula() ); 
        TaskPane newton_iter = new TaskPane( new NewtonIteration().getFormula() ); 
        TaskPane non_lin_equ = new TaskPane( new SolveNonLinearEquation().getFormula() ); 
        TaskPane gauss_int4o = new TaskPane( new GaussIntegrationOrder4().getFormula() );
        TaskPane rungkutta4o = new TaskPane( new GaussIntegrationOrder4().getFormula() );*/
        
        // --- Tab-Pane ---
        
        //tabMain = new JTabbedPane( JTabbedPane.BOTTOM );

        //tabMain.addTab("Expression", pnlExpression);
        /*tabMain.addTab("LU-Zerlegung",    lu_decompos);
        tabMain.addTab("Newton Wurzel",   newton_iter);
        tabMain.addTab("Non-Lin-GS",      non_lin_equ);
        tabMain.addTab("Gauss Int. 4",    gauss_int4o);
        tabMain.addTab("Runge Kutta 4",   rungkutta4o);*/
        
        //this.add( tabMain );
        
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
    }
}