package anki.hw.controller;

import anki.hw.dto.ImageSearchResultDto;
import anki.hw.service.ImageSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageSearchController {

    private final ImageSearchService imageSearchService;

    @GetMapping("/search")
    public List<ImageSearchResultDto> searchImages(@RequestParam String query) {
        return imageSearchService.searchImages(query);
    }
}
