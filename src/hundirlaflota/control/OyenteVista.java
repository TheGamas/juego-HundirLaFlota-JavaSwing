/**
 * OyenteVista.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (02/2024)
 * - Código para jugar, guardar y recuperar partida
 *  
 */

/**
 * Interfaz de oyente para recibir eventos de la interfaz de usuario
 * 
 */

package hundirlaflota.control;

public interface OyenteVista {
   public enum Evento { NUEVA, ABRIR, GUARDAR, GUARDAR_COMO, SALIR, 
                        DISPARAR }
  
   /**
    *  Llamado para notificar un evento de la interfaz de usuario
    * 
    */ 
   public void eventoProducido(Evento evento, Object obj);
}
