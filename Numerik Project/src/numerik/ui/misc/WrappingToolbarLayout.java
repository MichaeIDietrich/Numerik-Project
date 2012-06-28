package numerik.ui.misc;

import java.awt.*;

/*import java.awt.*;

public class WrappingToolbarLayout extends FlowLayout
{
    public WrappingToolbarLayout()
    {
        super(FlowLayout.LEADING);
    }
    
    @Override
    public Dimension preferredLayoutSize(Container parent)
    {
        synchronized (parent.getTreeLock())
        {
            Dimension size = super.minimumLayoutSize(parent);
            
            int nComponents = parent.getComponentCount();
            if (nComponents > 0)
            {
                int firstY = parent.getComponent(0).getY();
                Component last = parent.getComponent(nComponents - 1);
                int lastY = last.getY();
                
                if (lastY != firstY)
                {
                    size = new Dimension((int) size.getWidth(), lastY + last.getHeight());
                }
            }
            
            return size;
        }
    }
}*/

/*public class WrappingToolbarLayout implements LayoutManager {
    public static final int LEFT     = 0;
    public static final int CENTER   = 1;
    public static final int RIGHT    = 2;
    public static final int LEADING  = 3;
    public static final int TRAILING = 4;
    
    int align;
    int hgap;
    int vgap;
    
    public WrappingToolbarLayout() {
        this(CENTER, 5, 5);
    }
    
    public WrappingToolbarLayout(int align) {
        this(align, 5, 5);
    }
    
    public WrappingToolbarLayout(int align, int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
        setAlignment(align);
    }
    
    public int getAlignment() {
        return align;
    }
    
    public void setAlignment(int align) {
        switch (align) {
        case LEADING:
            this.align = LEFT;
            break;
        case TRAILING:
            this.align = RIGHT;
            break;
        default:
            this.align = align;
            break;
        }
    }
    
    public int getHgap() {
        return hgap;
    }
    
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }
    
    public int getVgap() {
        return vgap;
    }
    
    public void setVgap(int vgap) {
        this.vgap = vgap;
    }
    
    public Dimension preferredLayoutSize(Container parent) {
        synchronized(parent.getTreeLock()) {
            Dimension dim = new Dimension(0,0);
            int maxWidth = 0;
            int componentCount = parent.getComponentCount();
 
            for(int i = 0; i < componentCount; i++) {
                Component c = parent.getComponent(i);
                if(c.isVisible()) {
                    Dimension d = c.getPreferredSize();
                    if((dim.width + d.width + hgap) <= parent.getWidth()) {
                        dim.height = Math.max(dim.height, d.height);
                    } else {
                        dim.height += vgap + d.height;
                        dim.width = 0;
                    }
                    if(dim.width > 0) {
                        dim.width += hgap;
                    }
                    dim.width += d.width;
                    if(dim.width > maxWidth) {
                        maxWidth = dim.width;
                    }
                }
            }
            Insets insets = parent.getInsets();
            dim.width = Math.max(dim.width, maxWidth);
            dim.width += insets.left + insets.right + 2*hgap;
            dim.height += insets.top + insets.bottom + 2*vgap;
            return dim;
        }
    }
    
    public Dimension minimumLayoutSize(Container parent) {
        synchronized(parent.getTreeLock()) {
            Dimension dim = new Dimension(0,0);
            int componentCount = parent.getComponentCount();
            
            for(int i = 0; i < componentCount; i++) {
                Component c = parent.getComponent(i);
                if(c.isVisible()) {
                    Dimension d = c.getMinimumSize();
                    dim.height = Math.max(dim.height, d.height);
                    if(i > 0) {
                        dim.width += hgap;
                    }
                    dim.width += d.width;
                }
            }
            Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right + 2*hgap;
            dim.height += insets.top + insets.bottom + 2*vgap;
            return dim;
        }
    }
    
    public void layoutContainer(Container parent) {
        synchronized(parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int maxWidth = parent.getWidth() -
                (insets.left + insets.right + hgap*2);
            int componentCount = parent.getComponentCount();
            int x = 0, y = insets.top + vgap;
            int rowh = 0, start = 0;
            boolean ltr = parent.getComponentOrientation().isLeftToRight();
            
            for(int i = 0; i < componentCount; i++) {
                Component c = parent.getComponent(i);
                if(c.isVisible()) {
                    Dimension d = c.getPreferredSize();
                    c.setSize(d.width, d.height);
                    if((x == 0) || ((x + d.width) <= maxWidth)) {
                        if(x > 0) {
                            x += hgap;
                        }
                        x += d.width;
                        rowh = Math.max(rowh, d.height);
                    } else {
                        rowh = moveComponents(parent, insets.left + hgap, y,
                                          maxWidth - x, rowh, start, i, ltr);
                        x = d.width;
                        y += vgap + rowh;
                        rowh = d.height;
                        start = i;
                    }
                }
            }
            moveComponents(parent, insets.left + hgap, y, maxWidth - x,
                           rowh, start, componentCount, ltr);
        }
    }
    
    private int moveComponents(Container parent, int x, int y, int width,
                               int height, int rowStart, int rowEnd,
                               boolean ltr) {
        switch(align) {
            case LEFT:
                x += ltr ? 0 : width;
                break;
            case CENTER:
                x += width/2;
                break;
            case RIGHT:
                x += ltr ? width : 0;
                break;
            case LEADING:
                break;
            case TRAILING:
                x += width;
                break;
        }
        for(int i = rowStart; i < rowEnd; i++) {
            Component c = parent.getComponent(i);
            if(c.isVisible()) {
                int cy;
                cy = y + (height - c.getHeight())/2;
                if(ltr) {
                    c.setLocation(x, cy);
                } else {
                    c.setLocation(parent.getWidth() - x - c.getWidth(), cy);
                }
                x += c.getWidth() + hgap;
            }
        }
        return height;
    }
    
    public void addLayoutComponent(String name, Component comp) { }
    public void removeLayoutComponent(Component comp) { }
}*/

