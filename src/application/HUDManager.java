package application;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HUDManager{

	Stage mainStage;
	Stage stage;
	MainScreenController con = null;
	
	List<Stage> stageList = new ArrayList<>();
	
	Widget widget = null;
	
	HUDManager(MainScreenController con){
		this.con = con;
		con.setHUDManager(this);
		//return;
		//try {
			//stage = new Stage();
			/*FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/HUDScreen.fxml"));
			//AnchorPane root = (AnchorPane) loader.load();
			VBox root = (VBox) loader.load();
			Scene scene = new Scene(root);
			scene.setFill(Color.TRANSPARENT);
			stage.setScene(scene);*/
			//stage.setResizable(false);
			//stage.setFullScreen(true);
			
			/*Button btn = new Button("HELLO");
			Scene scene = new Scene(btn);
			
			stage.setScene(scene);
			//Testing HUD
		    stage.initStyle(StageStyle.TRANSPARENT);
		    stage.setAlwaysOnTop(true);
		    
		    stage.setX(1800);
		    stage.setY(0);
		    
		}catch(Exception e)
		{
			e.printStackTrace();
		}*/
		
		//First btn
		/*stage = new Stage();		
		Button btn = new Button("HELLO");
		Scene scene = new Scene(btn);
	
		stage.setScene(scene);
		//Testing HUD
	    stage.initStyle(StageStyle.TRANSPARENT);
	    stage.setAlwaysOnTop(true);	    
	    stage.setX(1800);
	    stage.setY(0);
	    stageList.add(stage);*/
		
		
		/*mainStage = new Stage();	
		Button btn = new Button("");
		btn.setPrefSize(0,0);
		Scene scene = new Scene(btn);
		mainStage.setScene(scene);
		//Testing HUD
	    //stage.initStyle(StageStyle.TRANSPARENT);
		mainStage.initStyle(StageStyle.UTILITY);
		//stage.initStyle(StageStyle.UNDECORATED);
		mainStage.setAlwaysOnTop(true);	   
	    //stage.get

		mainStage.toFront();
		mainStage.setResizable(false);*/
		
		
		mainStage = new Stage();
        StackPane rootPane = new StackPane();
        rootPane.setPrefSize(500, 500);
        rootPane.setBackground(Background.EMPTY);

        // add some node
        Label label = new Label("Bright & Shiny");
        label.setTextFill(Color.RED);

        rootPane.getChildren().add(label);

        // create scene
        Scene scene = new Scene(rootPane);
		mainStage.setScene(scene);
		mainStage.initStyle(StageStyle.UNDECORATED);
	    mainStage.setAlwaysOnTop(true);	 
	    mainStage.toFront();
	    mainStage.setResizable(false);
		
		
		
		addStage("Test1",0,0);
		addStage("Test2",0,50);
		addStage("Test3",0,100);
		addStage("Test4",0,150);
		
		addStage("Test5",1840,0);
		addStage("Test6",1840,50);
		addStage("Test7",1840,100);
		addStage("Test8",1840,150);
		
	}
	
	private void addStage(String btnName,double xPos, double yPos)
	{
		stage = new Stage();		
	
		ImageView imageView = new ImageView();
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
            imageView.setImage(image);


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
		//imageView.resize(30, 30);
		imageView.setFitWidth(30);
		imageView.setFitHeight(30);
		Button btn = new Button(btnName,imageView);
		//Button btn = new Button(btnName);
		btn.setMaxSize(80, 30);
		btn.setPrefSize(80, 30);
		
		Scene scene = new Scene(btn);
        //scene.getRoot().getChildren().add(new Button("Click me!"));
        
		stage.setScene(scene);
		scene.setFill(Color.TRANSPARENT);
		//Testing HUD
	    stage.initStyle(StageStyle.TRANSPARENT);
		//stage.initStyle(StageStyle.UTILITY);
		//stage.initStyle(StageStyle.UNDECORATED);
	    stage.setAlwaysOnTop(true);	    
	    stage.setX(xPos);
	    stage.setY(yPos);
	    stageList.add(stage);
	    //stage.get

	    stage.toFront();
	    stage.setResizable(false);
	    
	    File file = new File("TransparentButton.css");
	    //btn.getStylesheets().add(file.getName());
	    btn.getStylesheets().add(getClass().getResource("TransparentButton.css").toExternalForm());
	    
	    btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				con.setResult(btnName);
			}
	    	
	    });
	}
	
	public void capsPressed()
	{
		if(widget == null)
			widget = new Widget();
		/*if(mainStage.isShowing())
			mainStage.close();
		else
			mainStage.show();*/
		
		if(stageList.get(0).isShowing())
		{
			for(Stage stage:stageList)
				stage.close();
		}
		else
		{
			for(Stage stage:stageList)
				stage.show();
		}
	}
	
	public void TestMe()
	{
		System.out.println("TESTED");
	}
	
}
