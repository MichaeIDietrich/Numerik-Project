package numerik.ui.controls;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

import javax.swing.*;
import javax.swing.text.*;

import numerik.ui.misc.ListFilter;

public class SyntaxTextArea extends JTextPane
{
    
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
    
    private Collection<String> variables;
    
    private StyledDocument doc;
    
    public SyntaxTextArea(Collection<String> functionNames, Collection<String> variables)
    {
        this.variables = variables;
        
        filter = new ListFilter(functionNames);
        list = new JList<String>(filter);
        
        initFont();
        
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
        
        UNKNOWNVAR = doc.addStyle("UNKNOWNVAR", null);
        StyleConstants.setForeground(UNKNOWNVAR, new Color(90, 90, 90));
        
        COMMENT = doc.addStyle("COMMENT", null);
        StyleConstants.setForeground(COMMENT, new Color(0, 127, 0));
        StyleConstants.setItalic(COMMENT, true);
        
        KEYWORD = doc.addStyle("KEYWORD", null);
        StyleConstants.setBold(KEYWORD, true);
        
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
    
    private void initFont()
    {
        for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
        {
            if (font.equals("Consolas") || font.equals("Courier New") || font.equals("Monospaced"))
            {
                System.out.println("Font: " + font);
                this.setFont(new Font(font, Font.PLAIN, 12));
                return;
            }
        }
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
                else if (variables.contains(text.substring(start, end)))
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
}