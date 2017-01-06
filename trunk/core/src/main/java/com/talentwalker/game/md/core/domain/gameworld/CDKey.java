
package com.talentwalker.game.md.core.domain.gameworld;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: CDKey
 * @Description: 兑换码
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月28日 下午6:25:38
 */
@Document(collection = "admin_cd_key")
public class CDKey extends BaseDomain {

    public static final int USING = 0;// 可用
    public static final int USED = 1;// 已用
    public static final int CANCELLATION = 2;// 作废

    /**
     * 状态：可用、已用、作废
     */
    protected int status;
    /**
     * 使用者Id
     */
    @Field("use_id")
    protected String useId;
    /**
     * 使用时间
     */
    @Field("use_time")
    protected long useTime;
    /**
     * 批次
     */
    @DBRef
    protected CDKeyBatch batch;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CDKeyBatch getBatch() {
        return batch;
    }

    public void setBatch(CDKeyBatch batch) {
        this.batch = batch;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

}
