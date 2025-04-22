package youtube_tracker;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class YouTubeTrackingTask extends TimerTask {

    private final String handle;
    private ArrayList<YouTubeVideo> newlyParsedVideos = new ArrayList<>();
    private boolean isFirstIteration = true;

    public YouTubeTrackingTask(String handle) {
        this.handle = handle;
    }

    private void parseVideoList() {
        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.youtube.com/" + this.handle + "/videos");
        String pageSource = driver.getPageSource();
        assert pageSource != null;
        var doc = Jsoup.parse(pageSource);
        driver.quit();

        if (!doc.select("#error-page").isEmpty()) {
            throw new RuntimeException("Invalid handle or the user doesn't exist.");
        }

        Elements items = doc.select("#contents").getFirst().select("ytd-rich-item-renderer");
        for (Element item : items) {
            var aTag = item.select("#video-title-link").getFirst();
            var videoTitle = Objects.requireNonNull(item.select("#video-title").first()).text();
            var videoUrl = "https://www.youtube.com"
                    + aTag.attr("href");
            YouTubeVideo video = new YouTubeVideo(videoTitle, videoUrl);
            newlyParsedVideos.add(video);
        }
    }

    @Override
    public void run() {
        // Before clearing the original array, keep it as a clone.
        ArrayList<YouTubeVideo> previousVideos = new ArrayList<>(newlyParsedVideos);

        // Clear the original array.
        newlyParsedVideos = new ArrayList<>();
        parseVideoList();

        if (isFirstIteration) {
            var firstOne = newlyParsedVideos.getFirst();
            printVideoInformation(firstOne.getVideoTitle(), firstOne.getVideoURL());
            isFirstIteration = false;
        } else {
            var newVideoList = new ArrayList<YouTubeVideo>();
            for (var item : newlyParsedVideos) {
                if (!previousVideos.contains(item)) {
                    newVideoList.add(item);
                }
            }

            for (var item : newVideoList) {
                printVideoInformation(item.getVideoTitle(), item.getVideoURL());
            }
        }
    }

    private void printVideoInformation(String title, String URL) {
        System.out.println(handle + ": New video detected!");
        System.out.println("\t" + title);
        System.out.println("\t" + URL);
    }
}
