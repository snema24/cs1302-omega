package cs1302.api;

/**
 * Creates a url object.
 */
public class Url {
    protected String raw;
    protected String full;
    protected String regular;
    protected String small;
    protected String thumb;

    /**
     * Returns a raw image url.
     * @return a raw image url.
     */
    public String getRaw() {
        return raw;
    }

    /**
     * Sets the raw url.
     * @param raw takes in a raw url
     */
    public void setRaw(String raw) {
        this.raw = raw;
    }

} //url
