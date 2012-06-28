package numerik.ui.controls;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

import javax.swing.*;
import javax.swing.border.Border;
//import javax.swing.plaf.ComponentUI;
import javax.swing.text.*;

import numerik.ui.misc.ListFilter;

public class SyntaxTextArea extends JTextPane
{
    protected final class LineNumberBorder implements Border
    {
        @Override
        public Insets getBorderInsets(Component c)
        {
            return new Insets(1, 20, 1, 1);
        }
        
        @Override
        public boolean isBorderOpaque()
        {
            return false;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
        {
            int lineHeight = c.getFontMetrics(c.getFont()).getHeight();
            
            Graphics2D g2 = (Graphics2D) g;
            
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Rahmen
            g.setColor(Color.DARK_GRAY);
            g.drawRect(0, 0, width - 1, height - 1);
            
            // Trennstrich
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(18, 1, 18, height - 2);
            
            // Durchnummerieren
            g.setColor(Color.WHITE);
            g.fillRect(1, 1, 17, height - 2);
            
            g.setColor(Color.DARK_GRAY);
            int i = 0;
            while (true)
            {
                int pos = ++i * lineHeight - 2;
                if (pos > c.getHeight())
                {
                    break;
                }
                g.drawString(String.valueOf(i), 2, pos);
            }
        }
    }
    
    
    private JList<String> list;
    private ListFilter filter;
    private Popup popup;
    private boolean popupVisible;
    
    private static Style FUNCTION;
    private static Style NORMAL;
    private static Style ERROR;
    private static Style KNOWNVAR;
    private static Style UNKNOWNVAR;
    private static Style COMMENT;
    private static Style KEYWORD;
    
    private static Font TEXTFONT;
    
    private Collection<String> variables;
    
    private StyledDocument doc;
    
    static
    {
        for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
        {
            if (font.equals("Consolas") || font.equals("Courier New") || font.equals("Monospaced"))
            {
                System.out.println("Font: '" + font + "' festgelegt");
                TEXTFONT = new Font(font, Font.PLAIN, 12);
                break;
            }
        }
    }
    
