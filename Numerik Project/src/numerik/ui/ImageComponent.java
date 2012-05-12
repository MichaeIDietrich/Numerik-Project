package numerik.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

public class ImageComponent extends JComponent
{
    private static final long serialVersionUID = 8055865896136562197L;
    
    int width;
    int height;
    
    private Image image;
    
    public ImageComponent(Image image)
    {
        setImage(image);
    }
    
    public void setImage(Image image)
    {
        this.width  = image.getWidth(null);
        this.height = image.getHeight(null);
        
        this.setPreferredSize(new Dimension( this.width, this.height) );
        this.setMaximumSize(new Dimension( 10000, this.height) );
        this.image = image;
        
        invalidate();
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);
        if (image != null)
        {
            g.drawImage(image, 0, 0, this);
        }
        //g.setColor(Color.BLACK);
        //g.drawRect(0, 0, g.getClipBounds().width - 1, g.getClipBounds().height - 1);
    }
    
    public int width()
    {
        return width;
    }

    public int height()
    {
        return height;
    }
}
