package numerik.ui.controls;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.JList;

import numerik.ui.dialogs.ImagePopup;
import numerik.ui.misc.PopupManager;

public final class ListItemImageToolTip
{
    private int index = -1;
    
    
    public ListItemImageToolTip(final JList<?> list, final List<Image> previewImages, final Color popupBackground)
    {
        list.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                int newIndex = list.locationToIndex(e.getPoint());
                if (newIndex != index)
                {
                    if (newIndex >= previewImages.size())
                    {
                        PopupManager.getInstance().disposeAll();
                        index = -1;
                        return;
                    }
                    index = newIndex;
                    new ImagePopup(list, e.getXOnScreen() + 16, e.getYOnScreen(), previewImages.get(index), popupBackground, true);
                }
            }
        });
        
        list.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                PopupManager.getInstance().disposeAll();
                index = -1;
            }
            
            @Override
            public void mouseExited(MouseEvent e)
            {
                PopupManager.getInstance().disposeAll();
                index = -1;
            }
        });
    }
}