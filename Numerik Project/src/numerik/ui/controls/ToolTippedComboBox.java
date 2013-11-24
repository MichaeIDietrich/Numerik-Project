package numerik.ui.controls;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.*;

public final class ToolTippedComboBox
{
    
;    public ToolTippedComboBox(JComboBox<String> comboBox, ArrayList<Image> previewImages, Color background)
    {
        if (manipulateTooltip(comboBox, previewImages, background)) {
            return;
        }
        
        // an dieser Stelle muss zwischen den Betriebssystemen unterschieden werden, weil wir das jeweilige "native" Look&Feel unterstützen möchten
        // und wir deshalb von verschieden Klassen erben müssen und auch andere Sachen sich leicht unterscheiden (teilweise auch wegen Grafik-Bugs)
        switch (System.getProperty("os.name"))
        {
            case "Windows 7":
            case "Windows 8":
                comboBox.setUI(new ToolTippedComboBoxUI_Windows(previewImages, background));
                comboBox.setRenderer(new ComboBoxRenderer());
                break;
            
            case "Linux": //nur für Ubuntu Unity getestet
                comboBox.setUI(new ToolTippedComboBoxUI_Linux(previewImages, background));
                comboBox.setRenderer(new ComboBoxRenderer());
                break;
            
            default:
                //alle anderen Betriebssysteme werden keine Vorschau in der ComboBox sehen
                System.err.println(System.getProperty("os.name") + " wird zurzeit nicht unterstützt und wird daher keine Vorschau in Comboboxen anzeigen.");
                System.err.println("Look & Feel: " + UIManager.getSystemLookAndFeelClassName());
                System.err.println("ComboBox-UI: " + comboBox.getUI());
        }
    }
    
    
    private boolean manipulateTooltip(JComboBox<String> comboBox, ArrayList<Image> previewImages, Color background) {
        
        try {
            
            Field listBoxField = BasicComboBoxUI.class.getDeclaredField("listBox");
            if (listBoxField != null) {
                
                listBoxField.setAccessible(true);
                
                Object res = listBoxField.get(comboBox.getUI());
                if (res instanceof JList<?>) {
                    new ListItemImageToolTip((JList<?>)res, previewImages, background);
                    return true;
                }
            }
            
        } catch (Exception ex) { 
            ex.printStackTrace();
        }
        
        return false;
    }
    
    
    protected final class ToolTippedComboBoxUI_Windows extends com.sun.java.swing.plaf.windows.WindowsComboBoxUI
    {
        
        private ArrayList<Image> images;
        private Color background = Color.WHITE;
        
        public ToolTippedComboBoxUI_Windows(ArrayList<Image> previewImages, Color background)
        {
            images = previewImages;
            this.background = background;
        }
        
        @Override
        protected ComboPopup createPopup()
        {
            ComboPopup popup = super.createPopup();
            
            new ListItemImageToolTip(popup.getList(), images, background);
            
            return popup;
        }
    }
    
    
    protected final class ToolTippedComboBoxUI_Linux extends javax.swing.plaf.synth.SynthComboBoxUI
    {
        
        private ArrayList<Image> images;
        private Color background = Color.WHITE;
        
        public ToolTippedComboBoxUI_Linux(ArrayList<Image> previewImages, Color background)
        {
            images = previewImages;
            this.background = background;
        }
        
        @Override
        protected ComboPopup createPopup()
        {
            ComboPopup popup = super.createPopup();
            
            new ListItemImageToolTip(popup.getList(), images, background);
            
            return popup;
        }
    }
    
    
    protected final class ComboBoxRenderer extends DefaultListCellRenderer
    {
        public ComboBoxRenderer()
        {
            this.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        }
    }
}