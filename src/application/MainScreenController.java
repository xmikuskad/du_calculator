package application;


import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;


public class MainScreenController {

	//xBtns are row clear btns and imgLoad is clipboard img load btn
	@FXML
	private Button solveButton, clearButton, xBtn1,xBtn2,xBtn3,xBtn4,imgLoad1,imgLoad2,imgLoad3,imgLoad4,helpBtn;
	//posFields are positions and otherFields are ore distances
	@FXML
	private TextField resField, posField1, otherField1,posField2, otherField2,posField3, otherField3,posField4,otherField4;
	@FXML
	private Label clipboardLabel;
	//imageIcon is clipboard and img is X button
	@FXML
	private ImageView img1,img2,img3,img4,imageIcon1,imageIcon2,imageIcon3,imageIcon4;
	//This is display mode choice menu
	@FXML
	private ChoiceBox<String> screenChoiceMenu;
	private Image programIcon;
	
	final private String FULLSCREEN = "Fullscreen";
	final private String WINDOWED = "Windowed";
	
	ImageProcessing imgProcessing;
	
	//For easier setup
	private Button[] clearBtns,imgLoadBtns;
	private TextField[] posFields,otherFields;
	
	
	public void LoadThings()
	{				
		//Set references
		imgProcessing = new ImageProcessing(this);
		
		clearBtns = new Button[] {xBtn1,xBtn2,xBtn3,xBtn4};
		imgLoadBtns = new Button[] {imgLoad1,imgLoad2,imgLoad3,imgLoad4};
		posFields = new TextField[] {posField1,posField2,posField3,posField4};
		otherFields = new TextField[] {otherField1,otherField2,otherField3,otherField4};
		
		//UI setup
		clipboardLabel.setVisible(false);
		
		//Set up display mode checkbox choices
		screenChoiceMenu.getItems().add(FULLSCREEN);
		screenChoiceMenu.getItems().add(WINDOWED);
		screenChoiceMenu.setValue(FULLSCREEN);
			
		//Loading img icons of X buttons
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
        
        
        //Loading img icons of clipboard buttons
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
        
        //Load icon of application
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
		
        //Open a help.txt file after clicking help button
        helpBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					File file = new File("help.txt");
					
					if (Desktop.isDesktopSupported()) 
					    Desktop.getDesktop().edit(file);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					showAlertBox("ERROR!","Error opening help file");
				}
			}
			
		});
        
        //Calculate ore distance
		solveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				solveIt();
			}
			
		});
		
		//This will clear all field
		clearButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				clearFields();
			}

		});

		
		//Setting up clear btns (X) functionality
		for(int i=0; i<clearBtns.length;i++)
		{
			final int tmp = i; //Because of enclosed scope I cant use variable i directly
			clearBtns[i].setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					//Clear fields
					posFields[tmp].setText("");
					otherFields[tmp].setText("");
				}
				
			});
		}
		
		//Set up copy image from clipboard functionality
		for(int i=0; i<imgLoadBtns.length;i++)
		{
			final int tmp = i; //Because of enclosed scope I cant use variable i directly
			imgLoadBtns[i].setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					//Get image from clipboard
					Image img = Clipboard.getSystemClipboard().getImage();
					if(img==null)
						showAlertBox("ERROR!","No screenshot in clipboard!");
					else{
						//Calculate distance of ore and display it
						double oreDistance = imgProcessing.GetOreDistance(img,screenChoiceMenu.getValue().equals(WINDOWED));
						otherFields[tmp].setText(String.valueOf(oreDistance));
					}
				}
				
			});
		}
		
	}
		
	//Start calculations of ore
	private void solveIt()
	{
		Solver solver = new Solver();
		
		List<String> varList = new ArrayList<String>();
		
		//Getting info from textfields to list
		for(int i=0; i<posFields.length;i++)
		{
			varList.add(posFields[i].getText());
			varList.add(otherFields[i].getText());
		}
		
		//Calculate final position
		solver.startSolve(varList,this);
	}
	
	//Clear all text fields
	private void clearFields()
	{		
		for(int i=0; i<posFields.length;i++)
		{
			posFields[i].setText("");
			otherFields[i].setText("");

		}		
		
		resField.setText("");
		setClipMsg(false);
	}
	
	//Show result of ore calculation in result textfield
	public void setResult(String text)
	{
		resField.setText(text);
	}
	
	//Showing the message that distance was copied to clipboard
	public void setClipMsg(boolean shouldShow)
	{
		clipboardLabel.setVisible(shouldShow);
	}
	
	
	//Showing error messages in a box - this will be changed in future
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
