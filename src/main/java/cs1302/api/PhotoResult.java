package cs1302.api;

/**
 * Represents a result from the Unsplash API. This is
 * used by Gson to create an object from the JSON response body.
 */
public class PhotoResult {
//    public String id;
//    public String description;

    protected Url urls;

    /**
     * Gets a url.
     * @return url
     */
    public Url getUrl() {
        return urls;
    }
} //PhotoResult
