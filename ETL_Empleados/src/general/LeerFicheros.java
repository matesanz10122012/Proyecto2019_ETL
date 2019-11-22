package general;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.sql.Date;

/**
 * @author Beatriz Matesanz
 *
 */
public class LeerFicheros {	
	String cabeceraGeneralCSV;	
	List<Empleado> listEmpleados = new ArrayList<Empleado>(); //LISTA USADA PARA ALMACENAR NUESTROS EMPLEADOS
	
	//Establecemos una objeto de cada una de las clases para usar sus metodos 
	Transformaciones t = new Transformaciones();	
	Empleado e = new Empleado ();	
	BBDD bbdd = new BBDD ();

	/**
	 * Metodo para asegurarnos que se borra el fichero CREAR_ARCHIVO_LOG_DESCARGAR.txt con los datos de la carga anterior.
	 * Aunque tambien se borra en el metodo crearArchivoParaDescargar de la clase Transformaciones
	 */
	public void borrarFicheroBase() {
		String nombreFichero = "CREAR_ARCHIVO_LOG_DESCARGAR.txt";
		File f = new File (nombreFichero);
		if(f.exists()) {f.delete();}
	}
	
/* ***************************************
	LEER FICHERO BASE
*************************************** */	
	/**
	 * Metodo general para leer los datos de los ficheros.
	 * @param ruta cadena con la ruta donde se encuentra el fichero 
	 * @param tipoFichero : si es CSV o JSON
	 * @return cadena de texto con todo el contenido del fichero
	 */
	public String leerFichero(String ruta, String tipoFichero) {
		BufferedReader br = null;
		String cadenaFichero = "";
		String extension = "\n";
		
		//SI ES UN CSV ESTABLECEMOS QUE LA SEPARACION SE ESTABLECE POR ;
		switch (tipoFichero) {
			case "CSV":
				extension = ";";
		}
				
		try {	 
			if (tipoFichero != "JSON") 
				br =new BufferedReader(new FileReader(ruta));
			else { // SI ES TIPO JSON FORZAMOS A LEERLO CON LA CODIFICACION UTF8
				InputStreamReader is = new InputStreamReader(new FileInputStream(ruta),"UTF-8");
				br = new BufferedReader(is);
			}	
				
			String line = br.readLine();
			cabeceraGeneralCSV = line;
			while (null!=line) {				
				cadenaFichero+=line+extension;	
				line = br.readLine();
			}
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
		} finally {
			if (null!=br) {
				try {
					br.close();
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
				}
			}
		}
			
		switch (tipoFichero) {
			case "CSV":
				cadenaFichero = cadenaFichero.substring(cabeceraGeneralCSV.length()+1);
		}
		
		return cadenaFichero;
	}
	
/* ***************************************
	LEER FICHERO CSV
*************************************** */
	/**
	 * Metodo para leer cada linea de un fichero csv, crear un objeto de la clase Empleado e insertarlo en la lista.
	 * Esta lista la pasaremos por parametro al metodo listEmpleado
	 * @param ruta : Ruta del fichero CSV
	 * @param separador : Separador de campos del fichero
	 * @return numero de filas insertadas
	 */
	public int leerFicheroCSV(String ruta, String separador) {
		borrarFicheroBase();
		listEmpleados.clear();
		//LLAMAMOS AL METODO leerFichero PARA GUARDAR EL CONTENIDO DE NUESTRO FICHERO 
		String cadenaFichero = leerFichero (ruta, "CSV");		
		String [] datosCadenaFichero = cadenaFichero.split(separador);		
		
		ArrayList<String>datosCadenaFicheroArrayList=new ArrayList<>();
		for(String datos:datosCadenaFichero) 
			datosCadenaFicheroArrayList.add(datos);
		
		int lengthdatosCadenaFicheroArrayList = datosCadenaFicheroArrayList.size(); 
		
		if(datosCadenaFicheroArrayList.get(datosCadenaFicheroArrayList.size()-1).equals("")) { // borrar la ultima linea si esta vacia
			datosCadenaFicheroArrayList.remove(datosCadenaFicheroArrayList.size()-1); // borra el valor pero sigue teniendo la misma longitud	
			lengthdatosCadenaFicheroArrayList = datosCadenaFicheroArrayList.size() -1;
		}
		
		//OBTENEMOS LA CABECERA
		String [] cGeneral = cabeceraGeneralCSV.split(separador);
		List<Integer> indexColumnaOrdenado = new ArrayList<Integer>();

		/* 
			RECORREMOS LA CABECERA PARA EXCLUIR LAS COLUMNAS QUE NO QUERAMOS INSERTAR EN BBDD.
			Si no queremos incluirlas lo marcaremos con el valor -1, en caso contrario marcaremos la posicion 
			que ocupa en el array lista.
			El fin de esto es conseguir un array con la posicion de los campos en el fichero siguiendo el orden 
			establecido en la funcion cabeceraBBDD de la clase Empleado
		*/	
		String[] lista = e.cabeceraBBDD();
		for (String j : lista) {
			Integer lengthList = indexColumnaOrdenado.size();
			for(int k = 0; k < cGeneral.length; k++) {
				if (cGeneral[k].toUpperCase().contains(j.toUpperCase()))
					indexColumnaOrdenado.add(k);
			}
			if(lengthList == indexColumnaOrdenado.size() ) indexColumnaOrdenado.add(-1);
		}	
		if(lista.length < indexColumnaOrdenado.size()-1 ) indexColumnaOrdenado.add(-1);
				
		int contadorLinea = 0;
		int LongitudFila = cGeneral.length;
		Integer numeroFilas = (lengthdatosCadenaFicheroArrayList /(LongitudFila-1));

	    //SACAMOS CADA REGISTRO, CREAMOS EL EMPLEADO Y LO AÑADIMOS A NUESTRA LISTA
		while(contadorLinea <numeroFilas) {
			String nombre=null;
			String genero=null;
			String fechaNac=null;
			String salario= null;
			String departamento=null;
			String ciudad= null;
			String cp= null;
			String longitud=null;
			String latitud = null;
			String fechaInicio  =null;
			String fechaFin=null;
			
			for(int i = 0; i < indexColumnaOrdenado.size(); i++) {				
				if(indexColumnaOrdenado.get(i) != -1) {
					
					if((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(i) >=  lengthdatosCadenaFicheroArrayList ) 
						break;
				
					//SACAMOS EL VALOR DE CADA ATRIBUTO DE EMPLEADO
					switch (i) {
						case 0:  nombre		  = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(0));  break;
						case 1 : genero       = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(1)); break;
						case 2 : fechaNac     = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(2)); break;
						case 3 : salario 	  = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(3)); break;
						case 4 : departamento = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(4)); break;
						case 5 : ciudad 	  = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(5));  break;
						case 6 : cp 		  = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(6));  break;
						case 7 : longitud	  = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(7)); break;
						case 8 : latitud 	  = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(8)); break;
						case 9 : fechaInicio  = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(9)); break;
						case 10: fechaFin 	  = datosCadenaFicheroArrayList.get((contadorLinea*LongitudFila)+indexColumnaOrdenado.get(10));  break;	
					}
				}
			}
			contadorLinea ++;

			System.out.println("ini "+fechaInicio);
			System.out.println("fin "+fechaFin);
			
			//GUARDAMOS ESTOS DATOS EN UN OBJETO EMPLEADO
			Empleado empleadoCreado = new Empleado (nombre, t.unificarGenero(genero), t.convertDate(fechaNac), t.convertInt(salario), departamento,
				ciudad, cp, t.convertDouble(longitud), t.convertDouble(latitud), t.convertDate(fechaInicio), t.convertDate(fechaFin)); 
			
			/* TRANSFORMACIONES PARA ORDENAR LAS FECHAS
				1. SI LA FECHA DE INICIO ES MAYOR A LA FECHA DE NACIMIENTO --> CAMBIAMOS DICHAS FECHAS
				   Y SI LA FECHA DE FIN ES MAYOR QUE LA FECHA DE INICIO --> VACIAMOS EL VALOR DE LA FECHA FIN 
				2. SI LA FECHA DE NACIMIENTO ES NULA Y LA FECHA INICIO ES MAYOR A LA FECHA DE FIN --> CAMBIAMOS DICHAS FECHAS
			*/
			if (empleadoCreado.getFechaInicio()!= null) {
				if (empleadoCreado.getFechaNac()!= null) 
					if(empleadoCreado.getFechaInicio().compareTo(empleadoCreado.getFechaNac())>=0){ //CASO 1
						Date f1 = empleadoCreado.getFechaInicio();
						empleadoCreado.setFechaInicio(empleadoCreado.getFechaNac());
						empleadoCreado.setFechaNac(f1);
						if(empleadoCreado.getFechaFin()!= null)
							if(empleadoCreado.getFechaInicio().compareTo(empleadoCreado.getFechaFin())>=0)
								empleadoCreado.setFechaFin(null);
					} else 
						if (empleadoCreado.getFechaFin()!= null) 
							if(empleadoCreado.getFechaInicio().compareTo(empleadoCreado.getFechaFin())>=0){ // CASO 2
								Date f1 = empleadoCreado.getFechaInicio();		empleadoCreado.setFechaInicio(empleadoCreado.getFechaFin());
								empleadoCreado.setFechaFin(f1);
							}
			}
		
			//GUARDAMOS EN LA LISTA
			if (!empleadoCreado.comprobarSiNoTieneDatosNull())
				listEmpleados.add(empleadoCreado);
		}
			
		//LLAMAMOS AL METODO listEmpleado PARA ALMACENAR LOS VALORES EN BBDD Y NOS DEVUELVE EL NUMERO DE FILAS INSERTADOS
		int lineasInsertadas = 0;
		try {
			lineasInsertadas = listEmpleado(listEmpleados);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
		}

		return lineasInsertadas;	
	}
	
		
