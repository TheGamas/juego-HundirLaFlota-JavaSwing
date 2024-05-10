package hundirlaflota.control;

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
    return new Partida(vista, 10, 10);    
  }
  
  /**
   *  Devuelve partida de hundir la flota cargada desde fichero
   * 
   */  
  public static Partida abreHundirLaFlota(PartidaVista vista, String fichero) throws Exception {
    return new Partida(vista, fichero);    
  }  
}
