//Abhay Saxena & GVS Karthik

package view;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import object.Song;

public class SonglibController {
	
	@FXML         
	ListView<Song> listView;  
	
	@FXML TextField nameField;
	@FXML TextField artistField;
	@FXML TextField albumField;
	@FXML TextField yearField;
	
	@FXML Button addButton;
	@FXML Button editButton;
	@FXML Button deleteButton;
	@FXML Button clearButton;
	
	private ObservableList<Song> obsList; 
	
	public void start(Stage mainStage) {                
		
		//Implement reading songs from file!!
		
		obsList = FXCollections.observableArrayList(); 

		listView.setItems(obsList); 
		
		//Somehow sort the obsList!!
		
		listView.getSelectionModel().select(0);

		// set listener for the items
		
		/*listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				showItemInputDialog(mainStage));*/
		
	}

	//METHOD NOT WORKING FOR SOME REASON!!
	/*
	@FXML
	public void showItemInputDialog(Stage mainStage) {                
		Song item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();

		TextInputDialog dialog = new TextInputDialog(item);
		dialog.initOwner(mainStage); dialog.setTitle("List Item");
		dialog.setHeaderText("Selected Item (Index: " + index + ")");
		dialog.setContentText("Enter name: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) { obsList.set(index, result.get()); }
	}
	*/
	
	@FXML
	public void add(ActionEvent e) {
		
		if (!obsList.isEmpty()) {
			return;
		} else {
			String enteredName = nameField.getText(), enteredArtist = artistField.getText(), enteredAlbum = albumField.getText(), enteredYear = yearField.getText();
			Song newSong = new Song(enteredName, enteredArtist, enteredAlbum, enteredYear);
			
			//Create a check for existing songs with matching fields!!
			
			Alert confirmation = generateConfirmation();
			Optional<ButtonType> result = confirmation.showAndWait();
			if (result.get() == ButtonType.YES) {
				listView.getItems().add(newSong);
				listView.getSelectionModel().select(newSong);
			} else {
				clearFields();
				return;
			}
		}
	}
	
	//Delete Song
	@FXML
	public void delete(ActionEvent e) throws IOException {
		
	}
	
	//Edit Song
	@FXML
	public void edit(ActionEvent e) throws IOException {
		
	}
	
	//Clear Fields
	@FXML
	public void clearFields() {
		nameField.clear();
		artistField.clear();
		albumField.clear();
		yearField.clear();
	}
	
	public Alert generateConfirmation() {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("Confirmation Dialog");
		confirmation.setHeaderText("Confirm your choice");
		confirmation.setContentText("Are you sure you want to do this action?");

		confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

		return confirmation;
	}
}