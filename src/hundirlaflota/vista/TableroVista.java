/**
 * TableroVista.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (05/2024)
 * - Código jugar, cargar y guardar partida
 *  
 */

/**
 * Clase de la vista para el tablero
 * 
 */

package hundirlaflota.vista;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import hundirlaflota.modelo.Posicion;

/**
 * Vista de una posicion a partir de un JLabel
 */
public class TableroVista extends JPanel {
  private static final int ALTURA_FILA = 40;
  private static final int ANCHURA_COLUMNA = 40;
  private Casilla casillas[][];
  private PartidaVista juegoVista;

  public static final boolean RECIBIR_EVENTOS_RATON = true;
  public static final boolean NO_RECIBIR_EVENTOS_RATON = false;

  TableroVista(PartidaVista juegoVista, int filas, int columnas, 
                boolean recibeEventosRaton) {   
    this.setEnabled(false);
    this.juegoVista = juegoVista;
    
    setLayout(new GridLayout(filas, columnas));
    
    crearCasillas(filas, columnas, recibeEventosRaton);
    
    this.setPreferredSize(new Dimension(filas * ALTURA_FILA, 
                                        columnas * ANCHURA_COLUMNA));
  }

  /**
   * Crea casillas
   * 
   */
  private void crearCasillas(int filas, int columnas, boolean recibeEventosRaton) {
    setLayout(new GridLayout(filas + 1, columnas + 1));
    casillas = new Casilla[filas][columnas];    

    // Añadir espacio vacío en la esquina superior izquierda
    add(new JLabel(""));

    // Añadir letras para las columnas
    for (int col = 0; col < columnas; col++) {
        JLabel letra = new JLabel(Character.toString((char) ('A' + col)), SwingConstants.CENTER);
        letra.setBorder(null);
        add(letra);
    }

    for(int fil = 0; fil < filas; fil++) {
        // Añadir letra para la fila
        JLabel letra = new JLabel(Character.toString((char) ('A' + fil)), SwingConstants.CENTER);
        letra.setBorder(null);
        add(letra);

        for(int col = 0; col < columnas; col++) {
            casillas[fil][col] = new Casilla(juegoVista, new Posicion(fil, col), recibeEventosRaton);
            add(casillas[fil][col]);
        }       
    }
  }

  /**
   * Inicializa tablero
   * 
   */  
  void inicializar() {
    for(int fil = 0; fil < casillas.length; fil++) {
      for(int col = 0; col < casillas[0].length; col++) {
        ponerIconoCasilla(new Posicion(fil, col), null);     
      }
    } 
  }

  /**
   * Devuelve el tamaño del tablero
   * 
   */  
  Dimension dimensionCasilla() {
    return casillas[0][0].getSize();
  }

  /**
   * Habilita o deshabilita tablero vista
   * 
   */    
  void habilitar(boolean habilitacion) {
    this.setEnabled(habilitacion);  
    
    for(int fil = 0; fil < casillas.length; fil++) {
      for(int col = 0; col < casillas[0].length; col++) {
        casillas[fil][col].habilitar(habilitacion);     
      }
    }
  }
  
  /**
   * Pone un icono en la casilla de la posición indicada
   * 
   */   
  void ponerIconoCasilla(Posicion posicion, Icon icono) {     
    casillas[posicion.obtenerColumna()][posicion.obtenerFila()]
      .ponerIcono(icono);
  }
}
