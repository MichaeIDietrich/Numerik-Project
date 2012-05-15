package numerik.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.ComboPopup;

public class ToolTippedComboBoxUI extends com.sun.java.swing.plaf.windows.WindowsComboBoxUI implements MouseMotionListener, MouseListener
{
    private int index = -1;
    private ToolTipManager toolTipManager = new ToolTipManager();
    private ArrayList<Image> images;
    private Color backGround = Color.WHITE;
    
    public ToolTippedComboBoxUI(ArrayList<Image> previewImages)
    {
        images = previewImages;
    }
    
    public ToolTippedComboBoxUI(ArrayList<Image> previewImages, Color backGround)
    {
        images = previewImages;
        this.backGround = backGround;
    }
    
    @Override
    protected ComboPopup createPopup()
    {
        ComboPopup popup = super.createPopup();
        popup.getList().addMouseMotionListener(this);
        popup.getList().addMouseListener(this);
        return popup;
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        JList<?> list = (JList<?>) e.getSource();
        
        int newIndex = list.locationToIndex(e.getPoint());
        if (newIndex != index)
        {
            index = newIndex;
            toolTipManager.showToolTip(list, e.getXOnScreen() + 16, e.getYOnScreen(), images.get(index), backGround);
        }
        
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        toolTipManager.disposeAll();
        index = -1;
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
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
}
