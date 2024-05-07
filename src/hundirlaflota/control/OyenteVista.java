
package hundirlaflota.control;
/**
 *  Interfaz de oyente para recibir eventos de la interfaz de usuario
 * 
 */
public interface OyenteVista {
   public enum Evento { NUEVA, ABRIR, GUARDAR, GUARDAR_COMO, SALIR, 
                        DISPARAR }
  
   /**
    *  Llamado para notificar un evento de la interfaz de usuario
    * 
    */ 
   public void eventoProducido(Evento evento, Object obj);
}
