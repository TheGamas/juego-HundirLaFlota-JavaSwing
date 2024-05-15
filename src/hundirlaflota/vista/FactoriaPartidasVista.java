/**
 * FactoriaPartidasVista.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (05/2024)
 * - Código jugar, cargar y guardar partida
 *  
 */
package hundirlaflota.vista;

import hundirlaflota.control.HundirLaFlota;
import hundirlaflota.control.OyenteVista;

/**
 *  Factoría de partidas
 * 
 */
public class FactoriaPartidasVista {
  /**
   *  Devuelve vista de hundir la flota
   * 
   */  
  public static PartidaVista HundirLaFlota(OyenteVista oyenteVista, String version,
                                      String lenguaje, String pais) { 
    return PartidaVista.devolverInstancia(
      oyenteVista, version, HundirLaFlota.FILAS, HundirLaFlota.COLUMNAS, 
      lenguaje, pais);    
  } 
}
