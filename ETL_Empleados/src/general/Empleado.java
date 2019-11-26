package general;

import java.sql.Date;

/**
 * Clase para declarar los atributos que debe tener un objeto Empleado
 * @author Beatriz Matesanz
 */
public class Empleado {

		public String[] cabeceraBBDD;
	// EMPLEADO
		public String nombre;
		public String genero;
		public Date fechaNac;
	// SALARIOS
		public Integer salario;
	// DEPARTAMENTO
		public String departamento;	
	// LOCALIDAD
		public String ciudad;
		public String cp;
		public Double longitud;
		public Double latitud;
	// HISTORIAL_DEPARTAMENTO
		public Date fechaInicio;
		public Date fechaFin;
	
	/**
	 * Metodo constructor donde pasaremos por paramentros valores para rellenar todos los atributos
	 * @param nombre Nombre del empleado 
	 * @param genero Genero del empleado
	 * @param fechaNac Fecha de nacimiento del empleado
	 * @param salario Salario del empleado
	 * @param departamento Departamento asociado 
	 * @param ciudad Ciudad del empleado
	 * @param cp Codigo postal de la ciudad
	 * @param longitud Logitud geografica de la ciudad
	 * @param latitud Latitud geografica de la ciudad
	 * @param fechaInicio Fecha de inicio en el departamento
	 * @param fechaFin Fecha de fin en el departamento
	 */
	public Empleado(String nombre, String genero, Date fechaNac, Integer salario, String departamento,
				String ciudad, String cp, double longitud, double latitud, Date fechaInicio, Date fechaFin) {
	
			this.nombre = nombre;
			this.genero = genero;
			this.fechaNac = fechaNac;
			this.salario = salario;
			this.departamento = departamento;
			this.ciudad = ciudad;
			this.cp = cp;
			this.longitud = longitud;
			this.latitud = latitud;
			this.fechaInicio = fechaInicio;
			this.fechaFin = fechaFin;
		}

	/**
	 * Metodo constructor por defecto sin paramentros que no realiza ninguna funcion
	 **/
	public Empleado() {

	}

	/**
	 * Metodo constructor
	 * @return Array de String con los nombres de los atributos del objeto Empleado 
	 **/
	public String[] cabeceraBBDD() {
		String[] lista = {"NOMBRE" ,"GENERO","NAC","SALARIO" ,"DEPARTAMENTO","CIUDAD" ,"CP"	,"LONGITUD","LATITUD" ,"INICIO" ,"FIN"};
		return lista;
	}

	/**
	 * Metodo sobrescrito para ver en forma de String los diversos datos de un empleado
	 * @return Cadena de texto con los valores de los atributos del empleado
	 */
	@Override
	public String toString() {
		return "Empleado [nombreCompleto=" + nombre + ", genero=" + genero + ", fechaNac=" + fechaNac
				+ ", salario=" + salario + ", departamento=" + departamento + ", ciudad=" + ciudad + ", cp=" + cp
				+ ", longitud=" + longitud + ", latitud=" + latitud + ", fechaInicio=" + fechaInicio + ", fechaFin="
				+ fechaFin + "]";
	}

	/**
	 * Metodo para ver cada atributo en una linea distinta y devolverlo en un forma de String 
	 * @return Cadena de texto con los valores formateados de los atributos del empleado
	 */
	public String toStringFormateado() {
		return "nombreCompleto=" + nombre + "\n genero=" + genero + "\n fechaNac=" + fechaNac
				+ "\n salario=" + salario + "\n departamento=" + departamento + "\n ciudad=" + ciudad + "\n cp=" + cp
				+ "\n longitud=" + longitud + "\n latitud=" + latitud + "\n fechaInicio=" + fechaInicio + "\n fechaFin="
				+ fechaFin;
	}

	/**
	 * Metodo para ver cada atributo separado por el valor declarado por parametro y devolverlo en un forma de String 
	 * Empleado para el fichero que descargamos con nombre CREAR_ARCHIVO_LOG_DESCARGAR.txt con un separado ";" 
	 * Usado junto al metodo toStringCabeceraParaDescargar con el cual obtenemos las cabeceras
	 * @param separador Separador de los diferentes valores de los atributos
	 * @return Cadena de texto con los valores formateados de los atributos del empleado
	 */
	public String toStringParaDescargar(String separador) {
		return  nombre+separador+ genero+separador+fechaNac+separador+salario +separador+departamento+separador+ciudad+separador+cp
				+separador+ longitud +separador+ latitud +separador+ fechaInicio+separador+ fechaFin+"\n";
	}
		
