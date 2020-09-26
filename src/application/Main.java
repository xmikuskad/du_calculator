package application;
	
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
			
			MainScreenController controller = (MainScreenController)loader.getController();
			controller.LoadThings(primaryStage);
			//Testing HUD
		    //primaryStage.initStyle(StageStyle.TRANSPARENT);
		    //primaryStage.setAlwaysOnTop(true);
			
			primaryStage.show();
			//TESTING!
			primaryStage.setOnCloseRequest(event -> {
				primaryStage.hide();
			});
			
			try {
				// Get the logger for "com.github.kwhat.jnativehook" and set the level to off.
				Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
				logger.setLevel(Level.OFF);
				logger.setUseParentHandlers(false);
				
				//GlobalScreen.registerNativeHook();
				//GlobalScreen.addNativeKeyListener(new MyKeyListener(controller));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Loading icon
			InputStream strm = null;
	        try {
	            URL url = getClass().getResource("../resource/appIcon.png"); 
	            Image image = null;
	            
	            if(url!=null) {
		            strm = url.openStream(); 
		            image = new Image(strm);
	            }
	            else
	            {
	            	File file = new File("resource/appIcon.png");
	            	image = new Image(file.toURI().toString());
	            }
	            
				primaryStage.getIcons().add(image);

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
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
