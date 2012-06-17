package numerik.ui.controls;

import java.awt.Color;

import javax.swing.JScrollPane;

import numerik.Configuration;
import numerik.ui.misc.LatexFormula;

public final class TaskScrollPane extends JScrollPane
{
    public TaskScrollPane(LatexFormula formula) {
        
        ImageComponent   img = new ImageComponent( formula.toImage( Configuration.getActiveConfiguration().getFontSize() ));
        setViewportView( img );
        this.getViewport().setBackground(Color.WHITE);
        
        this.getVerticalScrollBar().setUnitIncrement( 20 );
        this.createVerticalScrollBar();
    }
}