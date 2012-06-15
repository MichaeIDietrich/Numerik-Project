package numerik.tasks;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import numerik.Configuration;
import numerik.calc.*;
import numerik.expression.Value;
import numerik.io.DocumentLoader;
import numerik.ui.controls.*;
import numerik.ui.dialogs.*;
import numerik.ui.misc.*;

public class Options implements Task
{
    private DocumentLoader docLoader = null;
    
    
    @Override
    public void init(final OutputFrame frame, final TaskPane taskPane)
    {
        
        docLoader = new DocumentLoader();
        
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JComboBox<String> cboChoices = new JComboBox<>(new String[] { "Einstellungen", "Matrizen", "Vektoren" });
        cboChoices.setPreferredSize(new Dimension(150, cboChoices.getPreferredSize().height));
        cboChoices.setMinimumSize(new Dimension(150, cboChoices.getPreferredSize().height));
        cboChoices.setMaximumSize(new Dimension(150, cboChoices.getPreferredSize().height));
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
        
        toolBar.add(new JLabel(" Option: "));
        toolBar.add(cboChoices);
        taskPane.setJToolBar(toolBar);
        
        setUpView(frame, taskPane, "Einstellungen");
    }
    
    private void setUpView(final OutputFrame frame, final TaskPane taskPane, String group)
    {
        JPanel pnlMain = null;
        
        switch (group)
        {
            case "Matrizen":
            case "Vektoren":
                
                final boolean isMatrix = group.equals("Matrizen");
                
                // verfügbare Matrizen / Vektoren auslesen
                String[] entries = null;
                if (isMatrix)
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
                
                lstObjects.setBorder(new LineBorder(Color.LIGHT_GRAY));
                
                // füge die Vorschaubilder zur JList hinzu
                final ArrayList<Image> imgMatrices = new ArrayList<Image>();
                final ArrayList<Image> imgVectors = new ArrayList<Image>();
                
                if (isMatrix)
                {
                    for (Matrix matrix : docLoader.readMatrices("Data.txt"))
                    {
                        imgMatrices.add(new LatexFormula().addMatrix(matrix).toImage());
                    }
                    
                    new ListItemImageToolTip(lstObjects, imgMatrices, new Color(255, 255, 150));
                }
                else
                {
                    for (Vector vector : docLoader.readVectors("Data.txt"))
                    {
                        imgVectors.add(new LatexFormula().addVector(vector).toImage());
                    }
                    
                    new ListItemImageToolTip(lstObjects, imgVectors, new Color(255, 255, 150));
                }
                
                JButton btnNew = new JButton("Neu");
                btnNew.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if (isMatrix)
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
                                imgMatrices.add(new LatexFormula().addMatrix(matrix).toImage());
                                
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
                                imgVectors.add(new LatexFormula().addVector(vector).toImage());
                                
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
                            if (isMatrix)
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
                
            case "Einstellungen":
                VerticalBox box = new VerticalBox();
                box.setBorder(new EmptyBorder(10, 10, 10, 10));
                
                JLabel lblHead = new JLabel("Einstellungen");
                lblHead.setFont(lblHead.getFont().deriveFont(20f));
                box.add(lblHead, false);
                box.addGap();
                box.addGap();
                
                box.add(new JLabel("Standardschriftgröße:"));
                
                final JSpinner spnFontSize = new JSpinner(new SpinnerNumberModel(Configuration.getActiveConfiguration().getFontSize(), 1, 100, 1));
                spnFontSize.setMaximumSize(spnFontSize.getPreferredSize());
                spnFontSize.addChangeListener(new ChangeListener()
                {
                    @Override
                    public void stateChanged(ChangeEvent e)
                    {
                        Configuration.getActiveConfiguration().setFontSize( (Integer) spnFontSize.getValue());
                    }
                });
                box.add(spnFontSize);
                
                box.addGap();
                
                final JCheckBox chkMaximized = new JCheckBox("Maximiert starten");
                chkMaximized.setSelected(Configuration.getActiveConfiguration().isMaximized());
                chkMaximized.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        Configuration.getActiveConfiguration().setMaximized(chkMaximized.isSelected());
                    }
                });
                box.add(chkMaximized);
                
                pnlMain = box;
        }
        
        taskPane.setViewPortView(pnlMain);
    }
    
    @Override
    public void run(Value... parameters) { } // wird nicht gebraucht
}