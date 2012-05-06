package numerik.ui;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.*;

import numerik.calc.Matrix;

public class NewMatrixWindow extends JDialog implements ActionListener, MouseListener
{
    
    private final String ACCEPT = "ACCEPT";
    private final String CANCEL = "CANCEL";
    private final String CLEAN = "CLEAN";
    
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
    private Point sel = new Point(0, 0);
    
    private Matrix result;
    
    private TextField txtInput = new TextField();
    
    private NewMatrixWindow(JFrame owner, Point position)
    {
        super(owner, true);
        this.setUndecorated(true);
        this.setLocation(position);
        calcIndents();
        resize();
        this.setResizable(false);
        
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setBackground(Color.WHITE);
        txtInput.setPreferredSize(new Dimension(150, 20));
        pnlButtons.add(txtInput);
        JButton btnClean = new JButton(clean);
        btnClean.setActionCommand(ACCEPT);
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
                super.paint(g);
                Graphics2D g2 = (Graphics2D)g;
                
                if (matrix[sel.y][sel.x] == null)
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
                            if (sel.x == col && sel.y == row)
                            {
                                g2.setColor(Color.BLUE);
                            }
                            g2.drawString(matrix[row][col], indents[col], (row + 1) * 50);
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
        pnlBackground.setBackground(Color.WHITE);
        pnlBackground.addMouseListener(this);
        
        this.add(pnlButtons, BorderLayout.PAGE_END);
        this.add(pnlBackground);
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
        this.setSize(Math.max(indents[cols] + 50, 280), rows * 50 + 50);
    }
    
    private void showInputField()
    {
        txtInput.setText(matrix[sel.y][sel.x] == null ? "" : matrix[sel.y][sel.x]);
        txtInput.requestFocusInWindow();
    }
    
    private void updateMatrixSize(int deltaRow, int deltaCol)
    {
        int rowsNew = rows + deltaRow;
        int colsNew = cols + deltaCol;
        
        if (rowsNew == 0 || colsNew == 0)
        {
            return;
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
        if (e.getActionCommand().equals(ACCEPT))
        {
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
        }
        else if (e.getActionCommand().equals(CANCEL))
        {
            this.dispose();
        }
        if (e.getActionCommand().equals(CLEAN));
        {
            for (int row = 0; row < rows; row++)
            {
                for (int col = 0; col < cols; col++)
                {
                    matrix[row][col] = null;
                }
            }
            txtInput.setText("");
            this.repaint();
        }
    }

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
                    
                    matrix[sel.y][sel.x] = txtInput.getText().equals("") ? null : txtInput.getText();
                    calcIndents();
                    resize();
                    this.repaint();
                    
                    sel = new Point(col, row);
                    showInputField();
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
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

}
