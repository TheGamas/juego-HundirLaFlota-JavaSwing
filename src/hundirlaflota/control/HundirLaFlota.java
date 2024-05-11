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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import hundirlaflota.modelo.Posicion;
import hundirlaflota.modelo.Tupla;
import hundirlaflota.vista.DebugVista;
import hundirlaflota.vista.FactoriaPartidasVista;
import hundirlaflota.vista.Localizacion;
import hundirlaflota.vista.PartidaVista;

public class HundirLaFlota implements OyenteVista{
  
  private static String ARG_DEBUG = "-d";
  
  public static int FILAS = 10;
  public static int COLUMNAS = 10;
  public static int MAX_BARCOS = 7;
  public static String VERSION = "2.0";

  private String ficheroPartida = null;
  private static boolean modoDebug = false;

  

  private PartidaVista vista;
  private Localizacion local;

  private Partida partida;


  /** Configuración */  
  private static String FICHERO_CONFIG_WRONG = 
          "Config file is wrong. Set default values";
  private static String COMENTARIO_CONFIG = "country = ES|US, language = es|en";
  
  private Properties configuracion; 
  private static final String FICHERO_CONFIG = "config.properties";
  
  public static final String LENGUAJE = "language";
  private String lenguaje;  
  public static final String PAIS = "country";
  public String pais;


  /**
   *  Construye juego Hundir la flota
   * 
   */
  public HundirLaFlota(String args[]) {
    procesarArgsMain(args);      
     
    leerConfiguracion();

    local = Localizacion.devolverInstancia(lenguaje, pais);
    vista = FactoriaPartidasVista.HundirLaFlota(this, VERSION, lenguaje, pais);

  }

  /**
   *  Lee configuración
   * 
   */ 
  private void leerConfiguracion() {
    // valores por defecto de localización;  
    lenguaje = Locale.getDefault().getLanguage();
    pais = Locale.getDefault().getCountry();
    
    try {
      configuracion = new Properties();
      configuracion.load(new FileInputStream(FICHERO_CONFIG));

      lenguaje = configuracion.getProperty(LENGUAJE);
      pais = configuracion.getProperty(PAIS);

      if ((lenguaje == null) || (pais == null)) {
        lenguaje = Locale.getDefault().getLanguage();
        configuracion.setProperty(LENGUAJE, lenguaje);              
        pais = Locale.getDefault().getCountry();
        configuracion.setProperty(PAIS, pais);
      }
    } catch (Exception e) {
      configuracion.setProperty(LENGUAJE, lenguaje);
      configuracion.setProperty(PAIS, pais);

      if (esModoDebug()) {
        DebugVista.devolverInstancia().mostrar(FICHERO_CONFIG_WRONG, e);
      }
    }
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

      case CAMBIAR_LENGUAJE:
        cambiarLenguaje((Tupla)obj);
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
   * Cambia lenguaje
   * 
   */  
  private void cambiarLenguaje(Tupla tupla) {
    configuracion.setProperty(LENGUAJE, (String)tupla.a);
    configuracion.setProperty(PAIS, (String)tupla.b);
    salir();    
  }

  

  /**
   *  Indica si la aplicación está en modo debug
   * 
   */ 
  public static boolean esModoDebug() {
    return modoDebug;
  }

  /**
   *  Empieza nueva partida
   * 
   */ 
  private void nuevaPartida() { 
    guardarPartidaActual();
    ficheroPartida = null;
    vista.ponerTitulo(local.devuelve(Localizacion.TITULO));      

    partida = FactoriaPartidas.nuevaHundirLaFlota(vista);
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
            partida = FactoriaPartidas.abreHundirLaFlota(vista, ficheroPartida);

            vista.habilitarEvento(Evento.GUARDAR, false); 
            vista.habilitarEvento(Evento.DISPARAR, true);
            vista.ponerTitulo(ficheroPartida);    
                     
         } catch(FileNotFoundException e1) {
           mensajeError(Localizacion.FICHERO_PARTIDA_NO_ENCONTRADO, e1);
         } catch(Exception e2) {
           mensajeError(local.devuelve(Localizacion.PARTIDA_NO_LEIDA), e2);
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
        mensajeError(Localizacion.PARTIDA_NO_GUARDADA, e);
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
   if ((partida != null) && ( ! partida.guardada())) {
     if (vista.mensajeConfirmacion(
      local.devuelve(Localizacion.CONFIRMACION_GUARDAR)) == PartidaVista.OPCION_SI) {               
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
    
    // guarda configuración
    try {
      FileOutputStream fichero = new FileOutputStream(FICHERO_CONFIG);
      configuracion.store(fichero, COMENTARIO_CONFIG);
      fichero.close();
    } catch(Exception e) {
      mensajeError(Localizacion.CONFIGURACION_NO_GUARDADA, e);
    }    
    
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
