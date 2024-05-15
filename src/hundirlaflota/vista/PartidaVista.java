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
import java.util.HashMap;
import java.util.Map;

import javax.swing.filechooser.FileNameExtensionFilter;

import hundirlaflota.control.*;
import hundirlaflota.control.OyenteVista.Evento;
import hundirlaflota.modelo.FactoriaBarcos;
import hundirlaflota.modelo.Posicion;
import hundirlaflota.modelo.Tablero;
import hundirlaflota.modelo.Tupla;

public class PartidaVista extends JFrame implements ActionListener, PropertyChangeListener {
  

  public static final int ABRIR_FICHERO = 0;
  public static final int GUARDAR_FICHERO = 1;
  public static final int OPCION_SI = JOptionPane.YES_OPTION;
 
  /* Ficheros de recursos */
  public static final String RUTA_RECURSOS = "/hundirlaflota/vista/recursos/";
  public static final String EXT_FICHERO_PARTIDA = ".txt";
  public static final String ICONO_BARCO = "Barco.jpg";
  public static final String ICONO_JUGADA_FALLIDA = "cruz_cursor.png";
  
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
  private JButton botonDebug;
  private JMenu menuOpciones;

  private ImageIcon iconoBarco;
  private ImageIcon iconoJugadaFallida;
  private JLabel contadorPortaviones;
  private JLabel contadorCruceros;
  private JLabel contadorDestructores;
  private JLabel contadorFragatas;
  private JLabel mensajeAdyacentes;

  private String lenguaje;
  private String pais;
  private JRadioButtonMenuItem radioBotonEspanol;
  private JRadioButtonMenuItem radioBotonIngles;
  private Map<String, JRadioButtonMenuItem> botonesLenguaje = new HashMap<>();

