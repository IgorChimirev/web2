
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.*;
import java.time.format.*;
import java.util.Locale;

@WebFilter(urlPatterns = {"/init"})
public class LastModifiedFilter implements Filter {
    private DateTimeFormatter httpDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME
            .withLocale(Locale.US)
            .withZone(ZoneId.of("GMT"));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();

        Long lastModifiedObj = (Long) session.getAttribute("lastModified");
        long lastModified = lastModifiedObj != null ? lastModifiedObj : 0;

        String ifModifiedSince = request.getHeader("If-Modified-Since");
        if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
            try {
                ZonedDateTime ifModZdt = ZonedDateTime.parse(ifModifiedSince, httpDateFormat);
                long ifModMillis = ifModZdt.toInstant().toEpochMilli();
                if (lastModified <= ifModMillis) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            } catch (DateTimeParseException e) {

            }
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}