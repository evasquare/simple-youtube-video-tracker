package youtube_tracker;

import java.util.Timer;

public class YouTubeTrackerService {

    private final String handle;

    public YouTubeTrackerService(String handle) {
        this.handle = handle;
    }

    public void start() {
        var timer = new Timer();
        var myTask = new YouTubeTrackingTask(handle);
        timer.schedule(myTask, 0, 60000);
    }
}
