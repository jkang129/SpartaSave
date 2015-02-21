package AmazonWebServices;

import Model.Book;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Amazon Web Service client that sends search requests and consumes XML response.
 * @author jackkang
 */
public class AmazonWebService {

    private static final String SECRET_KEY = "ZXOuuOvm7/bfPQ09sk2dE7eochlXU1dMeH2pcUvP";
    private static final String AWS_KEY = "AKIAJITWXSYYJNQWRX4Q";
    private static final String ASSOCIATE_TAG = "spart00-20";
    private static final String ENDPOINT = "ecs.amazonaws.com";
    
    private String sortOrder; // sort results by: relevancerank, pricerank, titlerank, daterank
    private String currentPage; // display search result page number: 1 to 10 max pages
    private int totalPages = 0; // number of pages in search results
    private int numberResults = 0; // total number of search results
    private int pageNumber; // search result page to display
    
    
    /**
     * Constructor
     */
    public AmazonWebService() { }
    
    /**
     * Sends search request to Amazon Web Service and returns books matching the given query using the Product Advertising API.
     * @param query the string term to search for
     * @return resulting search query as an array list of Book objects
     * @throws InvalidKeyException
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     */
    public ArrayList<Book> search(String query) throws InvalidKeyException, IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException {
    	SignedRequestsHelper helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_KEY, SECRET_KEY);
        Map<String, String> params = new HashMap<String, String>(); // search parameters as key, value pairs
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemSearch");
        params.put("Keywords", query);
        params.put("SearchIndex", "Books");
        params.put("ResponseGroup", "Medium");
        params.put("AssociateTag", ASSOCIATE_TAG);
        // optional additional search parameters
        //params.put("Author", "");
        //params.put("Title", "");
        //params.put("Power","");
        //params.put("Sort", sortOrder);
        //params.put("ItemPage", pageNumber);
        
        String url = helper.sign(params);
        ArrayList<Book> searchResults = new ArrayList<Book>();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            AmazonSAXHandler handler = new AmazonSAXHandler();
            parser.parse(url, handler);
            searchResults = handler.getBookList();
            for (Book bk : handler.getBookList()) {
            	handler.printBookDetail(bk);
            }
            totalPages = handler.getTotalPages();
            numberResults = handler.getTotalResults();
        } catch (Exception ex) {
            Logger.getLogger(AmazonWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    	return searchResults;
    }
    
    /**
     * Parse the content of the given URI as an XML document and return a new DOM Document object
     * @param url the web address of the given resource
     * @return response as XML document object
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException 
     */
    private static Document getResponse(String url) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(url);
        return doc;
    }
    
    /**
     * Print the XML document out to console as string
     * @param doc the XML document
     * @throws TransformerException
     * @throws FileNotFoundException 
     */
    private static void printResponse(Document doc) throws TransformerException, FileNotFoundException {
    	Transformer trans = TransformerFactory.newInstance().newTransformer();
        Properties props = new Properties();
        props.put(OutputKeys.INDENT, "yes");
        trans.setOutputProperties(props);
        StreamResult res = new StreamResult(new StringWriter());
        DOMSource src = new DOMSource(doc);
        trans.transform(src, res);
        String toString = res.getWriter().toString();
        System.out.println(toString);
    }
    
}