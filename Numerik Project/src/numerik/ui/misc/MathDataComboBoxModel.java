package numerik.ui.misc;

import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.event.*;

import numerik.ui.misc.MathDataSynchronizer.MathDataType;

public class MathDataComboBoxModel implements ComboBoxModel<String>, ChangeListener
{
    private final static MathDataSynchronizer DATA = MathDataSynchronizer.getInstance();
    
    private MathDataType type;
    
    private Object selectedItem = null;
    
    private ArrayList<ListDataListener> listDataListeners;
    
    
    public MathDataComboBoxModel(MathDataType type)
    {
        this.type = type;
        
        listDataListeners = new ArrayList<>();
    }
    
    
    @Override
    public void addListDataListener(ListDataListener l)
    {
        listDataListeners.add(l);
    }
    
    @Override
    public String getElementAt(int index)
    {
        if (type == MathDataType.MATRIX)
        {
            if (DATA.getMatrixNames().length == index)
            {
                return "...";
            }
            
            return DATA.getMatrixNames()[index];
        }
        
        if (DATA.getVectorNames().length == index)
        {
            return "...";
        }
        
        return DATA.getVectorNames()[index];
    }
    
    @Override
    public int getSize()
    {
        if (type == MathDataType.MATRIX)
        {
            return DATA.getMatrixNames().length + 1;
        }
        return DATA.getVectorNames().length + 1;
    }
    
    @Override
    public void removeListDataListener(ListDataListener l)
    {
        listDataListeners.remove(l);
    }
    
    @Override
    public Object getSelectedItem()
    {
        return selectedItem;
    }
    
    @Override
    public void setSelectedItem(Object anItem)
    {
        selectedItem = anItem;
    }


    @Override
    public void stateChanged(ChangeEvent e)
    {
        for (ListDataListener listener : listDataListeners)
        {
            listener.contentsChanged(new ListDataEvent(null, ListDataEvent.CONTENTS_CHANGED, 0, this.getSize() - 1));
        }
    }
}