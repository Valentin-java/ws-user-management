package com.workers.wsusermanagement.rest.inbound.filter;

import com.workers.wsusermanagement.config.props.CommonRequestProperties;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeaderUserIdIterceptor implements Filter {

    private final CommonRequestProperties commonProps;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        try {
//            HttpServletRequest req = (HttpServletRequest) request;
//            String userIdHeader = Optional.ofNullable(req.getHeader(USER_ID_HEADER))
//                    .filter(header -> !header.isEmpty())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing or empty User ID header"));
//
//            commonProps.setUserId(userIdHeader);
//            String httpMethod = HttpMethod.valueOf(req.getMethod()).name();
//            log.debug("[HeaderUserIdInterceptor] {} request {} was called with params: {}", httpMethod, req.getRequestURI(), commonProps);
//        } catch (ResponseStatusException e) {
//            log.error("[HeaderUserIdInterceptor] Error: {}", e.getReason());
//            ((HttpServletResponse) response).sendError(HttpStatus.BAD_REQUEST.value(), e.getReason());
//            return;
//        } catch (Exception e) {
//            log.error("[HeaderUserIdInterceptor] Error: {}", e.getMessage());
//        }

        chain.doFilter(request, response);
    }
}
