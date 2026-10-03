package org.edu.lab4;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Интернационализированный сервлет библиотечного каталога.
 * <p>
 * Язык интерфейса выбирается параметром запроса {@code lang} ({@code ru} или {@code en}).
 * Все тексты интерфейса загружаются из файлов ресурсов {@code messages_*.properties}.
 */
public class LocaleServlet extends HttpServlet {
    private final List<Book> books = new ArrayList<>();

    /**
     * Заполняет каталог книгами.
     */
    @Override
    public void init() {
        books.add(new Book("Булгаков", "Мастер и Маргарита", 1967));
        books.add(new Book("Достоевский", "Преступление и наказание", 1866));
        books.add(new Book("Толстой", "Война и мир", 1869));
    }

    /**
     * Определяет локаль по параметру {@code lang} и выводит страницу на выбранном языке.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException при ошибке работы сервлета
     * @throws IOException      при ошибке ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String lang = req.getParameter("lang");
        Locale locale = "en".equalsIgnoreCase(lang) ? Locale.of("en", "US") : Locale.of("ru", "RU");
        ResourceBundle res = ResourceBundle.getBundle("messages", locale);
        String other = "en".equalsIgnoreCase(lang) ? "ru" : "en";
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        out.println("<title>" + res.getString("title") + "</title></head>");
        out.println("<body style='font-family:Arial;margin:30px'>");
        out.println("<h1>" + res.getString("greeting") + "</h1>");
        out.println("<p>" + res.getString("today") + ": " + df.format(new Date()) + "</p>");
        out.println("<table border='1' cellpadding='6' style='border-collapse:collapse'>");
        out.println("<tr><th>" + res.getString("author") + "</th><th>" + res.getString("book")
                + "</th><th>" + res.getString("year") + "</th></tr>");
        for (Book b : books) {
            out.println("<tr><td>" + b.getAuthor() + "</td><td>" + b.getTitle() + "</td><td>"
                    + b.getYear() + "</td></tr>");
        }
        out.println("</table>");
        out.println("<p><a href='?lang=" + other + "'>" + res.getString("language_button") + "</a></p>");
        out.println("</body></html>");
    }
}
