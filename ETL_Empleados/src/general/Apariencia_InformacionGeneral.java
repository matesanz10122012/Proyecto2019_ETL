package general;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Clase que establece tanto el diseño de una pantalla con uno o dos botones como su correspondiente comportamiento al desencadenar 
 * el evento al pulsar dichos botones
 * @author Beatriz Matesanz
 *
 */
public class Apariencia_InformacionGeneral extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	JButton button_PedirFichero1;
	JButton button_PedirFichero2;
	JPanel p=new JPanel();
	
	String mensajeButon1 = "";
	String mensajeButon2 = "";
	JFrame frame = new JFrame("ETL para la BBDD EMPLEADOS");
	int tipoEvento;

	/**
	 * Metodo para crear un panel centrado en pantalla con un boton 
	 * @param mensajeButon Texto a aplicar al boton 
	 * @param tipoEvento Numero que nos permite generalizar la clase para reutilizar en varias pantallas
	 */
	public Apariencia_InformacionGeneral(String mensajeButon, int tipoEvento) {
		super(new BorderLayout());
		this.tipoEvento=tipoEvento;
		this.mensajeButon1=mensajeButon;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//CALCULAMOS EL CENTRO DE LA PANTALLA 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		int height = (screenSize.height)/2;
		int width = (screenSize.width)/2;
		
		button_PedirFichero1 = new JButton(mensajeButon);
		button_PedirFichero1.setPreferredSize(new Dimension(width, height));
		frame.add(button_PedirFichero1, BorderLayout.CENTER);
		button_PedirFichero1.addActionListener(this); 

		frame.pack();
		//CENTRAMOS EL JFRAME EN MITAD DE LA PANTALLA 
		frame.setSize(width, height);		 
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
	}
	
	/**
	 * Metodo para crear un panel centrado en pantalla con dos botones 
	 * @param mensajeButon1 Texto a aplicar al primer boton 
	 * @param mensajeButon2 Texto a aplicar al segundo boton 
	 * @param tipoEvento Numero que nos permite generalizar la clase para reutilizar en varias pantallas
	 */
	public Apariencia_InformacionGeneral(String mensajeButon1,String mensajeButon2, int tipoEvento) {
		super(new BorderLayout());
		this.tipoEvento=tipoEvento;
		this.mensajeButon1=mensajeButon1;
		this.mensajeButon2=mensajeButon2;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//CALCULAMOS EL CENTRO DE LA PANTALLA 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		int height = (screenSize.height)/2;
		int width = (screenSize.width)/2;
		
		//PRIMER BOTON
		button_PedirFichero1 = new JButton(mensajeButon1);
		button_PedirFichero1.setPreferredSize(new Dimension(height/2, width/3));
		frame.add(button_PedirFichero1, BorderLayout.WEST);
		button_PedirFichero1.addActionListener(this); 
		
		//SEGUNDO BOTON
		button_PedirFichero2 = new JButton(mensajeButon2);
		button_PedirFichero2.setPreferredSize(new Dimension(height/2, width/3));
		frame.add(button_PedirFichero2, BorderLayout.EAST);
		button_PedirFichero2.addActionListener(this); 
		
		//CENTRAMOS EL JFRAME EN MITAD DE LA PANTALLA 
		frame.setSize(width-(width/3), height);		 
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Metodo invocado cuando se pulsa alguno de los botones creados, seguiremos uno o otro flujo 
	 * dependiendo del numero pasado por parametro y almacenado en la variable tipoEvento
	 * @param e Fuente que provoca el evento 
	 */
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == button_PedirFichero1) { //SI PULSAMOS EL PRIMER BOTON
			switch (tipoEvento){
				case 1: // PEDIMOS EL ARCHIVO A CARGAR --> Clase Apariencia_PedirFichero					
					frame.setVisible(false);
					new Apariencia_PedirFichero ();
					break;
				case 2: //Pantalla inicial --> Clase Apariencia_InformacionGeneral
					new Apariencia_InformacionGeneral ("Acceder a la ETL para la BBDD EMPLEADOS",1);				
					break;
			}
		}
		else if (e.getSource() == button_PedirFichero2) { //SI PULSAMOS EL SEGUNDO BOTON (POR EJEMPLO:"Descargar en otra ruta")
			switch (tipoEvento){
				case 2:  //"Descargar en otra ruta" --> Clase Apariencia_Descargar
					frame.setVisible(false);
					new Apariencia_Descargar();
				break;
			}
		}
	}
}