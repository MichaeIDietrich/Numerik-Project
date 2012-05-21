package numerik.ui.dialogs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import numerik.ui.misc.*;

public class EditPopup implements PopupFrame
{
    private JWindow container;
    
    
    public EditPopup(Window parent, int x, int y, String defaultText, final ChangeListener listener)
    {
        PopupManager.getInstance().disposeAll();
        
        final JTextField text = new JTextField(defaultText == null ? "" : defaultText);
        text.selectAll();
        text.setBackground(new Color(255, 255, 100));
        text.setFocusTraversalKeysEnabled(false);
        text.setPreferredSize(new Dimension(100, text.getPreferredSize().height));
        container = new JWindow(parent);
        container.add(text);
        container.pack();
        container.setLocation(x, y);
        
        text.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                listener.stateChanged(new ChangeEvent(new String(text.getText())));
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    listener.stateChanged(new ChangeEvent(new String("\0")));
                    dispose();
                }
                else if (e.getKeyCode() == KeyEvent.VK_TAB)
                {
                    listener.stateChanged(new ChangeEvent(new String("\t")));
                }
            }
        });
        
        text.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                System.err.println("Focus lost");
//                dispose();
            }
        });
        
        PopupManager.getInstance().add(this);
        
        container.setVisible(true);
        text.requestFocus();
    }
    
    
    @Override
    public void dispose()
    {
        container.dispose();
    }
}