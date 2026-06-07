package anki.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageSearchResultDto {
    private String imageUrl;
    private String thumbnailUrl;
    private String source;
    private String author;
    private String sourceUrl;
}
