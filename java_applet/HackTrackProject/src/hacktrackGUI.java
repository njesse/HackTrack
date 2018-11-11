import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Font;
import java.awt.Frame;


import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JScrollBar;

public class hacktrackGUI implements ActionListener{

	private JFrame frame;
	private RestInterface rest;
	private JButton btnTrainMode = new JButton("Train Mode");
	private JButton btnMultiMode = new JButton("Multi Mode");
	private JLabel lblStarttime = new JLabel(" ");
	private JLabel lblEndtime = new JLabel(" ");
	private JLabel lblAccuracy = new JLabel(" ");
	private JButton btnDetails = new JButton("Details");
	private ResultAnalytic result;
	
	

	/**
	 * Create the application.
	 */
	
	public hacktrackGUI() {	
		initialise();
		chooseFile();
		showChooseModelView();
		frame.setVisible(true);
	}
	/**
	 * Message box method.
	 */
	public class infoboxmessage
	{

	    public void infoBox(String infoMessage, String titleBar)
	    {
	        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	    }
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialise() {
		
		// First init the frame itself, then the components
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// Labels used for displaying start and endtime of trip with train
		// aswell as the resulting accurancy 
		lblStarttime.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblStarttime);
		lblEndtime.setHorizontalAlignment(SwingConstants.CENTER);		
		frame.getContentPane().add(lblEndtime);
		lblAccuracy.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblAccuracy.setHorizontalAlignment(SwingConstants.CENTER);		
		frame.getContentPane().add(lblAccuracy);
		
		//Button to show more detailed result 
		btnDetails.setBounds(229, 344, 115, 29);
		frame.getContentPane().add(btnDetails);
		btnDetails.setVisible(false);
		btnDetails.addActionListener(this);			
		
		// init the restInterface, its global because its used 
		// for transmitting and receiving 
		//
		rest = new RestInterface();
	}
	
	
	@SuppressWarnings("deprecation")
	private void chooseFile() {		
		
		
		infoboxmessage infobox = new infoboxmessage();
		
		infobox.infoBox("To start, please press ok and select data file", "HackTrack - SilverRail Tool");
		
		// Read data file that will be used...
		
		int arrlen = 10000;
		byte[] infile = new byte[arrlen];
		Frame parent = new Frame();
		FileDialog fd = new FileDialog(parent, "Please choose a data file:",
		           FileDialog.LOAD);
		fd.show();
		String selectedItem = fd.getFile();
		if (selectedItem == null) {
			// no file selected, 
			System.exit(-1);
		} else {
			File ffile = new File( fd.getDirectory() + File.separator +
			        fd.getFile());
			// read the file
			System.out.println("reading file " + fd.getDirectory() +
			                         File.separator + fd.getFile() );
			try {
				FileInputStream fis = new FileInputStream(ffile); 
				BufferedInputStream bis = new BufferedInputStream(fis);
				@SuppressWarnings("resource")
				DataInputStream dis = new DataInputStream(bis);
				try {
					int filelength = dis.read(infile);
					String filestring = new String(infile, 0, filelength);
				//	System.out.println("FILE CONTENT=" + filestring);
					rest.readFileContent(filestring);
				} catch(IOException iox) {
					System.out.println("File read error...");
					iox.printStackTrace();
					infobox.infoBox("File read error", "HackTrack - SilverRail Tool");
					
				}
			} catch (FileNotFoundException fnf) {
				System.out.println("File not found...");
				fnf.printStackTrace();
				infobox.infoBox("File not found", "HackTrack - SilverRail Tool");
			}
			
				
			infobox.infoBox("File read succesfully: Select the Model you wish to use to analyse your data", "HackTrack - SilverRail Tool");
			
				
		}	
	
	}
	
	private void showChooseModelView() {
		// Select Mode that will be used for data analysis - two options, multi modal mode or train mode
		
		
		btnTrainMode = new JButton("Train Mode");
		btnTrainMode.setBounds(74, 6, 117, 29);
		frame.getContentPane().add(btnTrainMode);
		btnTrainMode.addActionListener(this);
		btnMultiMode = new JButton("Multi Mode");
		btnMultiMode.setBounds(240, 6, 117, 29);
		frame.getContentPane().add(btnMultiMode);
		btnMultiMode.addActionListener(this);
	}
		
