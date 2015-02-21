package AmazonWebServices;

import Model.*;

import java.util.ArrayList;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Parses XML response from Amazon Web Service and creates Book objects for display in search results
 * @author jackkang
 */
public class AmazonSAXHandler extends DefaultHandler {
    private final StringBuilder content = new StringBuilder(); 
    // Use Stack data structure to keep track of XML tag nesting level
    private final Stack<String> tagStack = new Stack<String>();
    
    private int totalResults;
    private int totalPages;
    private Book currentBook;
    private ArrayList<Book> bookList;
    
    /**
     * Override default startElement method to do appropriate processing when parser encounters XML opening tag
     * @param uri the namespace URI of an attribute
     * @param localName the local name of an attribute
     * @param qName the qualified (prefixed) name of an attribute
     * @param attributes the XML's attributes
     * @throws SAXException 
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        pushTag(qName);         // push current opening tag onto stack
        content.setLength(0);   // reset stringbuilder's contents
    	
        String tag = null;
    	String parentTag = null;
        if(!tagStack.empty()) {
            tag = peekTag();    // get current opening tag
            popTag();
        }
    	
    	if(!tagStack.empty()) {
            parentTag = peekTag(); // get parent of current opening tag if exists
        }
        
    	pushTag(tag);   // current opening tag on stack
    	
        switch (tag) {
        	case "Items":
                    if (parentTag.equals("ItemSearchResponse")) {   // beginning of new Item list
                        bookList = new ArrayList<Book>();
                    }
                    break;
        	
        	case "Item":
                    if (parentTag.equals("Items")) {    // opening tag for new Book item
                        System.out.println("Start Element: " + qName);
                        currentBook = new Book();
                    }
                    break;
        	
        	default:
                    // do nothing
                    break;
        }
    }

    /**
     * Override default endElement method to do appropriate processing when parser encounters XML closing tag
     * @param uri the namespace URI of an attribute
     * @param localName the local name of an attribute
     * @param qName the qualified (prefixed) name of an attribute
     * @throws SAXException 
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	String tag = null;
    	String parentTag = null;
    	String grandParentTag = null;
    	
    	if(!tagStack.empty()) {
            tag = peekTag();
            popTag();
        }
    	
    	if(!tagStack.empty()) {
            parentTag = peekTag();
        }
        
    	switch (tag) {
        	case "Item":
                    if (parentTag.equals("Items")) {
                        System.out.println("End Element: " + tag);
                        System.out.println();
                        bookList.add(currentBook);
                    }	
                    break;
        	
        	case "DetailPageURL":
                    if (parentTag.equals("Item")) {
                        String detailPageURL = content.toString().trim();
                        System.out.println("DetailPageURL: " + detailPageURL);
                        currentBook.setDetailPageURL(detailPageURL);
                    }
                    break;
    			
    		case "URL":
                    if (!tagStack.empty()) {
                        popTag();
                        grandParentTag = peekTag();
                        pushTag(parentTag);
                    }
                    if (parentTag.equals("MediumImage") && grandParentTag.equals("Item")) {
                        String mediumImageURL = content.toString().trim();
                        System.out.println("MediumImageURL: " + mediumImageURL);
                        currentBook.setImageURL(mediumImageURL);
                    }
                    break;
    			
    		case "Author":
                    if (parentTag.equals("ItemAttributes")) {
                        String author = content.toString().trim();
                        System.out.println("Author: " + author);
                        currentBook.getAuthors().add(author);
                    }
                    break;
    		
    		case "ISBN":
                    if (parentTag.equals("ItemAttributes")) {
                        String isbn = content.toString().trim();
                        System.out.println("ISBN: " + isbn);
                        currentBook.setIsbn(isbn);
                    }
                    break;
    		
    		case "Title":
                    if (parentTag.equals("ItemAttributes")) {
                        String title = content.toString().trim();
                        System.out.println("Title: " + title);
                        currentBook.setTitle(title);
                    }
                    break;
    			
    		case "FormattedPrice":
                    if (parentTag.equals("ListPrice")) {
                        String listPrice = content.toString().trim();
                        System.out.println("ListPrice: " + listPrice);
                        currentBook.setListPrice(listPrice);
                    } else if (parentTag.equals("LowestNewPrice")) {
                        String lowestNewPrice = content.toString().trim();
                        System.out.println("LowestNewPrice: " + lowestNewPrice);
                        currentBook.setLowestNewPrice(lowestNewPrice);
                    } else if (parentTag.equals("LowestUsedPrice")) {
                        String lowestUsedPrice = content.toString().trim();
                        System.out.println("LowestUsedPrice: " + lowestUsedPrice);
                        currentBook.setLowestPrice(lowestUsedPrice);
                    }
                    break;
    		
    		case "TotalResults":
                    if (parentTag.equals("Items")) {
                        totalResults = Integer.valueOf(content.toString().trim());
                    }
                    break;
    			
    		case "TotalPages":
                    if (parentTag.equals("Items")) {
                        totalPages = Integer.valueOf(content.toString().trim());
                    }
                    break;
    			
    		default:
                    // do nothing
                    break;
        }
    }
    
    /**
     * Override default characters method to use StringBuilder's append method
     * @param ch the characters from the XML document
     * @param start the start position in the array
     * @param length the number of characters to read from the array
     * @throws SAXException 
     */
    public void characters(char ch[], int start, int length) throws SAXException {
    	content.append(ch, start, length);
    }
    
    /**
     * Add XML tag to the top of the stack.
     * @param tag XML
     */
    private void pushTag(String tag) {
    	tagStack.push(tag);
    }
    
    /**
     * Remove and return XML tag at the top of the stack.
     * @return XML tag
     */
    private String popTag() {
        return tagStack.pop();
    }
    
    /**
     * Examine the XML tag at the top of the stack without removing.
     * @return XML tag
     */
    private String peekTag() {
        return tagStack.peek();
    }
    
    /**
     * Get the stored list of Book objects.
     * @return array list of Books
     */
    public ArrayList<Book> getBookList() {
    	return this.bookList;
    }
    
    /**
     * Get total number of search results.
     * @return int number of results
     */
    public int getTotalResults() {
    	return this.totalResults;
    }
    
    /**
     * Get total number of page results.
     * @return int number of pages
     */
    public int getTotalPages() {
    	return this.totalPages;
    }
    
    /**
     * Print out the Book's attributes as string.
     * @param bk the book object
     */
    public void printBookDetail(Book bk) {
    	System.out.println("DetailPageURL: " + bk.getDetailPageURL());
    	System.out.println("MediumImageURL: " + bk.getImageURL());
    	for (int i = 0; i < bk.getAuthors().size(); i++) {
            System.out.println("Author: " + bk.getAuthors().get(i));
    	}
    	System.out.println("ISBN: " + bk.getIsbn());
    	System.out.println("ListPrice: " + bk.getListPrice());
    	System.out.println("Title: " + bk.getTitle());
    	System.out.println("LowestNewPrice: " + bk.getLowestNewPrice());
    	System.out.println("LowestUsedPrice: " + bk.getLowestPrice());
    	System.out.println();
    }
    
}