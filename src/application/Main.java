package application;
	
import java.io.InputStream;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/MainScreen.fxml"));
			VBox root = (VBox) loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Triangulator");
			primaryStage.show();
			
			InputStream strm = null;
	        try {
	            URL url = getClass().getResource("../resource/appIcon.png"); 
	            strm = url.openStream(); 
	            Image image = new Image(strm);
				primaryStage.getIcons().add(image);

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
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
