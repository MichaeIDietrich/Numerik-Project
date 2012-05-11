package numerik.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class TaskPane extends JPanel
{
    JButton         btnTest;
    JToolBar        toolBar;
    TaskScrollPane  scrollpane;
    JComboBox       combobox;
    
    public TaskPane( LatexFormula formula ) 
    {            
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground( Color.getHSBColor(100, 50, 25) );
        this.setBorder(new javax.swing.border.EtchedBorder());
        
        btnTest = new JButton();
        btnTest.setIcon(new ImageIcon("icons/button_go_small.png"));

        Vector<String> list = new Vector<String>();
        list.add("Matrix A");
        list.add("Matrix B");
        list.add("Matrix C");
        list.add("Matrix D");
        
        JComboBox cb = new JComboBox( list );
        cb.setAlignmentX( JComponent.LEFT_ALIGNMENT  );
        cb.setMaximumSize( new Dimension(100,24) );
        
        toolBar = new JToolBar();
        toolBar.setLayout( new BoxLayout( toolBar, BoxLayout.X_AXIS) );
        toolBar.setFloatable( false );
        toolBar.setBackground( Color.getHSBColor(100, 50, 25) );
        toolBar.add( cb );
        toolBar.addSeparator( new Dimension( 16, 32));
        toolBar.add( btnTest );

        
        scrollpane = new TaskScrollPane( formula );
        
        this.add( toolBar );
        this.add( scrollpane );
    }
}
