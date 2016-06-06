package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;
//Simplified class to do parsing of fetched elements outside of the WikiPhilosophy.java
public class WikiParser {
	//to keep track of all the paragraphs found
	private Elements paragraphs;
	//stack to make sure all parentheses open and close
	//later used in a method to check parentheses
	private Stack<String> parentheses;
	
	//creates parser with elements (paragraphs obtained by wikifetcher)
	public WikiParser(Elements paras){
		paragraphs = paras;
		parentheses = new Stack<String>();
	}
	
	//finds the first link on a page and returns it
	public Element findLink(){
		//loops through and checks each paragraph
		//may not be in first
		for(Element para: paragraphs){
			Element first = findLinkPara(para);
			if(first != null){
				return first;
			}
		}
		return null;
	}
	//method to search paragraphs individually for valid links
	public Element findLinkPara(Node link){
		//creates node iterable so we can iterate over nodes
		Iterable<Node> nodeIter = new WikiNodeIterable(link);
		for(Node node: nodeIter){
			//if the node is a text node, check its parentheses
			if(node instanceof TextNode){
				checkTextNode((TextNode)node);
			}
			//if the node is an element, check it
			if(node instanceof Element){
				Element first = checkElement((Element) node);
				//if it's valid (not null), return it
				if(first != null){
					return first;
				}
			}
		}
		return null;
	}
	//Checks parentheses in text node
	private void checkTextNode(TextNode node){
		StringTokenizer parts = new StringTokenizer(node.text(), " ()", true);
		while(parts.hasMoreTokens()){
			String tok = parts.nextToken();
			if(tok.equals("(")){
				parentheses.push(tok);
			}
			if(tok.equals(")")){
				//if the parentheses stack is empty when coming accross ")", then not balanced
				if(parentheses.isEmpty()){
					System.out.println("*Parentheses unbalanced*");
				}
				parentheses.pop();
			}
		}
	}
	//checks to see if element is a VALID link, then returns first valid link
	private Element checkElement(Element node){
		if(node.tagName().equals("a") && !isItalics(node) && parentheses.isEmpty()){
			return node;
		}
		else{
			return null;
		}
	}
	//helper method to check if a link is in italics
	private boolean isItalics(Element st){
		//iterates throught the tags parents to make sure none are tagged "i" or "em"
		for(Element e = st; st != null; st = st.parent()){
			if(e.tagName().equals("i") || e.tagName().equals("em")){
				return true;
			}
		}
		return false;
	}
}
