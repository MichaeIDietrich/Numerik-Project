package numerik.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;

public class ExpandButton extends JButton implements ActionListener
{
    
    private ImageComponent img;
    private boolean expanded;
    
    public ExpandButton(ImageComponent relatedImage)
    {
        super("+");
        this.setMargin(new Insets(0, 0, 0 ,0));
        int size = this.getPreferredSize().height;
        this.setMaximumSize(new Dimension(size, size));
        this.setToolTipText("Zwischenschritte einblenden");
        this.addActionListener(this);
        img = relatedImage;
        expanded = false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Container parent = this.getParent();
        if (expanded)
        {
            
            parent.remove(img);
            
            this.setText("+");
            this.setToolTipText("Zwischenschritte einblenden");
            
        }
        else
        {
            parent.add(img, getComponentIndex() + 1);
            
            this.setText("-");
            this.setToolTipText("Zwischenschritte ausblenden");
            
        }
        
        parent.repaint();
        parent.validate();
        expanded = !expanded;
    }
    
    private int getComponentIndex()
    {
        Container c = this.getParent();
        for (int i = 0; i < c.getComponentCount(); i++)
        {
            if (c.getComponent(i) == this)
                return i;
        }
        return -1;
    }
    
}
