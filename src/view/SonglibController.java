//Abhay Saxena & GVS Karthik

package view;

import javafx.event.ActionEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
		
		if (!obsList.isEmpty())
	    	  listView.getSelectionModel().select(0);

		// Set listener for the items, to make editing easier.
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				populateFields(listView.getSelectionModel().getSelectedItem()));
		
		//Enable Buttons only if user enters SongName & ArtistName.
		
		//Enable Buttons if user clicks on any one of the song's from the list.
		 
		
	}
	 
	@FXML
	public void add(ActionEvent event) {
		
			String enteredName = nameField.getText(), enteredArtist = artistField.getText(),
					enteredAlbum = albumField.getText(), enteredYear = yearField.getText();
			
			Song newSong = new Song(enteredName, enteredArtist, enteredAlbum, enteredYear);
			
			//check for existing songs with matching fields!!
			if(!addCheck(newSong,true)) {
				return;	
			}
			
			Alert confirmation = generateConfirmation("Add");
			//MODIFIED: A different type of handling for confirmation. Passing .showAndWait() directly to the if-condition.
			//Optional<ButtonType> confirm = confirmation.showAndWait();
			
			if (confirmation.showAndWait().get() == ButtonType.YES) {
				listView.getItems().add(newSong);
				songListFileWriter();
				
				listView.getSelectionModel().select(newSong);
				
				if(obsList.size() == 1) {
					listView.getSelectionModel().select(0);
				}
				
				else {
					int index = 0;
					for (Song song:obsList) {
						if(song.getName().equals(newSong.getName())) {
							listView.getSelectionModel().select(index);
							break;
						}
						
						index++;
					}
				}
				
			}

				   	clearFields();
				
			
		}
	
	
	public boolean addCheck(Song song, boolean isAdd) {
		String Name = song.getName();
		String Artist = song.getArtistName();
		String year = song.getYear();
		
		if(Name.isEmpty() || Artist.isEmpty()) {
			errorAlert("Enter Song Title and Song Artist");
			
			return false;
		}

		if (isAdd) {
			
			for(Song s: obsList) {
				//MODIFIED: Added .toLowerCase() condition, since the check is case-insensitive.
				if(s.getName().toLowerCase().equals(song.getName().toLowerCase()) && s.getArtistName().toLowerCase().equals(song.getArtistName().toLowerCase())) {
					errorAlert("Item already exists in the list. Cannot duplicate.");
					return false;
				}
			}
		}
		
		
		if(year.isEmpty()) {
			return true;
		}
		
		else if(!year.isEmpty()) {
			if((year.length() != 4) || (!year.matches("[0-9]+"))) {
				errorAlert("Year must have only four digits(YYYY)");
				return false;
			} else if (Integer.valueOf(year) > 2020) {
				errorAlert("Enter a song with a valid year of release.");
				return false;
				//MODIFIED: Just some one more specification. Added Song's YearOfRelease cannot be greater than current year.
			}
			
		}
			
		return true;
	}
	
	//Delete Song
	@FXML
	public void delete(ActionEvent event) throws IOException {
		
		if (obsList.isEmpty()){
			errorAlert("The list is empty.");
			return;
		}
		
		 Song clickedSong = listView.getSelectionModel().getSelectedItem();
		 int index = listView.getSelectionModel().getSelectedIndex();
		
		Alert confirmation = generateConfirmation("Delete");
		Optional<ButtonType> confirm = confirmation.showAndWait();
		if (confirm.get() == ButtonType.YES) {
			obsList.remove(clickedSong);
			if (obsList.size() == 0) {
				clearFields();
			}
			 if(obsList.size() == index) {
				 listView.getSelectionModel().select(--index);
			 }
			 else {
				 listView.getSelectionModel().select(index++);
			 }
		}
		
	}
	
	//Edit Song
	@FXML
	public void edit(ActionEvent event) throws IOException {
		String enteredName = nameField.getText(), enteredArtist = artistField.getText(), enteredAlbum = albumField.getText(), enteredYear = yearField.getText();
		Song newSong = new Song(enteredName, enteredArtist, enteredAlbum, enteredYear);
		
		if(obsList.isEmpty()) {
			errorAlert("The song list is empty");
			return;
		}
		Song clickedSong = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		
		
		
		Alert confirmation = generateConfirmation("Edit");
		Optional<ButtonType> confirm = confirmation.showAndWait();
		
		if (confirm.get() == ButtonType.YES) {
			if(!addCheck(newSong,true)) {
				return;	
			}
			obsList.set(index,newSong);
			
			//SORT LIST CALL
			
			listView.getSelectionModel().select(index);

		}
		
		
	}
	
	//Clear Fields
	@FXML
	public void clearFields() {
		nameField.clear();
		artistField.clear();
		albumField.clear();
		yearField.clear();
	}
	
	public Alert generateConfirmation(String function) {
		//MODIFIED: Added a more specific confirmation dialog.
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("Confirmation Dialog");
		confirmation.setHeaderText("Operation: "+ function + "ing a song.");
		confirmation.setContentText("Are you sure you want to " + function.toLowerCase() + " this song?");

		confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

		return confirmation;
	}
	
	public void errorAlert(String error) {
		   Alert alert =  new Alert(AlertType.ERROR);
		   alert.setTitle("Error");
		   alert.setHeaderText("Error");
		   String content = error;
		   alert.setContentText(content);
		   alert.showAndWait();
	}
	
	public void songListFileWriter() {
		String songsTextFile = "storage/songsList.txt";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(songsTextFile));
			for(Song song : obsList) {
				bw.write(song.getName());
				bw.newLine();
				bw.write(song.getArtistName());
				bw.newLine();
				bw.write(song.getAlbumName());
				bw.newLine();
				bw.write(song.getYear());
				bw.newLine();
			}
			bw.write("end");
			
			bw.close();
		
		} catch (IOException e) {
			
		}
		
	}
	
	//MODIFIED: Populate Song Details on the Form.
	public void populateFields(Song selectedSong) {
		
		//This if-condition nullifies the error of: if the list is empty, and the listener calls for populate fields of an empty list.
		if (obsList.size() != 0) {
			nameField.setText(selectedSong.getName());
			artistField.setText(selectedSong.getArtistName());
			albumField.setText(selectedSong.getAlbumName());
			yearField.setText(selectedSong.getYear());
		} else {
			return;
		}
	}
	
	//MODIFIED: Disable all Buttons on the Form.
	public void disableButtons() {
		
		//If the song fields are empty.
		addButton.setDisable(true);
		deleteButton.setDisable(true);
		editButton.setDisable(true);
	}
	
	//MODIFIED: Enable all Buttons on the Form.
	public void enableButtons() {
		
		//If the Song Name && Artist Field is not empty.
		addButton.setDisable(false);
		deleteButton.setDisable(false);
		editButton.setDisable(false);
	}
}