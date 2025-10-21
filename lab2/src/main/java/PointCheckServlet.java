import utils.Result;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Logger;

public class PointCheckServlet extends HttpServlet {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(request.getReader(), Map.class);

        // Обработка проверки точки
        String x = String.valueOf(jsonMap.get("x"));
        String y = String.valueOf(jsonMap.get("y"));
        String r = String.valueOf(jsonMap.get("r"));
        String flag = jsonMap.containsKey("flag") ? String.valueOf(jsonMap.get("flag")) : null;
        String timezone = jsonMap.containsKey("timezone") ? String.valueOf(jsonMap.get("timezone")) : "UTC";

        request.setAttribute("x", x);
        request.setAttribute("y", y);
        request.setAttribute("r", r);
        request.setAttribute("flag", flag);
        request.setAttribute("timezone", timezone);

        // Делегирование обработки сервлету checkArea
        request.getRequestDispatcher("/checkArea").forward(request, response);
    }
}