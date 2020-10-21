package com.linkedin.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "CatalogServlet", asyncSupported = true)
public class CatalogServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AsyncContext asyncContext = request.startAsync();

        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);

                    System.out.println("Print the response");
                    System.out.println("Response returned by: " + Thread.currentThread().getName());

                    returnResponse(request, response);

                    asyncContext.complete();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Initial Request: " + Thread.currentThread().getName());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append(request.getParameter("name"));
        response.setHeader("someHeader", "someHeaderValue");
        response.addCookie(new Cookie("someCookie", "someCookieValue"));
    }

    private void returnResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String manufacturer = request.getParameter("manufacturer");
        String sku = request.getParameter("sku");

        response.setHeader("someHeader", "someHeaderValue");
        response.addCookie(new Cookie("someCookie", "someCookieValue"));

        Catalog.addItem(new CatalogItem(name, manufacturer, sku));

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head></head>");
        out.println("<body>");
        out.println("<table>");

        for (CatalogItem item : Catalog.getItems()) {
            out.println("<tr>");
            out.println("<td>");
            out.print(item.getName());
            out.println("</td>");
            out.println("<td>");
            out.print(item.getManufacturer());
            out.println("</td>");
            out.println("<td>");
            out.print(item.getSku());
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }
}
