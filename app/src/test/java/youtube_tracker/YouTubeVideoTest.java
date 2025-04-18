package youtube_tracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YouTubeVideoTest {
    @Test
    void compare1() {
        YouTubeVideo video1 = new YouTubeVideo(
                "Video 1",
                "https://www.youtube.com/watch?v=0AjnMVAn4E4&pp=0gcJCX4JAYcqIYzv"
        );
        YouTubeVideo video2 = new YouTubeVideo(
                "Video 1",
                "https://www.youtube.com/watch?v=0AjnMVAn4E4"
        );

        assertEquals(video1, video2);
    }

    void compare2() {
        YouTubeVideo video1 = new YouTubeVideo(
                "Video 1",
                "https://www.youtube.com/watch?v=0AjnMVAn4E4"
        );
        YouTubeVideo video2 = new YouTubeVideo(
                "Video 1",
                "https://www.youtube.com/watch?v=0AjnMVAn4E4&pp=0gcJCX4JAYcqIYzv"
        );

        assertEquals(video1, video2);
    }

    void compare3() {
        YouTubeVideo video1 = new YouTubeVideo(
                "Video 2",
                "https://www.youtube.com/watch?v=0AjnMVAn4E4"
        );
        YouTubeVideo video2 = new YouTubeVideo(
                "Video 1",
                "https://www.youtube.com/watch?v=0AjnMVAn4E"
        );

        assertEquals(video1, video2);
    }

    void compare4() {
        YouTubeVideo video1 = new YouTubeVideo(
                "Video 1",
                "https://www.youtube.com/watch?v=0AjnMVAn4E4"
        );
        YouTubeVideo video2 = new YouTubeVideo(
                "Video 2",
                "https://www.youtube.com/watch?v=0AjnMVAn4E"
        );

        assertEquals(video1, video2);
    }
}
