/**
 * PartidaVista.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (05/2024)
 * - Código jugar, cargar y guardar partida
 *  
 */

/**
 * Clase de la vista para la partida
 * 
 */

package hundirlaflota.vista;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

import hundirlaflota.control.*;
import hundirlaflota.control.OyenteVista.Evento;
import hundirlaflota.modelo.FactoriaBarcos;
import hundirlaflota.modelo.Posicion;
import hundirlaflota.modelo.Tablero;

public class PartidaVista extends JFrame implements ActionListener, PropertyChangeListener {
  

  public static final int ABRIR_FICHERO = 0;
  public static final int GUARDAR_FICHERO = 1;
  public static final int OPCION_SI = JOptionPane.YES_OPTION;
  
  /** Identificadores de textos dependientes del idioma */          
  private static final String MENU_ITEM_NUEVA = "Nueva";
  private static final String MENU_ITEM_ABRIR = "Abrir";
  private static final String MENU_ITEM_GUARDAR = "Guardar";
  private static final char ATAJO_MENU_ITEM_GUARDAR = 'G';
  private static final String MENU_ITEM_GUARDAR_COMO = 
          "Guardar partida como...";
  private static final char ATAJO_MENU_ITEM_GUARDAR_COMO = 'C';
  private static final String MENU_ITEM_SALIR = "Salir";
  private static final String MENU_ITEM_ACERCA_DE = "Acerca de...";
  
  
  public static final String EXT_FICHERO_PARTIDA = ".txt";
  public static final String FILTRO_PARTIDAS = "Partidas";
  public static final String CONFIRMACION_GUARDAR = 
          "¿Quieres guardar la partida actual?";
  public static final String PARTIDA_NO_GUARDADA = "No pudo guardarse partida";
  public static final String PARTIDA_NO_LEIDA = "No pudo leerse partida";
  public static final String FICHERO_NO_ENCONTRADO = "Fichero no encontrado";
  public static final String MENSAJE_ADYACENTES = "Hay barcos adyacentes";
  
 
  /* Ficheros de recursos */
  private static final String RUTA_RECURSOS = "/hundirlaflota/vista/recursos/";
  
  /* Constantes para redimensionamiento */
  private static final int MARGEN_HORIZONTAL = 50;
  private static final int MARGEN_VERTICAL = 100;

  
  
