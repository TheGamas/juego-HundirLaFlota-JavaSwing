/**
 * FactoriaPartidasVista.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (05/2024)
 * - Código jugar, cargar y guardar partida
 *  
 */


package hundirlaflota.vista;

import hundirlaflota.control.HundirLaFlota;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.UIManager;

/**
 * Localizacion recursos de la vista
 * 
 */
public class Localizacion {
  private static Localizacion instancia = null; // es singleton
  private ResourceBundle recursos;    
  
  private static final String FICHERO_LOCALIZACION = "localizacion"; 
  
  public static String LENGUAJE_ESPANOL = "es";
  public static String PAIS_ESPANA = "ES";
  public static String LENGUAJE_INGLES = "en";
  public static String PAIS_USA = "US";    
  
  /** Identificadores de textos dependientes del idioma */  
  /* PartidaVista */
  public static final String TITULO = "TITULO";
  public static final String BOTON_NUEVA = "BOTON_NUEVA";
  public static final String BOTON_ABRIR = "BOTON_ABRIR";
  public static final String BOTON_GUARDAR = "BOTON_GUARDAR";
  public static final String BOTON_SALIR = "BOTON_SALIR";
  public static final String BOTON_OPCIONES = "BOTON_OPCIONES";
  public static final String BOTON_ACERCA_DE = "BOTON_ACERCA_DE";
  public static final String BOTON_DEBUG = "BOTON_DEBUG";
  
  public static final String MENU_ITEM_NUEVA = "MENU_ITEM_NUEVA";
  public static final String MENU_ITEM_ABRIR = "MENU_ITEM_ABRIR";
  public static final String ATAJO_MENU_ITEM_ABRIR = "ATAJO_MENU_ITEM_ABRIR";
  public static final String MENU_ITEM_GUARDAR = "MENU_ITEM_GUARDAR";
  public static final String ATAJO_MENU_ITEM_GUARDAR = "ATAJO_MENU_ITEM_GUARDAR";
  public static final String MENU_ITEM_GUARDAR_COMO = "MENU_ITEM_GUARDAR_COMO";
  public static final String ATAJO_MENU_ITEM_GUARDAR_COMO = 
          "ATAJO_MENU_ITEM_GUARDAR_COMO";
  public static final String MENU_ITEM_OPCIONES = "MENU_ITEM_OPCIONES";
  public static final String MENU_ITEM_LENGUAJE = "MENU_ITEM_LENGUAJE";
  public static final String MENU_ITEM_ESPANOL = "MENU_ITEM_ESPANOL";
  public static final String MENU_ITEM_INGLES = "MENU_ITEM_INGLES";  

  public static final String MENU_ITEM_SALIR = "MENU_ITEM_SALIR";
  public static final String ATAJO_MENU_ITEM_SALIR = "ATAJO_MENU_ITEM_SALIR";
  public static final String MENU_AYUDA = "MENU_AYUDA";
  public static final String MENU_ITEM_DEBUG = "MENU_ITEM_DEBUG";
  public static final String ATAJO_ITEM_DEBUG = "ATAJO_ITEM_DEBUG";
  public static final String MENU_ITEM_ACERCA_DE = "MENU_ITEM_ACERCA_DE";
  public static final String ATAJO_ITEM_ACERCA_DE = "ATAJO_ITEM_ACERCA_DE";
  
  
  public static final String FILTRO_PARTIDAS = "FILTRO_PARTIDAS";
  public static final String CONFIRMACION_GUARDAR = "CONFIRMACION_GUARDAR";
  public static final String CONFIRMACION_LENGUAJE = "CONFIRMACION_LENGUAJE";
  public static final String MENSAJE_ADYACENTES = "MENSAJE_ADYACENTES";
  public static final String ETIQUETA_PORTAVIONES = "ETIQUETA_PORTAVIONES";
  public static final String ETIQUETA_CRUCERO = "ETIQUETA_CRUCERO";
  public static final String ETIQUETA_DESTRUCTOR = "ETIQUETA_DESTRUCTOR";
  public static final String ETIQUETA_FRAGATA = "ETIQUETA_FRAGATA";
  public static final String PARTIDA_NO_GUARDADA = "PARTIDA_NO_GUARDADA";
  public static final String PARTIDA_NO_LEIDA = "PARTIDA_NO_LEIDA";
  public static final String FICHERO_PARTIDA_NO_ENCONTRADO = 
          "FICHERO_PARTIDA_NO_ENCONTRADO";
  public static final String CONFIGURACION_NO_GUARDADA = "CONFIGURACION_NO_GUARDADA";