/**
  * A modified version of FlowLayout that allows containers using this
  * Layout to behave in a reasonable manner when placed inside a
  * JScrollPane
  * @author Babu Kalakrishnan
  * Modifications by greearb and jzd
  * 
  * Address: http://stackoverflow.com/questions/3679886/how-can-i-let-jtoolbars-wrap-to-the-next-line-flowlayout-without-them-being-hi
  * 
  * big thanks
  */

public class WrappingToolbarLayout extends FlowLayout
{
    public WrappingToolbarLayout()
    {
        super();
    }
    
    public WrappingToolbarLayout(int align)
    {
        super(align);
    }
    
    public WrappingToolbarLayout(int align, int hgap, int vgap)
    {
        super(align, hgap, vgap);
    }
    
    public Dimension minimumLayoutSize(Container target)
    {
        // Size of largest component, so we can resize it in
        // either direction with something like a split-pane.
        return computeMinSize(target);
    }
    
    public Dimension preferredLayoutSize(Container target)
    {
        return computeSize(target);
    }
    
    private Dimension computeSize(Container target)
    {
        synchronized (target.getTreeLock())
        {
            int hgap = getHgap();
            int vgap = getVgap();
            int w = target.getWidth();
            
            // Let this behave like a regular FlowLayout (single row)
            // if the container hasn't been assigned any size yet
            if (w == 0)
            {
                w = Integer.MAX_VALUE;
            }
            
            Insets insets = target.getInsets();
            if (insets == null)
            {
                insets = new Insets(0, 0, 0, 0);
            }
            int reqdWidth = 0;
            
            int maxwidth = w - (insets.left + insets.right + hgap * 2);
            int n = target.getComponentCount();
            int x = 0;
            int y = insets.top + vgap; // FlowLayout starts by adding vgap, so do that here too.
            int rowHeight = 0;
            
            for (int i = 0; i < n; i++)
            {
                Component c = target.getComponent(i);
                if (c.isVisible())
                {
                   Dimension d = c.getPreferredSize();
                   if ((x == 0) || ((x + d.width) <= maxwidth))
                   {
                       // fits in current row.
                       if (x > 0)
                       {
                           x += hgap;
                       }
                       x += d.width;
                       rowHeight = Math.max(rowHeight, d.height);
                   }
                   else
                   {
                       // Start of new row
                       x = d.width;
                       y += vgap + rowHeight;
                       rowHeight = d.height;
                   }
                   reqdWidth = Math.max(reqdWidth, x);
                }
            }
            y += rowHeight;
            y += insets.bottom;
            return new Dimension(reqdWidth+insets.left+insets.right, y);
        }
    }
    
    private Dimension computeMinSize(Container target)
    {
        synchronized (target.getTreeLock())
        {
            int minx = Integer.MAX_VALUE;
            int miny = Integer.MIN_VALUE;
            boolean found_one = false;
            int n = target.getComponentCount();
            
            for (int i = 0; i < n; i++)
            {
                Component c = target.getComponent(i);
                if (c.isVisible())
                {
                    found_one = true;
                    Dimension d = c.getPreferredSize();
                    minx = Math.min(minx, d.width);
                    miny = Math.min(miny, d.height);
                }
            }
            if (found_one)
            {
                return new Dimension(minx, miny);
            }
            return new Dimension(0, 0);
        }
    }
}