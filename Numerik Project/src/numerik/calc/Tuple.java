package numerik.calc;

/**
 * Klasse zum Erzeugen eines Objektes mit zwei generischen Eigenschaften
 * Geeignet für das Zurückgeben von zwei Werten
 * @param <T1> Typ des Objektes 1
 * @param <T2> Typ des Objektes 2
 */
public class Tuple<T1, T2> 
{ 
    public final T1 item1; 
    public final T2 item2; 
    public Tuple(T1 item1, T2 item2) 
    { 
      this.item1 = item1; 
      this.item2 = item2; 
    } 
} 
