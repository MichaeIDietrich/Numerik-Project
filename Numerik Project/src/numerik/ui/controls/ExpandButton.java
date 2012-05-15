package numerik.ui.controls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ExpandButton extends JComponent implements MouseListener
{
    
    private ImageComponent imgComponent;
    private boolean expanded;
    
    private static Image imgCollapse = new ImageIcon("icons/collapse16.png").getImage();
    private static Image imgExpand = new ImageIcon("icons/expand16.png").getImage();
    
    public ExpandButton(ImageComponent relatedImage)
    {
        //super("+");
        //this.setMargin(new Insets(0, 0, 0 ,0));
        //int size = this.getPreferredSize().height;
        //this.setMaximumSize(new Dimension(size, size));
        this.setMaximumSize(new Dimension(10000, 16));
        this.setMinimumSize(new Dimension(16, 16));
        this.setPreferredSize(new Dimension(16, 16));
        this.setToolTipText("Zwischenschritte einblenden");
        //this.addActionListener(this);
        this.addMouseListener(this);
        imgComponent = relatedImage;
        expanded = false;
    }
    
    /*@Override
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
    }*/
    
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
    public void mouseClicked(MouseEvent e)
    {
        Container parent = this.getParent();
        if (expanded)
        {
            parent.remove(imgComponent);
            this.setToolTipText("Zwischenschritte einblenden");
        }
        else
        {
            parent.add(imgComponent, getComponentIndex() + 1);
            this.setToolTipText("Zwischenschritte ausblenden");
        }
        expanded = !expanded;
        parent.repaint();
        parent.validate();
        imgComponent.repaint();
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
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