    public SyntaxTextArea(Collection<String> functionNames, Collection<String> variables, boolean showLineNumbers)
    {
        if (showLineNumbers)
        {
            this.setBorder(new LineNumberBorder());
        }
        else
        {
            this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        
        this.variables = variables;
        
        filter = new ListFilter(functionNames);
        list = new JList<String>(filter);
        
        this.setFont(TEXTFONT);
        
        doc = this.getStyledDocument();
        
        FUNCTION = doc.addStyle("FUNCTION", null);
        StyleConstants.setForeground(FUNCTION, new Color(127, 0, 127));
        StyleConstants.setBold(FUNCTION, true);
        
        NORMAL = doc.addStyle("NORMAL", null);
        
        ERROR = doc.addStyle("ERROR", null);
        StyleConstants.setForeground(ERROR, new Color(255, 0, 0));
        StyleConstants.setItalic(ERROR, true);
        
        KNOWNVAR = doc.addStyle("KNOWNVAR", null);
        StyleConstants.setForeground(KNOWNVAR, new Color(63, 63, 192));
        StyleConstants.setBold(KNOWNVAR, true);
        
        UNKNOWNVAR = doc.addStyle("UNKNOWNVAR", null);
        StyleConstants.setForeground(UNKNOWNVAR, new Color(90, 90, 90));
        
        COMMENT = doc.addStyle("COMMENT", null);
        StyleConstants.setForeground(COMMENT, new Color(0, 127, 0));
        StyleConstants.setItalic(COMMENT, true);
        
        KEYWORD = doc.addStyle("KEYWORD", null);
        StyleConstants.setBold(KEYWORD, true);
        
        // Zeilenumbruch deaktivieren
        if (!showLineNumbers)
        {
            this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "NULL");
        }
        
//        new ParagraphView(doc.getDefaultRootElement())
//        {
//            @Override
//            protected void layout(int width, int height)
//            {
//                super.layout(Short.MAX_VALUE, height);
//            }
//            
//            public float getMinimumSpan(int axis)
//            {
//                return super.getPreferredSpan(axis);
//            }
//        };
        
        list.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // selects the key from the list and changes yout text field
                // text
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
                {
                    // ProposalTextArea.this.replaceRange(list.getSelectedValue(),
                    // getInputStart(),
                    // ProposalTextArea.this.getCaretPosition());
                    
                    try
                    {
                        
                        int inputStart = getInputStart();
                        int caret = SyntaxTextArea.this.getCaretPosition();
                        
                        doc.remove(inputStart, caret - inputStart);
                        doc.insertString(SyntaxTextArea.this.getCaretPosition(), list.getSelectedValue() + "(", null);
                        
                    }
                    catch (BadLocationException ex)
                    {
                        ex.printStackTrace();
                    }
                    
                    filter.setFilter(SyntaxTextArea.this.getText());
                    hidePopup(false);
                }
            }
        });
        
        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                if (e.getKeyChar() == '*')
                {
                    e.setKeyChar('⋅');
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_UP:
                        break;
                    case KeyEvent.VK_ENTER:
                        e.consume();
                        // selects the key from the list and changes yout text
                        // field text
                        if (popupVisible && list.getSelectedIndex() > -1)
                        {
                            try
                            {
                                
                                int inputStart = getInputStart();
                                int caret = SyntaxTextArea.this.getCaretPosition();
                                
                                doc.remove(inputStart, caret - inputStart);
                                doc.insertString(SyntaxTextArea.this.getCaretPosition(), list.getSelectedValue() + "(", null);
                                
                            }
                            catch (BadLocationException ex)
                            {
                                ex.printStackTrace();
                            }
                            
                            filter.setFilter(SyntaxTextArea.this.getText());
                            hidePopup(false);
                        }
                        break;
                    default:
                        hidePopup(false);
                        filter.setFilter(getInput());
                        showPopup();
                }
                
                highlightSyntax();
            }
            
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (popupVisible)
                {
                    // lets you use the up and down key to navigate through the
                    // list
                    int index = list.getSelectedIndex();
                    switch (e.getKeyCode())
                    {
                        case KeyEvent.VK_DOWN:
                            if (index < filter.getSize() - 1)
                            {
                                list.setSelectedIndex(index + 1);
                                list.ensureIndexIsVisible(index + 1);
                            }
                            e.consume();
                            break;
                        case KeyEvent.VK_UP:
                            if (index > 0)
                            {
                                list.setSelectedIndex(index - 1);
                                list.ensureIndexIsVisible(index - 1);
                            }
                            e.consume();
                            break;
                        case KeyEvent.VK_ENTER:
                            e.consume();
                    }
                }
            }
        });
        
        this.addFocusListener(new FocusListener()
        {
            
            @Override
            public void focusLost(FocusEvent e)
            {
                hidePopup(true);
            }
            
            @Override
            public void focusGained(FocusEvent e)
            {
                showPopup();
            }
        });
        
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                hidePopup(true);
            }
        });
    }
    
    public void setList(java.util.List<String> items)
    {
        filter.setList(items);
    }
    
    private void hidePopup(boolean unselect)
    {
        if (popupVisible)
        {
            if (unselect)
                list.clearSelection();
            popup.hide();
            popupVisible = false;
        }
    }
    
    @Override
    public void setText(String text)
    {
        super.setText(text);
        
        highlightSyntax();
    }
    
    private void showPopup()
    {
        // always select the first list index, if nothing is selected
        if ((list.getSelectedIndex() == -1 || list.getSelectedIndex() >= filter.getSize()) && filter.getSize() > 0)
            list.setSelectedIndex(0);
        switch (filter.getSize())
        {
            case 0:
                // don't show an empty list
                break;
            case 1:
                // don't show this key if it's exactly the chosen one
                if (getInput().equals(filter.getElementAt(0)))
                    break;
            default:
                // show the key list when you you typed at least one char
                if (filter.getFilter().length() > 0)
                {
                    if (filter.getSize() > 8)
                        list.setVisibleRowCount(8);
                    else
                        list.setVisibleRowCount(filter.getSize());
                    // list.setFixedCellWidth(Math.max(textField.getWidth(),
                    // list.getPreferredSize().width));
                    Point pos = findCorrectCharacterPos(getInputStart());
                    pos.translate(SyntaxTextArea.this.getLocationOnScreen().x, SyntaxTextArea.this.getLocationOnScreen().y);
                    popup = PopupFactory.getSharedInstance().getPopup(this, new JScrollPane(list), pos.x, pos.y);
                    popup.show();
                    popupVisible = true;
                }
        }
    }
    
    private Point findCorrectCharacterPos(int pos)
    {
        Point position = null;
        
        try
        {
            
            Rectangle bounds = this.modelToView(pos);
            position = new Point(bounds.x, bounds.y + bounds.height);
            
        }
        catch (BadLocationException ex)
        {
            ex.printStackTrace();
        }
        
        return position;
    }
    
    private String getDocText()
    {
        try
        {
            
            return doc.getText(0, doc.getLength());
            
        }
        catch (BadLocationException ex)
        {
            
            ex.printStackTrace();
            return null;
        }
    }
    
    private String getInput()
    {
        return getDocText().substring(getInputStart(), this.getCaretPosition());
    }
    
    private int getInputStart()
    {
        int cursor = this.getCaretPosition();
        
        if (cursor == 0)
        {
            return 0;
        }
        
        int start;
        
        String text = getDocText();
        
        for (start = cursor - 1; start >= 0; start--)
        {
            char c = text.charAt(start);
            if (!(c == '_' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'))
            {
                break;
            }
        }
        return ++start;
    }
    
    
    private void highlightSyntax()
    {
        int start = -1;
        int end = -1;
        
        String text = getDocText();
        
        char[] content = text.toCharArray();
        
        while (true)
        {
            
            while (++start < doc.getLength())
            {
                
                if (content[start] == '[' || content[start] == ']' || content[start] == ',')
                {
                    doc.setCharacterAttributes(end, start - end, NORMAL, true);
                    
                    doc.setCharacterAttributes(start, 1, KEYWORD, true);
                    
                    end = start + 1;
                }
                
                if (isWord(content[start]))
                    break;
                
                if (content[start] == '/' && content.length > start + 1 && content[start + 1] == '/')
                {
                    
                    end = start;
                    
                    while (++end < doc.getLength())
                    {
                        if (content[end] == '\n')
                            break;
                    }
                    doc.setCharacterAttributes(start, end - start, COMMENT, true);
                    start = end;
                }
            }
            
            doc.setCharacterAttributes(end, start - end, NORMAL, true);
            
            if (start >= doc.getLength())
                return;
            
            boolean isFunction = false;
            
            end = start;
            while (++end < doc.getLength())
            {
                
                if (content[end] == '(')
                    isFunction = true;
                if (!isWord(content[end]))
                    break;
            }
            
            if (isFunction)
            {
                if (filter.getList().contains(text.substring(start, end)))
                {
                    doc.setCharacterAttributes(start, end - start, FUNCTION, true);
                }
                else
                {
                    doc.setCharacterAttributes(start, end - start, ERROR, true);
                }
            }
            else
            {
                if (filter.getList().contains(text.substring(start, end)))
                {
                    doc.setCharacterAttributes(start, end - start, ERROR, true);
                }
                else if (variables != null && variables.contains(text.substring(start, end)))
                {
                    doc.setCharacterAttributes(start, end - start, KNOWNVAR, true);
                }
                else if (text.substring(start, end).equals("Do") || text.substring(start, end).equals("While"))
                {
                    doc.setCharacterAttributes(start, end - start, KEYWORD, true);
                }
                else
                {
                    doc.setCharacterAttributes(start, end - start, UNKNOWNVAR, true);
                }
            }
            
            start = end;
        }
    }
    
    private boolean isWord(char c)
    {
        return c == '_' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }
    
//    public boolean getScrollableTracksViewportWidth()
//    {
//        Component parent = getParent();  
//        ComponentUI ui = getUI();  
//        
//        return parent != null ? (ui.getPreferredSize(this).width <= parent.getSize().width) : true;
//    }
//    @Override
//    public boolean getScrollableTracksViewportWidth()
//    {
//       return false;
//    }
    
//    @Override
//    public void layout(int width, int height) 
//    {
//        super.layout(Short.MAX_VALUE, height);
//    }
}