package platform;

//this class needed to return JSON with only code,time,date and views fields
public class SnippetForLatestAPI {
    String code;
    String date;
    int views;
    long time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String t) {
        this.date = t;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getViews() {
        return views;
    }

    public long getTime() {
        return time;
    }

    public SnippetForLatestAPI (){}

    public SnippetForLatestAPI (CodeSnippet codeSnippet){
        this.date = codeSnippet.getDate();
        this.code = codeSnippet.getCode();
        this.views = codeSnippet.getAllowedViews();
        this.time = codeSnippet.getTime();

    }
}
