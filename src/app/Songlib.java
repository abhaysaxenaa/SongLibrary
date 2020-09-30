//Abhay Saxena & GVS Karthik

package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.SonglibController;

public class Songlib extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(); 
		loader.setLocation(getClass().getResource("/view/display.fxml"));
		
		AnchorPane root = (AnchorPane)loader.load();
		
		SonglibController listController = loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root);
		primaryStage.setTitle("Song Library - ans192 & vg311");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show(); 
	}

}
