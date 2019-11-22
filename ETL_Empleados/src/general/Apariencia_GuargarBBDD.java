package general;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Clase que muestra el total de filas insertado en base de datos. A continuación, mostrara una pestaña con los botones  
 * para ir a la pantalla inicial, cerrar la aplicacion o para descargar un fichero con los datos insertados
 * @author Beatriz Matesanz
 *
 */
public class Apariencia_GuargarBBDD extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	JButton button0;
	JButton button1;
	JButton button2;
	JPanel p=new JPanel();
	String rutaFichero = "";
	JFrame frame = new JFrame("ETL para la BBDD EMPLEADOS");
	
	/**
	 * Clase que muestra un mensaje con el numero de filas que hemos insertado en base de datos
	 * @param lineasInsertadas Numero de filas que hemos insertado en base de datos
	 */
	public Apariencia_GuargarBBDD(int lineasInsertadas) {
		super(new BorderLayout());
        JOptionPane.showMessageDialog(p,"Guardado en BBDD correctamente. \nTotal de registros insertados: " + Integer.toString(lineasInsertadas), "INFORMACION", JOptionPane.INFORMATION_MESSAGE);

		//CALCULAMOS EL CENTRO DE LA PANTALLA 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (screenSize.height)/2;
		int width = (screenSize.width)/2;
		
		//ESTABLECEMOS LOS 3 BOTONES
		button0 = new JButton("Descargar LOG");
		button0.setPreferredSize(new Dimension(width/4, height/2));
		p.add(button0, BorderLayout.WEST);
		button0.addActionListener(this); 
		
		button1 = new JButton("Pantalla inicial");
		button1.setPreferredSize(new Dimension(width/4, height/2));
		p.add(button1, BorderLayout.CENTER);
		button1.addActionListener(this); 

		button2 = new JButton("Cerrar");
		button2.setPreferredSize(new Dimension(width/4, height/2));
		p.add(button2, BorderLayout.EAST);
		button2.addActionListener(this); 
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(p);
		frame.pack();		

		//CENTRAMOS EL JFRAME EN MITAD DE LA PANTALLA 
		frame.setSize(width-(width/5), height-(height/3));		 
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);					
	}

	/**
	 * Metodo invocado cuando se pulsa alguno de los 3 botones (Descargar log, Pantalla principal y Salir)
	 * @param e Fuente que provoca el evento 
	 */
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == button0) { //BOTON: Descargar log --> Clase Apariencia_Descargar 
			frame.setVisible(false);
			new Apariencia_Descargar();
		}
		else if (e.getSource() == button1) { //BOTON: Pantalla inicial --> Clase Apariencia_InformacionGeneral
			frame.setVisible(false);
			new Apariencia_InformacionGeneral("Acceder a la ETL para la BBDD EMPLEADOS",1);
		}
		else if (e.getSource() == button2) //BOTON: Cerrar --> Salir de la aplicacion
			System.exit(0);
	}

}