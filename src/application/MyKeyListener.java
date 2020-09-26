package application;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Platform;

public class MyKeyListener implements NativeKeyListener {	
	HUDManager hud;
	MainScreenController con;
	
	MyKeyListener(MainScreenController con) {
		this.con = con;
		hud = new HUDManager(con);
	}
	
	public void HelloThere()
	{
		System.out.println("Hello there");
	}

	//58 capslock
	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		//58 caps
		//u 22
		//f8 je 66
		//System.out.println("1 - "+arg0.getKeyCode());
		if(arg0.getKeyCode() == 22 || arg0.getKeyCode() == 66)
		{
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					hud.capsPressed();
				}
			});
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("2 - "+arg0.getKeyCode());
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("3 - "+arg0.getKeyCode());
		
	}
	
}
