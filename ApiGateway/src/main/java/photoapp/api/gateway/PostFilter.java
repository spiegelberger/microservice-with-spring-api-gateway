package photoapp.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class PostFilter implements GlobalFilter, Ordered{
	
	Logger logger = LoggerFactory.getLogger(GlobalFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		return chain.filter(exchange).then(Mono.fromRunnable(()->{
			
			logger.info("Last global post filter executed.");
		}));
	}

	@Override
	public int getOrder() {
		
		return 0;
	}

}
