package numerik.calc;

import java.math.BigDecimal;

public class Vector extends Matrix {
  
  private boolean transposed;
  private int length;
  
  
  public Vector(BigDecimal[] values) {
    this(values, false);
  }
  
  public Vector(BigDecimal[] values, boolean transposed) {
    super(values, transposed ? values.length : 1);
    this.transposed = transposed;
    this.length = values.length;
  }
  
  public boolean isTransposed() {
    return transposed;
  }
  
  public BigDecimal get(int index) {
    if (transposed) {
      return this.get(0, index);
      
    } else {
      return this.get(index, 0);
      
    }
  }
  
  public int getLength() {
    return length;
  }
  
  @Override
  public Vector getTransposedMatrix() {
    BigDecimal[] values = new BigDecimal[length];
    for (int i = 0; i < length; i++) {
      values[i] = get(i);
    }
    return new Vector(values, !transposed);
  }
  
}
