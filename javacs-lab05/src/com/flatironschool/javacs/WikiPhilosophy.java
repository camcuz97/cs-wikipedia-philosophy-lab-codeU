package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	//need to crawl webpage
	//fetch, parse, extract
	//creating seperate class to handle parsing because this would get too cluttered
	final static WikiFetcher wf = new WikiFetcher();
	//arraylist to keep track of visited links
	//initially used array but arraylist has better methods
	static ArrayList<String> visitedLinks = new ArrayList<String>();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//sets urls then calls helper
		String endURL = "https://en.wikipedia.org/wiki/Philosophy";
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		testLink(url, endURL);
	}
	public static void testLink(String sourceURL, String destURL) throws IOException{
		//should find philosophy in under 8 tries (7)
		int tempLim = 8; 
		//will change as moving link to link
		String link = sourceURL; 
		//iterates only a certain amount of times to prevent a weird infinite loop
		for(int i = 0; i < tempLim; i++){
			//making sure it's iterating properly
			System.out.println(i);
			if(visitedLinks.contains(link)){
				//if visitedlinks arraylist has the link, in loop
				System.out.println("Ending b/c Loop");
				return;
			}
			else{
				visitedLinks.add(link);
			}
			//printing link for debugging purposes
			System.out.println("Getting " + link);
			//calls wikifetcher
			Elements paragraphs = wf.fetchWikipedia(link);
			//creates parser then uses it to find first valid link
			WikiParser wp = new WikiParser(paragraphs);
			Element element = wp.findLink();
			if(element == null){
				System.out.println("*Could not find valid link*");
				return;
			}
			//prints link we're checking next
			System.out.println(element.text());
			//sets link to be the next url
			link = element.attr("abs:href");
			//returns if found philosophy page
			if (link.equals(destURL)) {
				System.out.println("Done!");
				break;
			}
		}
	}
	//changed visited from array to arraylist for simplicity, so following method is useless
	//left in in case i switch back
	/**
	*public static boolean contain(String[] arr, String target){
	*	for(String str: arr){
	*		if(str.equals(target)){
	*			return true;
	*		}
	*	}
	*	return false;
	*}
	*/
}
