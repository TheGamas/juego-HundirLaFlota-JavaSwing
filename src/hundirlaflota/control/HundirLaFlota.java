/**
 * HundirLaFlota.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (05/2024)
 * - Código para jugar, guardar y recuperar partida
 *  
 */

/**
 * Clase HundirLaFlota
 * 
 */

package hundirlaflota.control;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hundirlaflota.modelo.FactoriaBarcos;
import hundirlaflota.modelo.Posicion;
import hundirlaflota.vista.DebugVista;
import hundirlaflota.vista.PartidaVista;

public class HundirLaFlota implements OyenteVista{
  
  private static String ARG_DEBUG = "-d";
  
  public static int FILAS = 10;
  public static int COLUMNAS = 10;
  public static int MAX_BARCOS = 7;
  public static String VERSION = "Hundir la flota 2.0";

  private String ficheroPartida = null;


  private static boolean modoDebug = false;

  

  private PartidaVista vista;
  private Partida partida;


  /**
   *  Construye juego Hundir la flota
   * 
   */
  public HundirLaFlota(String args[]) {
     procesarArgsMain(args);      
     
  
     vista = PartidaVista.devolverInstancia(this, VERSION, FILAS, COLUMNAS);
  }

  
  /**
   * Gestiona eventos producidos por la vista
   * 
   */
  @Override
  public void eventoProducido(Evento evento, Object obj) {

    switch(evento) {
      case NUEVA:
        nuevaPartida();
        break;

      case ABRIR:
        abrirPartida();
        break;             
          
      case SALIR: 
        salir();
        break;
      
      case GUARDAR:
        guardarPartida();
        break;            

      case GUARDAR_COMO:
        guardarPartidaComo(); 
        break;            
                      
      case DISPARAR:
        Posicion posicion = (Posicion) obj;
        disparar(posicion);
        break;
    }
  }

  /**
   * Escribe mensaje error
   * 
   */
  private void mensajeError(String mensaje, Exception e) {
    if (esModoDebug()) {
      DebugVista.devolverInstancia().mostrar(mensaje, e);             
    }
    else{
      vista.mensajeDialogo(mensaje);    
    }
  }

  

  /**
   *  Indica si aplicación está en modo debug
   * 
   */ 
  public static boolean esModoDebug() {
    return modoDebug;
  }

  private void colocarBarcos(){
    partida.colocarBarco(FactoriaBarcos.PORTAVIONES);
    partida.colocarBarco(FactoriaBarcos.CRUCERO);
    partida.colocarBarco(FactoriaBarcos.DESTRUCTOR);
    partida.colocarBarco(FactoriaBarcos.DESTRUCTOR);
    partida.colocarBarco(FactoriaBarcos.FRAGATA);
    partida.colocarBarco(FactoriaBarcos.FRAGATA);
    partida.colocarBarco(FactoriaBarcos.FRAGATA);
  }

  /**
   *  Empieza nueva partida
   * 
   */ 
  private void nuevaPartida() { 
    guardarPartidaActual();
    ficheroPartida = null;
    vista.ponerTitulo("");      

    partida = new Partida(vista, FILAS, COLUMNAS);
    colocarBarcos();
    vista.habilitarEvento(Evento.GUARDAR, false);
    vista.habilitarEvento(Evento.GUARDAR_COMO, false);   
    vista.habilitarEvento(Evento.DISPARAR, true);
  }

  /**
   *  Abre partida
   * 
   */      
  private void abrirPartida() {
     guardarPartidaActual();
     ficheroPartida = vista.seleccionarFichero(PartidaVista.ABRIR_FICHERO);
     if (ficheroPartida != null) {
         try {
            partida = new Partida(vista, ficheroPartida);

            vista.habilitarEvento(Evento.GUARDAR, false); 
            vista.habilitarEvento(Evento.DISPARAR, true);
    
                     
         } catch(FileNotFoundException e1) {
           mensajeError(PartidaVista.FICHERO_NO_ENCONTRADO, e1);
         } catch(Exception e2) {
           mensajeError(PartidaVista.PARTIDA_NO_LEIDA, e2);
         }
     }
     else{
     }
  }

  /**
   * Pone ficha en tablero de juego
   * 
   */
  private void disparar(Posicion posicion) {  
    if (partida != null){
      partida.disparar(posicion);
    }
  }

  /**
   *  Guarda partida
   * 
   */
  private void guardarPartida() {      
    if (ficheroPartida == null) {
      guardarPartidaComo();
    } else {
      try {
        partida.guardar(ficheroPartida);
        vista.habilitarEvento(Evento.GUARDAR, false);         
      } catch(Exception e) {
        mensajeError(PartidaVista.PARTIDA_NO_GUARDADA, e);
      }
    }
  }
 

  /**
   *  Guarda partida como
   * 
   */      
  private void guardarPartidaComo() {
    ficheroPartida = vista.seleccionarFichero(PartidaVista.GUARDAR_FICHERO);
    if (ficheroPartida != null) { 
      guardarPartida();
    }  
 }  

 /**
  *  Guarda partida actual
  * 
  */        
 private void guardarPartidaActual() {
   if ((partida != null) && (! partida.guardada())) {
     if (vista.mensajeConfirmacion(
      PartidaVista.CONFIRMACION_GUARDAR) == PartidaVista.OPCION_SI) {               
       guardarPartida();
     }
   }      
 }

  /**
   *  Salir de la aplicación
   * 
   */      
  private void salir() {
    guardarPartidaActual();
    System.exit(0);    
  }

  /**
   *  Procesa argumentos de main
   * 
   */  
  private static void procesarArgsMain(String[] args) {
    List<String> argumentos = new ArrayList<String>(Arrays.asList(args));  
    
    if (argumentos.contains(ARG_DEBUG)) {
      modoDebug = true;    
    }
  }  
  
  /**
   *  Método main
   * 
   */   
  public static void main(String[] args) {
    new HundirLaFlota(args);
  }
}
