package hundirlaflota;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FactoriaBarcos {
    public static final int LONGITUD_PORTAVIONES = 5;
    public static final int LONGITUD_CRUCERO = 4;
    public static final int LONGITUD_DESTRUCTOR = 3;
    public static final int LONGITUD_FRAGATAS = 2;

    public static final String PORTAVIONES = "Portaviones";
    public static final String CRUCERO = "Crucero";
    public static final String DESTRUCTOR = "Destructor";
    public static final String FRAGATA = "Fragata";

    static final int HORIZONTAL = 0;
    static final int VERTICAL = 1;


    public static Barco crear(String nombreBarco) {
        
        if(nombreBarco.equals(PORTAVIONES)) {
            return portaviones();
        } else if(nombreBarco.equals(CRUCERO)) {
            return crucero();
        } else if(nombreBarco.equals(DESTRUCTOR)) {
            return destructor();
        } else if(nombreBarco.equals(FRAGATA)) {
            return fragata();
        } else {
            return null;
        }

    }

    private static Barco portaviones() {
        return barcoLineal(LONGITUD_PORTAVIONES);
    }   

    private static Barco crucero() {
        return barcoLineal(LONGITUD_CRUCERO);
    }

    private static Barco destructor() {
        return barcoLineal(LONGITUD_DESTRUCTOR); 
    }

    private static Barco fragata() {
        return barcoLineal(LONGITUD_FRAGATAS); 
    }

    private static Barco barcoLineal(int longitud) {
    
      Set<Posicion> posicionesBarco = new HashSet<Posicion>();
      Random random = new Random();
      Posicion posicion_inicial = new Posicion(random);
      int columna = posicion_inicial.obtenerColumna();
      int fila = posicion_inicial.obtenerFila();
      int direccion = random.nextInt(2);
      int incColumna = 0;
      int incFila = 0;

      if(direccion == VERTICAL) {
        incFila = 1;
      }
      else {
        incColumna = 1;
      }
      for(int i = 0; i < longitud; i++) {
        posicionesBarco.add(new Posicion(fila, columna));  
        columna = columna + incColumna;
        fila = fila + incFila;
      }
      return new Barco(posicionesBarco);
    
    }
}
