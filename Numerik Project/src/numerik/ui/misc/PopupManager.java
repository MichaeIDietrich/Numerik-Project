package numerik.ui.misc;

import java.util.ArrayList;

public class PopupManager
{
    private ArrayList<PopupFrame> popups;
    
    private static PopupManager popupManager;
    
    
    private PopupManager()
    {
        popups = new ArrayList<PopupFrame>();
    }
    
    
    public void add(PopupFrame popup)
    {
        popups.add(popup);
    }
    
    
    public void disposeAll()
    {
        for (PopupFrame popup : popups)
        {
            popup.dispose();
        }
    }
    
    
    public static PopupManager getInstance()
    {
        if (popupManager == null)
        {
            popupManager = new PopupManager();
        }
        return popupManager;
    }
}