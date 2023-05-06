package cs1302.api;

/**
 * Represents a result from the Unsplash API. This is
 * used by Gson to create an object from the JSON response body. This class
 * is provided with project's starter code, and the instance variables are
 * intentionally set to package private visibility.
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