  private static PartidaVista instancia = null;
  private OyenteVista oyenteVista;
  private String version;
  private TableroVista tableroVista;
  private ImageIcon icono;
  private JMenuItem menuFicheroGuardar;
  private JMenuItem menuFicheroGuardarComo;
  private JButton botonNueva;
  private JButton botonAbrir;
  private JButton botonGuardar;
  private JButton botonAcercaDe;
  private JButton botonSalir;
  private ImageIcon iconoBarco;
  private ImageIcon iconoJugadaFallida;
  private JLabel contadorPortaviones;
  private JLabel contadorCruceros;
  private JLabel contadorDestructores;
  private JLabel contadorFragatas;
  private JLabel mensajeAdyacentes;

  
  /**
   * Construye la vista del tablero de filas x columnas con el oyente para
   * eventos de la interfaz de usuario indicado
   * 
   */
  public PartidaVista(OyenteVista oyenteVista, String version, 
                     int filas, int columnas) { 
    super(version);

    this.oyenteVista = oyenteVista;
    this.version = version;
    
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        oyenteVista.eventoProducido(OyenteVista.Evento.SALIR, null);
      }
    });
    
    setLayout(new BorderLayout());
    
    JPanel panelNorte = new JPanel();
    panelNorte.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    // Creamos elementos
    creaBarraHerramientas(panelNorte);
    add(panelNorte, BorderLayout.NORTH);
    
    JPanel panelCentral = new JPanel();
    panelCentral.setLayout(new GridLayout(1,2));

    JPanel panelIzquierdo = new JPanel();
    //panelCentral.setLayout(new FlowLayout());
    creaTablero(panelIzquierdo, filas, columnas);
    add(panelIzquierdo, BorderLayout.CENTER);
    

    JPanel panelDerecho = new JPanel();
    panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.PAGE_AXIS));

    JPanel panelEstadisticas = new JPanel();
    creaEstadisticas(panelEstadisticas);
    panelDerecho.add(panelEstadisticas);
    mensajeAdyacentes = new JLabel();
    panelDerecho.add(mensajeAdyacentes);
    panelDerecho.add(Box.createVerticalGlue());
    add(panelDerecho, BorderLayout.EAST);
    



    panelCentral.add(panelIzquierdo);
    panelCentral.add(panelDerecho);
    add(panelCentral, BorderLayout.CENTER);

    
    menuFicheroGuardar = 
      new JMenuItem(MENU_ITEM_GUARDAR, ATAJO_MENU_ITEM_GUARDAR);
    menuFicheroGuardar.setEnabled(false);
    menuFicheroGuardar.addActionListener(this);
    menuFicheroGuardar.setActionCommand(MENU_ITEM_GUARDAR);
    
    menuFicheroGuardarComo = 
      new JMenuItem(MENU_ITEM_GUARDAR_COMO, ATAJO_MENU_ITEM_GUARDAR_COMO);
    menuFicheroGuardarComo.setEnabled(false);
    menuFicheroGuardarComo.addActionListener(this);
    menuFicheroGuardarComo.setActionCommand(MENU_ITEM_GUARDAR_COMO);

    iconoBarco = new ImageIcon(getClass().getResource(RUTA_RECURSOS + "Barco.jpg"));
    iconoJugadaFallida = new ImageIcon(getClass().getResource(RUTA_RECURSOS + "cruz_cursor.png"));
    
    
    // Hacemos visible con el tamaño y la posición deseados     
    setResizable(false);
    setSize((int)(tableroVista.dimensionCasilla().getWidth() * 
                  columnas + MARGEN_HORIZONTAL), 
            (int)(tableroVista.dimensionCasilla().getHeight() * 
                  filas + MARGEN_VERTICAL));
        
    pack();  // Ajusta ventana y sus componentes
    setLocationRelativeTo(null);  // Centra en la pantalla    
    setVisible(true);
  }

  private void modificarAdyacentes(String string) {
    mensajeAdyacentes.setText(string);
  }

  /**
   * Crea barra de herramientas
   * 
   */ 
  private void creaBarraHerramientas(JPanel panelNorte) {

    botonNueva = new JButton("Nueva");
    botonNueva.setToolTipText(MENU_ITEM_NUEVA);
    botonNueva.addActionListener(this);
    botonNueva.setActionCommand(MENU_ITEM_NUEVA);
    panelNorte.add(botonNueva);

    botonAbrir = new JButton("Abrir");  
    botonAbrir.setToolTipText(MENU_ITEM_ABRIR);
    botonAbrir.addActionListener(this);
    botonAbrir.setActionCommand(MENU_ITEM_ABRIR);
    panelNorte.add(botonAbrir); 
    
    botonGuardar = new JButton("Guardar");  
    botonGuardar.setToolTipText(MENU_ITEM_GUARDAR);
    botonGuardar.addActionListener(this);
    botonGuardar.setEnabled(false);
    botonGuardar.setActionCommand(MENU_ITEM_GUARDAR);
    panelNorte.add(botonGuardar);

    botonAcercaDe = new JButton("Acerca de...");
    botonAcercaDe.setToolTipText(MENU_ITEM_ACERCA_DE);
    botonAcercaDe.addActionListener(this);
    botonAcercaDe.setActionCommand(MENU_ITEM_ACERCA_DE);
    panelNorte.add(botonAcercaDe);

    botonSalir = new JButton("Salir");
    botonSalir.setToolTipText(MENU_ITEM_SALIR);
    botonSalir.addActionListener(this);
    botonSalir.setActionCommand(MENU_ITEM_SALIR);
    panelNorte.add(botonSalir);
  }  
  
  /**
   * Crea la vista del tablero
   * 
   */   
  private void creaTablero(JPanel panel, int numFilTab, int numColTab) {
    tableroVista = new TableroVista(this, numFilTab, numColTab, 
                                    TableroVista.RECIBIR_EVENTOS_RATON);
    panel.add(tableroVista);
  }

  /**
   * Crea la vista de las estadisticas
   * 
   */
  public void creaEstadisticas(JPanel panel) {
    JPanel panelEstadisticas = new JPanel(new GridLayout(4, 2));

    JLabel etiquetaPortaviones = new JLabel("Portaviones:");
    contadorPortaviones = new JLabel();
    panelEstadisticas.add(etiquetaPortaviones);
    panelEstadisticas.add(contadorPortaviones);

    JLabel etiquetaCruceros = new JLabel("Crucero:");
    contadorCruceros = new JLabel();
    panelEstadisticas.add(etiquetaCruceros);
    panelEstadisticas.add(contadorCruceros);

    JLabel etiquetaDestructor = new JLabel("Destructor:");
    contadorDestructores = new JLabel();
    panelEstadisticas.add(etiquetaDestructor);
    panelEstadisticas.add(contadorDestructores);

    JLabel etiquetaFragata = new JLabel("Fragata:");
    contadorFragatas = new JLabel();
    panelEstadisticas.add(etiquetaFragata);
    panelEstadisticas.add(contadorFragatas);

    panel.add(panelEstadisticas);
  }

  /**
   * Devuelve la instancia de la vista del tablero
   * 
   */        
  public static synchronized PartidaVista devolverInstancia(
          OyenteVista oyenteIU, String version, int filas, int columnas) {
    if (instancia == null)
      instancia = new PartidaVista(oyenteIU, version, filas, columnas);    
    return instancia;
  }

  /**
   * Sobreescribe actionPerformed
   * 
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    switch(e.getActionCommand()) {
        case MENU_ITEM_NUEVA:
           oyenteVista.eventoProducido(OyenteVista.Evento.NUEVA, null);
           tableroVista.habilitar(true);
           inicializarVista(); 
           break;

        case MENU_ITEM_ABRIR:           
           oyenteVista.eventoProducido(OyenteVista.Evento.ABRIR, null);
           tableroVista.habilitar(true);
           break;
           
        case MENU_ITEM_GUARDAR:
           oyenteVista.eventoProducido(OyenteVista.Evento.GUARDAR, null);
           break;           
             
        case MENU_ITEM_GUARDAR_COMO:
           oyenteVista.eventoProducido(OyenteVista.Evento.GUARDAR_COMO, null);
           break;             
           
        case MENU_ITEM_SALIR:
           oyenteVista.eventoProducido(OyenteVista.Evento.SALIR, null);
           break;
        
        case MENU_ITEM_ACERCA_DE:
          JOptionPane.showMessageDialog(this, version + "\n", 
             MENU_ITEM_ACERCA_DE, JOptionPane.INFORMATION_MESSAGE,  icono);   
          break;
    }
  }

  /**
   * Selecciona fichero de partida
   * 
   */  
  public String seleccionarFichero(int operacion) {
    String nombreFichero = null;
    int resultado = 0;
    
    JFileChooser dialogoSeleccionar = new JFileChooser(new File("."));
    FileNameExtensionFilter filtro = 
         new FileNameExtensionFilter(FILTRO_PARTIDAS, 
                                     EXT_FICHERO_PARTIDA.substring(1));
    
    dialogoSeleccionar.setFileFilter(filtro);
    
    if (operacion == ABRIR_FICHERO) {
      resultado = dialogoSeleccionar.showOpenDialog(this);
    } else {
      resultado = dialogoSeleccionar.showSaveDialog(this);
    }
    
    if(resultado == JFileChooser.APPROVE_OPTION) {
      nombreFichero = dialogoSeleccionar.getSelectedFile().getName();
      
      // pone extensión si hace falta al guardar
      if (! nombreFichero.endsWith(EXT_FICHERO_PARTIDA) && 
          (operacion == GUARDAR_FICHERO)) {
        nombreFichero = nombreFichero + EXT_FICHERO_PARTIDA;
      }
    
      ponerTitulo(nombreFichero);
    }
    return nombreFichero;
  }

  /**
   * Pone el titulo al fichero
   * 
   */
  public void ponerTitulo(String nombreFichero) {
    if (nombreFichero.equals("")) {
      setTitle(version); 
    } else {
      setTitle(nombreFichero + " - " + version);          
    }
  }

  /**
   * Escribe mensaje con diálogo modal
   * 
   */    
  public void mensajeDialogo(String mensaje) {
    JOptionPane.showMessageDialog(this, mensaje, version, 
        JOptionPane.INFORMATION_MESSAGE,  icono);    
  }

  /**
   * Cuadro diálogo de confirmación acción
   * 
   */    
  public int mensajeConfirmacion(String mensaje) {
    return JOptionPane.showConfirmDialog(this, mensaje, version,
               JOptionPane.YES_NO_OPTION, 
               JOptionPane.INFORMATION_MESSAGE); 
  }

  /**
   * Pone Icono del color indicado en la posición indicada
   * 
   */  
  public void ponerIconoCasilla(Posicion posicion, Posicion.EstadoPosicion estado) {


    if (estado != Posicion.EstadoPosicion.NO_TOCADA) {
      if (estado == Posicion.EstadoPosicion.TOCADA_BARCO) {
        tableroVista.ponerIconoCasilla(posicion, iconoBarco); 

      } 
      else if (estado == Posicion.EstadoPosicion.TOCADA_AGUA) {
        tableroVista.ponerIconoCasilla(posicion, iconoJugadaFallida);
      }
      habilitarEvento(Evento.GUARDAR, true);
      habilitarEvento(Evento.GUARDAR_COMO, true);
    } else {
      tableroVista.ponerIconoCasilla(posicion, null); 
    } 
  }
  
  /**
   * Inicializa vista
   * 
   */     
  public void inicializarVista() {
    tableroVista.inicializar();

    contadorPortaviones.setText(String.valueOf(Partida.NUM_PORTAVIONES));
    contadorCruceros.setText(String.valueOf(Partida.NUM_CRUCEROS));
    contadorDestructores.setText(String.valueOf(Partida.NUM_DESTRUCTORES));
    contadorFragatas.setText(String.valueOf(Partida.NUM_FRAGATAS));
  }

  /**
   * Recupera partida con el tablero indicado
   * 
   */   
  public void recuperarPartida(Tablero tablero) {
    for (int fila = 0; fila < HundirLaFlota.FILAS; fila++) {
      for (int columna = 0; columna < HundirLaFlota.COLUMNAS; columna++) {
        Posicion posicion = new Posicion(fila, columna);
        ponerIconoCasilla(posicion, tablero.estadoPosicion(posicion));
      }
    }
    contadorPortaviones.setText(String.valueOf(tablero.cuantosBarcosQuedan(FactoriaBarcos.PORTAVIONES)));
    contadorCruceros.setText(String.valueOf(tablero.cuantosBarcosQuedan(FactoriaBarcos.CRUCERO)));
    contadorDestructores.setText(String.valueOf(tablero.cuantosBarcosQuedan(FactoriaBarcos.DESTRUCTOR)));
    contadorFragatas.setText(String.valueOf(tablero.cuantosBarcosQuedan(FactoriaBarcos.FRAGATA)));
    modificarAdyacentes("");
  }
  
  /**
   * Notificación de un evento de la interfaz de usuario
   * 
   */
  public void notificacion(OyenteVista.Evento evento, Object obj) {
    oyenteVista.eventoProducido(evento, obj);    
  }

  /**
   * Recoge cambios en modelo
   * 
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals("HUNDIDO")){
      incrementarContadores((String) evt.getNewValue(), -1);
      System.out.println("Barco hundido: " + evt.getNewValue());
    }
    else if( evt.getPropertyName().equals("TOCADO") ){
      Posicion posicion = (Posicion) evt.getNewValue();
      ponerIconoCasilla((Posicion) evt.getNewValue(), Posicion.EstadoPosicion.TOCADA_BARCO);
      modificarAdyacentes("");

    }
    else if( evt.getPropertyName().equals("AGUA") ){
      ponerIconoCasilla((Posicion) evt.getNewValue(), Posicion.EstadoPosicion.TOCADA_AGUA);
      modificarAdyacentes("");

    }
    else if(evt.getPropertyName().equals("ADYACENTES")){
      modificarAdyacentes(MENSAJE_ADYACENTES);
    }
  }

  /**
   * Decrementa los contadores de barcos
   * 
   */
  private void incrementarContadores(String nombreContador, int incremento) {
    switch(nombreContador) {
      case FactoriaBarcos.PORTAVIONES:
        if (contadorPortaviones.getText() != "0"){
          contadorPortaviones.setText(String.valueOf(Integer.parseInt(contadorPortaviones.getText()) + incremento));  
        }
        break;
      case FactoriaBarcos.CRUCERO:
        contadorCruceros.setText(String.valueOf(Integer.parseInt(contadorCruceros.getText()) + incremento));
        break;
      case FactoriaBarcos.DESTRUCTOR:
        contadorDestructores.setText(String.valueOf(Integer.parseInt(contadorDestructores.getText()) + incremento));
        break;
      case FactoriaBarcos.FRAGATA:
        contadorFragatas.setText(String.valueOf(Integer.parseInt(contadorFragatas.getText()) + incremento));
        break;
    }
  }

  /**
   * Habilita o deshabilita un evento
   * 
   */
  public void habilitarEvento(OyenteVista.Evento evento, boolean habilitacion) { 
    switch(evento) {
        case GUARDAR:
            menuFicheroGuardar.setEnabled(habilitacion);
            botonGuardar.setEnabled(habilitacion);              
            break;

        case GUARDAR_COMO:
            menuFicheroGuardarComo.setEnabled(habilitacion);            
            break;   
            
        case DISPARAR:
            tableroVista.habilitar(habilitacion);
            break;
    }
  }
}


