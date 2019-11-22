package general;

import java.io.BufferedReader; 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Clase que establece un explorador para indicar el directorio deonde descargar un fichero con los registros cargados
 * con anterioridad en base de datos. A continuación, mostrara una pestaña con los botones  para ir a la pantalla inicial o para 
 * volver a descargar en otra ruta
 * @author Beatriz Matesanz
 *
 */
public class Apariencia_Descargar {
	JButton button1;
	JButton button2;
	JPanel p=new JPanel();
	int contadorLineasInsertadas=0;

	JFrame frame = new JFrame("ETL para la BBDD EMPLEADOS");
	
	/**
	 * Metodo que crea un explorador para indicar el directorio donde almacenamosel fichero de log con los registros que hemos insertado en base de datos
	 */
	public Apariencia_Descargar() {
		boolean returnOK=false;
		do {
			//PEDIREMOS UN DIRECTORIO HASTA QUE SELECCIONEMOS UNO VALIDO O PULSEMOS EN CANCELAR/CERRAR
			JFileChooser jf = new JFileChooser();
			jf.setDialogTitle("Indica el directorio donde guardar el fichero");
			jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			// CAMBIAMOS EL TEXTO DEL BOTON A Seleccionar directorio
			int seleccion = jf.showDialog(null, "Seleccionar directorio");
			
			if (seleccion == JFileChooser.APPROVE_OPTION)// SI PULSAMOS EN Abrir archivo
				returnOK = comprobarRuta(jf.getSelectedFile().toString()); // LLAMAMOS AL METODO comprobarRuta PASANDO UN STRING POR PARAMETRO (toString)
			else {  // SI PULSAMOS CANCELAR --> PANTALLA INICIAL Apariencia_InformacionGeneral
				frame.setVisible(false);
				new Apariencia_InformacionGeneral ("Acceder a la ETL para la BBDD EMPLEADOS",1);	
				return;	
			}
		}
		while(!returnOK);
		frame.pack();
		frame.setVisible(false);
		
		//MOSTRAMOS UNA PANTALLA CON DOS BOTONES ("Pantalla inicial" Y "Descargar en otra ruta") --> Clase Apariencia_InformacionGeneral
		new Apariencia_InformacionGeneral("Pantalla inicial","Descargar en otra ruta",2);
	}


	/**
	 * Metodo para comprobar que el directorio seleccionado exite.
	 * Posteriormente, copia el fichero CREAR_ARCHIVO_LOG_DESCARGAR.txt en un fichero llamado LOG[5 caracteres aleartorios].csv
	 * en el directorio declarado
	 * @param rutaFichero Ruta del directorio
	 * @return Devuelve true/falso en caso de tener que volver a pedir de nuevo el directorio.
	 */
	public boolean comprobarRuta(String rutaFichero){//DESCARGAR Y A LA PANTALLA INICIO
		if(rutaFichero.trim().length() == 0) {
			JOptionPane.showMessageDialog(frame,"Indica una ruta para el directorio correcta", "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		File pathFichero = new File(rutaFichero);
		//COMPROBAMOS QUE EL DIRECTORIO EXISTE
		if (!pathFichero.exists()) {
			JOptionPane.showMessageDialog(frame,"Indica una ruta para el directorio correcta", "ERROR", JOptionPane.ERROR_MESSAGE);	
			return false;
		}
		else{
			//COMPROBAMOS QUE ES UN DIRECTORIO
			if (pathFichero.isDirectory()){
				// POR MEDIO DE LA FUNCION randomUUID() NOS AYUDAREMOS PARA NOMBRAR DIFERENTES NOMBRES DE FICHEROS
				// Y ASI EVITAR REMPLAZAR EL FICHERO SI EXISTE
				String uniqueID = UUID.randomUUID().toString();
				String nombreFicheroDestino = "\\LOG"+uniqueID.substring(uniqueID.length()-5)+".csv";
				
				File destinoF = new File(pathFichero,nombreFicheroDestino);
				
				//FICHERO DE ORIGEN QUE SE CREA CUANDO INSERTAMOS DATOS EN BASE DE DATOS EN EL METODO listEmpleado DE LA CLASE 
				//LeerFicheros DONDE ESTA LLAMA AL METODO crearArchivoParaDescargar DE LA CLASE Transformaciones
				File origenF = new File("CREAR_ARCHIVO_LOG_DESCARGAR.txt");
				
				//SI NO EXISTE EL FICHERO SIGNIFICA QUE NO HAY NINGUN REGISTRO QUE HAYAMOS INSERTADO EN BASE DE DATOS
				if(!origenF.exists()) {
					JOptionPane.showMessageDialog(frame,"No hay ningun registro para mostrar", "ERROR", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				else{
					//COPIAMOS ESTE ARCHIVO CON EL NOMBRE DEFINIDO EN LA VARIABLE nombreFicheroDestino
					if(!copiarFicheros(origenF,destinoF)) {
						JOptionPane.showMessageDialog(frame,"Error al copiar el fichero", "ERROR", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					JOptionPane.showMessageDialog(frame,"Fichero descargado correctamente con "+contadorLineasInsertadas+ " registros", "DESCARGADO", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(frame,"Indica una ruta para el directorio correcta", "ERROR", JOptionPane.ERROR_MESSAGE);	
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Metodo que permite copiar los datos de un fichero de origen a uno de destino
	 * @param origenF Objeto tipo File con el fichero de origen 
	 * @param destinoF Objeto tipo File con el fichero de salida donde copiar los datos
	 * @return boolean para comprobar la verificacion de la copia correcta o fallido 
	 */
	public boolean copiarFicheros(File origenF, File destinoF) {
        if (origenF.exists()) {
            try {
                InputStream in = new FileInputStream(origenF);
                OutputStream out = new FileOutputStream(destinoF);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                
                in.close();
                out.close();
    			FileReader fr = new FileReader(destinoF);
				BufferedReader bf = new BufferedReader(fr);
				//En la variable contadorLineasInsertadas almacenamos el numero de lineas copiadas sin tener en cuenta el encabezado
				contadorLineasInsertadas=(int) bf.lines().count()-1; // no contamos el encabezado 
				bf.close();
                return true;
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
                return false;
            }
        } else 
            return false;
    }
}
