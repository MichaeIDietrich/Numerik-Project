package numerik.ui.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

public final class ImageComponent extends JComponent
{
    private int width;
    private int height;
    
    private Image image;
    private Color backGround = Color.WHITE;
    private boolean framed = false; // soll ein Rahmen ums Bild gezeichnet werden?
    
    public ImageComponent(Image image)
    {
        setImage(image);
    }
    
    public ImageComponent(Image image, Color backGround)
    {
        this.backGround = backGround;
        
        setImage(image);
    }
    
    public ImageComponent(Image image, Color backGround, boolean framed)
    {
        this.backGround = backGround;
        this.framed = framed;
        
        setImage(image);
    }
    
    public void setImage(Image image)
    {
        this.image = image;
        width  = image.getWidth(null);
        height = image.getHeight(null);
        
        this.setPreferredSize(new Dimension( getWidth(), getHeight()) );
        this.setMaximumSize(new Dimension( 10000, getHeight()) );
        
        invalidate();
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        g.setColor(backGround);
        g.fillRect(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);
        if (image != null)
        {
            if (framed)
            {
                g.drawImage(image, 1, 1, this);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, g.getClipBounds().width - 1, g.getClipBounds().height - 1);
            }
            else
            {
                g.drawImage(image, 0, 0, this);
            }
        }
    }
    
    public int getWidth()
    {
        return framed ? width + 2 : width;
    }

    public int getHeight()
    {
        return framed ? height + 2 : height;
    }

    public Color getBackGround()
    {
        return backGround;
    }

    public void setBackGround(Color backGround)
    {
        this.backGround = backGround;
    }

    public boolean isFramed()
    {
        return framed;
    }

    public void setFramed(boolean framed)
    {
        this.framed = framed;
    }
}