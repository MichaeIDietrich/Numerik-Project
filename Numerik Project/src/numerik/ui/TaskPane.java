package numerik.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import numerik.IO.DocumentLoader;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.Value;
import numerik.tasks.Argument;
import numerik.tasks.Task;

/*import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

import numerik.tasks.Argument;
import numerik.tasks.Task;

public class TaskPane extends JPanel
{
    JButton         btnTest;
    JToolBar        toolBar;
    TaskScrollPane  scrollpane;
    JComboBox       combobox;
    
    public TaskPane( LatexFormula formula ) 
    {   
        this.setLayout(new BorderLayout());
        this.setBackground( Color.getHSBColor(100, 50, 25) );
        this.setBorder(new javax.swing.border.EtchedBorder());
        
        btnTest = new JButton();
        btnTest.setIcon(new ImageIcon("icons/button_go_small.png"));

        Vector<String> list = new Vector<String>();
        list.add("Matrix A");
        list.add("Matrix B");
        list.add("Matrix C");
        list.add("Matrix D");
        
        JComboBox cb = new JComboBox( list );
        cb.setMaximumSize(new Dimension(100,24));
        
        toolBar = new JToolBar();
        toolBar.setFloatable( false );
        toolBar.setBackground( Color.getHSBColor(100, 50, 25) );
        
        toolBar.add(cb);
        toolBar.addSeparator( new Dimension( 12, 32) );
        toolBar.add( btnTest );
        
        
        scrollpane = new TaskScrollPane( formula );
        
        this.add( toolBar, BorderLayout.PAGE_START );
        this.add( scrollpane );
        
        //BoxLayout box = new BoxLayout( this , BoxLayout.Y_AXIS);
        //setLayout( box );
    }
    
}*/
    
public final class TaskPane extends JPanel implements ActionListener
{
    
    private JToolBar toolBar;
    private OutputFrame frame;
    private Task task;
    private Argument[] args;
    
    
    public TaskPane(OutputFrame frame, Task task, boolean scrollable)
    {
        this.frame = frame;
        this.task = task;
        
        this.setLayout(new BorderLayout());
        
        task.init(frame, this);
    }
    
    
    public void createJToolBarByArguments(Argument... arguments)
    {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        DocumentLoader docLoader = new DocumentLoader();
        String[] matrices = docLoader.getAllMatrixNames("Data.txt");
        String[] vectors = docLoader.getAllVectorNames("Data.txt");
        
        JComboBox combo;
        JButton button;
        
        args = arguments;
        for (Argument arg : arguments)
        {
            switch (arg.getArgumentType())
            {
                case MATRIX:
                    toolBar.add(new JLabel(arg.getName()));
                    combo = new JComboBox(matrices);
                    arg.setRelatedControl(combo);
                    toolBar.add(combo);
                    break;
                case VECTOR:
                    toolBar.add(new JLabel(arg.getName()));
                    combo = new JComboBox(vectors);
                    arg.setRelatedControl(combo);
                    toolBar.add(combo);
                    
                    break;
                case DECIMAL:
                    
                    break;
                case RUN_BUTTON:
                    button = new JButton(new ImageIcon("icons/button_go_small.png"));
                    button.addActionListener(this);
                    toolBar.add(button);
            }
        }
    }
    
    
    public void setJToolBar(JToolBar toolBar)
    {
        this.toolBar = toolBar;
    }
    
    
    public void showTask()
    {
        frame.setJToolBar(toolBar);
        task.run(getParameters());
    }
    
    
    private Value[] getParameters()
    {
        if(args == null)
        {
            return null;
        }
        
        ArrayList<Value> parameters = new ArrayList<Value>();
        
        for (Argument arg : args)
        {
            switch (arg.getArgumentType())
            {
                case MATRIX:
                    parameters.add(new Value(new Matrix("Data.txt", ((JComboBox)arg.getRelatedControl()).getSelectedItem().toString())));
                    break;
                case VECTOR:
                    parameters.add(new Value(new Vector("Data.txt", ((JComboBox)arg.getRelatedControl()).getSelectedItem().toString())));
                    break;
            }
        }
        
        return parameters.toArray(new Value[parameters.size()]);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        task.run(getParameters());
    }
    
}