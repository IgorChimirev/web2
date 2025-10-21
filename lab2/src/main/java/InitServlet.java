
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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Locale;
import java.util.logging.Logger;

public class InitServlet extends HttpServlet {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private DateTimeFormatter httpDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME
            .withLocale(Locale.US)
            .withZone(ZoneId.of("GMT"));

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<Result> results = (List<Result>) session.getAttribute("results");
        if (results == null) {
            results = new ArrayList<>();
            session.setAttribute("results", results);
        }
        Long lastModifiedObj = (Long) session.getAttribute("lastModified");
        long currentTime = System.currentTimeMillis();
        long lastModified = lastModifiedObj != null ? lastModifiedObj : (currentTime / 1000) * 1000;  // Обрезать до секунды
        session.setAttribute("lastModified", lastModified);  // Ensure it's set

        // Set Last-Modified header BEFORE writing response
        ZonedDateTime lastModZdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastModified), ZoneId.of("GMT"));
        response.setHeader("Last-Modified", httpDateFormat.format(lastModZdt));

        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.print(mapper.writeValueAsString(results));
        out.flush();
    }
}