	private void showLegendForResultGraph()
	{
		for (int i=0; i<=255 ; i++) {
			int width = 2;
			JTextField textField = new JTextField() {
			    @Override public void setBorder(Border border) {
			        // No!
			    }
			};
			textField.setEnabled(false);
			textField.setEditable(false);
			textField.setBackground(new Color(255,255-i,255-i));
			if ( i == 0) {
				textField.setBounds(31, 430, width, 42);
			} else {
				textField.setBounds(28+i*width, 430, width, 42);	
			}
			frame.getContentPane().add(textField);
			textField.setColumns(10);
			}
		for (int i=0; i<=255 ; i++) {
			int width = 2;
			JTextField textField = new JTextField() {
			    @Override public void setBorder(Border border) {
			        // No!
			    }
			};
			textField.setEnabled(false);
			textField.setEditable(false);
			textField.setBackground(new Color(255-i,255,255-i));
			if ( i == 0) {
				textField.setBounds(31, 520, width, 42);
			} else {
				textField.setBounds(28+i*width, 520, width, 42);	
			}
			frame.getContentPane().add(textField);
			textField.setColumns(10);
			}
		
		for (int i=0; i<=255 ; i++) {
			int width = 2;
			JTextField textField = new JTextField() {
			    @Override public void setBorder(Border border) {
			        // No!
			    }
			};
			textField.setEnabled(false);
			textField.setEditable(false);
			textField.setBackground(new Color(255-i,255-i,255));
			if ( i == 0) {
				textField.setBounds(31, 600, width, 42);
			} else {
				textField.setBounds(28+i*width, 600, width, 42);	
			}
			frame.getContentPane().add(textField);
			textField.setColumns(10);
			}
		JLabel lblLowAccuracy = new JLabel("Low Accuracy");
		lblLowAccuracy.setBounds(180, 400, 120, 20);
		frame.getContentPane().add(lblLowAccuracy);
		
		JLabel label = new JLabel("High Accuracy");
		label.setBounds(419, 400, 120, 20);
		frame.getContentPane().add(label);		
		
		
		JLabel lblTrain = new JLabel("Train");
		lblTrain.setBounds(42, 400, 69, 20);
		frame.getContentPane().add(lblTrain);
		
		JLabel lblWalk = new JLabel("Walk");
		lblWalk.setBounds(42, 490, 69, 20);
		frame.getContentPane().add(lblWalk);
		
		JLabel lblBusCar = new JLabel("Bus / Car");
		lblBusCar.setBounds(42, 570, 69, 20);
		frame.getContentPane().add(lblBusCar);
		
		
	frame.repaint();
	}
	
	private void showResult(String restResult) {
		btnTrainMode.setVisible(false);
		btnMultiMode.setVisible(false);
		btnDetails.setVisible(true);
		showLegendForResultGraph();
		
		result = new ResultAnalytic(restResult);
		
		for (int i=0; i<100 ; i++) {
				int width = 5;
				JTextField textField = new JTextField() {
				    @Override public void setBorder(Border border) {
				        // No!
				    }
				};
				textField.setEnabled(false);
				textField.setEditable(false);
				textField.setBackground(result.getColorAtPercent(i));
				if ( i == 0) {
					textField.setBounds(31, 86, width, 42);
				} else {
					textField.setBounds(28+i*width, 86, width, 42);	
				}
				frame.getContentPane().add(textField);
				textField.setColumns(10);
				}
		
		lblStarttime.setText("Start: "+ result.getStarttime());
		lblEndtime.setText(" End: "+ result.getEndtime());
		
		int positionStart = 15+ 45*(int) (Math.floor((result.getStartIndex()/10-1)));
		lblStarttime.setBounds( positionStart,50, 200, 20);
		
		int positionEnd = 15+ 45*(int) (Math.floor((result.getEndIndex()/10-1)));
		lblEndtime.setBounds( positionEnd,150, 200, 20);
		
		lblAccuracy.setText("Best accuracy achieved: "+ result.getBestAccuracy());		
		lblAccuracy.setBounds(50,150, 500, 200);
		frame.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	    String restResult ="";
		if (arg0.getActionCommand().equals("Train Mode"))
		{
			rest.setModel("tm");
			restResult = rest.sendValues();
		//	restResult =rest.dontGetValues(); //Use self-generated test data
			if (restResult.length()>0) {
				showResult(restResult);
			}
			
		}
		else if(arg0.getActionCommand().equals("Multi Mode"))
		{
			rest.setModel("mm");
	//	restResult =rest.dontGetValues(); //Use self-generated test data
			restResult = rest.sendValues();
			if (restResult.length()>0) {
				showResult(restResult);
			}
		}
		else if(arg0.getActionCommand().equals("Details"))
		{
			detailsGUI details = new detailsGUI(result);
			details.show();
		}
		
		
	}
}




