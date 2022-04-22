package hk.edu.hkbu.comp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class Extract_URL {
	
	LinkedList<String> extract_URL(LinkedList<String> all_Tag, String url_String) throws MalformedURLException {
				
		LinkedList<String> result_List = new LinkedList<String>(); // store the urls
				
		for(int i=0; i<all_Tag.size(); i++) {
						
			String[] tag_Elements = all_Tag.get(i).split(" "); // e.g. ["a", "href=?page=fast_facts"]
			
			if(tag_Elements[0].equals("img") || 
					tag_Elements[0].equals("link") || 
					tag_Elements[0].equals("style") ||
					tag_Elements[0].equals("script")) {continue;}
			
			for(int j=0; j<tag_Elements.length; j++) {
				
				if(tag_Elements[j].contains("href=")) { // href attribute (?page=fast_facts)
					
					String correctedURL = tag_Elements[j].replace("href=", ""); // get the url from the href attribute (?page=fast_facts)
					String urlLowerCase = correctedURL.toLowerCase();
					
					// check if it is an URL
					if(urlLowerCase.contains("mailto:") || urlLowerCase.contains("tel:") ||urlLowerCase.contains("javascript") ||urlLowerCase.contains("#")) {
						break;
					}
					
					if(!isAbsURL(correctedURL)) { // e.g.  ?page=media
						correctedURL = toAbsURL(correctedURL, new URL(url_String)); // https://www.comp.hkbu.edu.hk/v1/?page=fast_facts
					}
					result_List.add(correctedURL);
				}
				
			}
		}
		
		return result_List;
				
	}
	
	// check if it is a Absurl
	boolean isAbsURL(String str) {
		return str.matches("^[a-z0-9]+://.+");
	}
	
	String toAbsURL(String str, URL ref) {
		String prefix = ref.getProtocol() + "://" + ref.getHost(); // https://www.comp.hkbu.edu.hk
		
		if(ref.getPort() > -1)
			prefix += ":" + ref.getPort(); // add port

		if(prefix.contains("https://www.comp.hkbu.edu.hk") && !ref.getPath().contains("v1")){
			prefix += "/v1";  // add the "/v1" 
		}
		String tmp = "";
        
		// edit the path
		if(ref.getPath().equals("/") || ref.getPath().equals("//")){
			prefix += "/";
		}else{
			tmp =  "/" + ref.getPath() + "/";
			prefix += tmp.replace("//", "/");
		}


		return (prefix + str);
		
	}
	
}
