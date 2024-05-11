
/**
 * Tupla.java
 * 
 * Versión 2 David Colás (funcionamiento) y Samuel Felipe (calidad) (05/2024)
 * - Código jugar, cargar y guardar partida
 *  
 */
package hundirlaflota.modelo;

/**
 *  Tupla genérica de dos objetos
 * 
 */
public class Tupla<A, B> {
  public final A a;
  public final B b;

  /**
   *  Construye una tupla
   *  
   */   
  public Tupla(A a, B b) { 
    this.a = a; 
    this.b = b; 
  }
}  

