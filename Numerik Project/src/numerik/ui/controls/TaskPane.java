
package numerik.ui.controls;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.*;

import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.expression.Value;
import numerik.io.DocumentLoader;
import numerik.tasks.*;
import numerik.tasks.Argument.ArgType;
import numerik.ui.dialogs.*;
import numerik.ui.misc.LatexFormula;
import numerik.ui.misc.WrappingToolbarLayout;

    
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
        //toolBar.setLayout( new FlowLayout(FlowLayout.LEFT));
        toolBar.setLayout(new WrappingToolbarLayout(WrappingToolbarLayout.LEFT));
        toolBar.setFloatable(false);
        
        DocumentLoader docLoader = new DocumentLoader();
        String[] matrices = docLoader.getAllMatrixNames("Data.txt");
        String[] vectors = docLoader.getAllVectorNames("Data.txt");
        final ArrayList<Image> imgMatrices = new ArrayList<Image>();
        for (Matrix matrix : docLoader.readMatrices("Data.txt"))
        {
            imgMatrices.add(new LatexFormula().addMatrix(matrix).toImage());
        }
        final ArrayList<Image> imgVectors = new ArrayList<Image>();
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
        JPanel pnlGroup = null;
        
        args = arguments;
        for (Argument arg : arguments)
        {
            if (arg.getName() != null && arg.getArgumentType() != ArgType.BOOLEAN)
            {
                pnlGroup = new JPanel();
                pnlGroup.add(new JLabel(arg.getName()));
            }
            
            switch (arg.getArgumentType())
            {
                case MATRIX:
                    combo = new JComboBox<String>(matrices);
                    combo.insertItemAt("...", matrices.length);
                    
                    combo.addItemListener(new ItemListener()
                    {
                        @Override
                        public void itemStateChanged(ItemEvent e)
                        {
                            if (e.getItem().toString().equals("...") && e.getStateChange() == ItemEvent.SELECTED)
                            {
                                @SuppressWarnings("unchecked")
                                JComboBox<String> combo = (JComboBox<String>) e.getSource();
                                
                                Matrix matrix = NewMatrixWindow.createNewMatrix(frame, combo.getLocationOnScreen());
                                
                                if (matrix == null)
                                {
                                    combo.setSelectedIndex(0);
                                }
                                else if ((matrix.name = JOptionPane.showInputDialog(frame, "Bitte geben Sie den Namen der Matrix ein.", "Matrixname", JOptionPane.QUESTION_MESSAGE)) 
                                        != null && !matrix.name.equals(""))
                                {
                                    new DocumentLoader().addMatrixToFile(matrix, "Data.txt");
                                    int index = combo.getItemCount() - 1;
                                    combo.insertItemAt(matrix.name, index);
                                    combo.setSelectedIndex(index);
                                    imgMatrices.add(new LatexFormula().addMatrix(matrix).toImage());
                                    
                                }
                            }
                        }
                    });
                    
                    new ToolTippedComboBox(combo, imgMatrices, new Color(255, 255, 150));
                    combo.setPreferredSize(new Dimension(arg.getControlWidth(), combo.getPreferredSize().height));
                    arg.setRelatedControl(combo);
                    pnlGroup.add(combo);
                    toolBar.add(pnlGroup);
                    break;
                    
                case VECTOR:
                    combo = new JComboBox<String>(vectors);
                    combo.insertItemAt("...", vectors.length);
                    
                    combo.addItemListener(new ItemListener()
                    {
                        @Override
                        public void itemStateChanged(ItemEvent e)
                        {
                            if (e.getItem().toString().equals("...") && e.getStateChange() == ItemEvent.SELECTED)
                            {
                                @SuppressWarnings("unchecked")
                                JComboBox<String> combo = (JComboBox<String>) e.getSource();
                                
                                Vector vector = NewVectorWindow.createNewVector(frame, combo.getLocationOnScreen());
                                
                                if (vector == null)
                                {
                                    combo.setSelectedIndex(0);
                                }
                                else if ((vector.name = JOptionPane.showInputDialog(frame, "Bitte geben Sie den Namen des Vektors ein.", "Vektorname", JOptionPane.QUESTION_MESSAGE)) 
                                        != null && !vector.name.equals(""))
                                {
                                    new DocumentLoader().addVectorToFile(vector, "Data.txt");
                                    int index = combo.getItemCount() - 1;
                                    combo.insertItemAt(vector.name, index);
                                    combo.setSelectedIndex(index);
                                    imgVectors.add(new LatexFormula().addVector(vector).toImage());
                                    
                                }
                            }
                        }
                    });
                    
                    new ToolTippedComboBox(combo, imgVectors, new Color(255, 255, 150));
                    combo.setPreferredSize(new Dimension(arg.getControlWidth(), combo.getPreferredSize().height));
                    arg.setRelatedControl(combo);
                    pnlGroup.add(combo);
                    toolBar.add(pnlGroup);
                    break;
                    
                case DECIMAL:
                    text = new JTextField(arg.getDefaultValue());
                    text.setPreferredSize(new Dimension(arg.getControlWidth(), text.getPreferredSize().height));
                    text.setHorizontalAlignment(JTextField.RIGHT);
                    arg.setRelatedControl(text);
                    pnlGroup.add(text);
                    toolBar.add(pnlGroup);
                    break;
                    
                case INTEGER:
                    text = new JTextField(arg.getDefaultValue());
                    text.setPreferredSize(new Dimension(arg.getControlWidth(), text.getPreferredSize().height));
                    text.setHorizontalAlignment(JTextField.RIGHT);
                    arg.setRelatedControl(text);
                    pnlGroup.add(text);
                    toolBar.add(pnlGroup);
                    break;
                    
                case BOOLEAN:
                    check = new JCheckBox(arg.getName() ,arg.getDefaultValue().equals("true"));
                    arg.setRelatedControl(check);
                    toolBar.add(check);
                    break;
                    
                case CHOICE:
                    combo = new JComboBox<String>(arg.getChoices());
                    arg.setRelatedControl(combo);
                    pnlGroup.add(combo);
                    toolBar.add(pnlGroup);
                    break;
                    
                case PRECISION:
                    model = new SpinnerNumberModel(Integer.parseInt(arg.getDefaultValue()), 1, 100, 1);
                    spinner = new JSpinner(model);
                    spinner.setPreferredSize(new Dimension(arg.getControlWidth(), spinner.getPreferredSize().height));
                    arg.setRelatedControl(spinner);
                    pnlGroup.add(spinner);
                    toolBar.add(pnlGroup);
                    break;
                    
                case DOUBLEPRECISION:
                    model = new SpinnerNumberModel(Integer.parseInt(arg.getDefaultValue()), 1, 16, 1);
                    spinner = new JSpinner(model);
                    spinner.setPreferredSize(new Dimension(arg.getControlWidth(), spinner.getPreferredSize().height));
                    arg.setRelatedControl(spinner);
                    pnlGroup.add(spinner);
                    toolBar.add(pnlGroup);
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
                catch (IllegalArgumentException | ArithmeticException | IndexOutOfBoundsException ex)
                {
                    JLabel label = new JLabel(ex.getMessage());
                    label.setForeground(Color.RED);
                    setViewPortView(label);
                    
                    //ex.printStackTrace();
                }
            }
        };
        
        taskThread.start(); //asynchron
//        taskThread.run(); //snychron
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
                        
                    case CHOICE:
                        parameters.add(new Value(((JComboBox<?>)arg.getRelatedControl()).getSelectedItem().toString()));
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
        this.repaint();
    }
}