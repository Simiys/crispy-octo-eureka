package platform;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
public class CodeSnippet {

    @Id
    @GeneratedValue
    private long id;


    @Column
    private String uuid;

    @Column
    private String code;

    @Column
    private String date;

    @Enumerated(EnumType.STRING)
    @Column
    private RestrictionType restrictionType;

    @Column
    private int allowedViews;

    @NotNull
    @Column
    private long time;




    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(String t) {
        this.date = t;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public RestrictionType getRestrictionType() {
        return restrictionType;
    }

    public void setRestrictionType(RestrictionType restrictionType) {
        this.restrictionType = restrictionType;
    }

    public int getAllowedViews() {
        return allowedViews;
    }

    public void setAllowedViews(int allowedViews) {
        this.allowedViews = allowedViews;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



    public void minusViews() {
        this.allowedViews = allowedViews - 1;
    }

    //
//    public int getTime() {
//        return time;
//    }
//
//    public void setTime(int time) {
//        this.time = time;
//    }




    public CodeSnippet (){}



    public CodeSnippet (String code,int views,long time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.code = code;
        this.date = LocalDateTime.now().format(formatter);
        if (views <= 0 && time <= 0) {
            this.restrictionType = RestrictionType.UNRESTRICTED;
            this.allowedViews = 0;// 0 is sign of the absence of restrictions by current field
            this.time = 0;
        } else if (time > 0 && views <= 0) {
            this.restrictionType = RestrictionType.TIME_RESTRICTED;
            this.allowedViews = 0;
            this.time = time;
        } else if (views > 0 && time <= 0) {
            this.restrictionType = RestrictionType.VIEW_RESTRICTED;
            this.allowedViews = views;
            this.time = 0;
        } else {
            this.restrictionType = RestrictionType.RESTRICTED;
            this.allowedViews = views;
            this.time = time;
        }

    }

}



