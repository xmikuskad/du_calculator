package application;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
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
	
	public void initialize()
	{
		clipboardLabel.setVisible(false);
		
		InputStream strm = null;
        try {
            URL url = getClass().getResource("../resource/image.png"); 
            strm = url.openStream(); 
            Image image = new Image(strm);
            img1.setImage(image);
            img2.setImage(image);
            img3.setImage(image);
            img4.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
				try {
					strm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
        }
        
        try {
            URL url = getClass().getResource("../resource/imgIcon.png"); 
            strm = url.openStream(); 
            Image image = new Image(strm);
            imageIcon1.setImage(image);
            imageIcon2.setImage(image);
            imageIcon3.setImage(image);
            imageIcon4.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
			try {
				strm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        try {
            URL url = getClass().getResource("../resource/appIcon.png"); 
            strm = url.openStream(); 
            programIcon = new Image(strm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
				try {
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
		
		System.out.println("FINAL PIXEL WAS "+(heightTracker+7));
		System.out.println("FINAL HEIGHT "+finalHeight);
		
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
	
	public void setResult2(String text)
	{
		//resField2.setText(text);
	}
	
	public void hideClipMsg()
	{
		clipboardLabel.setVisible(false);
	}
	
	public void showClipMsg()
	{
		clipboardLabel.setVisible(true);
	}
	
	//Vytvara chybove hlasky pri prihlasovani
	public void showAlertBox(String header,String problem)
	{
		Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(header);
            alert.setHeaderText(problem);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(programIcon);
            
            alert.showAndWait();
        }
		);
	}
	
}
