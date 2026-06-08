package anki.hw.external.image;

import anki.hw.dto.ImageSearchResultDto;
import anki.hw.external.image.pexels.PexelsPhoto;
import anki.hw.external.image.pexels.PexelsSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Slf4j
@Component
public class PexelsImageSearchClient implements ImageSearchClient {

    private final RestClient restClient;
    private final String apikey;

    public PexelsImageSearchClient(
            @Value("${pexels.base-url}") String baseUrl,
            @Value("${pexels.api-key}") String apikey
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.apikey = apikey;
    }

    @Override
    public List<ImageSearchResultDto> search(String keyword) {
        try {
            PexelsSearchResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("query", keyword)
                            .queryParam("per_page", 10)
                            .queryParam("locale", "ko-KR")
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, apikey)
                    .retrieve()
                    .body(PexelsSearchResponse.class);

            if (response == null || response.getPhotos() == null) {
                return List.of();
            }

            return response.getPhotos().stream()
                    .map(this::toImageSearchResultDto)
                    .toList();
        } catch (RestClientException e) {
            log.error("Pexels API 호출 실패. keyword={}", keyword, e);
            throw new IllegalStateException("이미지 검색 중 문제가 발생했습니다.");
        }
    }

    private ImageSearchResultDto toImageSearchResultDto(PexelsPhoto photo) {
        String imageUrl = null;
        String thumbnailUrl = null;

        if (photo.getSrc() != null) {
            imageUrl = photo.getSrc().getMedium();
            thumbnailUrl = photo.getSrc().getTiny();
        }

        return new ImageSearchResultDto(
                imageUrl,
                thumbnailUrl,
                "Pexels",
                photo.getPhotographer(),
                photo.getUrl(),
                photo.getAlt()
        );
    }
}
