package numerik.ui.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import numerik.calc.Matrix;
import numerik.ui.misc.PopupManager;

public final class NewMatrixWindow extends JDialog implements ActionListener
{
    private final String ACCEPT = "ACCEPT";
    private final String CANCEL = "CANCEL";
    private final String CLEAN  = "CLEAN";
    
    private Image larrow = new ImageIcon("icons/arrow-left16.png").getImage();
    private Image rarrow = new ImageIcon("icons/arrow-right16.png").getImage();
    private Image uarrow = new ImageIcon("icons/arrow-up16.png").getImage();
    private Image darrow = new ImageIcon("icons/arrow-down16.png").getImage();
    private Image luarrow = new ImageIcon("icons/arrow-left-up16.png").getImage();
    private Image rdarrow = new ImageIcon("icons/arrow-right-down16.png").getImage();
    private Image field = new ImageIcon("icons/field16.png").getImage();
    
    private Icon clean = new ImageIcon("icons/clean16.png");
    private Icon accept = new ImageIcon("icons/accept16.png");
    private Icon cancel = new ImageIcon("icons/cancel16.png");
    
    private int rows = 3;
    private int cols = 3;
    private String[][] matrix = new String[rows][cols];
    private int indents[] = new int[rows + 1];
    private Point sel = new Point(-1, -1);
    
    private Matrix result;
    
    private PopupManager popupManager = PopupManager.getInstance();
    
    JPanel pnlButtons;
    
    private NewMatrixWindow(JFrame owner, Point position)
    {
        super(owner, true);
        this.setUndecorated(true);
        this.setLocation(position);
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(pnlMain);
        calcIndents();
        this.setResizable(false);
        
        pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setBackground(new Color(230, 240, 255));
        JButton btnClean = new JButton(clean);
        btnClean.setActionCommand(CLEAN);
        btnClean.addActionListener(this);
        pnlButtons.add(btnClean);
        JButton btnAccept = new JButton(accept);
        btnAccept.setActionCommand(ACCEPT);
        btnAccept.addActionListener(this);
        pnlButtons.add(btnAccept);
        JButton btnCancel = new JButton(cancel);
        btnCancel.setActionCommand(CANCEL);
        btnCancel.addActionListener(this);
        pnlButtons.add(btnCancel);
        
        JPanel pnlBackground = new JPanel()
        {
            @Override
            public void paint(Graphics g)
            {
                Graphics2D g2 = (Graphics2D)g;
                
                g.setColor(new Color(230, 240, 255));
                g.fillRect(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);
                
                
                g.setColor(Color.BLACK);
                g.drawString("Matrix " + rows + "x" + cols, 5, 15);
                
                
                if (sel.y > -1 && sel.x > -1)
                {
                    g2.setColor(Color.BLUE);
                    g2.fillRect((indents[sel.x] + indents[sel.x + 1]) / 2 - 8, (sel.y + 1) * 50 - 17, 16, 16);
                }
                
                for (int row = 0; row < rows; row++)
                {
                    for (int col = 0; col < cols; col++)
                    {
                        if (matrix[row][col] == null)
                        {
                            g2.drawImage(field, (indents[col] + indents[col + 1]) / 2 - 8, (row + 1) * 50 - 17, this);
                        }
                        else
                        {
                            g2.setColor(Color.RED);
                            if (isNumeric(matrix[row][col]))
                            {
                                g2.setColor(Color.BLACK);
                            }
                            if (sel.x != col || sel.y != row)
                            {
                                g2.drawString(matrix[row][col], indents[col], (row + 1) * 50);
                            }
                        }
                    }
                }
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(3));
                g2.setColor(Color.BLACK);
                g2.drawArc(15, 15, 75, 50 * rows, 145, 75);
                g2.drawArc(indents[cols] - 60, 15, 75, 50 * rows, 320, 75);
                
                g2.drawImage(larrow, this.getWidth() - 38, 5, this);
                g2.drawImage(rarrow, this.getWidth() - 19, 5, this);
                g2.drawImage(uarrow, 5, this.getHeight() - 38, this);
                g2.drawImage(darrow, 5, this.getHeight() - 19, this);
                g2.drawImage(luarrow, this.getWidth() - 30, this.getHeight() - 30, this);
                g2.drawImage(rdarrow, this.getWidth() - 19, this.getHeight() - 19, this);
            }
            
        };
        pnlBackground.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                for (int row = 0; row < rows; row++)
                {
                    for (int col = 0; col < cols; col++)
                    {
                        if (e.getX() >= indents[col] && e.getX() < indents[col + 1] &&
                            e.getY() >= row * 50 + 25 && e.getY() < row * 50 + 75)
                        {
                            
                            showInput(e.getXOnScreen(), e.getYOnScreen(), row, col);
                            return;
                        }
                    }
                }
                
                Rectangle rect = e.getComponent().getBounds();
                
