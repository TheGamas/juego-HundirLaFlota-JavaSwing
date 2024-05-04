/**
 * Tablero.java
 * 
 * Versión 1 David Colás (calidad) y Samuel Felipe (funcionamiento) (02/2024)
 * - Código para jugar, guardar y recuperar partida
 *  
 */

 /**
  * Clase para el tablero de juego
  * 
  */

package hundirlaflota;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Tablero {
    private Map<String, Set<Barco>> barcos;
    private int filas;
    private int columnas;
    private Set<Posicion> jugadasFallidas;

    static final int HORIZONTAL = 0;
    static final int VERTICAL = 1;
    
    //Disparos
    static final int BARCO_TOCADO = 0;
    static final int AGUA = 1;
    static final int BARCOS_ADYACENTES = 2;
    static final int FUERA = 3;

    static final int INTENTOS_MAX_COLOCAR = 20;
    static final char PRIMERA_LETRA = 'A';

  /**
   * Construye el tablero
   * 
   */
  public Tablero(int columnas, int filas) {
    this.filas = filas;
    this.columnas = columnas;
    this.barcos = new HashMap<String, Set<Barco>>();
    this.jugadasFallidas = new HashSet<Posicion>();
  }

  /**
   * Carga el tablero
   * 
   */
  public Tablero(Scanner scanner) throws Exception {
    columnas = scanner.nextInt();
    filas = scanner.nextInt();

    cargarBarcos(scanner);
    cargarJugadasFallidas(scanner);
  }

  /**
   * Realiza un disparo
   * 
   */  
  public int disparar(Posicion posicion) {

    posicion.disparar();

    if(estaFuera(posicion)) {
      return FUERA;
    }
    for(Set<Barco> barcos : barcos.values()) {
      for(Barco barco : barcos)
      if(barco.disparar(posicion)) {
          return BARCO_TOCADO;
      }
    }
    jugadasFallidas.add(posicion);
    if(hayBarcosAdyacentes(posicion)) {
      return BARCOS_ADYACENTES;
    }
    return AGUA;
  }

  /**
   * Devuelve un string del tablero
   * 
   */
  public String toString() {
    
    String tablero = "";
    char letra = '\0';

    //Poner primera linea
    letra = PRIMERA_LETRA;
    tablero = tablero + ' ';
    for(int columna = 0; columna < columnas; columna++) {
      tablero = tablero + " " + letra++;
    }
    tablero = tablero + "\n";
    letra = PRIMERA_LETRA;
    //Poner resto de lineas
    for(int fila = 0; fila < filas; fila++) {
      tablero = tablero + letra++;
      for(int columna = 0; columna < columnas; columna++) {
        tablero = tablero + " " + toStringPosicion(new Posicion(columna, fila));
      }
      tablero = tablero + "\n";
    }
    return tablero;
  }

  /**
   * Devuelve el String que equivale a una posición del tablero
   *
   */
  private String toStringPosicion(Posicion posicion) {
    
    String ficha = "";

    if(estaTocada(posicion)) {
      ficha = HundirLaFlotaTest.BARCO_SIMBOLO;
    }
    else if(esAgua(posicion)) {
      ficha = HundirLaFlotaTest.DISPARO_AGUA_SIMBOLO;
    }
    else {
      ficha = " ";
    }
    return ficha;
  }

  /**
   * Devuelve cuántos barcos quedan por hundir
   * 
   */
  public int cuantosBarcosQuedan(String tipoBarco) {
    
    int restantes = 0;
    Set<Barco> barcosTipo = barcos.get(tipoBarco);
    if (barcosTipo != null) {
        for(Barco barco : barcosTipo){
            if(! barco.estaHundido()) {
              restantes++;
          }
        }
    }
    return restantes;
  }
  
  /**
   * Comprueba si un barco se solapa con algun otro barco en el tablero
   * 
   */
  private boolean seSolapa(Barco barco) {
    for(Set<Barco> barcosTipo : barcos.values()) {
      for(Barco barcoLista : barcosTipo) {
        if(barco.seSolapa(barcoLista)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Comprueba si una posición está tocada
   * 
   */
  private boolean estaTocada(Posicion posicion) {
    for(Set<Barco> barcosTipo : barcos.values()) {
      for(Barco barco : barcosTipo) {
        if(barco.estaTocada(posicion)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Devuelve verdadero si una posición está ocupada
   * 
   * Devuelve falso en caso contrario
   */
  private boolean estaOcupada(Posicion posicion){
    for(Set<Barco> barcosTipo : barcos.values()) {
      for(Barco barco : barcosTipo) {
        if(barco.estaOcupado(posicion)) {
          return true;
        }
      }
    }
    return false;
  }
 
  /**
   * Devuelve verdadero si una posicion esta fuera del tablero
   * 
   * Devuelve falso en caso contrario
   */
  private boolean estaFuera(Posicion posicion) {
    return posicion.obtenerColumna() >= columnas || posicion.obtenerFila() >= filas;
  }

  /**
   * Devuelve verdadero si un barco esta posicionado fuera del tablero
   * 
   * Devuelve falso en caso contrario
   */
  private boolean estaFuera(Barco barco) {
    for(Posicion posicion : barco.obtenerPosiciones()) {
      if ( estaFuera(posicion) ){
        return true;
      }
    }
    return false;
  }

  /**
   * Devuelve verdadero si una posicion esta disponible
   * 
   * Devuelve falso en caso contrario
   */
  private boolean estaDisponible(Barco barco) {
    return ! estaFuera(barco) && ! seSolapa(barco);
  }

  /**
   * Coloca un barco en el tablero aleatoriamente
   * 
   * Devuelve el resultado de su colocación 
   */
  public int colocarBarco(String tipoBarco) {
  
    int intentos = 0;
    Barco barco = FactoriaBarcos.crear(tipoBarco);

    if(barcos.values().size() == HundirLaFlota.MAXBARCOS) {
      return Partida.DEMASIADOS_BARCOS;
    }

    while( ! estaDisponible(barco) &&
            (intentos < INTENTOS_MAX_COLOCAR)) {
      barco = FactoriaBarcos.crear(tipoBarco);
      intentos++;
    }

    if(intentos == INTENTOS_MAX_COLOCAR) {
      return Partida.BARCO_DEMASIADO_GRANDE;
    }

    Set<Barco> barcosTipo = barcos.getOrDefault(tipoBarco, new HashSet<>());
    barcosTipo.add(barco);
    barcos.put(tipoBarco, barcosTipo);

    return Partida.EXITO_COLOCAR;
  }
  
  /**
   * Devuelve verdadero si hay disparo fallido en una posición
   *
   * Devuelve falso en caso contrario
   */
  public Boolean esAgua(Posicion posicion) {
    
    for(Posicion pos : jugadasFallidas) {
      if(pos.equals(posicion)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Devuelve verdadero si hay barcos adyacentes
   * 
   * Devuelve falso en caso contrario
   */
  private boolean hayBarcosAdyacentes(Posicion posicion) {
    
    int columna = posicion.obtenerColumna();
    int fila = posicion.obtenerFila();
    
    return estaOcupada(new Posicion(columna - 1, fila - 1)) ||
           estaOcupada(new Posicion(columna, fila - 1))     ||
           estaOcupada(new Posicion(columna + 1, fila - 1)) ||
           estaOcupada(new Posicion(columna - 1, fila))     ||
           estaOcupada(new Posicion(columna + 1, fila))     ||
           estaOcupada(new Posicion(columna - 1, fila + 1)) ||
           estaOcupada(new Posicion(columna, fila + 1))     ||
           estaOcupada(new Posicion(columna + 1, fila + 1));
  }

  /**
   * Guarda los barcos en un fichero
   * 
   */
  private void guardarBarcos(PrintWriter pw) throws IOException {
    for(Set<Barco> barcosTipo : barcos.values()) {
      for(Barco barco : barcosTipo) {
        barco.guardar(pw);
        pw.print("\n");
      }
    }
  }

  /**
   * Guarda las posiciones fallidas en un fichero
   * 
   */
  private void guardarPosicionesFallidas(PrintWriter pw) throws IOException{
    for(Posicion pos : jugadasFallidas) {
      pw.print(pos.obtenerColumna() + " " + 
        pos.obtenerFila() +  " " +  
        pos.obtenerTocada() + "   ");
    }
  }
  
  /**
   * Guarda el tablero en un fichero
   * 
   */
  public void guardar(PrintWriter pw) throws IOException {
    pw.println(columnas + " " + filas);
    guardarBarcos(pw);
    pw.println(Partida.DELIMITADOR);
    guardarPosicionesFallidas(pw); 
  }

  /**
   * Carga los barcos desde un fichero
   * 
   */
  private int cargarBarcos(Scanner scanner) throws Exception {

    barcos = new HashMap<String, Set<Barco>>(); 
    Set<Barco> barcosTipo = new HashSet<Barco>();
    scanner.nextLine();
    while (scanner.hasNextLine()) {
      
      if(scanner.hasNext(Partida.DELIMITADOR)) {
        break;
      }
      String linea = scanner.nextLine();
      Scanner lineScanner = new Scanner (linea);
      Barco barco = new Barco(lineScanner);
      lineScanner.close();
      String tipoBarco = barco.obtenerNombre();

      barcosTipo = barcos.getOrDefault(tipoBarco, new HashSet<>());
      barcosTipo.add(barco);
      barcos.put(tipoBarco, barcosTipo);
    }
    
    return Partida.EXITO_CARGAR;
  }
  
  /**
   * Carga las jugadas fallidas desde un fichero
   * 
   */
  private void cargarJugadasFallidas(Scanner scanner) throws Exception {
    
    jugadasFallidas = new HashSet<Posicion>();

    scanner.nextLine();
    while (scanner.hasNext()) {
      jugadasFallidas.add(new Posicion(scanner));
    }
  }
}