package numerik.ui.misc;

import java.awt.Component;

import javax.swing.*;

public final class VerticalBox extends JPanel
{
    public VerticalBox()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    
    public Component add(JComponent comp)
    {
        return add(comp, true);
    }
    
    public Component add(JComponent comp, boolean adjust)
    {
        if (adjust)
        {
            comp.setMaximumSize(comp.getPreferredSize());
            comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        
        return super.add(comp);
    }
    
    public void addGap()
    {
        super.add(Box.createVerticalStrut(10));
    }
}