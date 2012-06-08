package numerik.ui.controls;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.ComboPopup;

import numerik.ui.dialogs.ImagePopup;
import numerik.ui.misc.PopupManager;

public final class ToolTippedComboBox
{
    
    public ToolTippedComboBox(JComboBox<String> comboBox, ArrayList<Image> previewImages, Color backGround)
    {
        // an dieser Stelle muss zwischen den Betriebssystemen unterschieden werden, weil wir das jeweilige "native" Look&Feel unterstützen möchten
        // und wir deshalb von verschieden Klassen erben müssen und auch andere Sachen sich leicht unterscheiden (teilweise auch wegen Grafik-Bugs)
        switch (System.getProperty("os.name"))
        {
            case "Windows 7":
                comboBox.setUI(new ToolTippedComboBoxUI_Windows7(previewImages, backGround));
                comboBox.setRenderer(new ComboBoxRenderer());
                break;
            
            case "Linux": //nur für Ubuntu Unity getestet
                comboBox.setUI(new ToolTippedComboBoxUI_Linux(previewImages, backGround));
                comboBox.setRenderer(new ComboBoxRenderer());
                break;
            
            default:
                //alle anderen Betriebssysteme werden keine Vorschau in der ComboBox sehen
                System.err.println(System.getProperty("os.name") + " wird zurzeit nicht unterstützt und wird daher keine Vorschau in Comboboxen anzeigen.");
                System.err.println("Look & Feel: " + UIManager.getSystemLookAndFeelClassName());
                System.err.println("ComboBox-UI: " + comboBox.getUI());
        }
    }
    
    
    protected final class ToolTippedComboBoxUI_Windows7 extends com.sun.java.swing.plaf.windows.WindowsComboBoxUI
    {
        
        private int index = -1;
        private PopupManager popupManager = PopupManager.getInstance();
        private ArrayList<Image> images;
        private Color backGround = Color.WHITE;
        
        public ToolTippedComboBoxUI_Windows7(ArrayList<Image> previewImages, Color backGround)
        {
            images = previewImages;
            this.backGround = backGround;
        }
        
        @Override
        protected ComboPopup createPopup()
        {
            ComboPopup popup = super.createPopup();
            
            popup.getList().addMouseMotionListener(new MouseMotionAdapter()
            {
                @Override
                public void mouseMoved(MouseEvent e)
                {
                    JList<?> list = (JList<?>) e.getSource();
                    
                    int newIndex = list.locationToIndex(e.getPoint());
                    if (newIndex != index)
                    {
                        if (newIndex >= images.size())
                        {
                            popupManager.disposeAll();
                            index = -1;
                            return;
                        }
                        index = newIndex;
                        new ImagePopup(list, e.getXOnScreen() + 16, e.getYOnScreen(), images.get(index), backGround, true);
                    }
                }
            });
            
            popup.getList().addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    popupManager.disposeAll();
                    index = -1;
                }
                
                @Override
                public void mouseExited(MouseEvent e)
                {
                    super.mouseExited(e);
                    popupManager.disposeAll();
                    index = -1;
                }
            });
            
            return popup;
        }
    }
    
    
    protected final class ToolTippedComboBoxUI_Linux extends javax.swing.plaf.synth.SynthComboBoxUI
    {
        
        private int index = -1;
        private PopupManager popupManager = PopupManager.getInstance();
        private ArrayList<Image> images;
        private Color backGround = Color.WHITE;
        
        public ToolTippedComboBoxUI_Linux(ArrayList<Image> previewImages, Color backGround)
        {
            images = previewImages;
            this.backGround = backGround;
        }
        
        @Override
        protected ComboPopup createPopup()
        {
            ComboPopup popup = super.createPopup();
            
            popup.getList().addMouseMotionListener(new MouseMotionAdapter()
            {
                @Override
                public void mouseMoved(MouseEvent e)
                {
                    JList<?> list = (JList<?>) e.getSource();
                    
                    int newIndex = list.locationToIndex(e.getPoint());
                    if (newIndex != index)
                    {
                        if (newIndex >= images.size())
                        {
                            popupManager.disposeAll();
                            index = -1;
                            return;
                        }
                        index = newIndex;
                        new ImagePopup(list, e.getXOnScreen() + 16, e.getYOnScreen(), images.get(index), backGround, true);
                    }
                }
            });
            
            popup.getList().addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    popupManager.disposeAll();
                    index = -1;
                }
                
                @Override
                public void mouseExited(MouseEvent e)
                {
                    popupManager.disposeAll();
                    index = -1;
                }
            });
            
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