package anki.hw.external.image.pexels;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties
public class PexelsSearchResponse {

    private int page;

    @JsonProperty("per_page")
    private int perPage;

    @JsonProperty("total_result")
    private int totalResults;

    private List<PexelsPhoto> photos = new ArrayList<>();
}
