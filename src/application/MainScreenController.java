package application;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class MainScreenController {

	@FXML
	private Button solveButton, clearButton, xBtn1,xBtn2,xBtn3;
	@FXML
	private TextField resField, resField2, posField1, otherField1,posField2, otherField2,posField3, otherField3;
	@FXML
	private Label clipboardLabel;
	@FXML
	private ImageView img1,img2,img3;
	
	public void initialize()
	{
		clipboardLabel.setVisible(false);
		
        try {
            URL url = getClass().getResource("../resource/image.png"); 
            InputStream strm = url.openStream(); 
            Image image = new Image(strm);
            img1.setImage(image);
            img2.setImage(image);
            img3.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
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
		
		solver.startSolve(varList,this);
	}
	
	private void clearFields()
	{
		posField1.setText("");
		posField2.setText("");
		posField3.setText("");
		otherField1.setText("");
		otherField2.setText("");
		otherField3.setText("");
		resField.setText("");
		clipboardLabel.setVisible(false);
	}
	
	public void setResult(String text)
	{
		resField.setText(text);
	}
	
	public void setResult2(String text)
	{
		resField2.setText(text);
	}
	
	public void hideClipMsg()
	{
		clipboardLabel.setVisible(false);
	}
	
	public void showClipMsg()
	{
		clipboardLabel.setVisible(true);
	}
	
}
