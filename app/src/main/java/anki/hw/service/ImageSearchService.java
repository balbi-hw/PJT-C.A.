package anki.hw.service;

import anki.hw.dto.ImageSearchResultDto;
import anki.hw.external.image.ImageSearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageSearchService {

    private final ImageSearchClient imageSearchClient;

    public List<ImageSearchResultDto> searchImages(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }

        return imageSearchClient.search(keyword);
    }

}
