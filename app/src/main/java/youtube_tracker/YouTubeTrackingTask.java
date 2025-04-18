package youtube_tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TimerTask;

public class YouTubeTrackingTask extends TimerTask {
    private final String handle;
    private ArrayList<YouTubeVideo> videos = new ArrayList<>();

    private boolean isFirstIteration = true;

    public YouTubeTrackingTask(String handle) {
        this.handle = handle;
    }

    private void parseVideoList() {
        var options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
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
        // Before clearing the original array, keep it as a clone.
        ArrayList<YouTubeVideo> previousVideos = new ArrayList<>(videos);

        // Clear the original array.
        videos = new ArrayList<>();
        parseVideoList();

        // Check if it detects a new video.
        boolean isnotequal = false;
        if (!isFirstIteration) {
            if (videos.size() != previousVideos.size()) {
                isnotequal = true;
            } else {
                for (int i = 0; i < videos.size(); i++) {
                    if (!videos.get(i).equals(previousVideos.get(i))) {
                        isnotequal = true;
                        break;
                    }
                }
            }
        }

        YouTubeVideo latestYouTubeVideo = videos.getFirst();
        if (isFirstIteration) {
            printVideoInformation(latestYouTubeVideo.getVideoTitle(), latestYouTubeVideo.getVideoURL());
            isFirstIteration = false;
        } else {
            if (isnotequal) {
                printVideoInformation(latestYouTubeVideo.getVideoTitle(), latestYouTubeVideo.getVideoURL());
            }
        }
    }

    private void printVideoInformation(String title, String URL) {
        System.out.println(handle + ": New video detected!");
        System.out.println("\t" + videos.getFirst().getVideoTitle());
        System.out.println("\t" + videos.getFirst().getVideoURL());
    }
}
