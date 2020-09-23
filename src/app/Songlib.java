package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
		loader.setLocation(
				getClass().getResource("/view/display.fxml"));
		
		AnchorPane root = (AnchorPane)loader.load();

		Button details = new Button("Details");
		Button add = new Button("Add");
		Button delete = new Button("Delete");
		
		HBox hbox = new HBox(10, details, add, delete);
		
		root.getChildren().addAll(hbox);
		AnchorPane.setBottomAnchor(hbox, 10d);
		AnchorPane.setRightAnchor(hbox, 10d);
		
		SonglibController listController = 
				loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show(); 
	}

}