	/**
	 * Metodo para ver cada encabezado de atributo separado por el valor declarado por parametro y devolverlo en un forma de String 
	 * Empleado para el encabezado del fichero que descargamos con nombre CREAR_ARCHIVO_LOG_DESCARGAR.txt con un separado ";" 
	 * Usado junto al metodo toStringParaDescargar con el que obtenemos los valores de cada atributo para cada empleado
	 * @param separador Separador de los diferentes valores de los atributos
	 * @return Cadena de texto con los valores formateados de los encabezado del empleado
	 */
	public String toStringCabeceraParaDescargar(String separador) {
		return "NombreCompleto"+separador+"Genero" + separador + "FechaNacimiento" + separador
			+ "Salario" + separador + "Departamento" + separador + "Ciudad" + separador + "CP" + separador
			+ "Longitud" + separador+ "Latitud" + separador + "FechaInicio" + separador + "FechaFin\n";						
	}
	
	/**
	 * Metodo para verificar si algunos de los atributos tiene un valor null o por defecto
	 * @return boolean con el valor de esta comprobacion 
	 */

	public boolean comprobarSiNoTieneDatosNull() {			
		if ( nombre instanceof Object) 
			return false;			
		return true ;
	}	
	
	/* ************ METODOS GETTER Y SETTER ************ */

	/**
	 * Metodo para obtener el valor del nombre. En caso de tener más de 150 caracteres cortaremos este valor
	 * @return cadena con el nombre
	 */
	public String getNombre() {
		if(nombre != null) {
			if(nombre.length()>150) nombre = nombre.substring(0, 150);
		}
		else nombre = "Sin nombre";
		return nombre;
	}

	/**
	 * Metodo para obtener el valor del genero. En caso de tener más de 10 caracteres cortaremos este valor
	 * @return cadena con el genero
	 */
	public String getGenero() {
		if(genero != null)
			if(genero.length()>10) genero = genero.substring(0, 10);
		return genero;
	}

	/**
	 * Metodo para obtener el valor de la fecha nacimiento. 
	 * @return cadena con la fecha nacimiento
	 */
	public Date getFechaNac() {
		return fechaNac;
	}

	/**
	 * Metodo para obtener el valor del Salario.
	 * @return cadena con el Salario
	 */
	public Integer getSalario() {
		return salario;
	}

	/**
	 * Metodo para obtener el valor del nombre. En caso de tener más de 150 caracteres cortaremos este valor
	 * @return cadena con el nombre
	 */
	public String getDepartamento() {
		if(departamento != null)
			if(departamento.length()>150) departamento=departamento.substring(0, 150);
		return departamento;
	}

	/**
	 * Metodo para obtener el valor de la ciudad. En caso de tener más de 150 caracteres cortaremos este valor
	 * @return cadena con la ciudad
	 */
	public String getCiudad() {
		if(ciudad != null)
			if(ciudad.length()>150) ciudad = ciudad.substring(0, 150);
		return ciudad;
	}

	/**
	 * Metodo para obtener el valor del codigo postal. En caso de tener más de 5 caracteres cortaremos este valor
	 * @return cadena con el codigo postal
	 */
	public String getCp() {
		if(cp != null) 
			if(cp.length()>5) cp = cp.substring(0, 5);
		return cp; 
	}

	/**
	 * Metodo para obtener el valor de la longitud.
	 * @return cadena con la longitud
	 */
	public double getLongitud() {
		return longitud;
	}

	/**
	 * Metodo para obtener el valor de la latitud.
	 * @return cadena con la latitud
	 */
	public double getLatitud() {
		return latitud;
	}

	/**
	 * Metodo para obtener el valor de la fecha de inicio.
	 * @return cadena con la fecha de inicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * Metodo para obtener el valor de la fecha de fin.
	 * @return cadena con la fecha de fin
	 */
	public Date getFechaFin() {
		return fechaFin;
	}
	
	/**
	 * Metodo para modificar la fecha de nacimiento 
	 * @param fechaNac valor al cual actualizar
	 */
	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}
	
	/**
	 * Metodo para modificar la fecha de inicio 
	 * @param fechaInicio valor al cual actualizar
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	/**
	 * Metodo para modificar la fecha de fin 
	 * @param fechaFin valor al cual actualizar
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
}
