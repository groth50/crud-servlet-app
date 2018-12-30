package utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper for message in JSP
 */
public class PageMessageUtil {

    /**
     * Перекладывает информационные сообщения из session scope в request scope
     * @param request
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
     * Удаляет информационные сообщения из session scope
     * @param request
     */
    public static void clearPageMessageForDoPost(HttpServletRequest request) {
        request.getSession().removeAttribute("errorMessage");
        request.getSession().removeAttribute("successMessage");
        request.getSession().removeAttribute("infoMessage");
        request.getSession().removeAttribute("warningMessage");}

}
