/**
 * Partida.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (05/2024)
 * - Código jugar, cargar y guardar partida
 *  
 */

/**
 * Clase para gestionar una partida de hundir la flota
 * 
 */

package hundirlaflota.control;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import hundirlaflota.modelo.FactoriaBarcos;
import hundirlaflota.modelo.Posicion;
import hundirlaflota.modelo.Tablero;
import hundirlaflota.vista.PartidaVista;

public class Partida {
    private Tablero tablero;
    private boolean guardada;

    public static int EXITO_COLOCAR = 1;
    public static int BARCO_DEMASIADO_GRANDE = 2;
    public static int DEMASIADOS_BARCOS = 3;
    public static int BARCO_DESCONOCIDO = -1;
    public static int EXITO_CARGAR = 5;
    public static final String DELIMITADOR_POSICIONES = ";";

    public static int NUM_PORTAVIONES = 1;
    public static int NUM_CRUCEROS = 1;
    public static int NUM_DESTRUCTORES = 2;
    public static int NUM_FRAGATAS = 3;

    public static String DELIMITADOR_BARCOS = "#";

  /**
   * Construye una partida nueva
   * 
   */
  public Partida(PartidaVista vista, int columnas, int filas) {
    tablero = new Tablero(columnas, filas);
    tablero.nuevoObservador(vista);
    guardada = false;


  }

  /**
   * Construye una partida a partir de un fichero
   * 
   */
  public Partida(PartidaVista vista, String nombreFichero) throws Exception {  
       
    recuperar(nombreFichero);
    
    guardada = true;
    
    tablero.nuevoObservador(vista);
    vista.inicializarVista();
    vista.recuperarPartida(tablero);
  }
  
  /**
   * Realiza un disparo
   * 
   */  
  public Posicion.EstadoPosicion disparar(Posicion posicion) {
    guardada = false;
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
    guardada = true;   

  }

  /**
   * Carga una partida desde un fichero
   * 
   */  
  private void recuperar(String nombreFichero) throws Exception {
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
   *  Devuelve si partida está guardada
   * 
   */    
  boolean guardada() {
    return guardada;
  }

  /**
   * Devuelve un String de la partida
   * 
   */
  @Override
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
