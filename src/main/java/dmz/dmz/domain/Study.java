package dmz.dmz.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String date;
    private String url;
    private String fileoriginname; // 원본 파일명
    private String filesavename; // 저장 파일명
    private String filesavepath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileoriginname() {
        return fileoriginname;
    }

    public void setFileoriginname(String fileoriginname) {
        this.fileoriginname = fileoriginname;
    }

    public String getFilesavename() {
        return filesavename;
    }

    public void setFilesavename(String filesavename) {
        this.filesavename = filesavename;
    }

    public String getFilesavepath() {
        return filesavepath;
    }

    public void setFilesavepath(String filesavepath) {
        this.filesavepath = filesavepath;
    }
}