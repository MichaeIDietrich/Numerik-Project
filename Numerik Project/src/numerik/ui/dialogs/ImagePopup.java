package numerik.ui.dialogs;

import java.awt.*;

import javax.swing.*;

import numerik.ui.controls.ImageComponent;
import numerik.ui.misc.*;

public class ImagePopup implements PopupFrame
{
    private Popup popup;
    
    
    public ImagePopup(Component parent, int x, int y, Image image)
    {
        this(parent, x, y, image, Color.WHITE, false);
    }
    
    
    public ImagePopup(Component parent, int x, int y, Image image, Color backGround)
    {
        this(parent, x, y, image, backGround, false);
    }
    
    
    public ImagePopup(Component parent, int x, int y, Image image, Color backGround, boolean framed)
    {
        PopupManager.getInstance().disposeAll();
        
        popup = PopupFactory.getSharedInstance().getPopup(parent, new ImageComponent(image, backGround, framed), x, y);
        popup.show();
        
        PopupManager.getInstance().add(this);
    }
    
    
    public void dispose()
    {
        popup.hide();
    }
}