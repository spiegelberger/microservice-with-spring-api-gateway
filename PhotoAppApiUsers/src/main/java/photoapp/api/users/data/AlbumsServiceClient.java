package photoapp.api.users.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import photoapp.api.users.ui.model.AlbumResponseModel;

@FeignClient(name="albums-ws", fallbackFactory=AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient>{

	@Override
	public AlbumsServiceClient create(Throwable cause) {
		
		return new AlbumsServiceClientFallback(cause);
	}
	
}

@Slf4j
class AlbumsServiceClientFallback implements AlbumsServiceClient{
	
	private final Throwable cause;

	public AlbumsServiceClientFallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		
		if(cause instanceof FeignException &&((FeignException)cause).status()==404) {
			log.error("404 error took place when getAlbums was called with userId: " +
					id + ". Error message: " + cause.getLocalizedMessage());
		}
		else {
			log.error("Other error took place: " + cause.getLocalizedMessage());
		}
		
		return new ArrayList<>();
	}
	
}