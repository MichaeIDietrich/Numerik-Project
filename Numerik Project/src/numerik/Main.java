package numerik;

import numerik.ui.dialogs.OutputFrame;

public class Main
{
    
    public static void main(String[] args)
    {
        // Configuration laden
        Configuration.getActiveConfiguration().load();
        
        new OutputFrame();
    }
}
