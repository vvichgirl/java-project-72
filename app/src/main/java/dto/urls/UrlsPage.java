package dto.urls;

import dto.BasePage;
import model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.UrlCheck;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class UrlsPage extends BasePage {
    private List<Url> urls;
    private Map<Long, UrlCheck> lastChecks;
}
