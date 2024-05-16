package com.wendaoren.springcloud.loadbalancer.support.hint;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class HintWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HintContext.set(request.getHeaders().getFirst(HintContext.HINT_ATTR_NAME));
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HintContext.remove();
        }));
    }
}
