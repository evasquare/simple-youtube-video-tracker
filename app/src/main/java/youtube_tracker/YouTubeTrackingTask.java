package youtube_tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TimerTask;

public class YouTubeTrackingTask extends TimerTask{
    private final String handle;
    private ArrayList<YouTubeVideo> videos = new ArrayList<>();

    public YouTubeTrackingTask(String handle) {
        this.handle = handle;
    }

    private void parseVideoList() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.youtube.com/" + this.handle + "/videos");
        String pageSource = driver.getPageSource();
        assert pageSource != null;
        var doc = Jsoup.parse(pageSource);
        driver.close();

        if (!doc.select("#error-page").isEmpty()) {
            throw new RuntimeException("Invalid handle or the user doesn't exist.");
        }

        Elements items = doc.select("#contents").getFirst().select("ytd-rich-item-renderer");
        for (Element item : items) {
            var aTag = item.select("#video-title-link").getFirst();
            var videoTitle = Objects.requireNonNull(item.select("#video-title").first()).text();
            var videoUrl = "https://www.youtube.com" +
                    aTag.attr("href");
            YouTubeVideo video = new YouTubeVideo(videoTitle, videoUrl);
            videos.add(video);
        }
    }

    @Override
    public void run() {
        var previousVideos = videos.clone();

        videos = new ArrayList<>();
        parseVideoList();

        System.out.println(previousVideos.toString());
        System.out.println(videos.toString());

        if (!previousVideos.equals(videos)) {
            System.out.println("New video detected!");
            System.out.println(videos.getFirst().getVideoTitle());
            System.out.println(videos.getFirst().getVideoURL());
        }
    }
}
