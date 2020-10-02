//Abhay Saxena (ans192) & GVS Karthik (vg311)

package view;

import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
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
	
	@FXML TextField songName;
	@FXML TextField artistName;
	@FXML TextField albumName;
	@FXML TextField yearOfRelease;
	
	@FXML Button addButton;
	@FXML Button editButton;
	@FXML Button deleteButton;
	@FXML Button clearButton;
	
	private ObservableList<Song> obsList; 
	
	public void start(Stage mainStage) {
		
		obsList = FXCollections.observableArrayList(); 

		//Previous session is fed into the new session.
		songListReader();
		
		listView.setItems(obsList); 
		
		
		//As application spins up, if List != Empty, select the first song.
		if (!obsList.isEmpty())
	    	  listView.getSelectionModel().select(0);

		
		// Set listener for the items, to make editing easier.
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				populateFields(listView.getSelectionModel().getSelectedItem()));
		
		//When closing the application, write songs to file.
		mainStage.setOnCloseRequest(event -> songListFileWriter());
		 
		
	}
	 
	@FXML
	public void add(ActionEvent event) {
		
			String enteredName = songName.getText(), enteredArtist = artistName.getText(),
					enteredAlbum = albumName.getText(), enteredYear = yearOfRelease.getText();
			
			Song newSong = new Song(enteredName, enteredArtist, enteredAlbum, enteredYear);
			
			//Add Check: For songs with the same song and artist name.
			if(!addCheck(newSong,true)) {
				return;	
			}
			
			Alert confirmation = generateConfirmation("Add");
			
			if (confirmation.showAndWait().get() == ButtonType.YES) {
				//Add song
				listView.getItems().add(newSong);
				
				//Sort List using a Comparator object.
				if (!obsList.isEmpty()) {
					FXCollections.sort(obsList, new Comparator<Song>() {
						@Override
						public int compare(Song s1, Song s2) {
							if (s1.getName().toLowerCase().equals(s2.getName().toLowerCase())) {
								return s1.getArtistName().toLowerCase().compareTo(s2.getArtistName().toLowerCase());
							} else {
								return s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
							}
						}
					});
					listView.setItems(obsList); 
				}
				
				listView.getSelectionModel().select(newSong);
				
				if(obsList.size() == 1) {
					listView.getSelectionModel().select(0);
				}
				
				else {
					int index = 0;
					for (Song song:obsList) {
						if(song.getName().equals(newSong.getName()) && song.getArtistName().equals(newSong.getArtistName())) {
							listView.getSelectionModel().select(index);
							break;
						}
						
						index++;
					}
				}
				
			}
				
			
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
				//Main Check: New Song should not have the same Song Name and Artist Name.
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
		populateFields(listView.getSelectionModel().getSelectedItem());
		
	}
	
	//Edit Song
	@FXML
	public void edit(ActionEvent event) throws IOException {
		String enteredName = songName.getText(), enteredArtist = artistName.getText(), enteredAlbum = albumName.getText(), enteredYear = yearOfRelease.getText();
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
			if(!addCheck(newSong, true)) {
				return;	
			}
			obsList.set(index,newSong);
			
			//Sort List using a Comparator object.
			if (!obsList.isEmpty()) {
				if (!obsList.isEmpty()) {
					FXCollections.sort(obsList, new Comparator<Song>() {
						@Override
						public int compare(Song s1, Song s2) {
							if (s1.getName().toLowerCase().equals(s2.getName().toLowerCase())) {
								return s1.getArtistName().toLowerCase().compareTo(s2.getArtistName().toLowerCase());
							} else {
								return s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
							}
						}
					});
				listView.setItems(obsList); 
			}
			
			listView.getSelectionModel().select(index);

			}
		}
		
		
	}
	
	//Clear Fields
	@FXML
	public void clearFields() {
		songName.clear();
		artistName.clear();
		albumName.clear();
		yearOfRelease.clear();
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
	
	public void songListReader() {
		File file = new File("src/storage/songs.out");
		if (!file.exists()){
			try {
				//if file doesn't exist, create a new file.
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		try {
			
		          BufferedReader br = new BufferedReader(new FileReader(file));
		          String line;            
		          line = br.readLine();
		          while (line  != null) {	
		        	  String[] songData = new String[4];
		        	  String[] temp = line.split(",");
	                       for(int i = 0; i < temp.length; i++) {
	                           songData[i] = temp[i];
	                       }
		          
	                       obsList.add(new Song(songData[0], songData[1], songData[2], songData[3]));
	                       line = br.readLine();
		          }
		          listView.setItems(obsList);
		          br.close();
	           
	       } catch(FileNotFoundException f) {
	           return;
	       } catch(IOException i) {
	           i.printStackTrace();
	           return;
	       }
    }
	
	public void songListFileWriter()  {
		String songsTextFile = "src/storage/songs.out";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(songsTextFile));
			for(Song song : obsList) {
				String album = song.getAlbumName();
				String year = song.getYear();
				
				bw.write(song.getName()+ ",");
				bw.write(song.getArtistName() + "," );
				
				if(album == null) {
					album = "";
				}
				bw.write(album + ",");
				
				if(year == null) {
					year = "";
				}
				bw.write(year+ "\n");
				
			}
			bw.close();
			

		
		} catch (IOException e) {
			//System.err.println("Error: " + e.getMessage());
			 e.printStackTrace();
		}
		
		
	}
	
	//MODIFIED: Populate Song Details on the Form.
	public void populateFields(Song selectedSong) {
		
		//This if-condition nullifies the error of: if the list is empty, and the listener calls for populate fields of an empty list.
		if (obsList.size() != 0 && selectedSong != null) {
			if (selectedSong.getName() != null) {
				songName.setText(selectedSong.getName());
			} else {
				
			}
			if (selectedSong.getArtistName() != null) {
				artistName.setText(selectedSong.getArtistName());
			}
			if (selectedSong.getAlbumName() != null) {
				albumName.setText(selectedSong.getAlbumName());
			}
			if (selectedSong.getYear() != null) {
				yearOfRelease.setText(selectedSong.getYear());
			}
		} else {
			return;
		}
	}
}