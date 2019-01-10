package utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Helper for message in JSP
 *
 * @autor Alex
 */
public class PageMessageUtil {

    /**
     * Move message from session scope in request scope,
     * need to clear message when redirecting,
     * usually use in doGet
     *
     * @param request see {@link HttpServletRequest}
     */
    public static void clearPageMessageForDoGet(HttpServletRequest request) {
        request.setAttribute("errorMessage", request.getSession().getAttribute("errorMessage"));
        request.getSession().removeAttribute("errorMessage");
        request.setAttribute("warningMessage", request.getSession().getAttribute("warningMessage"));
        request.getSession().removeAttribute("warningMessage");
        request.setAttribute("infoMessage", request.getSession().getAttribute("infoMessage"));
        request.getSession().removeAttribute("infoMessage");
        request.setAttribute("successMessage", request.getSession().getAttribute("successMessage"));
        request.getSession().removeAttribute("successMessage");
    }

    /**
     * Move message from session scope in request scope
     * Usually use in doPost
     *
     * @param request see {@link HttpServletRequest}
     */
    public static void clearPageMessageForDoPost(HttpServletRequest request) {
        request.getSession().removeAttribute("errorMessage");
        request.getSession().removeAttribute("successMessage");
        request.getSession().removeAttribute("infoMessage");
        request.getSession().removeAttribute("warningMessage");
    }

    /**
     * Displays a error message on the JSP
     * with status code 503 - SC_SERVICE_UNAVAILABLE
     *
     * @param request see {@link HttpServletRequest}
     * @param response  see {@link HttpServletResponse}
     * @param path JSP filename to which forwards
     * @param message error message
     *
     * @throws ServletException see {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     * @throws IOException see {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     */
    public static void printServiceUnavailableErrorMessage(HttpServletRequest request, HttpServletResponse response,
                                         String path, String message) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setAttribute("errorMessage", message);
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        request.getRequestDispatcher(path).forward(request, response);
    }

    /**
     * Displays a error message on the JSP
     * with status code 400 - SC_BAD_REQUEST
     *
     * @param request see {@link HttpServletRequest}
     * @param response  see {@link HttpServletResponse}
     * @param path JSP filename to which forwards
     * @param message error message
     *
     * @throws ServletException see {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     * @throws IOException see {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     */
    public static void printBadRequestErrorMessage(HttpServletRequest request, HttpServletResponse response, String path, String message) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setAttribute("errorMessage", message);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        request.getRequestDispatcher(path).forward(request, response);
    }
}
