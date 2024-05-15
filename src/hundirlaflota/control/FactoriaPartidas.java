
/**
 * FactoriaPartidas.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (05/2024)
 * - Código para jugar, guardar y recuperar partida
 *  
 */

/**
 * Clase FactoriaPartidas
 * 
 */
package hundirlaflota.control;

import hundirlaflota.modelo.FactoriaBarcos;
import hundirlaflota.vista.PartidaVista;

/**
 *  Factoría de partidas
 * 
 */
public class FactoriaPartidas {
  /**
   *  Devuelve nueva partida de hundir la flota
   * 
   */    
  public static Partida nuevaHundirLaFlota(PartidaVista vista) {
    Partida partida = new Partida(
      vista, HundirLaFlota.COLUMNAS, HundirLaFlota.FILAS);

    partida.colocarBarco(FactoriaBarcos.PORTAVIONES);
    partida.colocarBarco(FactoriaBarcos.CRUCERO);
    partida.colocarBarco(FactoriaBarcos.DESTRUCTOR);
    partida.colocarBarco(FactoriaBarcos.DESTRUCTOR);
    partida.colocarBarco(FactoriaBarcos.FRAGATA);
    partida.colocarBarco(FactoriaBarcos.FRAGATA);
    partida.colocarBarco(FactoriaBarcos.FRAGATA);
    
    return partida;
        
  }
  
  /**
   *  Devuelve partida de hundir la flota cargada desde fichero
   * 
   */  
  public static Partida abreHundirLaFlota(PartidaVista vista, String fichero) 
  throws Exception {
    return new Partida(vista, fichero);    
  }  
}
