package numerik.calc;

import java.math.BigDecimal;

public class MathLib {
  
  private static int precision;
  private static boolean active;
  
  public static BigDecimal round(BigDecimal value) {
    
    // ziemlich wahrscheinlich noch fehlerhaft -> ungenutzte Variablen
    if (active) {
      
      long tempmant = 0;
      double tempexp = 0;
      String tempstr[];
      boolean sign = value.signum() == -1;
      value = value.abs();
      
      String strValue = value.toEngineeringString();
      
      
      String[] parts = strValue.split("\\.");
      String intDigits = parts[0];
      String fracDigits = parts[1];
      int exponent = 0;
      int mantissa = 0;
      
      if (fracDigits.contains("E")) {
        parts = fracDigits.split("E");
        fracDigits = parts[0];
        exponent = Integer.valueOf(parts[1]);
      } 
      
      if (Integer.valueOf(intDigits)!=0 && value.doubleValue() != 0) {
        
        mantissa = mantissa - intDigits.length();
        
      } else {
        
        tempexp  = fracDigits.length();
        tempmant = Long.valueOf( fracDigits );
        
        fracDigits = String.valueOf(tempmant);
        mantissa = precision + 3;
        
        if(fracDigits.length() < mantissa) {
          //System.out.println( "nachher: "+zahl+"\n" );
          
        } else {
        
//          zahl = toBD( String.copyValueOf( nkomma.toCharArray(), 0, mantisse+1 ) );
//          d_mantisse = (int)(-(tempexp-String.valueOf(tempmant).length()+mantisse)-2);
//          zahl = zahl.divide( toBD( toBD( Math.pow(10, 1)).doubleValue()), 0, BigDecimal.ROUND_HALF_UP);
//          zahl = zahl.divide( toBD( Math.pow(10, mantisse)), mantisse, BigDecimal.ROUND_HALF_UP);
//          dec  = zahl.multiply( toBD(sign) );
          
//          System.out.println("nachher: "+dec+"\n");
        }
      }
      
      value = value.multiply(pow10(mantissa + 1)).setScale(0, BigDecimal.ROUND_FLOOR);
      value = value.divide(new BigDecimal(10), 0, BigDecimal.ROUND_HALF_UP);
      value = value.divide(pow10(mantissa), precision, BigDecimal.ROUND_HALF_UP);
      if (sign) {
        value = value.negate();
      }
      
//      System.out.println("nachher: "+ dec +", d_mantisse: "+ d_mantisse );
    }
    return value;
  }
  
  
  public static BigDecimal pow10(int exponent) {
    if (exponent < 0) {
      System.err.println("MathLib.pow10-Funktion: Nur positive Exponenten erlaubt: exponent=" + exponent);
      return BigDecimal.ZERO;
    }
    
    if (exponent == 1) {
      return BigDecimal.ONE;
    }
    
    StringBuilder number = new StringBuilder("10");
    
    for (int i = 1; i < exponent; i++) {
      number.append("0");
    }
    return new BigDecimal(number.toString());
  }
  
  
  // getters and setters
  public static int getPrecision() {
    return precision;
  }
  
  public static void setPrecision(int precision) {
    MathLib.precision = precision;
  }
  
  
  public static boolean isActive() {
    return active;
  }
  
  public static void setActive(boolean active) {
    MathLib.active = active;
  }
}