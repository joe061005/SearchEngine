package hk.edu.hkbu.comp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class Extract_Image {
	
	LinkedList<String[]> extract_Image(LinkedList<String> all_Tag, String url_String) throws MalformedURLException {
		
		
		LinkedList<String[]> result_List = new LinkedList<String[]>();
				
		for(int i=0; i<all_Tag.size(); i++) {
			
			String image_URL = "";
			String image_ALT = "";
			String image_Name = "";
			
			String[] tag_Elements = all_Tag.get(i).split(" "); // ["img", "src=/?pic=1010", "alt="HKBU COMPUTER SCIENCE"]
			
			if(!tag_Elements[0].equals("img")) {continue;}
			
			for(int j=0; j<tag_Elements.length; j++) {
				
				if(tag_Elements[j].contains("src=")) {
					
					image_URL = tag_Elements[j].replace("src=", ""); // "/?pic=1010"
					if(!isAbsURL(image_URL)) {
						image_URL = toAbsURL(image_URL, new URL(url_String)); // https://www.comp.hkbu.edu.hk/v1/?pic=1010
					}

					System.out.println("IMAGEURL: " + image_URL);
					
					// get image name
					String url_Path_Split[] = tag_Elements[j].split("/");
					String url_Last_Path[] = url_Path_Split[url_Path_Split.length-1].split("=");
					image_Name = url_Last_Path[url_Last_Path.length - 1];
					
					if(image_Name.contains(".")) {
						image_Name = image_Name.substring(0, image_Name.indexOf("."));
					}
				}
				
				// get keywords 
				if(tag_Elements[j].contains("alt=")) {
					image_ALT = tag_Elements[j].replace("alt=", "");
					
					int count = Math.min(j + 1, tag_Elements.length - 1);
					
					while(count < tag_Elements.length && !tag_Elements[count].contains("=")) {
						image_ALT += " " + tag_Elements[count];
						count++;
					}
				}
				
			}	
			
			// add the record
			
			if(image_Name!= "") {
				String [] current_Result = new String[] {"img",url_String,image_Name,image_URL};
				result_List.add(current_Result);
			}
			
			
			if(image_ALT != "") {
				String [] split_ALT = image_ALT.split(" ");
				for(int j = 0; j < split_ALT.length; j++) {
					String [] current_Result = new String[] {"img",url_String,split_ALT[j],image_URL};
					result_List.add(current_Result);
				}
				
			}

		}
		
		return result_List;
				
	}
	
	// explained in Extract_URL.java

	boolean isAbsURL(String str) {
		return str.matches("^[a-z0-9]+://.+");
	}

	String toAbsURL(String str, URL ref) {
		String prefix = ref.getProtocol() + "://" + ref.getHost();

		if(ref.getPort() > -1)
			prefix += ":" + ref.getPort();

		if(prefix.contains("https://www.comp.hkbu.edu.hk") && !ref.getPath().contains("v1")){
			prefix += "/v1";
		}
		String tmp = "";

		if(ref.getPath().equals("/") || ref.getPath().equals("//")){
			prefix += "/";
		}else{
			 tmp =  "/" + ref.getPath() + "/";
			prefix += tmp.replace("//", "/");
		}


		return (prefix + str);

	}
	
}
