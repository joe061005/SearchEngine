package hk.edu.hkbu.comp;

import java.util.Scanner;
import java.util.regex.Pattern;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SearchEngineApplication {

	public static void main(String[] args) {
		boolean runAgain = false;
		do {
			boolean catchXError = false;
			boolean catchYError = false;
			boolean catchSeedError = false;
			Scanner scan = new Scanner(System.in);
			int x = 0;
			int y = 0;
			String seed = "";
			String regex = "((http|https)://)(www.)?" + "[a-zA-Z0-9@:%._\\+~#?&//=]" + "{2,256}\\.[a-z]"
					+ "{2,6}\\b([-a-zA-Z0-9@:%" + "._\\+~#?&//=]*)";
			Pattern p = Pattern.compile(regex);
			//Run Scanner for user input with loop
			do {
				System.out.println("Input x (Size of the URL pool): ");
				if (scan.hasNextInt()) {
					x = scan.nextInt();
					catchXError = true;
				} else {
					scan.nextLine();
					System.out.println("Error: Enter a valid Integer value.");
				}
			} while (!catchXError);
			do {
				System.out.println("Input y(Size of the processed URL pool): ");
				if (scan.hasNextInt()) {
					y = scan.nextInt();
					catchYError = true;
				} else {
					scan.nextLine();
					System.out.println("Error: Enter a valid Integer value.");
				}
			} while (!catchYError);
			scan.nextLine();
			do {
				System.out.println("Input the seed URL: ");
				if (scan.hasNext(p)) {
					seed = scan.nextLine();
					catchSeedError = true;
				} else {
					scan.nextLine();
					System.out.println("Error: Enter valid String URL value.");
				}
			} while (!catchSeedError);
			System.out.println();

			Pool pool = new Pool(x, y, seed); // "https://www.comp.hkbu.edu.hk" contain the URL pool and processed URL
												// pool
			Extract_Webpage_Elements webpageExtractor = new Extract_Webpage_Elements(); // can extract text and HTML
																						// tags
																						// for further processing
			Database db = new Database(""); // Provide path to create database

			while (pool.count_URL_Pool() > 0 && pool.count_Processed_URL_Pool() < pool.processed_URL_Pool_Limit) {
				String currentURL = pool.poll_URL_Pool(); // get URL from the queue (URL pool)
				System.out.println("Current URL:" + currentURL);

				// extract text and HTML tags
				// true if the web page exists
				// false, otherwise
				boolean success = webpageExtractor.start_Extracting(currentURL);

				System.out.println("success: " + success);
				if (success) {
					// push Keyword to database (Linkedlist<String[Type,URL,Keyword]>)
					db.pushKeyword(webpageExtractor.extractedKeyword);

					// push Image to database (Linkedlist<String[Type,URL,Keyword]>)
					db.pushIMG(webpageExtractor.extractedImage);

					// push Video to database (Linkedlist<String[Type,URL,Keyword]>)
					db.pushVideo(webpageExtractor.extractedVideo);

					pool.offer_Processed_URL_Pool(currentURL); // add the URL to the processed URL pool

					pool.offer_URL_Pool(webpageExtractor.extractedURL); // add new URLs to the URL pool
				}
			}

			System.out.println("PROCESSED: " + pool.count_Processed_URL_Pool());
			System.out.println();
			System.out.println("Enter 'Again' to run again Or Next  to run Spring Boot:");
			if (scan.hasNext("Next")) {
				System.out.println("Running Spring Boot...");
			    scan.close();
				runAgain = true;
			}else if(scan.hasNext("Again")){
				scan.nextLine();
				System.out.println("Running Again.");
				System.out.println();
			}else {
				System.out.println("Ivalid Input. Running Spring Boot...");
			    scan.close();
				runAgain = true;
			}

		} while (!runAgain);
		
		SpringApplication.run(SearchEngineApplication.class, args);

		return;
	
	}

}
