package numerik.ui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class ToolTipManager
{
    ArrayList<ImageToolTip> toolTips = new ArrayList<ImageToolTip>();
    
    class ImageToolTip
    {
        private Popup popup;
        
        public ImageToolTip(Component parent, int x, int y, Image image)
        {
            popup = PopupFactory.getSharedInstance().getPopup(parent, new ImageComponent(image), x, y);
            popup.show();
        }
        
        public ImageToolTip(Component parent, int x, int y, Image image, Color backGround)
        {
            popup = PopupFactory.getSharedInstance().getPopup(parent, new ImageComponent(image, backGround), x, y);
            popup.show();
        }
        
        public ImageToolTip(Component parent, int x, int y, Image image, Color backGround, boolean framed)
        {
            popup = PopupFactory.getSharedInstance().getPopup(parent, new ImageComponent(image, backGround, framed), x, y);
            popup.show();
        }
        
        public void dispose()
        {
            popup.hide();
        }
    }
    
    public void showToolTip(Component parent, int x, int y, Image image)
    {
        disposeAll();
        toolTips.add(new ImageToolTip(parent, x, y, image));
    }
    
    public void showToolTip(Component parent, int x, int y, Image image, Color backGround)
    {
        disposeAll();
        toolTips.add(new ImageToolTip(parent, x, y, image, backGround, true));
    }
    
    public void disposeAll()
    {
        for (ImageToolTip t : toolTips)
        {
            t.dispose();
        }
    }
}