/*
 * This source file was generated by the Gradle 'init' task
 */
package youtube_tracker;

public class App {

    public static void main(String[] args) {
        var youtubeTracker = new YouTubeTrackerService("@minecraft");
        youtubeTracker.start();
    }
}
