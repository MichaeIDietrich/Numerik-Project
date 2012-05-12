package numerik.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.*;
import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.Value;
import numerik.io.DocumentLoader;
import numerik.tasks.*;
import numerik.tasks.Argument.ArgType;

    
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
        JTextField text;
        JCheckBox check;
        JButton button;
        
        args = arguments;
        for (Argument arg : arguments)
        {
            if (arg.getName() != null && arg.getArgumentType() != ArgType.BOOLEAN)
            {
                toolBar.add(new JLabel(arg.getName()));
            }
            switch (arg.getArgumentType())
            {
                case MATRIX:
                    combo = new JComboBox(matrices);
                    combo.setMaximumSize(new Dimension(100, combo.getPreferredSize().height));
                    arg.setRelatedControl(combo);
                    toolBar.add(combo);
                    break;
                    
                case VECTOR:
                    combo = new JComboBox(vectors);
                    combo.setMaximumSize(new Dimension(100, combo.getPreferredSize().height));
                    arg.setRelatedControl(combo);
                    toolBar.add(combo);
                    break;
                    
                case DECIMAL:
                    text = new JTextField(arg.getDefaultValue());
                    text.setMaximumSize(new Dimension(100, text.getPreferredSize().height));
                    arg.setRelatedControl(text);
                    toolBar.add(text);
                    break;
                    
                case INTEGER:
                    text = new JTextField(arg.getDefaultValue());
                    arg.setRelatedControl(text);
                    toolBar.add(text);
                    break;
                    
                case BOOLEAN:
                    check = new JCheckBox(arg.getName() ,arg.getDefaultValue().equals("true"));
                    arg.setRelatedControl(check);
                    toolBar.add(check);
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
    
    
    public void runTask()
    {
        try
        {
            task.run(getParameters());
        }
        catch (IllegalArgumentException ex)
        {
            JLabel label = new JLabel(ex.getMessage());
            label.setForeground(Color.RED);
            setViewPortView(label);
            
            ex.printStackTrace();
        }
        catch (ArithmeticException ex)
        {
            JLabel label = new JLabel(ex.getMessage());
            label.setForeground(Color.RED);
            setViewPortView(label);
            
            ex.printStackTrace();
        }
        catch (IndexOutOfBoundsException ex)
        {
            JLabel label = new JLabel(ex.getMessage());
            label.setForeground(Color.RED);
            setViewPortView(label);
            
            ex.printStackTrace();
        }
    }
    
    
    public void showTask()
    {
        frame.setJToolBar(toolBar);
        runTask();
    }
    
    
    private Value[] getParameters() throws IllegalArgumentException
    {
        if(args == null)
        {
            return null;
        }
        
        ArrayList<Value> parameters = new ArrayList<Value>();
        
        for (Argument arg : args)
        {
            try
            {
                switch (arg.getArgumentType())
                {
                    case MATRIX:
                        parameters.add(new Value(new Matrix("Data.txt", ((JComboBox)arg.getRelatedControl()).getSelectedItem().toString())));
                        break;
                        
                    case VECTOR:
                        parameters.add(new Value(new Vector("Data.txt", ((JComboBox)arg.getRelatedControl()).getSelectedItem().toString())));
                        break;
                        
                    case DECIMAL:
                        parameters.add(new Value(new BigDecimal(((JTextField)arg.getRelatedControl()).getText())));
                        break;
                        
                    case INTEGER:
                        parameters.add(new Value(new BigDecimal(Integer.parseInt(((JTextField)arg.getRelatedControl()).getText()))));
                        break;
                        
                    case BOOLEAN:
                        parameters.add(new Value(((JCheckBox)arg.getRelatedControl()).isSelected()));
                }
            }
            catch (NullPointerException ex)
            {
                throw new IllegalArgumentException("Überprüfen Sie die Eingabe von " + arg.getName());
            }
            catch (NumberFormatException ex)
            {
                throw new IllegalArgumentException(arg.getName() + " entspricht nicht der gewünschten Eingabe.");
            }
        }
        
        return parameters.toArray(new Value[parameters.size()]);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        runTask();
    }
    
    
    public void setViewPortView(JComponent component)
    {
        this.removeAll();
        this.add(component);
        this.revalidate();
    }
}
