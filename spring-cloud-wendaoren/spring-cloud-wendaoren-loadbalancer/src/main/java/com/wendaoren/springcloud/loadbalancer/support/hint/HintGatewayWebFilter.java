package com.wendaoren.springcloud.loadbalancer.support.hint;

import com.wendaoren.springcloud.loadbalancer.prop.SpringCloudLoadBalancerProperties;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class HintGatewayWebFilter implements WebFilter {

    private SpringCloudLoadBalancerProperties springCloudLoadBalancerProperties;

    public HintGatewayWebFilter(SpringCloudLoadBalancerProperties springCloudLoadBalancerProperties) {
        this.springCloudLoadBalancerProperties = springCloudLoadBalancerProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (springCloudLoadBalancerProperties.getHint().isEnableGatewayRequestHeader()) {
            ServerWebExchange removeRequestHintExchange = exchange.mutate()
                    .request(request -> request.headers(headers -> headers.remove(HintContext.HINT_ATTR_NAME)))
                    .build();
            return chain.filter(removeRequestHintExchange).then(Mono.fromRunnable(() -> {
                HintContext.remove();
            }));
        }
        return chain.filter(exchange);
    }
}
