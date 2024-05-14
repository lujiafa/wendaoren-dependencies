package com.tchain.springcloud.nacos.support;

import com.tchain.springcloud.nacos.context.ServiceContext;
import com.tchain.springcloud.nacos.type.ServiceStatus;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServiceStatusFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletResponse instanceof HttpServletResponse) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            if (ServiceStatus.UP.equals(ServiceContext.getServiceState())) {
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            }
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
