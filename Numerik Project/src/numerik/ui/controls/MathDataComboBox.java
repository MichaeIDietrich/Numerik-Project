package numerik.ui.controls;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import numerik.calc.Matrix;
import numerik.calc.Vector;
import numerik.ui.dialogs.NewMatrixWindow;
import numerik.ui.dialogs.NewVectorWindow;
import numerik.ui.misc.*;
import numerik.ui.misc.MathDataSynchronizer.MathDataType;

public final class MathDataComboBox extends JComboBox<String>
{
    public MathDataComboBox(final MathDataType type, final JFrame owner)
    {
        super(new MathDataComboBoxModel(type));
        
        this.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getItem().toString().equals("...") && e.getStateChange() == ItemEvent.SELECTED)
                {
                    if (type == MathDataType.MATRIX)
                    {
                        Matrix matrix = NewMatrixWindow.createNewMatrix(owner, MathDataComboBox.this.getLocationOnScreen());
                        
                        if (matrix == null)
                        {
                            MathDataComboBox.this.setSelectedIndex(0);
                        }
                        else if ((matrix.name = JOptionPane.showInputDialog(owner, "Bitte geben Sie den Namen der Matrix ein.", "Matrixname", JOptionPane.QUESTION_MESSAGE)) 
                                != null && !matrix.name.equals(""))
                        {
                            MathDataSynchronizer.getInstance().add(matrix);
                            MathDataComboBox.this.setSelectedItem(matrix.name);
                        }
                    }
                    else
                    {
                        Vector vector = NewVectorWindow.createNewVector(owner, MathDataComboBox.this.getLocationOnScreen());
                        
                        if (vector == null)
                        {
                            MathDataComboBox.this.setSelectedIndex(0);
                        }
                        else if ((vector.name = JOptionPane.showInputDialog(owner, "Bitte geben Sie den Namen des Vektors ein.", "Vektorname", JOptionPane.QUESTION_MESSAGE)) 
                                != null && !vector.name.equals(""))
                        {
                            MathDataSynchronizer.getInstance().add(vector);
                            MathDataComboBox.this.setSelectedItem(vector.name);
                        }
                    }
                }
            }
        });
        
        this.setSelectedIndex(0);
    }
}