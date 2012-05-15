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
        toolBar.setLayout( new FlowLayout(FlowLayout.LEFT));
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        //toolBar.setMaximumSize(new Dimension(10000, 15));
        
        DocumentLoader docLoader = new DocumentLoader();
        String[] matrices = docLoader.getAllMatrixNames("Data.txt");
        String[] vectors = docLoader.getAllVectorNames("Data.txt");
        ArrayList<Image> imgMatrices = new ArrayList<Image>();
        for (Matrix matrix : docLoader.readMatrices("Data.txt"))
        {
            imgMatrices.add(new LatexFormula().addMatrix(matrix).toImage());
        }
        ArrayList<Image> imgVectors = new ArrayList<Image>();
        for (Vector vector : docLoader.readVectors("Data.txt"))
        {
            imgVectors.add(new LatexFormula().addVector(vector).toImage());
        }
        
        JComboBox<String> combo;
        JTextField text;
        JCheckBox check;
        JSpinner spinner;
        SpinnerModel model;
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
                    combo = new JComboBox<String>(matrices);
                    combo.setUI(new ToolTippedComboBoxUI(imgMatrices, new Color(255, 255, 150)));
//                    combo.setMinimumSize(new Dimension(70, 0));
                    arg.setRelatedControl(combo);
                    toolBar.add(combo);
                    break;
                    
                case VECTOR:
                    combo = new JComboBox<String>(vectors);
                    combo.setUI(new ToolTippedComboBoxUI(imgVectors, new Color(255, 255, 150)));
//                    combo.setMinimumSize(new Dimension(70, 0));
                    arg.setRelatedControl(combo);
                    toolBar.add(combo);
                    break;
                    
                case DECIMAL:
                    text = new JTextField(arg.getDefaultValue());
//                    text.setMinimumSize(new Dimension(70, 0));
                    arg.setRelatedControl(text);
                    toolBar.add(text);
                    break;
                    
                case INTEGER:
                    text = new JTextField(arg.getDefaultValue());
//                    text.setMinimumSize(new Dimension(70, 0));
                    arg.setRelatedControl(text);
                    toolBar.add(text);
                    break;
                    
                case BOOLEAN:
                    check = new JCheckBox(arg.getName() ,arg.getDefaultValue().equals("true"));
                    arg.setRelatedControl(check);
                    toolBar.add(check);
                    break;
                    
                case PRECISION:
                    model = new SpinnerNumberModel(Integer.parseInt(arg.getDefaultValue()), 1, 100, 1);
                    spinner = new JSpinner(model);
                    arg.setRelatedControl(spinner);
                    toolBar.add(spinner);
                    break;
                    
                case DOUBLEPRECISION:
                    model = new SpinnerNumberModel(Integer.parseInt(arg.getDefaultValue()), 1, 16, 1);
                    spinner = new JSpinner(model);
                    arg.setRelatedControl(spinner);
                    toolBar.add(spinner);
                    break;
                    
                case RUN_BUTTON:
                    button = new JButton(new ImageIcon("icons/button_go_small.png"));
                    button.addActionListener(this);
                    toolBar.add(button);
            }
            
            toolBar.add(new JLabel(" "));
        }
    }
    
    
    public void setJToolBar(JToolBar toolBar)
    {
        this.toolBar = toolBar;
    }
    
    
    public void runTask()
    {
        Thread taskThread = new Thread()
        {
            @Override
            public void run()
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
                    
                    //ex.printStackTrace();
                }
                catch (ArithmeticException ex)
                {
                    JLabel label = new JLabel(ex.getMessage());
                    label.setForeground(Color.RED);
                    setViewPortView(label);
                    
                    //ex.printStackTrace();
                }
                catch (IndexOutOfBoundsException ex)
                {
                    JLabel label = new JLabel(ex.getMessage());
                    label.setForeground(Color.RED);
                    setViewPortView(label);
                    
                    //ex.printStackTrace();
                }
            }
        };
        
//        taskThread.start();
        taskThread.run();
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
                        parameters.add(new Value(new Matrix("Data.txt", ((JComboBox<?>)arg.getRelatedControl()).getSelectedItem().toString())));
                        break;
                        
                    case VECTOR:
                        parameters.add(new Value(new Vector("Data.txt", ((JComboBox<?>)arg.getRelatedControl()).getSelectedItem().toString())));
                        break;
                        
                    case DECIMAL:
                        parameters.add(new Value(new BigDecimal(((JTextField)arg.getRelatedControl()).getText())));
                        break;
                        
                    case INTEGER:
                        parameters.add(new Value(new BigDecimal(Integer.parseInt(((JTextField)arg.getRelatedControl()).getText()))));
                        break;
                        
                    case BOOLEAN:
                        parameters.add(new Value(((JCheckBox)arg.getRelatedControl()).isSelected()));
                        break;
                        
                    case PRECISION:
                    case DOUBLEPRECISION:
                        parameters.add(new Value(new BigDecimal((Integer)((JSpinner)arg.getRelatedControl()).getValue())));
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
