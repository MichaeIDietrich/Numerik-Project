package numerik.ui;

public final class Recorder
{
    private static Recorder recorder;
    private LatexFormula formula = new LatexFormula();
    private boolean      isempty = true;
    private boolean       active = true;
    
    
    private Recorder() { } //private Contructor, um zu vermeiden, dass jemand diese Klasse instanziieren will (Singleton)
    
    
    public void add(LatexFormula formula) 
    {
        isempty = false;
        this.formula.addFormula(formula);
    }
    
    
    public LatexFormula get()
    {
        return this.formula;
    }
    
    
    public LatexFormula get(boolean clear)
    {
        LatexFormula tempFormula = this.formula;
        if (clear) clear();
        return tempFormula;
    }
    
    
    public void clear()
    {
        isempty = true;
        formula = new LatexFormula();
    }
    
    
    public static Recorder getInstance()
    {    
        if (recorder==null)
        {
            recorder = new Recorder();
        }
        return recorder;
    }
    
    
    public boolean isEmpty() {
        return isempty;
    }
    
    
    public boolean isActive()
    {
        return active;
    }
    
    
    public void setActive(boolean active)
    {
        this.active = active;
    }
}
