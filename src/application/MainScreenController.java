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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
	
	private Image programIcon;
	private Stage stage;
	private HUDManager hud;
	
	public void LoadThings(Stage stage)
	{		
		clipboardLabel.setVisible(false);
		this.stage = stage;
				
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
		
		SetUpTray();
		
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
	
	private double GetOreDistance(Image image)
	{
		//	1421/580
		int baseHeight = 580;
		int heightTracker = 580;
		int baseWidth = 1421;
		while(heightTracker >1)
		{
			PixelReader pReader = image.getPixelReader();
			Color color = pReader.getColor(baseWidth,heightTracker);
			Color nextColor = pReader.getColor(baseWidth,heightTracker-1);
			
			double red1 = color.getRed()*255;
			double red2 = nextColor.getRed()*255;
			
			if((red1 >=147 || red1 <=85) && (red2 >=147 || red2 <=85))
			{
				
				heightTracker -=7;
				break;
			}
			
			heightTracker--;
		}
		
		int finalHeight = baseHeight - heightTracker;
		double finalAmount = 0;
		
		//100
		if(finalHeight > 85) {
			finalHeight -= 85;
			finalAmount +=100;
		}
		else
		{
			finalAmount += (100.0/85.0) * finalHeight;
			finalHeight = 0;
		}
		//200
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
		finalAmount += (100.0/86.0) * finalHeight;
		
		
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
