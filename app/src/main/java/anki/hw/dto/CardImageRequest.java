package anki.hw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardImageRequest {

    private String imageUrl;
    private String thumbnailUrl;
    private String source;
    private String author;
    private String sourceUrl;
}
