package hk.edu.hkbu.comp;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.util.LinkedList;


public class Database {
	
	File db_Txt;
	LinkedList<String> blacklist_Words = new LinkedList<String>(); 
	LinkedList<String> processedVideoURL = new LinkedList<String>();
	LinkedList<String> processedPhotoURL = new LinkedList<>();
	
	Database(String db_Path){
		db_Txt = new File("Search_Engine_Database.txt");
		
		try {
			// create the text file for storing records
			if(!db_Txt.createNewFile()) {
				db_Txt.delete();
				db_Txt.createNewFile();
			}
			
			// store the blackList words in the LinkedList
	        File blacklist_File = new File("BlackListWords.txt");
			Scanner reader = new Scanner(blacklist_File);
			while(reader.hasNextLine()) {
				blacklist_Words.add(reader.nextLine());
			}
			
			System.out.println("Database Created:" + db_Path + "\\" + db_Txt.getName());
			
		}catch (IOException error) {
			System.out.println(error);
		}

	}
	
	//add image to the text file
	void pushIMG(LinkedList<String[]> data){
		LinkedList<String> photoURL = new LinkedList<String>();
		try {
			BufferedWriter db_BufferedWriter = new BufferedWriter(new FileWriter(db_Txt,true));
			for(int i = 0; i < data.size(); i++) {
				
				// contain black list word
				if(blacklist_Words.contains(data.get(i)[2])) {continue;}
                
				// duplicated image
				if(processedPhotoURL.contains(data.get(i)[3])) {continue;}
				
				
				db_BufferedWriter.write("{,./.,eD");
				db_BufferedWriter.write(data.get(i)[0]);
				db_BufferedWriter.write(",./.,eD");
				db_BufferedWriter.write(data.get(i)[1]);
				db_BufferedWriter.write(",./.,eD");
				db_BufferedWriter.write(data.get(i)[2]);
				db_BufferedWriter.write(",./.,eD");
				db_BufferedWriter.write(data.get(i)[3]);
				db_BufferedWriter.write(",./.,eD}");
				db_BufferedWriter.newLine();
                
				// temporary stroage for storing the urls of the processed images in this round
				if(!photoURL.contains(data.get(i)[3])) {
					photoURL.add(data.get(i)[3]);
				}
				
			}

			processedPhotoURL.addAll(photoURL); // add the urls of the processed images to the main storage
			
			db_BufferedWriter.close();
			
		}catch (IOException error) {
			System.out.println(error);
		}
		
	}
	
	// store the videos
	void pushVideo(LinkedList<String[]> data){
		LinkedList<String> videoURL = new LinkedList<String>();
		try {
			BufferedWriter db_BufferedWriter = new BufferedWriter(new FileWriter(db_Txt,true));
			for(int i = 0; i < data.size(); i++) {
				
				// contain black list word
				if(blacklist_Words.contains(data.get(i)[2])) {continue;}
				
				// duplicated video
				if(processedVideoURL.contains(data.get(i)[3])) {continue;}
				
				db_BufferedWriter.write("{,./.,eD");
				db_BufferedWriter.write(data.get(i)[0]);
				db_BufferedWriter.write(",./.,eD");
				db_BufferedWriter.write(data.get(i)[1]);
				db_BufferedWriter.write(",./.,eD");
				db_BufferedWriter.write(data.get(i)[2]);
				db_BufferedWriter.write(",./.,eD");
				db_BufferedWriter.write(data.get(i)[3]);
				db_BufferedWriter.write(",./.,eD}");
				db_BufferedWriter.newLine();
				
				// temporary stroage for storing the urls of the processed videos in this round
				if(!videoURL.contains(data.get(i)[3])) {
					videoURL.add(data.get(i)[3]);
				}
				
			}

			processedVideoURL.addAll(videoURL); // add the urls of the processed videos to the main storage
			
			db_BufferedWriter.close();
			
		}catch (IOException error) {
			System.out.println(error);
		}
		
	}
	
	// store the keywords
	void pushKeyword(LinkedList<String[]> data){
		try {
			BufferedWriter db_BufferedWriter = new BufferedWriter(new FileWriter(db_Txt,true));
			for(int i = 0; i < data.size(); i++) {
				
				// contain black list word
				if(blacklist_Words.contains(data.get(i)[2])) {continue;}
				
				db_BufferedWriter.write("{,./.,eD");
				db_BufferedWriter.write(data.get(i)[0]);
				db_BufferedWriter.write(",./.,eD");
				db_BufferedWriter.write(data.get(i)[1]);
				db_BufferedWriter.write(",./.,eD");
				db_BufferedWriter.write(data.get(i)[2]);
				db_BufferedWriter.write(",./.,eD");
				db_BufferedWriter.write(data.get(i)[3]);
				db_BufferedWriter.write(",./.,eD}");
				db_BufferedWriter.newLine();
                
				
			}
			
			db_BufferedWriter.close();
			
		}catch (IOException error) {
			System.out.println(error);
		}
		
	}
	
	
}
