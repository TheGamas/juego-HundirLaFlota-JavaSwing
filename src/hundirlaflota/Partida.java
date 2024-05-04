/**
 * Partida.java
 * 
 * Versión 1 David Colás (calidad) y Samuel Felipe (funcionamiento) (02/2024)
 * - Código para jugar, guardar y recuperar partida
 *  
 */

 /**
  * Clase para la partida de hundir la flota
  * 
  */

package hundirlaflota;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Partida {
    private Tablero tablero;

    static final int EXITO_COLOCAR = 1;
    static final int BARCO_DEMASIADO_GRANDE = 2;
    static final int DEMASIADOS_BARCOS = 3;
    static final int BARCO_DESCONOCIDO = -1;
    static final int EXITO_CARGAR = 5;

    static final String DELIMITADOR = "#";

  /**
   * Construye una partida nueva
   * 
   */
  public Partida(int columnas, int filas) {
    tablero = new Tablero(columnas, filas);
  }
  
  /**
   * Realiza un disparo
   * 
   */  
  public int disparar(Posicion posicion) {
    return tablero.disparar(posicion);
  }
 
  /**
   * Guarda la partida actual
   * 
   */  
  public void guardar(String nombreFichero) throws IOException {
    PrintWriter fichero = 
      new PrintWriter(new BufferedWriter(new FileWriter(nombreFichero)));    
    
    tablero.guardar(fichero);    
    fichero.close();
  }

  /**
   * Carga una partida 
   * 
   */  
  public Partida(String nombreFichero) throws Exception {
    Scanner fichero = new Scanner(new FileInputStream(nombreFichero));
    tablero = new Tablero(fichero);
    fichero.close();
  }

  /**
   * Coloca un barco en la partida aleatoriamente
   * 
   * Devuelve el resultado de la colocación
   */
  public int colocarBarco(String nombreBarco) {
    int resultado = tablero.colocarBarco(nombreBarco);
    
    return resultado;
  }

  /**
   * Devuelve un String de la partida
   * 
   */
  public String toString() {
    String cadena = "";
    cadena = cadena + HundirLaFlota.VERSION + "\n";
    
    cadena = cadena + tablero.toString();

    cadena = cadena + "Quedan " + 
    tablero.cuantosBarcosQuedan(FactoriaBarcos.PORTAVIONES) + " portavion/es, " + 
    tablero.cuantosBarcosQuedan(FactoriaBarcos.CRUCERO) + " crucero/s, " + 
    tablero.cuantosBarcosQuedan(FactoriaBarcos.DESTRUCTOR) + " destructor/es y " + 
    tablero.cuantosBarcosQuedan(FactoriaBarcos.FRAGATA) + " fragata/s. \n";
   
    return cadena;
  }
}
