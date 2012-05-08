package numerik.ui;

import javax.swing.JScrollPane;

public class NiceScrollPane extends JScrollPane
{
    public NiceScrollPane(LatexFormula formula) {
        
        ImageComponent   img = new ImageComponent( formula.toImage(18) );
        setViewportView( img );

        this.getVerticalScrollBar().setUnitIncrement( 20 );
        this.createVerticalScrollBar();
    }
}
