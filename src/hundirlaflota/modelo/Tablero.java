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

package hundirlaflota.modelo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;


import hundirlaflota.control.HundirLaFlota;
import hundirlaflota.control.OyenteVista;
import hundirlaflota.control.Partida;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class Tablero {
    private Map<String, Set<Barco>> barcos;
    private int filas;
    private int columnas;
    private Set<Posicion> jugadasFallidas;
      private PropertyChangeSupport observadores;


    static final int HORIZONTAL = 0;
    static final int VERTICAL = 1;
    
    //Disparos

    static final int INTENTOS_MAX_COLOCAR = 20;
    static final char PRIMERA_LETRA = 'A';

    

  /**
   * Construye el tablero
   * 
   */
  public Tablero(int columnas, int filas) {
    this.filas = filas;
    this.columnas = columnas;
    observadores = new PropertyChangeSupport(this);
    
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
    observadores = new PropertyChangeSupport(this);
    cargarBarcos(scanner);
    cargarJugadasFallidas(scanner);
    
  }
  /**
  *  Añade observador del tablero
  *  
  */     
  public void nuevoObservador(PropertyChangeListener observador) {
    observadores.addPropertyChangeListener(observador);
  }

  private Map<String, Integer> barcosRestantes(){
    Map <String, Integer> barcosRestantes = new HashMap<String, Integer>();
    for(String tipoBarco : barcos.keySet()) {
      barcosRestantes.put(tipoBarco, cuantosBarcosQuedan(tipoBarco));
    }
    return barcosRestantes;  
  }

  /**
   * Realiza un disparo
   * 
   */  
  public Posicion.EstadoPosicion disparar(Posicion posicion) {

    posicion.disparar();
    String nombre = null;

    for(Entry<String, Set<Barco>> barcosTipo : barcos.entrySet()) {
      nombre = barcosTipo.getKey();
      for(Barco barco : barcosTipo.getValue()) {
        if(barco.disparar(posicion)) {
          if (barco.estaHundido()){
            observadores.firePropertyChange("HUNDIDO", null, nombre);
          }
          else{
            observadores.firePropertyChange("TOCADO", null, posicion);
          }
          return Posicion.EstadoPosicion.TOCADA_BARCO;
        }
      }
      jugadasFallidas.add(posicion);
      if(hayBarcosAdyacentes(posicion)) {
        return Posicion.EstadoPosicion.TOCADA_AGUA;
      }

    
  } 
  return Posicion.EstadoPosicion.NO_TOCADA;
  }

  public Posicion.EstadoPosicion estadoPosicion(Posicion posicion){
    if(estaTocada(posicion)) {
      return Posicion.EstadoPosicion.TOCADA_BARCO;
    }
    else if(esAgua(posicion)) {
      return Posicion.EstadoPosicion.TOCADA_AGUA;
    }
    else if (hayBarcosAdyacentes(posicion)){
      return Posicion.EstadoPosicion.BARCOS_ADYACENTES;
    }
    else {
      return Posicion.EstadoPosicion.NO_TOCADA;

    }
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

    if(barcos.values().size() == HundirLaFlota.MAX_BARCOS) {
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
    for(Entry<String, Set<Barco>> barcosTipo : barcos.entrySet()) {
      pw.print(barcosTipo.getKey());
      pw.print(" ");
      for(Barco barco : barcosTipo.getValue()) {
        barco.guardar(pw);
        pw.print(Partida.DELIMITADOR_POSICIONES);
        pw.print(" ");
      }
      pw.print("\n");
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
    pw.println(Partida.DELIMITADOR_BARCOS);
    guardarPosicionesFallidas(pw); 
  }

  /**
   * Carga los barcos desde un fichero
   * 
   */
  /**
   * Carga los barcos desde un fichero
   * 
   */
  private int cargarBarcos(Scanner scanner) throws Exception {

    barcos = new HashMap<String, Set<Barco>>(); 
    Set<Barco> barcosTipo = new HashSet<Barco>();
    scanner.nextLine();
    while (scanner.hasNextLine()) {
      
      if(scanner.hasNext(Partida.DELIMITADOR_BARCOS)) {
        break;
      }
      String linea = scanner.nextLine();
      Scanner lineScanner = new Scanner (linea);
      String tipoBarco = lineScanner.next();
      
      while (lineScanner.hasNext()) {
        Barco barco = new Barco(lineScanner);
        lineScanner.next();
        
        barcosTipo = barcos.getOrDefault(tipoBarco, new HashSet<>());
        barcosTipo.add(barco);
        barcos.put(tipoBarco, barcosTipo);
      }
      lineScanner.close();
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

  public String toString() {
    String tablero = "";
    for(int fila = 0; fila < filas; fila++) {
      for(int columna = 0; columna < columnas; columna++) {
        Posicion posicion = new Posicion(columna, fila);
        if(estaTocada(posicion)) {
          tablero += "X";
        }
        else if(esAgua(posicion)) {
          tablero += "O";
        }
        else {
          tablero += " ";
        }
      }
      tablero += "\n";
    }
    return tablero;
  
  }
}