  public static final String FICHERO_CONFIGURACION_NO_ENCONTRADO_O_ERRONEO = 
          "Config file not found or wrong";
  public static final String FICHERO_LOCALIZACION_NO_ENCONTRADO_O_ERRONEO = 
          "Locale file not found or wrong";
  public static final String FICHERO_LOCALIZACION_NO_GUARDADO = 
          "Locale file not saved"; 
  
  /* identificadores sistema */
  String[] sistema = {
          "OptionPane.cancelButtonText", 
          "OptionPane.okButtonText",
          "OptionPane.noButtonText",
          "OptionPane.yesButtonText",
          "OptionPane.yesButtonText",
          
          "FileChooser.openDialogTitleText",
          "FileChooser.saveDialogTitleText",
          "FileChooser.lookInLabelText",
          "FileChooser.saveInLabelText",
          "FileChooser.openButtonText",
          "FileChooser.saveButtonText",
          "FileChooser.cancelButtonText",
          "FileChooser.fileNameLabelText",
          "FileChooser.filesOfTypeLabelText",
          "FileChooser.openButtonToolTipText",
          "FileChooser.cancelButtonToolTipText",
          "FileChooser.fileNameHeaderText",
          "FileChooser.upFolderToolTipText",
          "FileChooser.homeFolderToolTipText",
          "FileChooser.newFolderToolTipText",
          "FileChooser.listViewButtonToolTipText",
          "FileChooser.newFolderButtonText",
          "FileChooser.renameFileButtonText",
          "FileChooser.deleteFileButtonText",
          "FileChooser.filterLabelText",
          "FileChooser.detailsViewButtonToolTipText",
          "FileChooser.fileSizeHeaderText",
          "FileChooser.fileDateHeaderText",
          "FileChooser.acceptAllFileFilterText" };

  /**
   * Construye localizacion
   * 
   */
  private Localizacion(String lenguaje, String pais) { 
    try {   

      Locale locale = Locale.forLanguageTag(lenguaje + "-" + pais);            
      recursos = ResourceBundle
         .getBundle(PartidaVista.RUTA_RECURSOS.substring(1).replace('/', '.') + 
                    FICHERO_LOCALIZACION, locale);       

      // localiza textos sistema si no son los de defecto
      if (! locale.equals(Locale.getDefault())) {   
        for (int i = 0; i < sistema.length; i++) { 
          UIManager.put(sistema[i], 
                        recursos.getString(sistema[i]));    
        }
      }
    } catch (MissingResourceException e) {
       if (HundirLaFlota.esModoDebug()) {
          DebugVista.devolverInstancia().mostrar(
                   FICHERO_LOCALIZACION_NO_ENCONTRADO_O_ERRONEO, e);
       }        
    }
  }   
  
  /**
   * Devuelve la instancia de la localizacion
   * 
   */        
  public static synchronized Localizacion devolverInstancia(
          String lenguaje, String pais) { 
    if (instancia == null) {
      instancia = new Localizacion(lenguaje, pais);
    }
    return instancia;
  } 
  
  /**
   * Localiza recurso
   * 
   */    
  public String devuelve(String texto) {
    return recursos.getString(texto);    
  }  
}
