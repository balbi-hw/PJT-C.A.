package anki.hw.external.image.pexels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PexelsPhotoSource {

    private String original;
    private String large;
    private String medium;
    private String small;
    private String tiny;
}
