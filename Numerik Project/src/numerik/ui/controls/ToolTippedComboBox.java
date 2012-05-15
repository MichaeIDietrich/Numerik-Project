package numerik.ui.controls;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.ComboPopup;

public final class ToolTippedComboBox
{
    
    @SuppressWarnings("unchecked")
    public ToolTippedComboBox(JComboBox<String> comboBox, ArrayList<Image> previewImages, Color backGround)
    {
        switch (System.getProperty("os.name"))
        {
            case "Windows":
                ToolTippedComboBoxUI_Windows windowsUI = new ToolTippedComboBoxUI_Windows(previewImages, backGround);
                comboBox.setUI(windowsUI);
                break;
            
            case "Linux":
                ToolTippedComboBoxUI_Linux linuxUI = new ToolTippedComboBoxUI_Linux(previewImages, backGround);
                comboBox.setUI(linuxUI);
                comboBox.setRenderer(new LinuxRenderer());
                break;
            
            default:
                System.err.println(System.getProperty("os.name"));
                System.err.println(UIManager.getSystemLookAndFeelClassName());
                System.err.println(comboBox.getUI());
        }
    }
    
    protected final class ToolTippedComboBoxUI_Windows extends com.sun.java.swing.plaf.windows.WindowsComboBoxUI
    {
        
        private int index = -1;
        private ToolTipManager toolTipManager = new ToolTipManager();
        private ArrayList<Image> images;
        private Color backGround = Color.WHITE;
        
        public ToolTippedComboBoxUI_Windows(ArrayList<Image> previewImages, Color backGround)
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
                    super.mouseMoved(e);
                    JList<?> list = (JList<?>) e.getSource();
                    
                    int newIndex = list.locationToIndex(e.getPoint());
                    if (newIndex != index)
                    {
                        index = newIndex;
                        toolTipManager.showToolTip(list, e.getXOnScreen() + 16, e.getYOnScreen(), images.get(index), backGround);
                    }
                }
            });
            
            popup.getList().addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    toolTipManager.disposeAll();
                    index = -1;
                }
                
                @Override
                public void mouseExited(MouseEvent e)
                {
                    super.mouseExited(e);
                    toolTipManager.disposeAll();
                    index = -1;
                }
            });
            
            return popup;
        }
    }
    
    protected final class ToolTippedComboBoxUI_Linux extends javax.swing.plaf.synth.SynthComboBoxUI
    {
        
        private int index = -1;
        private ToolTipManager toolTipManager = new ToolTipManager();
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
                    super.mouseMoved(e);
                    JList<?> list = (JList<?>) e.getSource();
                    
                    int newIndex = list.locationToIndex(e.getPoint());
                    if (newIndex != index)
                    {
                        index = newIndex;
                        toolTipManager.showToolTip(list, e.getXOnScreen() + 16, e.getYOnScreen(), images.get(index), backGround);
                    }
                }
            });
            
            popup.getList().addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    toolTipManager.disposeAll();
                    index = -1;
                }
                
                @Override
                public void mouseExited(MouseEvent e)
                {
                    toolTipManager.disposeAll();
                    index = -1;
                }
            });
            
            return popup;
        }
    }
    
    protected final class LinuxRenderer extends BasicComboBoxRenderer
    {
        
        @Override
        public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
        
    }
}