//Abhay Saxena & GVS Karthik

package object;

public class Song {
	
	private String name;
	private String artistName;
	private String albumName;
	private String year;

	public Song(String name, String artistName, String albumName, String year) {
		this.name = name;
		this.artistName = artistName;
		this.albumName = albumName;
		this.year = year;
	
	}

	public String getName() {
		return this.name;
	}

	public String getArtistName() {
		return this.artistName;
	}

	public String getAlbumName() {
		return this.albumName;
	}

	public String getYear() {
		return this.year;
	}
	
	public void setTitle(String name) {
		this.name = name;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public int compareTo(Song comparable) {
		if (this.name.equals(comparable.name))
			return this.artistName.toLowerCase().compareTo(comparable.artistName.toLowerCase());

		return this.name.toLowerCase().compareTo(comparable.name.toLowerCase());
	}

	@Override
	public String toString() {
		return this.name + " - " + this.artistName;
	}

}