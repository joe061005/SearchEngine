package hk.edu.hkbu.comp;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Pool {
	Queue<String> url_Pool = new LinkedList<String>();  // URL pool
	Queue<String> processed_URL_Pool = new LinkedList<String>(); // processed URL pool
	int url_Pool_Limit = 0;  // maximum size of the URL pool
	int processed_URL_Pool_Limit = 0; // maximum size of the processed URL pool
	LinkedList<String> blacklist_URL = new LinkedList<String>(); // for storing the blacklist URL
	
	Pool(int url_Pool_Limit, int processed_URL_Pool_Limit, String url){  // constructor of Pool object
		this.url_Pool_Limit = url_Pool_Limit;  // set the size of the URL pool
		this.processed_URL_Pool_Limit = processed_URL_Pool_Limit; // set the size of the processed URL pool
		url_Pool.offer(url); // add the seed URL

		try {
			// read the blacklist URL file and then add them to the blackList_URL LinkedList
			File blacklist_File = new File("BlackListUrls.txt");
			Scanner reader = new Scanner(blacklist_File);
			while (reader.hasNextLine()) {
				blacklist_URL.add(reader.nextLine());
			}
		}catch(FileNotFoundException err){

		}
	}
	
	void offer_URL_Pool (LinkedList<String> list) {
		// loop through the URL list
		for(int i = 0; i < list.size(); i++) {
			
			// if the url pool is full, stop processing the url
			if(url_Pool_Limit != 0 && url_Pool.size() >= url_Pool_Limit) {
				System.out.println("");
				return;
			}

			try {
				String URLSTR = list.get(i); // get the url string
				URL url = new URL(URLSTR); // create a URL object for getting the protocol and host

				String prefix = url.getProtocol() + "://" + url.getHost(); // format: e.g. https://www.weibo.com
				String prefix2 = url.getProtocol() + "://" + prefix.substring(prefix.indexOf(".") + 1); // format: e.g. https://weibo.com
                
				//check whether the URL is in the blacklist
				//case 1. same URL (e.g. https://www.comp.hkbu.edu.hk/v1/?page=contact)
				// case2. same protocol and host (e.g. https://www.facebook.com/hkbu.comp/ -> https://www.facebook.com*)
				// case3. without www (e.g. https://www.weibo.com/hkbucs -> http://weibo.com*)

				if (blacklist_URL.contains(URLSTR) || blacklist_URL.contains(prefix + "*") || blacklist_URL.contains(prefix2 + "*")) {
					System.out.println("BlackList: " + URLSTR);
					continue;
				}

			}catch(MalformedURLException e){

			}
			
			// the url is not processed and the url pool does not contain this url.		
			if (!processed_URL_Pool.contains(list.get(i)) && !url_Pool.contains(list.get(i))) {
				url_Pool.offer(list.get(i));
				System.out.println("URL Pool Offered:" + list.get(i));
			}
			
		}
		
		System.out.println("\n");
		return;
	}
	
	// get the first url string in the queue
	String poll_URL_Pool() {
		return url_Pool.poll();
	}
	
	
	// get the number of urls in the list
	int count_URL_Pool() {
		return url_Pool.size();
	}
	
	// add the url in the processed url pool
	void offer_Processed_URL_Pool (String url) {
		if(processed_URL_Pool_Limit != 0 && processed_URL_Pool.size() >= processed_URL_Pool_Limit) {return;}
		
		processed_URL_Pool.offer(url);
		System.out.println("Processed URL Offered:" + url);
		return;
	}
	
	// get the first processed url string
	String poll_Processed_URL_Pool() {
		return processed_URL_Pool.poll();
	} 
	
	// get the number of urls in the processed url pool
	int count_Processed_URL_Pool(){
		return processed_URL_Pool.size();
	}
}