  private Localizacion local;


  
  /**
   * Construye la vista del tablero de filas x columnas con el oyente para
   * eventos de la interfaz de usuario indicado
   * 
   */
  private PartidaVista(OyenteVista oyenteVista, String version, 
                     int filas, int columnas, String lenguaje, String pais) { 
    super();

    this.oyenteVista = oyenteVista;
    this.version = version;
    this.lenguaje = lenguaje;
    this.pais = pais;

    local = Localizacion.devolverInstancia(lenguaje, pais);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        oyenteVista.eventoProducido(OyenteVista.Evento.SALIR, null);
      }
    });
    crearElementosVentanaPrincipal(filas, columnas);
    
  }

  /**
   * Crea elementos de la ventana principal
   * 
   */
  private void crearElementosVentanaPrincipal(int filas, int columnas){
    setLayout(new BorderLayout()); 
    creaBarraHerramientas();
    crearPanelCentral(filas, columnas);
    

    menuFicheroGuardar = crearMenuItem(Localizacion.MENU_ITEM_GUARDAR,
      Localizacion.ATAJO_MENU_ITEM_GUARDAR_COMO);
    
    menuFicheroGuardarComo = crearMenuItem(Localizacion.MENU_ITEM_GUARDAR_COMO, 
                                   Localizacion.ATAJO_MENU_ITEM_GUARDAR_COMO);
    inicializarIconos();
    ajustarElementos(filas, columnas);
  }

  /**
   * Ajusta elementos de la ventana
   * 
   */
  private void ajustarElementos(int filas, int columnas){
    setResizable(false);
    setSize((int)(tableroVista.dimensionCasilla().getWidth() * 
                  columnas + MARGEN_HORIZONTAL), 
            (int)(tableroVista.dimensionCasilla().getHeight() * 
                  filas + MARGEN_VERTICAL));
        
    pack();  // Ajusta ventana y sus componentes
    setLocationRelativeTo(null);  // Centra en la pantalla    
    setVisible(true);
  }

  /**
   * Crea panel central
   * 
   */
  private void crearPanelCentral(int filTablero, int colTablero){
    JPanel panelCentral = new JPanel();

    panelCentral.setLayout(new GridLayout(1,2));

    JPanel panelIzquierdo = new JPanel();

    creaTablero(panelIzquierdo, filTablero, colTablero);
    panelCentral.add(panelIzquierdo, BorderLayout.CENTER);

    JPanel panelDerecho = new JPanel();
    panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
    JPanel panelEstadisticas = new JPanel();
    creaEstadisticas(panelEstadisticas);
    mensajeAdyacentes = new JLabel();

    panelDerecho.add(panelEstadisticas);
    panelDerecho.add(Box.createVerticalGlue());
    panelDerecho.add(mensajeAdyacentes);

    panelCentral.add(panelDerecho);
    add(panelCentral);
  }

  /**
   * Inicializa iconos
   * 
   */
  private void inicializarIconos(){
    iconoBarco = new ImageIcon(getClass().getResource(RUTA_RECURSOS + ICONO_BARCO));
    iconoJugadaFallida = new ImageIcon(
      getClass().getResource(RUTA_RECURSOS + ICONO_JUGADA_FALLIDA));
  }
  

  /**
   * Crea un menú con el nombre y atajo indicados
   * 
   */
  private JMenuItem crearMenuItem( String nombreMenu, String atajoMenu){
    JMenuItem menuBarra = new JMenuItem(nombreMenu, atajoMenu.charAt(0));
    menuBarra.setEnabled(false);
    menuBarra.addActionListener(this);
    menuBarra.setActionCommand(nombreMenu);
    return menuBarra;
  }


  /**
   * Modifica mensaje de adyacentes
   * 
   */
  private void modificarAdyacentes(String string) {
    mensajeAdyacentes.setText(string);
  }

  /**
   * Crea barra de herramientas
   * 
   */ 
  private void creaBarraHerramientas() {

    JMenuBar panelNorte = new JMenuBar();
    panelNorte.setLayout(new FlowLayout(FlowLayout.LEFT));
    
    botonNueva = crearBoton(local.devuelve(Localizacion.BOTON_NUEVA), 
                            Localizacion.MENU_ITEM_NUEVA);
    panelNorte.add(botonNueva);
    botonAbrir = crearBoton(local.devuelve(Localizacion.BOTON_ABRIR), 
                            Localizacion.MENU_ITEM_ABRIR);
    panelNorte.add(botonAbrir);
    botonGuardar = crearBoton(local.devuelve(Localizacion.BOTON_GUARDAR), 
                              Localizacion.MENU_ITEM_GUARDAR, false);
    panelNorte.add(botonGuardar);
    botonAcercaDe = crearBoton(local.devuelve(Localizacion.BOTON_ACERCA_DE), 
                                Localizacion.MENU_ITEM_ACERCA_DE);
    panelNorte.add(botonAcercaDe);
    menuOpciones = crearOpciones(panelNorte);

    if (HundirLaFlota.esModoDebug()) {
      botonDebug = crearBoton(local.devuelve(Localizacion.BOTON_DEBUG), 
                              Localizacion.MENU_ITEM_DEBUG);
      panelNorte.add(botonDebug);      
    }

    botonSalir = crearBoton(local.devuelve(Localizacion.BOTON_SALIR), 
                            Localizacion.MENU_ITEM_SALIR);
    panelNorte.add(botonSalir);

    add(panelNorte, BorderLayout.NORTH);
  }

  /**
   * Crea un boton habilitado con la etiqueta y el menú item indicados
   * 
   */
  private JButton crearBoton(String etiqueta, String menu_item){
    return crearBoton(etiqueta, menu_item, true);

  }

  /**
   * Crea un boton con la etiqueta y el menú item indicados
   * 
   */
  private JButton crearBoton(String etiqueta, String menu_item, boolean eneabled){
    JButton boton = new JButton(etiqueta);
    boton.setToolTipText(local.devuelve(menu_item));
    boton.addActionListener(this);
    boton.setEnabled(eneabled);
    boton.setActionCommand(menu_item);
    return boton;
  }

  /**
   * Crea menú de opciones
   * 
   */
  private JMenu crearOpciones(JMenuBar panelNorte){
    JMenu menuOpciones = crearMenu(Localizacion.MENU_ITEM_OPCIONES);
    panelNorte.add(menuOpciones);    
    
    JMenu menuLenguaje = crearMenu(Localizacion.MENU_ITEM_LENGUAJE);
    menuLenguaje.setActionCommand(Localizacion.MENU_ITEM_LENGUAJE);
    menuOpciones.add(menuLenguaje);    

    ButtonGroup grupoBotonesLenguaje = new ButtonGroup();

    radioBotonEspanol = crearBotonMenuItem(Localizacion.MENU_ITEM_ESPANOL);
    grupoBotonesLenguaje.add(radioBotonEspanol);
    menuLenguaje.add(radioBotonEspanol);
    botonesLenguaje.put(Localizacion.LENGUAJE_ESPANOL, radioBotonEspanol);

    radioBotonIngles = crearBotonMenuItem(Localizacion.MENU_ITEM_INGLES);
    grupoBotonesLenguaje.add(radioBotonIngles);
    menuLenguaje.add(radioBotonIngles);
    botonesLenguaje.put(Localizacion.LENGUAJE_INGLES, radioBotonIngles);

    // seleccionamos botón según lenguaje configurado
    botonesLenguaje.get(lenguaje).setSelected(true);
    return menuOpciones;
  }

  /**
   * Crea menú
   * 
   */
  private JMenu crearMenu(String etiqueta){
    JMenu menu = new JMenu(local.devuelve(etiqueta));
    menu.addActionListener(this);
    return menu;
  }

  /**
   * Crea boton menú ítem
   * 
   */
  private JRadioButtonMenuItem crearBotonMenuItem(String etiqueta) {
    JRadioButtonMenuItem radioBotonItem = new JRadioButtonMenuItem(
            local.devuelve(etiqueta));
 
    radioBotonItem.addActionListener(this);    
    radioBotonItem.setActionCommand(etiqueta);    
    
    return radioBotonItem;
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
  private void creaEstadisticas(JPanel panel) {
    JPanel panelEstadisticas = new JPanel(new GridLayout(4, 2));
    
    contadorPortaviones = new JLabel();
    crearLineaEstadisticas(Localizacion.ETIQUETA_PORTAVIONES, contadorPortaviones,
      panelEstadisticas);

    contadorCruceros = new JLabel();
    crearLineaEstadisticas(Localizacion.ETIQUETA_CRUCERO, contadorCruceros, 
      panelEstadisticas);

    contadorDestructores = new JLabel();
    crearLineaEstadisticas(Localizacion.ETIQUETA_DESTRUCTOR, contadorDestructores,
      panelEstadisticas);

    contadorFragatas = new JLabel();
    crearLineaEstadisticas(Localizacion.ETIQUETA_FRAGATA, contadorFragatas, 
      panelEstadisticas);

    panel.add(panelEstadisticas);
  }

  /**
   * Crea una línea de estadísticas
   * 
   */
  private void crearLineaEstadisticas(String etiqueta, JLabel contador, JPanel panel){
    JLabel nombre = new JLabel(local.devuelve(etiqueta) + ": ");
    panel.add(nombre);
    panel.add(contador);

  }

  /**
   * Devuelve la instancia de la vista del tablero
   * 
   */        
  public static synchronized PartidaVista devolverInstancia(
          OyenteVista oyenteIU, String version, int filas, int columnas, String lenguaje, 
          String pais) {
    if (instancia == null){
      instancia = new PartidaVista(oyenteIU, version, filas, columnas, lenguaje, pais);
    }    
    return instancia;
  }

  /**
   * Sobreescribe actionPerformed
   * 
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    switch(e.getActionCommand()) {
      case Localizacion.MENU_ITEM_NUEVA:
        oyenteVista.eventoProducido(OyenteVista.Evento.NUEVA, null);
        tableroVista.habilitar(true);
        inicializarVista(); 
        break;

      case Localizacion.MENU_ITEM_ABRIR:           
        oyenteVista.eventoProducido(OyenteVista.Evento.ABRIR, null);
        tableroVista.habilitar(true);
        break;
          
      case Localizacion.MENU_ITEM_GUARDAR:
        oyenteVista.eventoProducido(OyenteVista.Evento.GUARDAR, null);
        break;           
            
      case Localizacion.MENU_ITEM_GUARDAR_COMO:
        oyenteVista.eventoProducido(OyenteVista.Evento.GUARDAR_COMO, null);
        break; 
      case Localizacion.MENU_ITEM_ESPANOL: 
        cambiarLenguaje(Localizacion.LENGUAJE_ESPANOL, Localizacion.PAIS_ESPANA);           
        break;           

      case Localizacion.MENU_ITEM_INGLES:
        cambiarLenguaje(Localizacion.LENGUAJE_INGLES, Localizacion.PAIS_USA);        
        break;            
          
      case Localizacion.MENU_ITEM_SALIR:
        oyenteVista.eventoProducido(OyenteVista.Evento.SALIR, null);
        break;
        
      case Localizacion.MENU_ITEM_DEBUG:
          DebugVista.devolverInstancia().mostrar();
          break;
      
      case Localizacion.MENU_ITEM_ACERCA_DE:
        JOptionPane.showMessageDialog(this,
          local.devuelve(Localizacion.TITULO) + " " + version + "\n", 
          local.devuelve(Localizacion.MENU_ITEM_ACERCA_DE), 
          JOptionPane.INFORMATION_MESSAGE,  icono);   
        break;
    }
  }

  /**
   * Cambia al lenguaje indicado y sale del programa
   * 
   */   
  private void cambiarLenguaje(String nuevoLenguaje, String nuevoPais) {
    // si opción es distinta de la actual preguntamos cambiar  
    if ( ! lenguaje.equals(nuevoLenguaje)) { 
      // si cambiamos modificamos configuración y salimos  
      if (mensajeConfirmacion(local.devuelve(
        Localizacion.CONFIRMACION_LENGUAJE)) == OPCION_SI) {               
        oyenteVista.eventoProducido(OyenteVista.Evento.CAMBIAR_LENGUAJE, 
                                    new Tupla(nuevoLenguaje, nuevoPais)); 
      } 
      // si no cambiamos volvemos a seleccionar botón del lenguaje actual
      else {
        botonesLenguaje.get(lenguaje).setSelected(true);
      }
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
         new FileNameExtensionFilter(local.devuelve(Localizacion.FILTRO_PARTIDAS), 
                                     EXT_FICHERO_PARTIDA.substring(1));
    
    dialogoSeleccionar.setFileFilter(filtro);
    
    if (operacion == ABRIR_FICHERO) {
      resultado = dialogoSeleccionar.showOpenDialog(this);
    } 
    else {
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
    StringBuilder titulo = new StringBuilder(version);  
    
    if (! nombreFichero.equals("")) {
      titulo.insert(0, nombreFichero + " - ");
    }     
    
    if (HundirLaFlota.esModoDebug()) {
      titulo.insert(0, DebugVista.TITULO);
    }

    this.setTitle(titulo.toString());
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
    recuperarEstadisticas(tablero);
    modificarAdyacentes("");
  }

  /**
   * Recupera estadisticas de barcos
   * 
   */
  private void recuperarEstadisticas(Tablero tablero){
    contadorPortaviones.setText(String.valueOf(
      tablero.cuantosBarcosQuedan(FactoriaBarcos.PORTAVIONES)));

    contadorCruceros.setText(String.valueOf(
      tablero.cuantosBarcosQuedan(FactoriaBarcos.CRUCERO)));

    contadorDestructores.setText(String.valueOf(
      tablero.cuantosBarcosQuedan(FactoriaBarcos.DESTRUCTOR)));
      
    contadorFragatas.setText(String.valueOf(
      tablero.cuantosBarcosQuedan(FactoriaBarcos.FRAGATA)));
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
    if(evt.getPropertyName().equals(Tablero.MENSAJE_HUNDIDO)){
      incrementarContadores((String) evt.getNewValue(), -1);
    }
    else if( evt.getPropertyName().equals(Tablero.MENSAJE_TOCADO) ){
      ponerIconoCasilla((Posicion) evt.getNewValue(), Posicion.EstadoPosicion.TOCADA_BARCO);
      modificarAdyacentes("");

    }
    else if( evt.getPropertyName().equals(Tablero.MENSAJE_AGUA) ){
      ponerIconoCasilla((Posicion) evt.getNewValue(), Posicion.EstadoPosicion.TOCADA_AGUA);
      modificarAdyacentes("");

    }
    else if(evt.getPropertyName().equals(Tablero.MENSAJE_ADYACENTES)){
      modificarAdyacentes(local.devuelve((Localizacion.MENSAJE_ADYACENTES)));
    }
  }

  /**
   * Decrementa los contadores de barcos
   * 
   */
  private void incrementarContadores(String nombreContador, int incremento) {
    switch(nombreContador) {
      case FactoriaBarcos.PORTAVIONES:
        
        contadorPortaviones.setText(
          String.valueOf(Integer.parseInt(contadorPortaviones.getText()) + incremento));  
        break;
      case FactoriaBarcos.CRUCERO:
        contadorCruceros.setText(
          String.valueOf(Integer.parseInt(contadorCruceros.getText()) + incremento));
        break;
      case FactoriaBarcos.DESTRUCTOR:
        contadorDestructores.setText(
          String.valueOf(Integer.parseInt(contadorDestructores.getText()) + incremento));
        break;
      case FactoriaBarcos.FRAGATA:
        contadorFragatas.setText(
          String.valueOf(Integer.parseInt(contadorFragatas.getText()) + incremento));
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


