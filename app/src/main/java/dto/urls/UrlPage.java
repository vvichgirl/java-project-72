package dto.urls;

import dto.BasePage;
import model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.UrlCheck;

import java.util.List;

@AllArgsConstructor
@Getter
public class UrlPage extends BasePage {
    private Url url;
    private List<UrlCheck> checks;
}
