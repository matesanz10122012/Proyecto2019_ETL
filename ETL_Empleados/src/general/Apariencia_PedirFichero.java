package general;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Clase que establece un explorador para indicar la ruta del archivo con los registros para cargar en base de datos
 * @author Beatriz Matesanz
 *
 */
public class Apariencia_PedirFichero {
	String button_PedirFichero;	
	JFrame frame = new JFrame("ETL para la BBDD EMPLEADOS");
	public int lineasInsertadas= 0;
	
	enum extensionPermitida {CSV,XML,JSON}	//ENUMERADO con las extensiones de ficheros que permitimos
	boolean existeExtension = false;
	
	/**
	 * Metodo que crea un explorador para indicar la ruta del archivo para cargar.
	 */
	public Apariencia_PedirFichero() {  
		boolean returnOK=false;
		//PEDIREMOS UN ARCHIVO HASTA QUE SELECCIONEMOS UNO VALIDO O PULSEMOS EN CANCELAR/CERRAR
		do {
			JFileChooser jf = new JFileChooser();
			jf.setDialogTitle("Indica la ruta de un archivo para cargar");
			//FITRAMOS PARA SOLO PERMITIR LAS 3 EXTENSIONES DE ARCHIVOS 
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON, XML o CSV", "json", "xml", "csv");
			jf.setFileFilter(filter);
			jf.addChoosableFileFilter(filter);    
		    // CAMBIAMOS EL TEXTO DEL BOTON A Abrir archivo
			int seleccion = jf.showDialog(null,"Abrir archivo");
			
			if (seleccion == JFileChooser.APPROVE_OPTION) // SI PULSAMOS EN Abrir archivo
				returnOK = comprobarRuta(jf.getSelectedFile().toString()); // LLAMAMOS AL METODO comprobarRuta PASANDO UN STRING POR PARAMETRO (toString)
			else { // SI PULSAMOS CANCELAR --> PANTALLA INICIAL Apariencia_InformacionGeneral
				frame.setVisible(false);
				new Apariencia_InformacionGeneral ("Acceder a la ETL para la BBDD EMPLEADOS",1);
				return;	
			}			
		}
		while(!returnOK);
		
		frame.pack();		
		frame.setVisible(false);
		
		//System.out.println("TOTAL INSERTADO: "+lineasInsertadas);		
		new Apariencia_GuargarBBDD (lineasInsertadas); //MOSTRAMOS UNA PANTALLA CON EL NUMERO DE REGISTROS INSERTADOS LLAMANDO AL CONSTRUCTOT DE LA CLASE Apariencia_GuargarBBDD
	}

	/**
	 * Metodo para comprobar que el fichero seleccionado exite y verificar que es un fichero con una de las tres extensiones permitidas.
	 * Cada una de las extensiones realiza una llamada a un metodo diferente de la clase LeerFicheros
	 * @param rutaFichero Ruta del archivo a cargar
	 * @return Devuelve true/falso en caso de tener que volver a pedir de nuevo el archivo.
	 */
	public boolean comprobarRuta(String rutaFichero){
		String extension = "";
		LeerFicheros lF = new LeerFicheros();				
		File archivo = new File(rutaFichero);
		//COMPROBAMOS QUE EL FICHERO EXISTE
		if (!archivo.exists()) {
			JOptionPane.showMessageDialog(frame,"El fichero no existe. \nIndica una ruta correcta", "ERROR", JOptionPane.ERROR_MESSAGE);	
			return false;
		}
		else{
			// EXTRAEMOS SU EXTENSION 
			int i = rutaFichero.lastIndexOf('.');
			if (i > 0) {
				extension = rutaFichero.substring(i+1);
				for (extensionPermitida extensionP : extensionPermitida.values()) 
					if (extension.toUpperCase().equals(extensionP.name())) existeExtension = true; 

				if (!existeExtension) {
					JOptionPane.showMessageDialog(frame,"Extension de fichero no permitida", "ERROR", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				//System.out.println("EXTENSION DEL FICHERO " + extension.toUpperCase());
				
				// SEGUN SU EXTENSION LLAMAMOS A UNO DE LOS METODOS PARA LEER SU FICHERO
				switch (extension.toUpperCase()){
					case "CSV":
						String separadorCSV = ";";
						lineasInsertadas = lF.leerFicheroCSV(rutaFichero, separadorCSV);
						break;
					case "XML":
						lineasInsertadas = lF.leerFicheroXML(rutaFichero);
						break;
					case "JSON":
						lineasInsertadas = lF.leerFicheroJSON(rutaFichero);
						break;
				}
			}
			else {
				JOptionPane.showMessageDialog(frame,"No existe una extension para el fichero indicado", "ERROR", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;		
	}
}