/**
 * @Title: MailRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月23日  赵丽宝
 */

package com.talentwalker.game.md.core.repository.gameworld;

import java.util.List;

import com.talentwalker.game.md.core.domain.gameworld.Mail;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: MailRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月23日 下午3:11:34
 */

public interface MailRepository extends BaseMongoRepository<Mail, String> {

    /**
     * @Description:
     * @param id 玩家id
     * @param type 邮件类型
     * @throws
     */
    public List<Mail> findByPlayerIdAndType(String id, int type);

    public List<Mail> findByPlayerIdAndTypeNot(String id, int type);

}
