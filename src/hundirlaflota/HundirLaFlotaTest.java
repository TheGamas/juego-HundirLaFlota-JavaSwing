/**
 * HundirLaFlotaTest.java
 * 
 * Versión 1 David Colás (calidad) y Samuel Felipe (funcionamiento) (02/2024)
 * - Código para jugar, guardar y recuperar partida
 *  
 */

 /**
  * Clase para probar el juego de hundir la flota
  * 
  */

 package hundirlaflota;

public class HundirLaFlotaTest {

    static final String BARCO_SIMBOLO = "*";
    static final String DISPARO_AGUA_SIMBOLO = "X";
    static final int FILAS = 10;
    static final int COLUMNAS = 10;

     /**
      * Método main
      * 
      */
     public static void main(String[] args) {

        
        HundirLaFlota hundirlaflota1 = new HundirLaFlota(COLUMNAS, FILAS);
        HundirLaFlota hundirlaflota2 = new HundirLaFlota(COLUMNAS, FILAS);

        int resultado = hundirlaflota1.colocarBarco(FactoriaBarcos.PORTAVIONES);
        hundirlaflota1.colocarBarco(FactoriaBarcos.CRUCERO);
        hundirlaflota1.colocarBarco(FactoriaBarcos.DESTRUCTOR);
        hundirlaflota1.colocarBarco(FactoriaBarcos.DESTRUCTOR);
        hundirlaflota1.colocarBarco(FactoriaBarcos.FRAGATA);
        hundirlaflota1.colocarBarco(FactoriaBarcos.FRAGATA);
        hundirlaflota1.colocarBarco(FactoriaBarcos.FRAGATA);

        if(resultado == Partida.DEMASIADOS_BARCOS){
            System.out.println("ERROR: Demasiados barcos a añadir");
        }
        else if(resultado == Partida.BARCO_DEMASIADO_GRANDE) {
            System.out.println("ERROR: Barco demasiado grande");
        }
        else {  // Zona de pruebas de funcionamiento
                System.out.println(hundirlaflota1.toString());
                System.out.println("Resultados de disparos:");
                System.out.println(hundirlaflota1.disparar(new Posicion(0,1)));
                System.out.println(hundirlaflota1.disparar(new Posicion(0,2)));
                System.out.println(hundirlaflota1.disparar(new Posicion(0,3)));
                System.out.println(hundirlaflota1.disparar(new Posicion(0,4)));
                System.out.println(hundirlaflota1.disparar(new Posicion(0,5)));
                System.out.println(hundirlaflota1.disparar(new Posicion(0,6)));
                System.out.println(hundirlaflota1.disparar(new Posicion(0,7)));
                System.out.println(hundirlaflota1.disparar(new Posicion(0,8)));
                System.out.println(hundirlaflota1.disparar(new Posicion(0,9)));

            System.out.println(hundirlaflota1.toString());
            //Guardamos esta partida
            try { 
                //Codigo que puede generar excepciones
                hundirlaflota1.guardarPartida("fichero.txt");
                //Cargamos la partida antes guardada en otra instancia de hundirlaflota
                hundirlaflota2 = new HundirLaFlota("fichero.txt");
                
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println(hundirlaflota2.toString());
            //Comprobamos que es la partida de antes
        }
     }
 }