/* ***************************************
	LEER FICHERO XML
*************************************** */	
	/**
	 * Metodo para leer cada linea de un fichero XML, crear un objeto de la clase Empleado e insertarlo en la lista.
	 * Esta lista la pasaremos por parametro al metodo listEmpleado
	 * @param ruta : Ruta del fichero xml
	 * @return numero de filas insertadas
	 */
	public int leerFicheroXML(String ruta){
		int lineasInsertadas = 0; 
		borrarFicheroBase();
		listEmpleados.clear();
		try {	      
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document document = documentBuilder.parse(new File(ruta));
			document.getDocumentElement().normalize();
			
			String[] lista = e.cabeceraBBDD();
			List<Integer> indexColumnaOrdenado = new ArrayList<Integer>();

	        Vector<String> listaCabecera = new Vector<String>();
	        Vector<String> listaCabeceraSinRepetidos = new Vector<String>();
				        
	        NodeList nodeListLista=document.getDocumentElement().getChildNodes();
	        
	        //SACAMOS LAS CABECERAS Y GUARDAMOS EN EL VECTOR listaCabecera
	         for(int k=0;k<nodeListLista.getLength();k++){
	        	 Node nodo = nodeListLista.item(k);
	        	
	        	if(nodo != null) 
				if (nodo.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nodo;
					NodeList listaEmpleados02 = element.getChildNodes();				
					
			        for(int j=0;j<listaEmpleados02.getLength();j++){
			        	Node nodo02 = listaEmpleados02.item(j);
			        	if(nodo02 != null) 
							if (nodo02.getNodeType() == Node.ELEMENT_NODE) {
								Element element02 = (Element) nodo02;
								listaCabecera.add(element02.getNodeName());
							}
			        }	        		
	        	 }
	         }
	         
	        /* QUITAR DUPLICADOS DE LA LISTA PARA SACAR SOLO LAS CABECERAS UNICAS */
	 		for(int i=0;i<listaCabecera.size();i++){
				for(int j=0;j<listaCabecera.size()-1;j++){
					if(i!=j){
						if(listaCabecera.get(i)==listaCabecera.get(j)){
							listaCabecera.set(j, "");
						}
					}
				}
			} 		
			
			/*LIMPIAMOS LA LISTA Y LA ALMACENAMOS EN LA VARIABLE listaCabeceraSinRepetidos*/
			for (String j : listaCabecera) 
				if (j != "")
					listaCabeceraSinRepetidos.add(j);
			
			/* 
				RECORREMOS LA CABECERA PARA EXCLUIR LAS COLUMNAS QUE NO QUERAMOS INSERTAR EN BBDD.
				Si no queremos incluirlas lo marcaremos con el valor -1, en caso contrario marcaremos la posicion 
				que ocupa en el array lista.
				El fin de esto es conseguir un array con la posicion de los campos en el fichero siguiendo el orden 
				establecido en la funcion cabeceraBBDD de la clase Empleado
			*/	
			for (int j = 0; j < lista.length; j++) {
				Integer lengthList = indexColumnaOrdenado.size();
				for(int k = 0; k < listaCabeceraSinRepetidos.size(); k++) 
					if (listaCabeceraSinRepetidos.get(k).toUpperCase().contains(lista[j].toUpperCase())) 
						indexColumnaOrdenado.add(j);
				if(lengthList == indexColumnaOrdenado.size() ) indexColumnaOrdenado.add(-1);
			}				
			if(lista.length < indexColumnaOrdenado.size() ) indexColumnaOrdenado.add(-1);

	        //SACAMOS CADA REGISTRO, CREAMOS EL EMPLEADO Y LO AÑADIMOS A NUESTRA LISTA
	         for(int k=0;k<nodeListLista.getLength();k++){
	        	 Node nodo = nodeListLista.item(k);	        	
	        	if(nodo != null) 
				if (nodo.getNodeType() == Node.ELEMENT_NODE) {
					String nombre=null;
					String genero=null;
					String fechaNac=null;
					String salario= null;
					String departamento=null;
					String ciudad= null;
					String cp= null;
					String longitud=null;
					String latitud = null;
					String fechaInicio  =null;
					String fechaFin=null;
					
					Element element = (Element) nodo;
					
					// REALIZAMOS ALGUNAS TRANSFORMACIONES
					String [] corteUbicacion = new String [2]; //ARRAY PARA SEPARAR LA LATITUD Y LONGITUD 
					for (String j : listaCabeceraSinRepetidos) {
						// UNIR NOMBRE Y APELLIDO 
						if (j.toUpperCase().contains("APE")) 								
							element.getElementsByTagName("Nombre").item(0).setTextContent(t.unirNombre(
									element.getElementsByTagName("Nombre").item(0).getTextContent(),
									element.getElementsByTagName("Apellidos").item(0).getTextContent()
									));
						// SEPARAR UBICACION EN LATITUD Y LONGITUD 
						if (j.toUpperCase().contains("UBICACION")) {
							corteUbicacion = t.dividirString(element.getElementsByTagName("Ubicacion").item(0).getTextContent(), ",");
							longitud	  = corteUbicacion[0];
							latitud 	  = corteUbicacion[1];
						}
					}							
					
					//SACAMOS EL VALOR DE CADA ATRIBUTO DE EMPLEADO
					if(indexColumnaOrdenado.contains(0)) nombre		  =element.getElementsByTagName("Nombre").item(0).getTextContent(); 
					if(indexColumnaOrdenado.contains(1)) genero       =element.getElementsByTagName("Genero").item(0).getTextContent(); 
					if(indexColumnaOrdenado.contains(2)) fechaNac     =element.getElementsByTagName("FechaNacimiento").item(0).getTextContent(); 
					if(indexColumnaOrdenado.contains(3)) salario 	  =element.getElementsByTagName("Salario").item(0).getTextContent(); 
					if(indexColumnaOrdenado.contains(4)) departamento =element.getElementsByTagName("Departamento").item(0).getTextContent(); 
					if(indexColumnaOrdenado.contains(5)) ciudad 	  =element.getElementsByTagName("Ciudad").item(0).getTextContent(); 
					if(indexColumnaOrdenado.contains(6)) cp 		  =element.getElementsByTagName("CP").item(0).getTextContent(); 
					if(indexColumnaOrdenado.contains(7) && element.getElementsByTagName("Longitud").item(0) != null) 
							longitud=element.getElementsByTagName("Longitud").item(0).getTextContent(); 
					if(indexColumnaOrdenado.contains(8)  && element.getElementsByTagName("Latitud").item(0) != null)  
							latitud  =element.getElementsByTagName("Latitud").item(0).getTextContent(); 
					if(indexColumnaOrdenado.contains(9)) fechaInicio  =element.getElementsByTagName("FechaInicio").item(0).getTextContent();
					   
					//GUARDAMOS ESTOS DATOS EN UN OBJETO EMPLEADO
					Empleado empleadoCreado = new Empleado (nombre, t.unificarGenero(genero), t.convertDate(fechaNac), t.convertInt(salario), departamento,
						ciudad, cp, t.convertDouble(longitud), t.convertDouble(latitud), t.convertDate(fechaInicio), t.convertDate(fechaFin)); 
					
					/* TRANSFORMACIONES PARA ORDENAR LAS FECHAS
						1. SI LA FECHA DE INICIO ES MAYOR A LA FECHA DE NACIMIENTO --> CAMBIAMOS DICHAS FECHAS
						   Y SI LA FECHA DE FIN ES MAYOR QUE LA FECHA DE INICIO --> VACIAMOS EL VALOR DE LA FECHA FIN 
						2. SI LA FECHA DE NACIMIENTO ES NULA Y LA FECHA INICIO ES MAYOR A LA FECHA DE FIN --> CAMBIAMOS DICHAS FECHAS
					*/
					if (empleadoCreado.getFechaInicio()!= null) {
						if (empleadoCreado.getFechaNac()!= null) 
							if(empleadoCreado.getFechaInicio().compareTo(empleadoCreado.getFechaNac())>=0){ //CASO 1
								Date f1 = empleadoCreado.getFechaInicio();
								empleadoCreado.setFechaInicio(empleadoCreado.getFechaNac());
								empleadoCreado.setFechaNac(f1);
								if(empleadoCreado.getFechaFin()!= null)
									if(empleadoCreado.getFechaInicio().compareTo(empleadoCreado.getFechaFin())>=0)
										empleadoCreado.setFechaFin(null);
							} else 
								if (empleadoCreado.getFechaFin()!= null) 
									if(empleadoCreado.getFechaInicio().compareTo(empleadoCreado.getFechaFin())>=0){ // CASO 2
										Date f1 = empleadoCreado.getFechaInicio();		empleadoCreado.setFechaInicio(empleadoCreado.getFechaFin());
										empleadoCreado.setFechaFin(f1);
									}
					}
		
					//GUARDAMOS EN LA LISTA
					if (!empleadoCreado.comprobarSiNoTieneDatosNull())
						listEmpleados.add(empleadoCreado);
	        	 }
	         }
			
			//LLAMAMOS AL METODO listEmpleado PARA ALMACENAR LOS VALORES EN BBDD Y NOS DEVUELVE EL NUMERO DE FILAS INSERTADOS
	        lineasInsertadas = listEmpleado(listEmpleados);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
		}
		return lineasInsertadas;
	}


