package sank.xbook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Chapters {
    @Id
    private Long id;
    private Long customId;
    private String sectionTables;
    private String sectionContent;
    @Generated(hash = 2127154994)
    public Chapters(Long id, Long customId, String sectionTables,
            String sectionContent) {
        this.id = id;
        this.customId = customId;
        this.sectionTables = sectionTables;
        this.sectionContent = sectionContent;
    }
    @Generated(hash = 120664092)
    public Chapters() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getCustomId() {
        return this.customId;
    }
    public void setCustomId(Long customId) {
        this.customId = customId;
    }
    public String getSectionTables() {
        return this.sectionTables;
    }
    public void setSectionTables(String sectionTables) {
        this.sectionTables = sectionTables;
    }
    public String getSectionContent() {
        return this.sectionContent;
    }
    public void setSectionContent(String sectionContent) {
        this.sectionContent = sectionContent;
    }
}
