package com.wendaoren.springcloud.loadbalancer.support.hint;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HintRequestFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HintContext.set(httpRequest.getHeader(HintContext.HINT_ATTR_NAME));
        }
        filterChain.doFilter(request, response);
        HintContext.remove();
    }

}
