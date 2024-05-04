/**
 * Barco.java
 * 
 * Versión 1 David Colás (calidad) y Samuel Felipe (funcionamiento) (02/2024)
 * - Código para jugar, guardar y recuperar partida
 *  
 */

 /**
  * Clase para los barcos del juego
  * 
  */

package hundirlaflota;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class Barco {
    private Set<Posicion> posiciones;

  /**
   * Construye un barco con sus posiciones
   * 
   */
  public Barco(Set<Posicion> posicionesBarco){
    this.posiciones = posicionesBarco;
  }

  /**
   * Construye el barco desde un fichero 
   * 
   */
  public Barco(Scanner scanner) throws Exception {
    posiciones = new HashSet<Posicion>();
    while(scanner.hasNext()) {
      posiciones.add(new Posicion(scanner));
    }
  }
    
  /**
   * Realiza un disparo
   * 
   */  
  public boolean disparar(Posicion posicion) {
    for(Posicion pos : posiciones) { 
      if(pos.equals(posicion)) {
          pos.disparar();
          return true;
      }
    }
    return false;   
  }
  
  /**
   * Devuelve verdero si la posicion esta ocupada por el barco
   * 
   * Devuelve falso en caso contrario
   */
  public Boolean estaOcupado(Posicion posicion) {
    for(Posicion pos : posiciones) {
        if(pos.equals(posicion)) {
            return true;
        }
    }
    return false;
  }

  /**
   * Devuelve verdadero si el barco esta hundido
   * 
   * Devuelve falso en caso contrario
   */
  public boolean estaHundido() {
    for(Posicion pos : posiciones) {
      if( ! pos.obtenerTocada()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Devuelve verdadero si el barco esta tocado en la posicion
   * 
   * Devuelve falso en caso contrario
   */
  public boolean estaTocada(Posicion posicion) {
    for(Posicion pos : posiciones) {
      if(pos.equals(posicion) && pos.obtenerTocada()) {
        return true;
      }
    }
    return false;
  }


  /**
   * Devuelve verdadero si el barco se solapa con otro barco
   * 
   * Devuelve falso en caso contrario
   */
  public boolean seSolapa(Barco barco){
    for(Posicion pos : posiciones) {
      for(Posicion pos2 : barco.posiciones) {
        if(pos.equals(pos2)) {
          return true;
        }
      }
    }
    return false;
  }

  
  /**
   * Devuelve las posiciones del barco
   * 
   */
  public Set<Posicion> obtenerPosiciones() {
    return posiciones;
  }

  /**
   * Guarda el barco 
   * 
   */
  public void guardar(PrintWriter pw) throws IOException {
    for(Posicion posicion : posiciones) {
        pw.print(posicion.obtenerColumna() + " " 
               + posicion.obtenerFila() + " "
               + posicion.obtenerTocada() + "   ");
    }
  }

  @Override
  public boolean equals(Object obj) {
    if( ! (obj instanceof Barco) || obj == null) {
        return false;
    } 
    if(this == obj) {
        return true;
    }
    return (posiciones.equals(((Barco)obj).posiciones));
  }

  @Override
  public int hashCode() {
    return posiciones.hashCode();
  }

public String obtenerNombre() {
    return "hola";
}
}