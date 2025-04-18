package youtube_tracker;

import java.util.Objects;

public class YouTubeVideo {
    private final String videoTitle;
    private final String videoURL;

    public YouTubeVideo(String videoTitle, String videoURL) {
        this.videoTitle = videoTitle;
        this.videoURL = videoURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof YouTubeVideo that)) return false;
        return Objects.equals(videoTitle, that.videoTitle) && Objects.equals(videoURL, that.videoURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoTitle, videoURL);
    }

    @Override
    public String toString() {
        return "YouTubeVideo{" +
                "videoTitle='" + videoTitle + '\'' +
                ", videoURL='" + videoURL + '\'' +
                '}';
    }
}
