package numerik.ui.controls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public final class ExpandButton extends JComponent
{
    
    private ImageComponent imgComponent;
    private boolean expanded;
    
    private static Image imgCollapse = new ImageIcon("icons/collapse16.png").getImage();
    private static Image imgExpand = new ImageIcon("icons/expand16.png").getImage();
    
    
    public ExpandButton(ImageComponent relatedImage)
    {
        this.setMaximumSize(new Dimension(10000, 16));
        this.setMinimumSize(new Dimension(16, 16));
        this.setPreferredSize(new Dimension(16, 16));
        this.setToolTipText("Zwischenschritte einblenden");
        
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Container parent = ExpandButton.this.getParent();
                if (expanded)
                {
                    parent.remove(imgComponent);
                    ExpandButton.this.setToolTipText("Zwischenschritte einblenden");
                }
                else
                {
                    parent.add(imgComponent, getComponentIndex() + 1);
                    ExpandButton.this.setToolTipText("Zwischenschritte ausblenden");
                }
                expanded = !expanded;
                parent.revalidate();
                parent.repaint();
                imgComponent.repaint();
            }
            
        });
        imgComponent = relatedImage;
        expanded = false;
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
    
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        if (expanded)
        {
            g.drawImage(imgExpand, 0, 0, this);
        }
        else
        {
            g.drawImage(imgCollapse, 0, 0, this);
        }
    }
}