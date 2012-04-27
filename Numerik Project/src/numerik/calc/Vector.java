package numerik.calc;

import java.math.BigDecimal;

public class Vector extends Matrix {
  
  private boolean transposed;
  private int length;
  
  public Vector( int rows ) {
//	super( rows, 0 );
//	setRows( rows );
//	setCols( 0 );
//
//	BigDecimal[] values = new BigDecimal[rows];
//
//	for (int row = 0; row<rows; row++) {
//		values[row] = BigDecimal.ZERO;
//	}
//    this.transposed = false;
//    this.length 	= values.length;
	  this(new BigDecimal[rows]);
	  setToNullvector();
  }
  
  public Vector(BigDecimal[] values) {
    this(values, false);
  }
  
  public Vector(BigDecimal[] values, boolean transposed) {
    super(values, transposed ? values.length : 1);
    this.transposed = transposed;
    this.length 	= values.length;
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
  
  public void set(int index, BigDecimal value) {
	    if (transposed) {
	      this.set(0, index, value);
	      
	    } else {
	      this.set(index, 0, value);
	      
	    }
	  }
  
  public int getLength() {
    return length;
  }
  
  @Override
  public Vector getTransposed() {
    BigDecimal[] values = new BigDecimal[length];
    for (int i = 0; i < length; i++) {
      values[i] = get(i);
    }
    return new Vector(values, !transposed);
  }
  
  public void setToNullvector() {
	  for(int i=0; i<length; i++) {
		  set(i, BigDecimal.ZERO);
	  }
  }
  
  
}
