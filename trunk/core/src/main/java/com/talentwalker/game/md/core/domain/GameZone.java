
package com.talentwalker.game.md.core.domain;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @ClassName: GameZoneInfo
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年4月25日 下午4:30:36
 */
@Document(collection = "portal_gamezone")
public class GameZone extends GameZoneBase {

    private static final long serialVersionUID = -8146459571761503920L;

    public static final int STATUS_NEW = 1;
    public static final int STATUS_HOT = 2;
    public static final int STATUS_FULL = 3;
    public static final int STATUS_RECOVERING = 4;

    public static final int TYPE_TEST = 1;
    public static final int TYPE_AUDIT = 2;
    public static final int TYPE_ONLINE = 3;

    /**
     * 开启时间
     */
    @Field("start_time")
    private long startTime;

    @Field("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @DBRef
    @Field("data_logic")
    private DataZone dataLogic;

    @DBRef
    @Field("physical_server")
    private PhysicalServer physicalServer;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate 要设置的 createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return dataLogic
     */
    public DataZone getDataLogic() {
        return dataLogic;
    }

    /**
     * @param dataLogic 要设置的 dataLogic
     */
    public void setDataLogic(DataZone dataLogic) {
        this.dataLogic = dataLogic;
    }

    /**
     * @return physicalServer
     */
    public PhysicalServer getPhysicalServer() {
        return physicalServer;
    }

    /**
     * @param physicalServer 要设置的 physicalServer
     */
    public void setPhysicalServer(PhysicalServer physicalServer) {
        this.physicalServer = physicalServer;
    }

}
