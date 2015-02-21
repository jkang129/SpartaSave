package Controller;

import AmazonWebServices.AmazonWebService;
import DAO.BookSearchDAO;
import Model.Book;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for searching user queries from the SpartaSave database and Amazon Web Service.
 * @author jackkang
 */
public class SearchServlet extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        String search = request.getParameter("search");
        
        try {
            BookSearchDAO dao = new BookSearchDAO(); // Create connection to database.
            // Find the listings where the search matches the title.
            ArrayList<Book> titleSearch = dao.getUserListings("title", search, "price");
            
            // Find the listings where the search matches the author.
            ArrayList<Book> authorSearch = dao.getUserListings("author", search, "price");
            
            // Find the listings where the search matches the isbn.
            ArrayList<Book> isbnSearch = dao.getUserListings("isbn", search, "price");
            
            // Search through amazon web service.
            AmazonWebService amazon = new AmazonWebService();
            ArrayList<Book> amazonSearch = amazon.search(search);
            System.out.println(amazonSearch);
            
            // Combine the arraylists together.
            titleSearch.addAll(authorSearch);
            titleSearch.addAll(isbnSearch);
            titleSearch.addAll(amazonSearch);
            
            request.setAttribute("titleSearch", titleSearch);
            //response.sendRedirect("SearchResultsPage.jsp");
            request.getRequestDispatcher("SearchResultsPage.jsp").forward(request, response);
            
        } catch (SQLException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
