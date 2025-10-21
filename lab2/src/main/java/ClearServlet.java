// Modified file: ClearServlet.java
import utils.Result;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class ClearServlet extends HttpServlet {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private DateTimeFormatter httpDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME
            .withLocale(Locale.US)
            .withZone(ZoneId.of("GMT"));

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        long currentTime = System.currentTimeMillis();
        long lastModified = (currentTime / 1000) * 1000;  // Обрезать до секунды
        session.setAttribute("results", new ArrayList<Result>());
        session.setAttribute("lastModified", lastModified);  // Обновить обрезанное значение

        // Set Last-Modified header BEFORE writing response
        ZonedDateTime lastModZdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastModified), ZoneId.of("GMT"));
        response.setHeader("Last-Modified", httpDateFormat.format(lastModZdt));

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("[]");
        out.flush();
    }
}