/* ***************************************
	LEER FICHERO JSON
*************************************** */
	/**
	 * Metodo para leer cada linea de un fichero JSON, crear un objeto de la clase Empleado e insertarlo en la lista.
	 * Esta lista la pasaremos por parametro al metodo listEmpleado
	 * @param ruta : Ruta del fichero xml
	 * @return numero de filas insertadas
	 */
	public int leerFicheroJSON(String ruta){
		int lineasInsertadas = 0; 
		borrarFicheroBase();
		listEmpleados.clear();
		//LLAMAMOS AL METODO leerFichero PARA GUARDAR EL CONTENIDO DE NUESTRO FICHERO 
		String cadenaFichero = leerFichero (ruta, "JSON");
		
		JSONObject objetoJson = new JSONObject(cadenaFichero);
		JSONArray cabecera = (JSONArray) objetoJson.get("cols"); 
		JSONArray arrayJSON = (JSONArray) objetoJson.get("data"); 		
		String[] lista = e.cabeceraBBDD();

		List<Integer> indexColumnaOrdenado = new ArrayList<Integer>();

		/*
			HALLAMOS TODAS LAS CABECERAS DEL FICHERO INTRODUCIDO, ELIMINAMOS LAS COLUMNAS QUE NO QUERAMOS INSERTAR EN BBDD
			Si no queremos incluirlas lo marcaremos con el valor -1, en caso contrario marcaremos la posicion 
			que ocupa en el array lista.
			El fin de esto es conseguir un array con la posicion de los campos en el fichero siguiendo el orden 
			establecido en la funcion cabeceraBBDD de la clase Empleado.
		*/	
		String cabeceraString = cabecera.toString();
		cabeceraString = cabeceraString.replace("\",\"", "|").replace("\",", "|").replace(",\"","|").replace("\"","");
		String [] listaCabecera = cabeceraString.split("\\|");
		
		for (String j : lista) {
			Integer lengthList = indexColumnaOrdenado.size();
			for(int k = 0; k < listaCabecera.length; k++) {
				if (listaCabecera[k].toUpperCase().contains(j.toUpperCase())) indexColumnaOrdenado.add(k);
				else if ((j.toUpperCase().contains("LAT") ||j.toUpperCase().contains("LONG")) && listaCabecera[k].toUpperCase().contains("UBICACION")) 
					indexColumnaOrdenado.add(k);
			}
			if(lengthList == indexColumnaOrdenado.size() ) indexColumnaOrdenado.add(-1);
		}			
		if(lista.length < indexColumnaOrdenado.size() ) indexColumnaOrdenado.add(-1);
		
     
		//SACAMOS CADA REGISTRO, CREAMOS EL EMPLEADO Y LO AÑADIMOS A NUESTRA LISTA
		Iterator<Object> iterator = arrayJSON.iterator();				
		while(iterator.hasNext()) {
			String valorLinea = String.valueOf(iterator.next());
			
			//REMPLAZA LOS VALORES 1. "," 2. ",  3. ," 4." 5 [[ 6. ]]
			valorLinea = valorLinea.replace("\",\"", "|").replace("\",", "|").replace(",\"","|").replace("\"","").replace("[[", "|").replace("]]", "|");
			
			String separador = "\\|";
			String [] valorLineaArray = valorLinea.split(separador);
			
			
			Integer contadorLinea = 0;
			Integer LongitudFila = listaCabecera.length;
			Integer numeroFilas = (valorLineaArray.length /(LongitudFila-1));
			
			while(contadorLinea <numeroFilas) {
				String nombre=null;
				String genero=null;
				String fechaNac=null;
				String salario= null;
				String departamento=null;
				String ciudad= null;
				String cp= null;
				String longitud=null;
				String latitud = null;
				String fechaInicio  =null;
				String fechaFin=null;
			
				for(int i = 0; i < indexColumnaOrdenado.size(); i++) {
					System.out.println("bb"+valorLineaArray[i]);
					if(valorLineaArray[i].length()!=0)
						if(valorLineaArray[i].substring(0,1).equals("[")) valorLineaArray[i]= valorLineaArray[i].substring(1);
						else if(valorLineaArray[i].substring(valorLineaArray[i].length()-1,valorLineaArray[i].length()).contentEquals("]")) 
							valorLineaArray[i]= valorLineaArray[i].substring(0,valorLineaArray[i].length()-1);
					
					// REALIZAMOS ALGUNAS TRANSFORMACIONES
					// UNIR NOMBRE Y APELLIDO 
					if(indexColumnaOrdenado.get(i) ==0) 
						for (int j = 0 ; j<listaCabecera.length;j++) 				
					        if (listaCabecera[j].toUpperCase().contains("APE")) 
					        	valorLineaArray[0]= t.unirNombre(valorLineaArray[0], valorLineaArray[j]);
	
					// SEPARAR UBICACION EN LATITUD Y LONGITUD 
					String [] corteUbicacion = new String [2];
					if(i == indexColumnaOrdenado.size() -1)  // PARA SOLO HACERLO 1 VEZ 						
						for (int j = 0 ; j<listaCabecera.length;j++) 				
					        if (listaCabecera[j].toUpperCase().contains("UBICACION")) {
					        	corteUbicacion = t.dividirString(valorLineaArray[j], ",");
								longitud	  = corteUbicacion[0];
								latitud 	  = corteUbicacion[1];
					        }
					        
					//SACAMOS EL VALOR DE CADA ATRIBUTO DE EMPLEADO					
					if(indexColumnaOrdenado.get(i) != -1)
						switch (i) {
						case 0:  nombre		  = valorLineaArray[(contadorLinea*LongitudFila)+indexColumnaOrdenado.get(0)];  break;
						case 1 : genero       = valorLineaArray[(contadorLinea*LongitudFila)+indexColumnaOrdenado.get(1)]; break;
						case 2 : fechaNac     = valorLineaArray[(contadorLinea*LongitudFila)+indexColumnaOrdenado.get(2)]; break;
						case 3 : salario 	  = valorLineaArray[(contadorLinea*LongitudFila)+indexColumnaOrdenado.get(3)]; break;
						case 4 : departamento = valorLineaArray[(contadorLinea*LongitudFila)+indexColumnaOrdenado.get(4)]; break;
						case 5 : ciudad 	  = valorLineaArray[(contadorLinea*LongitudFila)+indexColumnaOrdenado.get(5)];  break;
						case 6 : cp 		  = valorLineaArray[(contadorLinea*LongitudFila)+indexColumnaOrdenado.get(6)];  break;
						case 7 : longitud	  = corteUbicacion[0]; break;
						case 8 : latitud 	  = corteUbicacion[1]; break;
						case 9 : fechaInicio  = valorLineaArray[(contadorLinea*LongitudFila)+indexColumnaOrdenado.get(9)]; break;
						case 10: fechaFin 	  = valorLineaArray[(contadorLinea*LongitudFila)+indexColumnaOrdenado.get(10)];  break;	
						}
				}
				contadorLinea ++;

				//GUARDAMOS ESTOS DATOS EN UN OBJETO EMPLEADO
				Empleado empleadoCreado = new Empleado (nombre, t.unificarGenero(genero), t.convertDate(fechaNac), t.convertInt(salario), departamento,
					ciudad, cp, t.convertDouble(longitud), t.convertDouble(latitud), t.convertDate(fechaInicio), t.convertDate(fechaFin)); 
				System.out.println(empleadoCreado.toStringFormateado());
				/* TRANSFORMACIONES PARA ORDENAR LAS FECHAS
					1. SI LA FECHA DE INICIO ES MAYOR A LA FECHA DE NACIMIENTO --> CAMBIAMOS DICHAS FECHAS
					   Y SI LA FECHA DE FIN ES MAYOR QUE LA FECHA DE INICIO --> VACIAMOS EL VALOR DE LA FECHA FIN 
					2. SI LA FECHA DE NACIMIENTO ES NULA Y LA FECHA INICIO ES MAYOR A LA FECHA DE FIN --> CAMBIAMOS DICHAS FECHAS
				*/
				if (empleadoCreado.getFechaInicio()!= null) {
					if (empleadoCreado.getFechaNac()!= null) 
						if(empleadoCreado.getFechaInicio().compareTo(empleadoCreado.getFechaNac())>=0){ //CASO 1
							Date f1 = empleadoCreado.getFechaInicio();
							empleadoCreado.setFechaInicio(empleadoCreado.getFechaNac());
							empleadoCreado.setFechaNac(f1);
							if(empleadoCreado.getFechaFin()!= null)
								if(empleadoCreado.getFechaInicio().compareTo(empleadoCreado.getFechaFin())>=0)
									empleadoCreado.setFechaFin(null);
						} else 
							if (empleadoCreado.getFechaFin()!= null) 
								if(empleadoCreado.getFechaInicio().compareTo(empleadoCreado.getFechaFin())>=0){ // CASO 2
									Date f1 = empleadoCreado.getFechaInicio();		empleadoCreado.setFechaInicio(empleadoCreado.getFechaFin());
									empleadoCreado.setFechaFin(f1);
								}
				}
	
				//GUARDAMOS EN LA LISTA
				if (!empleadoCreado.comprobarSiNoTieneDatosNull())
					listEmpleados.add(empleadoCreado);
			}					
		}
		try {
			//LLAMAMOS AL METODO listEmpleado PARA ALMACENAR LOS VALORES EN BBDD Y NOS DEVUELVE EL NUMERO DE FILAS INSERTADOS
			lineasInsertadas = listEmpleado(listEmpleados);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
		}
		return lineasInsertadas;
	}


/* ***************************************
	RECORRER EMPLEADOS
*************************************** */	
	/**
	 * Metodo para insertar cada Empleado en base de datos llamando al metodo insertBBDD de la clase BBDD
	 * Además contabilizaremos cuantos son los registros insertados y, seguidamente, descargamos el archivo
	 * CREAR_ARCHIVO_LOG_DESCARGAR.txt con los registros insertados
	 * @param listEmpleados2 Lista de empleado
	 * @return int con el numero de filas insertadas en base de datos
	 */
	public int listEmpleado(List<Empleado> listEmpleados2) {		
		//1. INSERTAMOS EN BBDD
		for (Empleado empleado : listEmpleados2) 
			bbdd.insertBBDD(empleado);
		//2. CONTADOR CON FILAS INSERTADAS
		int lineasInsertadas=listEmpleados2.size(); //lineas insertadas
		System.out.println("TOTAL INSERTADO: "+lineasInsertadas);
		//3.CREAMOS ARCHIVO CREAR_ARCHIVO_LOG_DESCARGAR.txt
		t.crearArchivoParaDescargar(listEmpleados2);	
		return lineasInsertadas;
	}
	
}