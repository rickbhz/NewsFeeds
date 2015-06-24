package newsfeed.projetouna.com.newsfeed;

public class FeedItem {
    private String title, name, image, timeStamp, url;

    public FeedItem() {
    }

    public FeedItem(String title, String name, String image, String timeStamp, String url) {
        super();
        this.title = title;
        this.name = name;
        this.image = image;
        this.timeStamp = timeStamp;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
