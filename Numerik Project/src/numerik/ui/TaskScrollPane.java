package numerik.ui;

import javax.swing.JScrollPane;

public class TaskScrollPane extends JScrollPane
{
    public TaskScrollPane(LatexFormula formula) {
        
        ImageComponent   img = new ImageComponent( formula.toImage(18) );
        setViewportView( img );
        
        this.getVerticalScrollBar().setUnitIncrement( 20 );
        this.createVerticalScrollBar();
    }
}
