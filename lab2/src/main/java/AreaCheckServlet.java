
import utils.HitChecker;
import utils.Result;
import utils.Validator;
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
import com.fasterxml.jackson.databind.ObjectMapper;

public class AreaCheckServlet extends HttpServlet {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private DateTimeFormatter httpDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME
            .withLocale(Locale.US)
            .withZone(ZoneId.of("GMT"));

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            logger.info("meow2");
            long start = System.currentTimeMillis();
            float x = Float.parseFloat(request.getAttribute("x").toString());
            float y = Float.parseFloat(request.getAttribute("y").toString());
            float r = Float.parseFloat(request.getAttribute("r").toString());
            String timezone = request.getAttribute("timezone") != null ? request.getAttribute("timezone").toString() : "UTC";
            if (Validator.validateX(x) && Validator.validateY(y) && Validator.validateR(r)) {
                Result result = new Result();
                result.setValue(String.valueOf(HitChecker.hit(x, y, r)));
                result.setX(String.valueOf(x));
                result.setY(String.valueOf(y));
                result.setR(String.valueOf(r));
                result.setTimezone(timezone);
                long currentTime = System.currentTimeMillis();
                long lastModified = (currentTime / 1000) * 1000;  // Обрезать до секунды
                ZoneId zoneId;
                try {
                    zoneId = ZoneId.of(timezone);
                } catch (Exception e) {
                    zoneId = ZoneId.of("UTC");
                }
                Instant instant = Instant.ofEpochMilli(currentTime);
                ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.forLanguageTag("ru-RU"));
                String timePart = zdt.format(timeFormatter);
                DateTimeFormatter zoneFormatter = DateTimeFormatter.ofPattern("z", Locale.ENGLISH);
                String zoneName = zdt.format(zoneFormatter);
                String timeStr = timePart + " " + zoneName;
                result.setTime(timeStr);
                double execTime = (System.currentTimeMillis() - start);
                result.setExecTime(String.valueOf(execTime));
                HttpSession session = request.getSession();
                @SuppressWarnings("unchecked")
                List<Result> results = (List<Result>) session.getAttribute("results");
                if (results == null) {
                    results = new ArrayList<>();
                }
                results.add(result);
                session.setAttribute("results", results);
                session.setAttribute("lastModified", lastModified);  // Update lastModified on modification

                // Set Last-Modified header BEFORE writing response
                ZonedDateTime lastModZdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastModified), ZoneId.of("GMT"));
                response.setHeader("Last-Modified", httpDateFormat.format(lastModZdt));

                response.setContentType("application/json");
                ObjectMapper mapper = new ObjectMapper();
                PrintWriter out = response.getWriter();
                out.print(mapper.writeValueAsString(results));
                out.flush();
            } else {
                logger.info("qwerty");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Недопустимые параметры");
            }
        } catch (Exception e) {
            logger.info("zxc");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}