package hk.edu.hkbu.comp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MyController {

	@GetMapping("load")
	@ResponseBody
	String load(@RequestParam(name = "query", required = false, defaultValue = "") String query) {

		String html = ""; // html to be sent to the user
		String[] queryArray = query.split(" "); // Split the query by space

		// separate operations by the length of the query
		// Maximum of 4 parameters can be included search in the search bar
		if (queryArray.length == 1) {
			if (queryArray[0].equals("")) {
				html += " <h3>No search parameters given. Please input text in the search field.</h3>";
			} else if (queryArray[0].startsWith("-")) {
				html += keywordNotSearch(queryArray[0].substring(1));
			} else {
				html += keywordSearch(queryArray[0]);
			}
		} else if (queryArray.length == 2) {
			if (queryArray[0].toLowerCase().equals("image") && queryArray[1].startsWith("-")) {
				html += imageNotSearch(queryArray[1]);
			} else if (queryArray[0].toLowerCase().equals("image")) {
				html += imageSearch(queryArray[1]);
			} else if (queryArray[0].toLowerCase().equals("video") && queryArray[1].startsWith("-")) {
				html += videoNotSearch(queryArray[1].substring(1));
			} else if (queryArray[0].toLowerCase().equals("video")) {
				html += videoSearch(queryArray[1].substring(1));
			} else {
				html += keywordAndSearch(queryArray[0], queryArray[1]);
			}
		} else if (queryArray.length == 3) {
			if (queryArray[1].toLowerCase().equals("or")) {
				html += keywordOrSearch(queryArray[0], queryArray[2]);
			} else if (queryArray[0].toLowerCase().equals("image")) {
				html += imageAndSearch(queryArray[1], queryArray[2]);
			} else if (queryArray[0].toLowerCase().equals("video")) {
				html += videoAndSearch(queryArray[1], queryArray[2]);
			}
		} else if (queryArray.length == 4) {
			if (queryArray[0].toLowerCase().equals("image") && queryArray[2].toLowerCase().equals("or")) {
				html += imageOrSearch(queryArray[1], queryArray[3]);
			} else if (queryArray[0].toLowerCase().equals("video") && queryArray[2].toLowerCase().equals("or")) {
				html += videoOrSearch(queryArray[1], queryArray[3]);
			}
		} else {
			html += "<h3>To many parameters. Use up to 4 parameters separated by spaces.</h3> ";
		}

		return html;

	}

	// Read the database and split the records according to the delimiter
	List<List<String>> returnAllList() {
		String regex = "\\{(.*?)\\}";
		String regex2 = "\\.,eD(.*?)\\,./";
		Pattern p = Pattern.compile(regex);
		Pattern p1 = Pattern.compile(regex2);
		String line = "";
		List<List<String>> allRecordsList = new ArrayList<List<String>>();
		try {
			File file = new File("Search_Engine_Database.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				Matcher m = p.matcher(line);
				while (m.find()) {
					Matcher m1 = p1.matcher(m.group(1) + ",./");
					List<String> piecesRecordsList = new ArrayList<String>();
					while (m1.find()) {
						piecesRecordsList.add(m1.group(1));
					}
					allRecordsList.add(piecesRecordsList);
				}
			}
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println(ioe);
		}
		return allRecordsList;
	}

	// method for searching for a single keyword
	String keywordSearch(String keyword) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();
		html += "<h3>Showing keyword results for: " + keyword + "</h3> ";

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("web")) {
				if (allRecordsList.get(i).get(2).toLowerCase().equals(keyword.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h1>" + allRecordsList.get(i).get(3) + "</h1> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>" + allRecordsList.get(i).get(1) + "</a>"
							+ "<p>-----------------------------------------------------------------------</p>";
				}
			}
		}

		if (html.equals("<h3>Showing keyword results for: " + keyword + "</h3> ")) {
			html += "<h3>No keyword results found.</h3> ";
		}

		return html;
	}

	String keywordNotSearch(String keyword) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();
		List<String> getURLlist = new ArrayList<String>();

		html += "<h3>Showing keyword results for not: " + keyword + "</h3> ";

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("web")) {
				if (allRecordsList.get(i).get(2).toLowerCase().equals(keyword.toLowerCase())) {
					if (!getURLlist.contains(allRecordsList.get(i).get(1))) {
						getURLlist.add(allRecordsList.get(i).get(1));
					}

				}

			}
		}
		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("web")) {
				if (!allRecordsList.get(i).get(2).toLowerCase().equals(keyword.toLowerCase())
						&& !allRecordsList.get(i).get(1).contains(getURLlist.toString())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h1>" + allRecordsList.get(i).get(3) + "</h1> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>" + allRecordsList.get(i).get(1) + "</a>"
							+ "<p>-----------------------------------------------------------------------</p>";
				}

			}
		}

		if (html.equals("<h3>Showing keyword results for not: " + keyword + "</h3> ")) {
			html += "<h3>No keyword results found.</h3> ";
		}

		return html;
	}

	// Iterate through the loop twice to get check if both keywords are in link
	String keywordAndSearch(String keyword1, String keyword2) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();
		List<String> getURLlist = new ArrayList<String>();
		html += "<h3>Showing keyword results for: " + keyword1 + " and " + keyword2 + "</h3> ";

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("web")) {
				if (allRecordsList.get(i).get(2).toLowerCase().equals(keyword1.toLowerCase())) {
					getURLlist.add(allRecordsList.get(i).get(1));
					System.out.println(allRecordsList.get(i).get(1));
				}

			}
		}
		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("web")) {
				if (allRecordsList.get(i).get(2).toLowerCase().equals(keyword2.toLowerCase())
						&& getURLlist.contains(allRecordsList.get(i).get(1))) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>" + allRecordsList.get(i).get(1) + "</a>"
							+ "<h1>" + allRecordsList.get(i).get(3) + "</h1> ";
				}

			}
		}

		if (html.equals("<h3>Showing keyword results for: " + keyword1 + " and " + keyword2 + "</h3> ")) {
			html += "<h3>No keyword results found.</h3> ";
		}

		return html;
	}

	String keywordOrSearch(String keyword1, String keyword2) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();
		html += "<h3>Showing keyword results for: " + keyword1 + " or " + keyword2 + "</h3> ";

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("web")) {
				if (allRecordsList.get(i).get(2).toLowerCase().equals(keyword1.toLowerCase())
						|| allRecordsList.get(i).get(2).toLowerCase().equals(keyword2.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>" + allRecordsList.get(i).get(1) + "</a>"
							+ "<h1>" + allRecordsList.get(i).get(3) + "</h1> ";
				}

			}
		}

		if (html.equals("<h3>Showing keyword results for: " + keyword1 + " or " + keyword2 + "</h3> ")) {
			html += "<h3>No keyword results found.</h3> ";
		}

		return html;
	}

	String imageSearch(String imgName) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("img")) {
				if (allRecordsList.get(i).get(2).toLowerCase().contains(imgName.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3>" + "<a href='" + allRecordsList.get(i).get(1)
							+ "'target='_blank'>Got to Webpage</a>" + "<img src='" + allRecordsList.get(i).get(3)
							+ "' onerror=\"this.onerror=null;this.src='https://bitsofco.de/content/images/2018/12/broken-1.png';\" style='width:300px; height:300px; object-fit: contain;' >";
				}

			}
		}
		if (html.equals("")) {
			html += "<h3>No image results found.</h3> ";
		}
		return html;
	}

	String imageNotSearch(String imgName) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("img")) {
				if (!allRecordsList.get(i).get(2).toLowerCase().contains(imgName.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>Got to Webpage</a>" + "<img src='"
							+ allRecordsList.get(i).get(3)
							+ "' onerror=\"this.onerror=null;this.src='https://bitsofco.de/content/images/2018/12/broken-1.png';\" style='width:300px; height:300px; object-fit: contain;' >";
				}

			}
		}
		if (html.equals("")) {
			html += "<h3>No image results found.</h3> ";
		}
		return html;
	}

	String imageAndSearch(String imgName1, String imgName2) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("img")) {
				if (allRecordsList.get(i).get(2).toLowerCase().contains(imgName1.toLowerCase())
						&& allRecordsList.get(i).get(2).toLowerCase().contains(imgName2.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>Got to Webpage</a>" + "<img src='"
							+ allRecordsList.get(i).get(3)
							+ "' onerror=\"this.onerror=null;this.src='https://bitsofco.de/content/images/2018/12/broken-1.png';\" style='width:300px; height:300px; object-fit: contain;' >";
				}

			}
		}
		if (html.equals("")) {
			html += "<h3>No image results found.</h3> ";
		}
		return html;
	}

	String imageOrSearch(String imgName1, String imgName2) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("img")) {
				if (allRecordsList.get(i).get(2).toLowerCase().contains(imgName1.toLowerCase())
						|| allRecordsList.get(i).get(2).toLowerCase().contains(imgName2.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>Got to Webpage</a>" + "<img src='"
							+ allRecordsList.get(i).get(3)
							+ "' onerror=\"this.onerror=null;this.src='https://bitsofco.de/content/images/2018/12/broken-1.png';\" style='width:300px; height:300px; object-fit: contain;' >";
				}

			}
		}
		if (html.equals("")) {
			html += "<h3>No image results found.</h3> ";
		}
		return html;
	}

	String videoSearch(String videoName) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("vdo")) {
				if (allRecordsList.get(i).get(2).toLowerCase().contains(videoName.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>Got to Webpage</a>"
							+ "<iframe width='220' height='140' src='" + allRecordsList.get(i).get(3) + "'> </iframe>";
				}
			}
		}
		if (html.equals("")) {
			html += "<h3>No video results found.</h3> ";
		}
		return html;
	}

	String videoNotSearch(String videoName) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("vdo")) {
				if (!allRecordsList.get(i).get(2).toLowerCase().contains(videoName.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>Got to Webpage</a>"
							+ "<iframe width='220' height='140' src='" + allRecordsList.get(i).get(3) + "'> </iframe>";
				}
			}
		}
		if (html.equals("")) {
			html += "<h3>No video results found.</h3> ";
		}
		return html;
	}

	String videoAndSearch(String videoName1, String videoName2) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("img")) {
				if (allRecordsList.get(i).get(2).toLowerCase().contains(videoName1.toLowerCase())
						&& allRecordsList.get(i).get(2).toLowerCase().contains(videoName2.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>Got to Webpage</a>" + "<img src='"
							+ allRecordsList.get(i).get(3)
							+ "' style='width:300px; height:300px; object-fit: contain;' >";
				}

			}
		}
		if (html.equals("")) {
			html += "<h3>No image results found.</h3> ";
		}
		return html;
	}

	String videoOrSearch(String videoName1, String videoName2) {
		String html = "";
		List<List<String>> allRecordsList = returnAllList();

		for (int i = 0; i < allRecordsList.size(); i++) {

			if (allRecordsList.get(i).get(0).equals("img")) {
				if (allRecordsList.get(i).get(2).toLowerCase().contains(videoName1.toLowerCase())
						|| allRecordsList.get(i).get(2).toLowerCase().contains(videoName2.toLowerCase())) {
					System.out.println(allRecordsList.get(i).get(1));
					html += "<h3>" + allRecordsList.get(i).get(2) + "</h3> " + "<a href='"
							+ allRecordsList.get(i).get(1) + "'target='_blank'>Got to Webpage</a>" + "<img src='"
							+ allRecordsList.get(i).get(3)
							+ "' style='width:300px; height:300px; object-fit: contain;' >";
				}

			}
		}
		if (html.equals("")) {
			html += "<h3>No image results found.</h3> ";
		}
		return html;
	}

}
