/**
 * Posicion.java
 * 
 * Versión 1 David Colás (calidad) y Samuel Felipe (funcionamiento) (02/2024)
 * - Código para jugar, guardar y recuperar partida
 *  
 */

 /**
  * Clase para las posiciones del tablero
  * 
  */

package hundirlaflota.modelo;

import hundirlaflota.control.HundirLaFlota;
import java.util.Random;
import java.util.Scanner;


public class Posicion {
    private int columna;
    private int fila;
    private boolean tocada;

    public enum EstadoPosicion { NO_TOCADA, TOCADA_AGUA, TOCADA_BARCO, BARCOS_ADYACENTES}

  /**
   * Construye una posición con el estado
   * 
   */
  public Posicion(int columna, int fila, boolean tocada) {
      this.columna = columna;
      this.fila = fila;
      this.tocada = tocada;
  }

  /**
   * Construye una posición no tocada
   * 
   */
  public Posicion(int columna, int fila) {
      this.columna = columna;
      this.fila = fila;
      tocada = false;
  }

  /**
   * Construye una posición aleatoria
   * 
   */
  public Posicion(Random random){
      this.columna = random.nextInt(HundirLaFlota.COLUMNAS);
      this.fila = random.nextInt(HundirLaFlota.FILAS);
  }

  /**
   * Carga una posicion de un fichero
   * 
   */
  public Posicion(Scanner scanner) throws Exception {
    columna = scanner.nextInt();
    fila = scanner.nextInt();
    tocada = scanner.nextBoolean();
  }

  /**
   * Cambia el estado de la posición
   * 
   */
  public void disparar() {
      tocada = true;
  }

  /**
   * Devuelve la columna
   * 
   */
  public int obtenerColumna() {
      return columna;
  }

  /**
   * Devuelve la fila
   * 
   */
  public int obtenerFila() {
      return fila;
  }

  /**
   * Devuelve el estado de la posición
   * 
   */
  public boolean obtenerTocada() {
    return tocada;
  }

  /**
   * Devuelve si dos posiciones son iguales
   * 
   */
  @Override
  public boolean equals(Object obj) {
    if( ! (obj instanceof Posicion) || obj == null) {
        return false;
    } 
    if(this == obj) {
        return true;
    }
    return (columna == ((Posicion)obj).columna &&
            fila == ((Posicion)obj).fila);
  }

  @Override
  public int hashCode() {
    int resultado = 17;
    resultado = 37 * resultado + columna;
    return 37 * resultado + fila;
  }

  public String to() {
    return "Posicion [columna=" + columna + ", fila=" + fila + ", tocada=" + tocada + "]";
  }
}