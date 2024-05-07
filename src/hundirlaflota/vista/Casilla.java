package hundirlaflota.vista;

import hundirlaflota.control.*;
import hundirlaflota.modelo.Posicion;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * Vista de una casilla del tablero en un JLabel
 * 
 */
public class Casilla extends JLabel{
    private Posicion posicion;
  private PartidaVista partidaVista;

  /**
   * Construye una vista con el icono indicado en la casilla
   * 
   */
  Casilla(PartidaVista partidaVista, Posicion posicion, Icon icono,
          boolean recibeEventosRaton) {
    this.posicion = posicion;
    this.partidaVista = partidaVista;
    setIcon(icono);
        
    setEnabled(false);
    
    setHorizontalAlignment(SwingConstants.CENTER);
    //setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    
    if (recibeEventosRaton) {
      recibirEventosRaton();
    }
  }

  /**
   * Construye una vista sin icono en la casilla 
   * 
   */
  Casilla(PartidaVista vista, Posicion posicion, 
               boolean recibeEventosRaton) {
    this(vista, posicion, null, recibeEventosRaton);
  }

  /**
   * Recibe los eventos de ratón
   * 
   */ 
  public void recibirEventosRaton(){
    addMouseListener(new MouseAdapter() { 
        @Override
        public void mousePressed(MouseEvent e) { 
          if (isEnabled()) {          
            partidaVista.notificacion(OyenteVista.Evento.DISPARAR, posicion); 
          }        
        } 
      });
  }

  /**
   * Habilita la casilla
   * 
   */
  void habilitar(boolean habilitacion) {
    setEnabled(habilitacion);
  }

  /**
   * Devuelve la posición de la casilla
   * 
   */
  Posicion devuelvePosicion() {
    return posicion;
  }
    
  /**
   * Pone icono
   * 
   */    
  void ponerIcono(Icon icono) {
    setIcon(icono);
  }
  
  
  /**
   * Sobreescribe toString
   * 
   */  
  @Override
  public String toString() {
    return posicion.toString();
  }
    
}
