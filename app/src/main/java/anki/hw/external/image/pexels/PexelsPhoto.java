package anki.hw.external.image.pexels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PexelsPhoto {

    private Long id;

    private String url;

    private String photographer;

    @JsonProperty("photographer_url")
    private String photographerUrl;

    private PexelsPhotoSource src;

    private String alt;
}
