package numerik.tasks;

import numerik.ui.misc.LatexFormula;

public class GaussIntegrationOrder4
{
    final LatexFormula formula = new LatexFormula();
    
    public GaussIntegrationOrder4() 
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
