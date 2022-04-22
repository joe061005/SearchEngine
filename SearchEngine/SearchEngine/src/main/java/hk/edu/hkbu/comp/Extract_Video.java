package hk.edu.hkbu.comp;

import java.util.LinkedList;

public class Extract_Video {

	// for extracting video information
	// all_tag: tag information (e.g. a class=normal href=?page=news )
	// url_String : url String
	LinkedList<String[]> extract_Video(LinkedList<String> all_tag, String url_String) {
		LinkedList<String[]> result_List = new LinkedList<String[]>(); // store all video information in specific format
																		// ["type", "URL of the video", "keyword","embedded URL of the video"]
																		// ( e.g. ["vdo","https://youtu.be/f2WLxvixzCQ", "hkbu","https://www.youtube.com/embed/f2WLxvixzCQ"]
		                                                               
		LinkedList<String> videoURL = new LinkedList<String>(); // store the video urls processed

		for (int i = 0; i < all_tag.size(); i++) {
			// System.out.println(all_tag.get(i));
			String[] tag_Elements = all_tag.get(i).split(" "); // split the tag

			if (tag_Elements[0].equals("a")) { // 'a' tag
				// System.out.println(tag_Elements.length);
				for (int k = 0; k < tag_Elements.length; k++) {
					if (tag_Elements[k].contains("href=")) { // href attribute (?page=fast_facts)
						String correctedURL = tag_Elements[k].replace("href=", ""); // get the url from the href attribute (?page=fast_facts)
						// check if it is a youtube url and not processed
						if (correctedURL.contains("youtu.be")  
								|| (correctedURL.contains("youtube") && correctedURL.contains("watch"))
										&& !videoURL.contains(correctedURL)) {
							System.out.println("YOUTUBE: " + correctedURL);
							videoURL.add(correctedURL); // add it to the processed video pool
							LinkedList<String> list = getKeyWords(correctedURL); // get the keyword list
							
							// loop through the keyword list to store the video information
							// 1 key word -> 1 record
							for (int j = 0; j < list.size(); j++) {          // list item: "https://www.youtube.com/embed/Po8yNWScQ3g hkbu"
								String[] splitlist = list.get(j).split(" "); // e.g. ["https://www.youtube.com/embed/Po8yNWScQ3g", "hkbu"]
								String[] current_Result = new String[] { "vdo", correctedURL, splitlist[1],
										splitlist[0] };
								result_List.add(current_Result);
							}

						}
					}
				}
			}else if (tag_Elements[0].equals("iframe")) {
				for(int k=0; k <tag_Elements.length; k++) {
					if (tag_Elements[k].contains("src=https://www.youtube.com/embed/")) {
						
						String ytURL = "https://www.youtube.com/watch?v=" + tag_Elements[k].split("src=https://www.youtube.com/embed/")[1];
						System.out.println("YTIFRAME: " + ytURL);
						videoURL.add(ytURL); // add it to the processed video pool
						LinkedList<String> list = getKeyWords(ytURL); // get the keyword list
						
						// loop through the keyword list to store the video information
						// 1 key word -> 1 record
						for (int j = 0; j < list.size(); j++) {          // list item: "https://www.youtube.com/embed/Po8yNWScQ3g hkbu"
							String[] splitlist = list.get(j).split(" "); // e.g. ["https://www.youtube.com/embed/Po8yNWScQ3g", "hkbu"]
							String[] current_Result = new String[] { "vdo", ytURL, splitlist[1],
									splitlist[0] };
							result_List.add(current_Result);
						}
						
					}
				}
			}

		}

		return result_List;

	}

	LinkedList<String> getKeyWords(String url) {
		LinkedList<String> result_list = new LinkedList<String>();

		Extract_Webpage_Elements extractWords = new Extract_Webpage_Elements();
		extractWords.start_Extracting_video(url);
		LinkedList<String> ytTags = extractWords.all_Tag;
		LinkedList<String> keywords = new LinkedList<String>(); // store the keywords
		String embedURL = ""; // embedded url for youtube video

		for (int i = 0; i < ytTags.size(); i++) {
			String tag = ytTags.get(i);
			String[] tag_Elements = tag.split(" ");

			if (tag_Elements[0].equals("meta")) {
				// to identify the attributes storing the video information (e.g. title, description, keywords, embedded url)
				if (tag_Elements[1].equals("name=title") || tag_Elements[1].equals("name=description")
						|| tag_Elements[1].equals("name=keywords") || tag_Elements[1].equals("property=og:video:url")) {
					
					// tag: meta name=title content=[Computer Science] Learn more about HKBU JS2510 JS2910 Science programmes (for 2021 Entry) 
                    
					String[] content = tag.split("content="); // content = ["meta name=title ","[Computer Science] Learn more about HKBU JS2510 JS2910 Science programmes (for 2021 Entry) "]

					if (tag_Elements[1].equals("property=og:video:url")) {
						// store the embedded url for video
						embedURL = content[1].trim();
					} else {
						// split the keywords
						String[] Splitwords = content[1].split("[0-9\\W]+");
						for (String str : Splitwords) {
							// store the keyword that the list does not contain.
							if (!keywords.contains(str)) {
								keywords.add(str);
							}
						}
					}

				}
			}
		}
		
		for (int j = 0; j < keywords.size(); j++) {
			// make sure that it does not only store blank space
			if (keywords.get(j).trim().length() != 0) {
				result_list.add(embedURL + " " + keywords.get(j)); // store the record
			}
		}

		return result_list;

	}

}
