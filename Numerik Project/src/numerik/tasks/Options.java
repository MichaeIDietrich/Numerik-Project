package numerik.tasks;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import numerik.calc.*;
import numerik.expression.Value;
import numerik.io.DocumentLoader;
import numerik.ui.controls.TaskPane;
import numerik.ui.dialogs.*;

public class Options implements Task
{
    private DocumentLoader docLoader = null;
    
    
    @Override
    public void init(final OutputFrame frame, final TaskPane taskPane)
    {
        
        docLoader = new DocumentLoader();
        
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JComboBox<String> cboChoices = new JComboBox<>(new String[] { "Matrizen", "Vektoren", "Sonstiges" });
        cboChoices.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    setUpView(frame, taskPane, e.getItem().toString());
                }
            }
        });
        
        toolBar.add(cboChoices);
        taskPane.setJToolBar(toolBar);
        
        setUpView(frame, taskPane, "Matrizen");
    }
    
    private void setUpView(final OutputFrame frame, final TaskPane taskPane, String group)
    {
        JPanel pnlMain = null;
        
        switch (group)
        {
            case "Matrizen":
            case "Vektoren":
                
                final boolean isMat = group.equals("Matrizen");
                
                String[] entries = null;
                if (isMat)
                {
                    entries = docLoader.getAllMatrixNames("Data.txt");
                }
                else
                {
                    entries = docLoader.getAllVectorNames("Data.txt");
                }
                
                // hier wird extra umständlich ein Model angelegt, um Standardfunktionen einer Liste zu ermöglichen...
                final DefaultListModel<String> listModel = new DefaultListModel<>();
                for (String name : entries)
                {
                    listModel.addElement(name);
                }
                final JList<String> lstObjects = new JList<>(listModel);
                
                lstObjects.setBorder(new LineBorder(Color.DARK_GRAY));
                
                JButton btnNew = new JButton("Neu");
                btnNew.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if (isMat)
                        {
                            Matrix matrix = NewMatrixWindow.createNewMatrix(frame, MouseInfo.getPointerInfo().getLocation());
                            
                            if (matrix == null)
                            {
                                lstObjects.setSelectedIndex(0);
                            }
                            else if ((matrix.name = JOptionPane.showInputDialog(frame, "Bitte geben Sie den Namen der Matrix ein.", "Matrixname", JOptionPane.QUESTION_MESSAGE)) 
                                    != null && !matrix.name.equals(""))
                            {
                                new DocumentLoader().addMatrixToFile(matrix, "Data.txt");
                                int index = listModel.getSize();
                                listModel.add(index, matrix.name);
                                lstObjects.setSelectedIndex(index);
    //                            lstObjects.add(new LatexFormula().addMatrix(matrix).toImage());
                                
                            }
                        }
                        else
                        {
                            Vector vector = NewVectorWindow.createNewVector(frame, MouseInfo.getPointerInfo().getLocation());
                            
                            if (vector == null)
                            {
                                lstObjects.setSelectedIndex(0);
                            }
                            else if ((vector.name = JOptionPane.showInputDialog(frame, "Bitte geben Sie den Namen des Vektors ein.", "Vektorname", JOptionPane.QUESTION_MESSAGE)) 
                                    != null && !vector.name.equals(""))
                            {
                                new DocumentLoader().addVectorToFile(vector, "Data.txt");
                                int index = listModel.size();
                                listModel.add(index, vector.name);
                                lstObjects.setSelectedIndex(index);
//                                imgVectors.add(new LatexFormula().addVector(vector).toImage());
                                
                            }

                        }
                    }
                });
                
                JButton btnRemove = new JButton("Entfernen");
                btnRemove.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        String item = lstObjects.getSelectedValue();
                        
                        if (item != null)
                        {
                            System.out.println("delete: " + item);
                            if (isMat)
                            {
                                docLoader.deleteMatrix(item, "Data.txt");
                            }
                            else
                            {
                                docLoader.deleteVector(item, "Data.txt");
                            }
                            
                            listModel.removeElement(item);
                            lstObjects.setSelectedIndex(0);
                        }
                    }
                });
                
                JPanel pnlButtons = new JPanel();
                pnlButtons.add(btnNew);
                pnlButtons.add(btnRemove);
                
                pnlMain = new JPanel(new BorderLayout());
                pnlMain.setBorder(new EmptyBorder(10, 10, 5, 10));
                pnlMain.add(lstObjects);
                pnlMain.add(pnlButtons, BorderLayout.PAGE_END);
                break;
                
            case "Sonstiges":
                pnlMain = new JPanel();
                pnlMain.add(new JLabel("folgt noch^^"));
        }
        
        taskPane.setViewPortView(pnlMain);
    }
    
    @Override
    public void run(Value... parameters)
    {
        
    }
}