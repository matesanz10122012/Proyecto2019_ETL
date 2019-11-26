package general;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Clase que contiene la conexion a base de datos y la llamada al procedimiento almacenado para insertar los datos.
 * @author Beatriz Matesanz
 *
 */

public class BBDD {
	
	public Connection conexion;
	
	//VALORES DE NUESTRA CONEXION A BBDD
    public String driver = "com.mysql.jdbc.Driver";// Librería de MySQL
    public String database = "empleadosetl"; //Nombre de la base de datos
    public String hostname = "localhost"; //Nombre del host
    public String port = "3307"; //Puerto    
    public String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false"; // Ruta de nuestra base de datos (desactivamos el uso de SSL con "?useSSL=false")
    public String username = "UsuarioETL"; //Nombre de usuario 
    public String password = "administrator"; //Contraseña
    
    
	/**
	 * Metodo para establecer la conexion hacia base de datos y almacenarla en una variable tipo Connection
	 */
	public void ConectarBBDD() {	   
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(url, username, password);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
		}
	}
	
	/**
	 * Metodo que realiza una llamada al procedimiento almacenado con los datos de cada empleado 
	 * @param empleado Pasaremos un objeto de tipo Empleado
	 */
	public void insertBBDD(Empleado empleado){
		ConectarBBDD(); // Establecemos un valor para la variable de tipo Connection
		CallableStatement sp;
		try {
			sp = conexion.prepareCall(" CALL insertBBDD(?,?,?,?,?,?,?,?,?,?,?)");
			
			// PARAMETRO DE ENTRADA
			if (empleado.getNombre()!=null)	sp.setString("nombre",  StringUtils.stripAccents(empleado.getNombre()).replace("?",""));
				else sp.setString("nombre",null);
			if (empleado.getGenero()!=null)	sp.setString("genero",  StringUtils.stripAccents(empleado.getGenero()).replace("?",""));
				else sp.setString("genero",null);
			sp.setDate("fechaNac",      empleado.getFechaNac());
			sp.setInt("salario",      	empleado.getSalario());
			if (empleado.getDepartamento()!=null)	sp.setString("departamento",  StringUtils.stripAccents(empleado.getDepartamento()).replace("?",""));
				else sp.setString("departamento",null);
			if (empleado.getCiudad()!=null)	sp.setString("ciudad",  StringUtils.stripAccents(empleado.getCiudad()).replace("?",""));
				else sp.setString("ciudad",null);
			if (empleado.getCp()!=null)	sp.setString("cp",  StringUtils.stripAccents(empleado.getCp()).replace("?",""));
				else sp.setString("cp", null);
			sp.setDouble("longitud",    empleado.getLongitud());
			sp.setDouble("latitud",     empleado.getLatitud());
			sp.setDate("fechaInicio",   empleado.getFechaInicio());
			sp.setDate("fechaFin",      empleado.getFechaFin());

			sp.execute();  
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
			System.out.println(ExceptionUtils.getStackTrace(e));
		}finally{
			if(conexion!=null)
				try {
					conexion.close(); //Cerramos la conexion para evitar tener abiertas multiples sesiones
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null,ExceptionUtils.getStackTrace(e), "ERROR", JOptionPane.ERROR_MESSAGE);	
				}
		}
	}
}
