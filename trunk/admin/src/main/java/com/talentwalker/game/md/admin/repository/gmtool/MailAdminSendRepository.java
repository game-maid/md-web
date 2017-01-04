/**
 * @Title: MailAdminSendRepository.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年8月31日  赵丽宝
 */

package com.talentwalker.game.md.admin.repository.gmtool;

import java.util.List;

import com.talentwalker.game.md.admin.form.MailSendForm;
import com.talentwalker.game.md.core.repository.BaseMongoRepository;

/**
 * @ClassName: MailAdminSendRepository
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月31日 下午2:47:10
 */

public interface MailAdminSendRepository extends BaseMongoRepository<MailSendForm, String> {

    List<MailSendForm> findByIdIn(List<String> ids);
}
