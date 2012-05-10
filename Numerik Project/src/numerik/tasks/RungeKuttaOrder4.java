package numerik.tasks;

import numerik.ui.LatexFormula;

public class RungeKuttaOrder4
{
    final LatexFormula formula = new LatexFormula();
    
    public RungeKuttaOrder4() 
    {
        init();
    }
    
    public void init()
    {
        formula.addText("");
    }
    
    public LatexFormula getFormula() {
        return this.formula;
    }
}