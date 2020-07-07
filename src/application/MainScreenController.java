package application;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainScreenController {

	@FXML
	private Button solveButton, clearButton;
	@FXML
	private TextField resField, posField1, otherField1,posField2, otherField2,posField3, otherField3,posField4, otherField4;
	
	public void initialize()
	{
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
	}
	
	public void setResult(String text)
	{
		resField.setText(text);
	}
	
}
