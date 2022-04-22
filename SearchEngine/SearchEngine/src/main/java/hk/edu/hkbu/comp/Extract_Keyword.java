package hk.edu.hkbu.comp;

import java.util.LinkedList;

public class Extract_Keyword {
	
	// store the processed words
	LinkedList<String> processedWord = new LinkedList<String>();
	
	LinkedList<String[]> extract_Keyword(String title, LinkedList<String> text, String url_String){
		
		// check if the title contains css code 
		if(isCss(title)) {
			title = "";
		}
		
		LinkedList<String[]> result_List = new LinkedList<String[]>(); // store all keyword information in specific format e.g. ["web", "https://www.comp.hkbu.edu.hk", "HKBU", "Department of Computer Science, Hong Kong Baptist University"]
		                                
		
		
		for(int i=0 ; i<text.size() ; i++) {
			// remove '&nbsp' (No-Break Space)
			String txt = text.get(i).replaceAll("[\\s\\u00A0]+$", "");
			
			// only have blank spaces
			if(txt.trim().isEmpty()) {
				continue;
			}
			// not css code
			if(!isCss(txt)) {
				//get the unique words
				LinkedList<String> uniqueWords = getUniqueWords(txt);
				
				// add records to the result_List
				for(int j=0 ; j < uniqueWords.size(); j++) {
					result_List.add(new String[] {"web", url_String, uniqueWords.get(j), title});
				}
			}
			
		}
		
		return result_List;
		
	}
	 
	 // get the unique words
	 LinkedList<String> getUniqueWords(String text) {
		LinkedList<String> words = new LinkedList<String>(); // store the words processed
        String[] Splitwords = text.split("[0-9\\W]+"); // regex checking

        for (String w : Splitwords) {
            w = w.toLowerCase();
            
            // the keyword is not processed and make sure that it does not only store blank spaces
            if (!words.contains(w) && !processedWord.contains(w) && !w.trim().isEmpty())
                words.add(w);
                processedWord.add(w);
        }

        return words;
    }
	 
	 // check if the text contains css code
	 boolean isCss(String txt) {
		 return(txt.contains(":") && txt.contains(";") && txt.contains("{") && txt.contains("}"));
	 }

}
