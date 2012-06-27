package numerik.expression;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

public final class VariablesPool
{
    public static final BigDecimal PI = new BigDecimal("3.14159265358979");
    
    
    private boolean readOnly;
    
    private HashMap<String, Value> pool = null;
    
    
    public VariablesPool()
    {
        this(false);
    }
    
    public VariablesPool(boolean readOnly)
    {
        // Map initialisieren
        clear();
        
        this.readOnly = readOnly;
    }
    
    
    public void set(String name, Value value)
    {
        if (!readOnly && !name.equals("PI"))
        {
            pool.put(name, value);
        }
    }
    
    public Value get(String name)
    {
        return pool.get(name);
    }
    
    public void remove(String name)
    {
        pool.remove(name);
    }
    
    public boolean contains(String name)
    {
        return pool.containsKey(name);
    }
    
    public void clear()
    {
        pool = new HashMap<>();
        pool.put("PI", new Value(PI));
    }
    
    
    public Set<String> getAllNames()
    {
        return pool.keySet();
    }
    
    
    public boolean isReadOnly()
    {
        return readOnly;
    }
    
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }
}