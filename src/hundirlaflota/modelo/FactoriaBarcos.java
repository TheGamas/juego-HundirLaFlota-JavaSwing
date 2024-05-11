/**
 * FactoriaBarcos.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (05/2024)
 * - Código jugar, cargar y guardar partida
 *  
 */

/**
 * Clase de control para la creación de barcos
 * 
 */

package hundirlaflota.modelo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FactoriaBarcos {
private static final int LONGITUD_PORTAVIONES = 5;
private static final int LONGITUD_CRUCERO = 4;
private static final int LONGITUD_DESTRUCTOR = 3;
private static final int LONGITUD_FRAGATAS = 2;

public static final String PORTAVIONES = "Portaviones";
public static final String CRUCERO = "Crucero";
public static final String DESTRUCTOR = "Destructor";
public static final String FRAGATA = "Fragata";

static final int HORIZONTAL = 0;
static final int VERTICAL = 1;

  /**
   * Construye un barco a partir de su nombre
   * 
   */
  public static Barco crear(String nombreBarco) {
      
    if(nombreBarco.equals(PORTAVIONES)) {
      return portaviones();
    } 
    else if(nombreBarco.equals(CRUCERO)) {
      return crucero();
    } 
    else if(nombreBarco.equals(DESTRUCTOR)) {
      return destructor();
    } 
    else if(nombreBarco.equals(FRAGATA)) {
      return fragata();
    } 
    else {
      return null;
    }

  }

  /**
   * Crea un barco de tipo portaviones
   * 
   */
  private static Barco portaviones() {
    return barcoLineal(LONGITUD_PORTAVIONES);
  }   

  /**
   * Crear un barco de tipo crucero
   * 
   */
  private static Barco crucero() {
    return barcoLineal(LONGITUD_CRUCERO);
  }

  /**
   * Crear un barco de tipo destructor
   * 
   */
  private static Barco destructor() {
    return barcoLineal(LONGITUD_DESTRUCTOR); 
  }

  /**
   * Crear un barco de tipo fragata
   * 
   */
  private static Barco fragata() {
    return barcoLineal(LONGITUD_FRAGATAS); 
  }

  /**
   * Crear un barco lineal
   * 
   */
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
