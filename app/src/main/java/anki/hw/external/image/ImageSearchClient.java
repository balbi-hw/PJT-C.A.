package anki.hw.external.image;

import anki.hw.dto.ImageSearchResultDto;

import java.util.List;

public interface ImageSearchClient {

    List<ImageSearchResultDto> search(String keyword);
}
