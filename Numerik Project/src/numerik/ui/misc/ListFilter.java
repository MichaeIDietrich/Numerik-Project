package numerik.ui.misc;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultListModel;

public class ListFilter extends DefaultListModel<String>
{
    Collection<String> items;
    java.util.List<String> filteredItems;
    String filter;
    
    
    public ListFilter(Collection<String> list)
    {
        items = list;
        filter = "";
        filterList();
    }
    
    
    public void setList(Collection<String> list)
    {
        items = list;
        filterList();
    }
    
    
    public void setFilter(String filter)
    {
        this.filter = filter.toLowerCase();
        filterList();
    }
    
    
    public String getFilter()
    {
        return filter;
    }
    
    
    @Override
    public String getElementAt(int index)
    {
        return filteredItems.get(index);
    }
    
    
    @Override
    public int getSize()
    {
        return filteredItems.size();
    }
    
    
    private void filterList()
    {
        filteredItems = new ArrayList<String>();
        if (filter.equals(""))
        {
            for (String s : items)
                filteredItems.add(s);
            return;
        }
        for (String s : items)
        {
            if (s.toLowerCase().indexOf(filter) > -1)
            {
                filteredItems.add(s);
            }
        }
    }
    
    
    public Collection<String> getList()
    {
        return items;
    }
}