package photoapp.api.gateway;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class PreFilter implements GlobalFilter, Ordered {
	
	final Logger logger = LoggerFactory.getLogger(PreFilter.class); 

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		logger.info("PreFilter is executed...");
		
		String requestPath = exchange.getRequest().getPath().toString();
		logger.info("Request path = " + requestPath);
		
		HttpHeaders  headers= exchange.getRequest().getHeaders();
		
		Set<String>headerNames = headers.keySet();
		
		headerNames.forEach((headerName)->{
			String headerValue = headers.getFirst(headerName);
			logger.info(headerName + " " + headerValue); 
		});
		
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {	
		
		return 0;
	}

}
