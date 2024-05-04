/**
 * HundirLaFlota.java
 * 
 * Versión 1 David Colás (calidad) y Samuel Felipe (funcionamiento) (02/2024)
 * - Código para jugar, guardar y recuperar partida
 *  
 */

 /**
  * Juego hundir la flota
  * 
  */

package hundirlaflota;
import java.io.IOException;

public class HundirLaFlota {
    private Partida partida;
    private static HundirLaFlota instanciaUnica = null;
    public static String VERSION = "Hundir la flota 1.0";
    public static int MAX_JUGADAS_FALLIDAS = 50; 

    public static final int FILAS = 10;
    public static final int COLUMNAS = 10;
    public static final int MAXBARCOS = 7;

    private HundirLaFlota() {

    }

    public static synchronized HundirLaFlota obtenerInstancia() {
        if(instanciaUnica == null) {
           instanciaUnica = new HundirLaFlota(); 
        }
        return instanciaUnica;
    }

    /**
     * Construye la partida desde un fichero
     * 
     */
    public HundirLaFlota(String nombreFichero) throws Exception {
        partida = new Partida(nombreFichero);
    }

    /**
     * Construye una partida nueva            
     * 
     */
    public HundirLaFlota(int columnas, int filas) {
        partida = new Partida(columnas, filas);
    }
    
    /**
     * Crea una nueva partida
     * 
     */   
    public int colocarBarco(String nommbreBarco) {
        return partida.colocarBarco(nommbreBarco);
    }

    /**
     * Devuelve el estado actual del tablero
     * 
     */
    public String toString() {
        return partida.toString();
    }
    
    /**
     * Hace un disparo
     * 
     */
    public int disparar(Posicion posicion) {
        return partida.disparar(posicion);
    }

    /**
     * Guarda la partida en un fichero
     * 
     */
    public void guardarPartida(String nombreFichero) throws IOException {
        partida.guardar(nombreFichero);
    }
}
