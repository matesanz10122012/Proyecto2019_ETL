package general;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Date;

/**
 * Clase que establece algunos de los metodos para transformar los datos de origen segun varias caracteristicas.
 * Ademas, declararemos un metodo para crear el fichero CREAR_ARCHIVO_LOG_DESCARGAR.txt empleado para posteriromente 
 * usarlo para descargar el log 
 * @author Beatriz Matesanz
 *
 */

public class Transformaciones {

	/**
	 * Metodo para separar una cadena en diferentes elementos separados por un separador
	 * @param cadena cadena con el texto a separar
	 * @param separador Separador por el cual dividir la cadena
	 * @return array con los diferentes elementos divididos
	 */
	public String []  dividirString (String cadena, String separador){
		String [] cortes = cadena.split(separador, 0);   
		return cortes;
	}

	/**
	 * Metodo para unificar los valores de la cadena genero, para que los valores como '1' o 'M' se sustituyan por 'Mujer'
	 * @param cadena cadena con el texto a unificar
	 * @return array con los diferentes elementos divididos
	 */
	public String unificarGenero (String cadena){
		String sexo = "Hombre";
		if(cadena==null)
			sexo = null;	
		else if(cadena.trim().length() <1)
			sexo = null;	
		else if(cadena.equals("1")|| cadena.equals("M"))
			sexo = "Mujer";	
		return sexo;
	}
	
	/**
	 * Metodo para convertir una cadena en un tipo Date 
	 * @param Fecha : cadena con el texto de la Fecha de origen 
	 * @return valor convertido a un tipo Date
	 */
	public Date convertDate (String Fecha ){	
		Date fechaFinal = null;
		//SI EXISTE UNA FECHA
		if(Fecha != null && Fecha.trim().length() != 0 ) {
			if(Fecha.trim().length() <=8 ) { //por ejemplo:  20-08-17 --> CAMBIAR A UN FORMATO 20/08/2017
				String ano = Fecha.substring(Fecha.length()-2);; 
				if(Integer.parseInt(ano) > 60) ano = "19" + ano;
				else ano = "20" + ano;				
				Fecha = Fecha.substring(0,Fecha.length()-2)+ano; 
				Fecha = Fecha.replace("-", "/");				
			}
			
			//INTENTAMOS PASAR EL STRING A UN FORMATO dd/MM/yyyy
			try {
				java.util.Date fechaFinal02 = new java.util.Date();
				fechaFinal02= new SimpleDateFormat("dd/MM/yyyy").parse(Fecha);				
				fechaFinal = new java.sql.Date(fechaFinal02.getTime());		
				
				//System.out.println(fechaFinal);
				//System.out.println(fechaFinal.toString().length());
				if(fechaFinal.toString().length()>10) {// 202012-06-07
					String dateOK = fechaFinal.toString().substring(2);
					fechaFinal02= new SimpleDateFormat("dd/MM/yyyy").parse(dateOK);				
					fechaFinal = new java.sql.Date(fechaFinal02.getTime());	
				}
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
			}
		}
		return fechaFinal;  	    
	}
	/**
	 * Metodo para convertir un String en un valor de tipo int. Empleado para el salario
	 * @param dato cadena con el texto a parsear
	 * @return int : con el valor convertido a numero 
	 */
	public Integer convertInt (String dato){
		Integer numeroConvertir = 0;
		if(dato != null && dato !="") {
			dato = dato.replaceAll("[^0-9.]", ""); //quitamos los caracteres que no son numeros
			numeroConvertir = Integer.parseInt(dato);  	
			
		}
		return numeroConvertir;  	    
	}
	
	/**
	 * Metodo para convertir un String en un valor de tipo int. Empleado para la latitud y longitud geografica
	 * @param dato cadena con el texto a parsear
	 * @return double : con el valor convertido a double 
	 */
	public Double convertDouble (String dato ){
		Double numeroConvertir = 0.0;
		if(dato != null && !dato.equals("")) {
			dato = dato.replaceAll("[^0-9.]", ""); //quitamos los caracteres que no son numeros
			numeroConvertir = Double.parseDouble(dato);  			
		}
		return numeroConvertir;  	    
	}
	
	/**
	 * Metodo para unir dos String en uno. Empleado para la unir el nombre y el apellido
	 * @param cadenaNombre : cadena con el nombre
	 * @param cadenaApellido : cadena con el apellido
	 * @return string con la union del nombre y apellido
	 */
	public String unirNombre (String cadenaNombre, String cadenaApellido ){
		String nombreCompleto = cadenaNombre + " " +cadenaApellido;
		return nombreCompleto;
	}
	
	/**
	 * Metodo para verificar los campos que debemos almacenar en base de datos. Empleado para excluir, por ejemplo, el campo email 
	 * @param cadena cadena con el encabezado a comprobar si debemos almacenarlo
	 * @return boolean si debemos o no almacenarlo 
	 */
	public boolean guardarColumnaTF (String cadena){
		Empleado e = new Empleado ();
		String[] lista = e.cabeceraBBDD();
		boolean contiene = false;		
		for (String i : lista) 
			if (cadena.contains(i))
				contiene = true;

		return contiene;
	}
	
	/**
	 * Metodo para guardar un fichero llamado CREAR_ARCHIVO_LOG_DESCARGAR.txt tanto con el encabezado como con los datos 
	 * de los registros almacenados en nuestra base de datos
	 * @param lista Lista con los empleados 
	 */
	public void crearArchivoParaDescargar(List<Empleado> lista) {
		Empleado e = new Empleado ();
		String nombreFichero = "CREAR_ARCHIVO_LOG_DESCARGAR.txt";
		File f = new File (nombreFichero);
		if(f.exists()) {f.delete();} //SI EL FICHERO YA EXISTE LO ELIMINAMOS
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(nombreFichero);
			if(f.exists()) {f.delete();}
			
			BufferedWriter bfwriter = new BufferedWriter(fw);
			bfwriter.write(e.toStringCabeceraParaDescargar(";")); //INSERTAMOS LAS CABECERAS
			
			//INSETAMOS LOS VALORES DE CADA EMPLEADO SEPARADOS POR ;
			for (Empleado empleado : lista)
				bfwriter.write(empleado.toStringParaDescargar(";"));
					
			bfwriter.close();
			System.out.println("Archivo creado satisfactoriamente: "+nombreFichero);
			
		} catch(Exception e1) {
			JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e1), "ERROR", JOptionPane.ERROR_MESSAGE);	
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch(Exception e2) {
					JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e2), "ERROR", JOptionPane.ERROR_MESSAGE);	
				}
		}
		//SI NO EXISTEN EMPLEADOS INSERTADOS ELIMINAMOS EL FICHERO 
		if(f.exists() && lista.size() <1) {f.delete();}
	}	
}
