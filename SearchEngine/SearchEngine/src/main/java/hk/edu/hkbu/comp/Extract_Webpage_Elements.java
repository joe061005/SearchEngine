package hk.edu.hkbu.comp;

import javax.swing.text.html.*;
import javax.swing.text.html.HTML.*;
import javax.swing.text.html.HTMLEditorKit.*;
import javax.swing.text.html.parser.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.text.*;
import java.util.LinkedList;

class Extract_Webpage_Elements extends ParserCallback {

	// for callback
	LinkedList<String> body_Text = new LinkedList<String>(); // store the text from the HTML document
	LinkedList<String> all_Tag = new LinkedList<String>(); // store all tags from the HTML document
	String webTitle = ""; // store the web title
	boolean isTitle = false; // true when the 'title' tag is being processed

	// for storing the URLs, images, videos, and keywords
	LinkedList<String> extractedURL = new LinkedList<String>(); // store the URLs
	LinkedList<String[]> extractedImage = new LinkedList<String[]>(); // store the images
	LinkedList<String[]> extractedVideo = new LinkedList<String[]>(); // store the videos
	LinkedList<String[]> extractedKeyword = new LinkedList<String[]>(); // store the keywords

	// collect all tags and text first, then process them.
	@Override
	public void handleText(char[] data, int pos) {
		if (isTitle) { // get the web title
			webTitle = new String(data);
			isTitle = false;
			System.out.println("Title: " + webTitle);
		}
		body_Text.add(new String(data));

	}

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attr, int pos) {
		if (tag.toString().equals("title")) { // the program is processing the title tag
			isTitle = true;
		}
		this.all_Tag.add(tag + " " + attr);
	}

	public void handleSimpleTag(Tag tag, MutableAttributeSet attr, int pos) {
		this.all_Tag.add(tag + " " + attr);
	}

	// extract text and tags from the HTML document
	boolean start_Extracting(String urlString) {

		try {
			Extract_Webpage_Elements callback = new Extract_Webpage_Elements();
			ParserDelegator parser = new ParserDelegator();
			Extract_URL urlExtractor = new Extract_URL();
			Extract_Image imageExtractor = new Extract_Image();
			Extract_Video videoExtractor = new Extract_Video();
			Extract_Keyword keywordExtractor = new Extract_Keyword();

			// read the HTML document
			URL url = new URL(urlString);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			parser.parse(reader, callback, true);

			// Extract Keyword Call
			this.extractedKeyword = keywordExtractor.extract_Keyword(callback.webTitle, callback.body_Text, urlString);
			// Extract Image Call
			this.extractedImage = imageExtractor.extract_Image(callback.all_Tag, urlString);
			// Extract URL Call
			this.extractedURL = urlExtractor.extract_URL(callback.all_Tag, urlString);

			// Extract Video Call
			this.extractedVideo = videoExtractor.extract_Video(callback.all_Tag, urlString);

		} catch (IOException error) {
			System.out.println(error);
			return false;
		}

		return true;
	}

	// for extracting the tags in video URL
	void start_Extracting_video(String urlString) {
		try {
			Extract_Webpage_Elements callback = new Extract_Webpage_Elements();
			ParserDelegator parser = new ParserDelegator();

			URL url = new URL(urlString);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			parser.parse(reader, callback, true);

			all_Tag = callback.all_Tag;

		} catch (IOException error) {

		}

	}
}
