package application;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Desktop;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class MainScreenController {

	@FXML
	private Button solveButton, clearButton, xBtn1,xBtn2,xBtn3,xBtn4,imgLoad1,imgLoad2,imgLoad3,imgLoad4,helpBtn;
	@FXML
	private TextField resField, posField1, otherField1,posField2, otherField2,posField3, otherField3,posField4,otherField4;
	@FXML
	private Label clipboardLabel;
	@FXML
	private ImageView img1,img2,img3,img4,imageIcon1,imageIcon2,imageIcon3,imageIcon4;
	@FXML
	private ChoiceBox screenChoiceMenu;
	private Image programIcon;
	private Stage stage;
	private HUDManager hud;
	
	final private String FULLSCREEN = "Fullscreen";
	final private String WINDOWED = "Windowed";
	
	public void LoadThings(Stage stage)
	{				
		clipboardLabel.setVisible(false);
		this.stage = stage;
		
		screenChoiceMenu.getItems().add(FULLSCREEN);
		screenChoiceMenu.getItems().add(WINDOWED);
		screenChoiceMenu.setValue(FULLSCREEN);
		
		
		/*screenChoiceMenu.setOnAction(new EventHandler() {

			@Override
			public void handle(Event arg0) {
				System.out.println(screenChoiceMenu.getValue());
				
			}
			
		});*/
		//System.out.println(screenChoiceMenu.getValue());
		
				
		InputStream strm = null;
        try {
            URL url = getClass().getResource("../resource/image.png"); 
            Image image = null;
            
            if(url!=null) {
	            strm = url.openStream(); 
	            image = new Image(strm);
            }
            else
            {
            	File file = new File("resource/image.png");
            	image = new Image(file.toURI().toString());
            }
            img1.setImage(image);
            img2.setImage(image);
            img3.setImage(image);
            img4.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
				try {
					if(strm!=null)
							strm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
        }
        
        try {
            URL url = getClass().getResource("../resource/imgIcon.png"); 
            Image image = null;
            
            if(url!=null) {
	            strm = url.openStream(); 
	            image = new Image(strm);
            }
            else
            {
            	File file = new File("resource/imgIcon.png");
            	image = new Image(file.toURI().toString());
            }
            imageIcon1.setImage(image);
            imageIcon2.setImage(image);
            imageIcon3.setImage(image);
            imageIcon4.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
			try {
				if(strm!=null)
						strm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        try {
            URL url = getClass().getResource("../resource/appIcon.png"); 
            if(url!=null) {
	            strm = url.openStream(); 
	            programIcon = new Image(strm);
            }
            else
            {
            	File file = new File("resource/appIcon.png");
            	programIcon = new Image(file.toURI().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
			try {
				if(strm!=null)
						strm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
        helpBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					File file = new File("help.txt");
					
					if (Desktop.isDesktopSupported()) {
					    Desktop.getDesktop().edit(file);
					} else {
					    // dunno, up to you to handle this
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					showAlertBox("ERROR!","Error opening help file");
				}
			}
			
		});
        
		solveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				solveIt();
			}
			
		});
		
		clearButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				clearFields();
				//hud.HelloThere();
			}

		});
		
		xBtn1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				posField1.setText("");
				otherField1.setText("");
			}
			
		});
		
		xBtn2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				posField2.setText("");
				otherField2.setText("");
			}
			
		});
		
		xBtn3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				posField3.setText("");
				otherField3.setText("");
			}
			
		});
		
		xBtn4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				posField4.setText("");
				otherField4.setText("");
			}
			
		});
		
		imgLoad1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Image img = Clipboard.getSystemClipboard().getImage();
				if(img==null)
					//otherField1.setText("NO IMG");
					showAlertBox("ERROR!","No screenshot in clipboard!");
				else
					otherField1.setText(String.valueOf(GetOreDistance(img)));
			}
			
		});
		
		imgLoad2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Image img = Clipboard.getSystemClipboard().getImage();
				if(img==null)
					//otherField2.setText("NO IMG");
					showAlertBox("ERROR!","No screenshot in clipboard!");
				else
					otherField2.setText(String.valueOf(GetOreDistance(img)));
			}
			
		});
		
		imgLoad3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Image img = Clipboard.getSystemClipboard().getImage();
				if(img==null)
					//otherField3.setText("NO IMG");
					showAlertBox("ERROR!","No screenshot in clipboard!");
				else
					otherField3.setText(String.valueOf(GetOreDistance(img)));
			}
			
		});
		
		imgLoad4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Image img = Clipboard.getSystemClipboard().getImage();
				if(img==null)
					//otherField4.setText("NO IMG");
					showAlertBox("ERROR!","No screenshot in clipboard!");
				else
					otherField4.setText(String.valueOf(GetOreDistance(img)));
			}
			
		});
		
		//SetUpTray();
		
	}
		
	private void SetUpTray()
	{
		Platform.setImplicitExit(false);
		
		   //Check the SystemTray is supported
	    if (!SystemTray.isSupported()) {
	        System.out.println("SystemTray is not supported");
	        return;
	    }
	    final PopupMenu popup = new PopupMenu();

		URL url = getClass().getResource("../resource/appIcon.png"); 
		java.awt.Image trayImage = Toolkit.getDefaultToolkit().getImage(url);

		if(trayImage == null)
		{
			showAlertBox("ERROR","Tray loading error!");
			return;
		}
		
	    final TrayIcon trayIcon = new TrayIcon(trayImage);
	    trayIcon.setImageAutoSize(true);

	    final SystemTray tray = SystemTray.getSystemTray();

	    // Create a pop-up menu components
	    MenuItem showItem = new MenuItem("Show");
	    //MenuItem name = new MenuItem("Triangulator");
	    //CheckboxMenuItem showHUD = new CheckboxMenuItem("Show HUD");
	    //CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
	    /*Menu displayMenu = new Menu("Display");
	    MenuItem errorItem = new MenuItem("Error");
	    MenuItem warningItem = new MenuItem("Warning");
	    MenuItem infoItem = new MenuItem("Info");
	    MenuItem noneItem = new MenuItem("None");*/
	    MenuItem exitItem = new MenuItem("Exit");

	    //Add components to pop-up menu
	    
	    popup.add("Triangulator");
	    popup.addSeparator();
	    popup.add(showItem);
	    //popup.add(showHUD);
	    popup.addSeparator();
	    //popup.add(cb2);
	    //popup.addSeparator();
	    /*popup.add(displayMenu);
	    displayMenu.add(errorItem);
	    displayMenu.add(warningItem);
	    displayMenu.add(infoItem);
	    displayMenu.add(noneItem);*/
	    popup.add(exitItem);

	    trayIcon.setPopupMenu(popup);

	    try {
	        tray.add(trayIcon);
	    } catch (AWTException e) {
	        System.out.println("TrayIcon could not be added.");
	    }
	    
	    /*showHUD.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("TEST1");
				hud.TestMe();
			}
	    	
	    });*/
	    
	    trayIcon.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1)
					ShowStage();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	    
	    exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent arg0) {
				Platform.exit();
				System.exit(0);
			}
	    	
	    });
	    
	    showItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent arg0) {
				ShowStage();
			}
	    	
	    });
	}
	
	private void ShowStage()
	{
		//TOTO MAYBE ADD CLOSE ON ANOTHER CLICK IF STAGE IS OPEN ?
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				stage.show();
			}
			
		});
	}
	
	//Debug used for getting RGB info
	private void saveRGB(Image image)
	{
		BufferedWriter writer;
		//Logging
		try {
		    writer = new BufferedWriter(
                    new FileWriter("debug.txt", true)  //Set true for append mode
                ); 
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		
		//	1421/580
		int baseHeight = 580;
		int heightTracker = 580;
		int baseWidth = 1421;
		while(heightTracker >234)
		{
			PixelReader pReader = image.getPixelReader();
			Color color = pReader.getColor(baseWidth,heightTracker);
			
			int red1 = (int) (color.getRed()*255);
			int red2 = (int) (color.getBlue()*255);
			int red3 = (int) (color.getGreen()*255);
			
			
			try {
				writer.write(String.valueOf(red1)+"\t"+String.valueOf(red2)+"\t"+String.valueOf(red3)+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			heightTracker--;
		}
		
		
		try {
			writer.write("\n\n\n\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int GetFinalPos(int heightTracker,Image image, PixelReader pReader, int baseWidth)
	{
		
		int count =0;
		while(true) {
			heightTracker--;
			count++;
			Color color = pReader.getColor(baseWidth,heightTracker);
			Color nextColor = pReader.getColor(baseWidth,heightTracker-1);
			
			double blue = color.getBlue()*255 + nextColor.getBlue()*255;
			if(blue < 388)
			{
				System.out.println("Count: "+count);
				return count;
			}
		}
	}
	
	private double GetOreDistance(Image image)
	{
		//16:9
		double ratioX = 2843.0/3840.0;
		double ratioY = 1163.0/2160.0;
		
		double height;
		int baseHeight,baseWidth,heightTracker;
		
		
		if(screenChoiceMenu.getValue().equals(WINDOWED))
		{
			height = image.getHeight()-32;
			baseHeight = (int)(ratioY * height) +31;
			baseWidth = (int)Math.round(ratioX * (image.getWidth()-2))+1;
			System.out.println("WINDOWED MODE!");
		}
		else
		{
			height = image.getHeight();
			baseHeight = (int)(ratioY * height);
			baseWidth = (int)Math.round(ratioX * image.getWidth());
		}
		heightTracker = baseHeight;
		
		while(heightTracker >1)
		{	
			PixelReader pReader = image.getPixelReader();
			Color color = pReader.getColor(baseWidth,heightTracker);
			Color nextColor = pReader.getColor(baseWidth,heightTracker-1);
			
			double blue = color.getBlue()*255 + nextColor.getBlue()*255;
			if(blue > 388)
			{
				int count = GetFinalPos(heightTracker,image,pReader,baseWidth);
				
				if(count >3) {
					heightTracker -= count/2;
					break;
				}
			}
			
			heightTracker--;
		}
		
		int finalHeight = baseHeight - heightTracker;
		double finalAmount = 0;
		double stepper = 172.0/2160.0 * height;
		finalAmount = (100.0/stepper) * finalHeight;
		
		System.out.println("finalAmount "+finalAmount+"\n");
		System.out.println("finalHeight "+finalHeight +"\n");
		System.out.println("baseHeight "+baseHeight +"\n");
		System.out.println("baseWidth"+baseWidth+"\n");
		System.out.println("height "+height +"\n");
		System.out.println("ratioY "+ratioY+"\n");
		/*double stepper = 172/2160 * height;
		//100
		if(finalHeight > stepper) {
			finalHeight -= 85;
			finalAmount +=100;
		}
		else
		{
			finalAmount += (100.0/85.0) * finalHeight;
			finalHeight = 0;
		}
		
		//200
		stepper = 173/2160*height;
		if(finalHeight > 89) {
			finalHeight -= 89;
			finalAmount +=100;
		}
		else
		{
			finalAmount += (100.0/89.0) * finalHeight;
			finalHeight = 0;
		}
		
		//300
		stepper = 172/2160*height;
		if(finalHeight > 87) {
			finalHeight -= 87;
			finalAmount +=100;
		}
		else
		{
			finalAmount += (100.0/87.0) * finalHeight;
			finalHeight = 0;
		}
		
		//400+
		finalAmount += (100.0/stepper) * finalHeight;*/
		
		
		if(finalAmount < 10)
		{
			showAlertBox("ERROR!","Scanner lines not found! Check instructions!");
			finalAmount = 0;
		}
		
		return finalAmount;		
	}
		
	private void solveIt()
	{
	
		Solver solver = new Solver();
		
		List<String> varList = new ArrayList<String>();
		varList.add(posField1.getText());
		varList.add(otherField1.getText());
		varList.add(posField2.getText());
		varList.add(otherField2.getText());
		varList.add(posField3.getText());
		varList.add(otherField3.getText());
		varList.add(posField4.getText());
		varList.add(otherField4.getText());
		
		solver.startSolve(varList,this);
	}
	
	private void clearFields()
	{
		posField1.setText("");
		posField2.setText("");
		posField3.setText("");
		posField4.setText("");
		otherField1.setText("");
		otherField2.setText("");
		otherField3.setText("");
		otherField4.setText("");
		resField.setText("");
		clipboardLabel.setVisible(false);
	}
	
	public void setResult(String text)
	{
		resField.setText(text);
	}
	
	public void hideClipMsg()
	{
		clipboardLabel.setVisible(false);
	}
	
	public void showClipMsg()
	{
		clipboardLabel.setVisible(true);
	}
	
	public void setHUDManager(HUDManager hud)
	{
		this.hud = hud;
	}
	
	//Vytvara chybove hlasky pri prihlasovani
	public void showAlertBox(String header,String problem)
	{
		Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(header);
            alert.setHeaderText(problem);
            
            if(programIcon!=null)
            	((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(programIcon);
            
            alert.showAndWait();
        }
		);
	}
	
}