                if (e.getX() >= rect.width - 38 && e.getX() < rect.width - 22 && e.getY() >= 5 && e.getY() < 21)
                {
                    updateMatrixSize(0, -1);
                }
                if (e.getX() >= rect.width - 19 && e.getX() < rect.width - 3 && e.getY() >= 5 && e.getY() < 21)
                {
                    updateMatrixSize(0, 1);
                }
                if (e.getY() >= rect.height - 38 && e.getY() < rect.height - 22 && e.getX() >= 5 && e.getX() < 21)
                {
                    updateMatrixSize(-1, 0);
                }
                if (e.getY() >= rect.height - 19 && e.getY() < rect.height - 3 && e.getX() >= 5 && e.getX() < 21)
                {
                    updateMatrixSize(1, 0);
                }
                if (e.getX() >= rect.width - 30 && e.getX() < rect.width - 19 && e.getY() >= rect.height - 30 && e.getY() < rect.height - 19)
                {
                    updateMatrixSize(-1, -1);
                }
                if (e.getX() >= rect.width - 19 && e.getX() < rect.width - 3 && e.getY() >= rect.height - 19 && e.getY() < rect.height - 3)
                {
                    updateMatrixSize(1, 1);
                }
            }
            
        });
        
        pnlMain.add(pnlButtons, BorderLayout.PAGE_END);
        pnlMain.add(pnlBackground);
        resize();
        this.setVisible(true);
    }
    
    
    public Matrix getResult()
    {
        return result;
    }
    
    
    private void calcIndents()
    {
        indents[0] = 25;
        FontMetrics fm = this.getFontMetrics(this.getFont());
        for (int col = 0; col < cols; col++)
        {
            int width = 0;
            for (int row = 0; row < rows; row++)
            {
                int w = matrix[row][col] == null ? 40 : fm.stringWidth(matrix[row][col]);
                width = w > width ? w : width;
            }
            indents[col + 1] = indents[col] + width + 10;
        }
    }
    
    
    private void resize()
    {
        this.setSize(Math.max(indents[cols] + 50, pnlButtons.getPreferredSize().width), rows * 50 + 50);
    }
    
    
    private void updateMatrixSize(int deltaRow, int deltaCol)
    {
        int rowsNew = rows + deltaRow;
        int colsNew = cols + deltaCol;
        
        if (rowsNew == 0 || colsNew == 0)
        {
            return;
        }
        
        if (sel.x == colsNew || sel.y == rowsNew)
        {
            sel = new Point(-1, -1);
            popupManager.disposeAll();
        }
        
        String[][] newMatrix = new String[rowsNew][colsNew];
        for (int row = 0; row < Math.min(rows, rowsNew); row++)
        {
            for (int col = 0; col < Math.min(cols, colsNew); col++)
            {
                newMatrix[row][col] = matrix[row][col];
            }
        }
        
        matrix = newMatrix;
        
        rows += deltaRow;
        cols += deltaCol;
        
        indents = new int[cols + 1];
        calcIndents();
        resize();
        this.repaint();
    }
    
    
    private void showInput(int x, int y, int row, int col)
    {
        calcIndents();
        resize();
        sel = new Point(col, row);
        this.repaint();
        
        new EditPopup(NewMatrixWindow.this, x, y, matrix[sel.y][sel.x], new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                switch (e.getSource().toString())
                {
                    case "\0":
                        sel = new Point(-1, -1);
                        calcIndents();
                        resize();
                    break;
                    
                    case "\t":
                        System.out.println("tab");
                        int x = ++sel.x % cols;
                        int y = x == 0 ? ++sel.y % rows : sel.y;
                        showInput(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y, y, x);
                        calcIndents();
                        resize();
                        break;
                        
                    case "":
                        matrix[sel.y][sel.x] = null;
                        break;
                        
                    default:
                        matrix[sel.y][sel.x] = e.getSource().toString();
                        System.out.println(e.getSource().toString());
                }
                NewMatrixWindow.this.repaint();
            }
        });
    }
    
    
    private boolean isNumeric(String number)
    {
        if (number == null)
        {
            return false;
        }
        try
        {
            new BigDecimal(number);
            return true;
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
    }
    
    
    public static Matrix createNewMatrix(JFrame owner, Point position)
    {
        NewMatrixWindow mw = new NewMatrixWindow(owner, position);
        
        return mw.getResult();
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case ACCEPT:
                ArrayList<BigDecimal> values = new ArrayList<BigDecimal>();
                for (int row = 0; row < rows; row++)
                {
                    for (int col = 0; col < cols; col++)
                    {
                        if (matrix[row][col] != null)
                        {
                            try
                            {
                                values.add(new BigDecimal(matrix[row][col]));
                                continue;
                            }
                            catch (NumberFormatException ex) { }
                        }
                        return;
                    }
                }
                result = new Matrix(values, cols);
                this.dispose();
                break;
        
            case CANCEL:
                this.dispose();
                break;
                
            case CLEAN:
                for (int row = 0; row < rows; row++)
                {
                    for (int col = 0; col < cols; col++)
                    {
                        matrix[row][col] = null;
                    }
                }
                System.out.println("clean");
                this.repaint();
        }
    }
}