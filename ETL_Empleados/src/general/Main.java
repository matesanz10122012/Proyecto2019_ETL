package general;

import javax.swing.SwingUtilities;

public class Main {

	/**
	 * Metodo principal que abre un Hilo hacia la clase Apariencia_InformacionGeneral para mostrarnos
	 * la pantalla principal
	 * @param args Parametro no usado
	 */
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Apariencia_InformacionGeneral("Acceder a la ETL para la BBDD EMPLEADOS",1);
			}
		});
	}
}
