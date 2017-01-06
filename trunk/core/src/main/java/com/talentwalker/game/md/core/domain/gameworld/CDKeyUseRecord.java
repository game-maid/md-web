
package com.talentwalker.game.md.core.domain.gameworld;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.talentwalker.game.md.core.domain.BaseDomain;

/**
 * @ClassName: CDKeyUseRecord
 * @Description: CDK 玩家领奖记录
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年11月29日 上午9:51:45
 */
@Document(collection = "admin_cd_key_use_record")
public class CDKeyUseRecord extends BaseDomain {

    /**
     * @Fields serialVersionUID : Description
     */

    private static final long serialVersionUID = 7880004913191726505L;
    /**
     * 领过该批次兑换码的玩家
     */
    protected Set<String> record;

    public Set<String> getRecord() {
        return record;
    }

    public void setRecord(Set<String> record) {
        this.record = record;
    }

}
