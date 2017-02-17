/**
 * @Title: LeagueService.java
 * @Copyright (C) 2016 太能沃可
 * @Description: 联盟
 * @Revision History:
 * @Revision 1.0 2016年8月15日  赵丽宝
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.gameworld.League;
import com.talentwalker.game.md.core.domain.gameworld.LeagueLord;
import com.talentwalker.game.md.core.domain.gameworld.LeagueName;
import com.talentwalker.game.md.core.domain.gameworld.LeagueShop;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Mail;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.gameworld.LeagueLordRepository;
import com.talentwalker.game.md.core.repository.gameworld.LeagueNameRepository;
import com.talentwalker.game.md.core.repository.gameworld.LeagueRepository;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.sensitiveword.SensitiveWord;
import com.talentwalker.game.md.core.util.ConfigKey;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.StringUtils;

/**
 * @ClassName: LeagueService
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年8月15日 上午11:29:32
 */
@Service
public class LeagueService extends GameSupport {
    @Autowired
    private LeagueLordRepository leagueLordRepository;
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private LeagueNameRepository leagueNameRepository;
    @Autowired
    private GainPayService gainPayService;
    @Autowired
    private LordRepository lordRepository;
    @Autowired
    private DuelService duelService;
    @Autowired
    private MailService mailService;
    /**
     * 盟主
     */
    private static final int DUTY_HEGEMON = 1;
    /**
     * 副盟主
     */
    private static final int DUTY_PORTREEVE = 2;
    /**
     * 精英成员
     */
    private static final int DUTY_ELITE = 3;
    /**
     * 见习成员
     */
    private static final int DUTY_COMMON = 4;
    /**
     * 联盟开启等级
     */
    private static final int LIMIT_LEVEL = 10;
    /**
     * 同意入盟申请
     */
    private static final int AUTHORITY_APPLY_PASS = 1;
    /**
     * 拒绝入盟申请
     */
    private static final int AUTHORITY_APPLY_REFUSAL = 2;
    /**
     * 踢出公会
     */
    private static final int AUTHORITY_OUNT_LEAGUE = 3;
    /**
     * 升职
     */
    private static final int AUTHORITY_DUTY_UP = 4;
    /**
     *降职
     */
    private static final int AUTHORITY_DUTY_LOW = 5;
    /**
     * 修改公会宣言
     */
    private static final int AUTHORITY_CHANGE_NOTICE = 6;

    /**
     * 转让盟主
     */
    private static final int AUTHORITY_TRANSFER = 7;
    /**
     * 转让盟主
     */
    private static final int AUTHORITY_SET_LIMIT = 8;
    /**
     * 退出联盟个人资金减少概率
     */
    private static final Double EXIT_LEAGUE_PROBABILITY = 1 - 0.5;
    /**
     * 踢出联盟个人资金减少概率
     */
    private static final Double OUT_LEAGUE_PROBABILITY = 1 - 0.2;

    /**
     * 加入联盟 
     */
    private static final String JION = "jion";
    /**
     * 推出联盟
     */
    private static final String EXIT = "exit";

    /**
     * @Description:登陆获取公会数据
     * @throws
     */
    public void loginInfo(Lord lord) {
        LeagueLord leagueLord = this.getLeagueLord(lord);
        League league = null;
        if (leagueLord.getLeagueId() != null) {
            league = leagueRepository.findOne(leagueLord.getLeagueId());
        }
        leagueLordRepository.save(leagueLord);
        this.gameModel.addObject(ResponseKey.LEAGUE, league);
        this.gameModel.addObject(ResponseKey.LEAGUE_LORD, leagueLord);
    }

    /**
     * 
     * @Description:
     * @throws
     */
    public void main() {
        Lord lord = this.getLord();
        // 等级校验
        this.isLevelOpen(ConfigKey.LEAGUE, lord, true);
        LeagueLord leagueLord = this.getLeagueLord(lord);
        League league = null;
        if (leagueLord.getLeagueId() != null) {
            league = leagueRepository.findOne(leagueLord.getLeagueId());
        }
        leagueLordRepository.save(leagueLord);
        this.gameModel.addObject(ResponseKey.LEAGUE, league);
        this.gameModel.addObject(ResponseKey.LEAGUE_LORD, leagueLord);
    }

    /**
     * 
     * @Description:
     * @throws
     */
    public void leagueList() {
        Lord lord = this.getLord();
        List<League> leagueList = leagueRepository.findAll();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<String> lordIds = new ArrayList<String>();
        for (League league : leagueList) {
            lordIds.add(league.getPresidentId());
        }
        List<Lord> lords = lordRepository.findByIdIn(lordIds);
        Map<String, Lord> lordMap = new HashMap<>();
        for (Lord presidentLord : lords) {
            lordMap.put(presidentLord.getId(), presidentLord);
        }
        for (League league : leagueList) {
            Map<String, Object> leagueMap = new HashMap<String, Object>();
            leagueMap.put("id", league.getId());
            leagueMap.put("name", league.getName());
            leagueMap.put("level", league.getLevel());
            leagueMap.put("count", league.getLordList().size());
            leagueMap.put("notice", league.getNotice());
            leagueMap.put("presidentName", lordMap.get(league.getPresidentId()).getName());
            boolean isApply = false; // 是否已申请，true:已申请，false：未申请
            if (league.getApplicant().contains(lord.getId())) {
                isApply = true;
            }
            leagueMap.put("isApply", isApply);
            list.add(leagueMap);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
        List<League> ls = leagueRepository.findByApplicantIn(lord.getId());
        List<String> leagueIds = new ArrayList<String>();
        for (League l : ls) {
            leagueIds.add(l.getId());
        }
        LeagueLord leagueLord = leagueLordRepository.findOne(lord.getId());
        Map<String, Object> mapLord = new HashMap<String, Object>();
        mapLord.put("applyNumber", leagueLord.getApplyNumber());
        mapLord.put("applyLeague", leagueIds);
        this.gameModel.addObject(ResponseKey.LEAGUE_LORD, mapLord);
    }

    /**
     * @Description:
     * @param name
     * @throws
     */
    public void create(String name) {
        LeagueName leagueName = this.checkLeagueName(name);
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        leagueLord.setJoinTime(System.currentTimeMillis());
        leagueLord.setEveryDonate(new HashMap<>());
        List<League> leagueList = new ArrayList<>();
        if (leagueLord.getApplyNumber() > 0) {
            leagueLord.setApplyNumber(0);
            List<League> ls = leagueRepository.findByApplicantIn(lord.getId());
            for (League l : ls) {
                l.getApplicant().remove(lord.getId());
                leagueList.add(l);
            }
        }
        leagueLord.setDuty(DUTY_HEGEMON);
        if (!StringUtils.isPrarmsEmpty(leagueLord.getLeagueId())) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31000, "已加入联盟，不能创建");
        }
        League league = this.initLeague(lord, name);
        leagueLord.setLeagueId(league.getId());
        DataConfig config = this.getDataConfig().get("league_other").get("building");
        gainPayService.pay(lord, config.getString("type"), config.getInteger("amount"));
        league.setPresidentName(lordRepository.findOne(league.getPresidentId()).getName());
        this.gameModel.addObject(ResponseKey.LEAGUE, league);
        this.gameModel.addObject(ResponseKey.LEAGUE_LORD, leagueLord);
        leagueLordRepository.save(leagueLord);
        leagueRepository.save(league);
        leagueNameRepository.save(leagueName);
        lordRepository.save(lord);
        if (leagueList.size() > 0) {
            leagueRepository.save(leagueList);
        }
    }

    /**
     * @Description:
     * @param name
     * @throws
     */
    public void checkName(String name) {
        this.checkLeagueName(name);
    }

    /**
     * @Description:
     * @param lord
     * @return
     * @throws
     */
    public LeagueLord getLeagueLord(Lord lord) {
        LeagueLord leagueLord = leagueLordRepository.findOne(lord.getId());
        if (leagueLord == null) {
            // 初始化主公联盟
            leagueLord = new LeagueLord();
            leagueLord.setId(lord.getId());
            leagueLord.setFund(0);
            leagueLord.setJoinTime(System.currentTimeMillis());
            leagueLord.setWorship(new HashMap<String, Object>());
            leagueLord.setWorshiped(new HashMap<String, Object>());
            leagueLord.setEveryDonate(new HashMap<String, Integer>());
            leagueLord.setExitTime(0);
            leagueLord.setApplyNumber(0);
            leagueLord.setLastLeagueId("");
            leagueLord.setLastGoldDonateTime(System.currentTimeMillis());
            leagueLord.setLastDiamondDonateTime(System.currentTimeMillis());
            leagueLord.setGoldDonateTimes(0);
            leagueLord.setDiamondDonateTimes(0);
            LeagueShop shop = new LeagueShop();
            shop.setLastBuyTime(System.currentTimeMillis());
            shop.setRecord(new HashMap<Integer, Integer>());
            leagueLord.setShop(shop);
        } else {// 已加入联盟
            // 自动提升为精英成员league_other
            if (leagueLord.getDuty() == DUTY_COMMON) {
                DataConfig leagueOther = getDataConfig().get("league_other");
                // 入会时间限制
                int hourLimit = leagueOther.get("promoteTime").getInteger("amount");
                // 入会贡献限制
                int contributionLimit = leagueOther.get("contribution").getInteger("amount");
                // 入会的总贡献
                int contributionSum = 0;
                if (leagueLord.getEveryDonate() != null && leagueLord.getEveryDonate().size() != 0) {
                    for (int contribution : leagueLord.getEveryDonate().values()) {
                        contributionSum += contribution;
                    }
                }
                if (DateUtils.addHours(new Date(leagueLord.getJoinTime()), hourLimit).getTime() <= new Date().getTime()
                        && contributionSum >= contributionLimit) {
                    // 提升为精英成员
                    leagueLord.setDuty(DUTY_ELITE);
                }
            }
            // 刷新金币捐献的次数
            if (!DateUtils.isSameDay(new Date(leagueLord.getLastGoldDonateTime()), new Date())) {
                leagueLord.setLastGoldDonateTime(System.currentTimeMillis());
                leagueLord.setGoldDonateTimes(0);
            }
            // 刷新钻石捐献的次数
            if (!DateUtils.isSameDay(new Date(leagueLord.getLastDiamondDonateTime()), new Date())) {
                leagueLord.setLastDiamondDonateTime(System.currentTimeMillis());
                leagueLord.setDiamondDonateTimes(0);
            }
        }
        leagueLordRepository.save(leagueLord);
        return leagueLord;
    }

    /**
     * @Description:初始化主公联盟
     * @param lord
     * @throws
     */
    private League initLeague(Lord lord, String name) {
        League league = new League();
        league.setId(leagueRepository.getNextSequence("leagueid") + "");
        league.setName(name);
        league.setNotice("");
        league.setPresidentId(lord.getId());
        league.setLevel(1);
        league.setExp(0);
        league.setDonate(0);
        league.setLordList(new ArrayList<String>());
        league.setAutomate(false);
        league.setLimitLevel(10);
        league.setLimitVip(0);
        league.setCreateTime(System.currentTimeMillis());
        league.setApplicant(new ArrayList<String>());
        List<String> lordList = new ArrayList<String>();
        lordList.add(lord.getId());
        league.setLordList(lordList);
        return league;

    }

    /**
     * @Description:校验公会名称
     * @param name
     * @throws
     */
    private LeagueName checkLeagueName(String name) {
        String regEx = ".*[(\\pP|\\p{Space}|\\p{Blank}|\\p{Cntrl})].*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);
        if (m.matches()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21015, "含有非法字符");
        }
        if (SensitiveWord.isContaintSensitiveWord(name)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21016, "含有敏感词");
        }
        LeagueName leagueName = leagueNameRepository.findByName(name);
        if (leagueName != null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31001, "名称已经被使用，请重新填写");
        }
        leagueName = new LeagueName();
        leagueName.setName(name);
        return leagueName;
    }

    /**
     * @Description:退出公会
     * @throws
     */
    public void exit() {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        List<String> ids = league.getLordList();
        ids.remove(lord.getId());
        league.setLordList(ids);
        if (leagueLord.getDuty() == DUTY_HEGEMON) {
            // 获取下一任盟主
            this.getHegemon(league, lord);
        }
        leagueLord.setLastLeagueId(league.getId());
        leagueLord.setLeagueId("");
        leagueLord.setExitTime(System.currentTimeMillis());
        int fund = leagueLord.getFund();
        leagueLord.setFund((int) Math.ceil(fund * EXIT_LEAGUE_PROBABILITY));
        leagueLordRepository.save(leagueLord);
        if (ids.size() > 0) {
            leagueRepository.save(league);
        }
        this.gameModel.addObject(ResponseKey.LEAGUE_LORD, leagueLord);
        this.gameModel.addObject(ResponseKey.LEAGUE, null);
        sendMail(EXIT, lord.getId(), league.getName());
    }

    /**
     * @Description:
     * @param type 邮件类型：（加入联盟、推出联盟）
     * @param goalId 
     * @param leagueName 联盟名称
     * @throws
     */
    private void sendMail(String type, String goalId, String leagueName) {
        DataConfig leagueOther = getDataConfig().get("league_other");
        String mailKey = leagueOther.get(type).getString("amount");
        List<Object> paramet = null;
        if (leagueName != null) {
            paramet = new ArrayList<Object>();
            paramet.add(leagueName);
        }
        mailService.sendMail(goalId, mailKey, paramet, null, Mail.TYPE_NPC);
    }

    /**
     * @Description:
     * @param league
     * @throws
     */
    private void getHegemon(League league, Lord lord) {
        // 发送通知，未完成
        if (league.getLordList().size() <= 0) {
            // 解散公会
            leagueRepository.delete(league.getId());
        } else {
            List<LeagueLord> leagueLords = leagueLordRepository.findByIdIn(league.getLordList());
            String id = "0";
            int donate = 0;
            long lastTime = 0;
            for (LeagueLord leagueLord1 : leagueLords) {
                int d = 0;
                for (int value : leagueLord1.getEveryDonate().values()) {
                    d += value;
                }
                if (d > donate) {
                    id = leagueLord1.getId();
                    donate = d;
                    lastTime = leagueLord1.getLastTime();
                } else if (d == donate) {
                    if (leagueLord1.getLastTime() > lastTime) {
                        id = leagueLord1.getId();
                        donate = d;
                        lastTime = leagueLord1.getLastTime();
                    } else if (Integer.parseInt(leagueLord1.getId()) > Integer.parseInt(id)) {
                        id = leagueLord1.getId();
                        donate = d;
                        lastTime = leagueLord1.getLastTime();
                    }
                }
            }
            LeagueLord leagueLord = leagueLordRepository.findOne(id);
            leagueLord.setDuty(DUTY_HEGEMON);
            leagueLordRepository.save(leagueLord);
            // 随便写的，未完成
            league.setPresidentId(id);
        }
    }

    /**
     * @Description:修改宣言
     * @param notice
     * @throws
     */

    public void changeNotice(String notice) {
        // String regEx = ".*[(\\pP|\\p{Space}|\\p{Blank}|\\p{Cntrl})].*";
        // Pattern p = Pattern.compile(regEx);
        // Matcher m = p.matcher(notice);
        // if (m.matches()) {
        // GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21015, "含有非法字符");
        // }
        if (!StringUtils.isPrarmsEmpty(notice) && SensitiveWord.isContaintSensitiveWord(notice)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21016, "含有敏感词");
        }
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        // 检验权限
        this.checkDutySuthority(leagueLord, AUTHORITY_CHANGE_NOTICE);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        league.setNotice(notice);
        leagueRepository.save(league);
        leagueLordRepository.save(leagueLord);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("notice", league.getNotice());
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
    }

    /**
     * @Description:
     * @throws
     */
    @SuppressWarnings("unchecked")
    private void checkDutySuthority(LeagueLord leagueLord, int authority) {
        if (this.getDataConfig().get("league_dutyl").get(leagueLord.getDuty() + "").getJsonObject()
                .containsKey("permission")) {
            List<Integer> permission = this.getDataConfig().get("league_dutyl").get(leagueLord.getDuty() + "")
                    .get("permission").getJsonArray();
            if (!permission.contains(authority)) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31002, "无权限");
            }
        } else {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31002, "无权限");
        }
    }

    /**
     * @Description:玩家不能对自己操作（踢出联盟，升职，降职），该方法检查是否对自己操作
     * @param lordId 玩家
     * @param targetLordId 被操作的玩家
     * @throws
     */
    private void checkIsOneself(String lordId, String targetLordId) {
        if (lordId.equals(targetLordId)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31016, "禁止操作");
        }
    }

    /**
     * @Description:申请
     * @throws
     */
    public void apply(String leagueId) {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        League league = leagueRepository.findOne(leagueId);
        List<String> applicant = league.getApplicant();
        if (applicant.contains(lord.getId())) {
            applicant.remove(lord.getId());
            leagueLord.setApplyNumber(leagueLord.getApplyNumber() - 1);
        } else {
            int cooling = this.getDataConfig().get("league_other").get("coolDown").getInteger("amount");
            cooling = cooling * 60 * 60 * 1000;
            if (leagueLord.getExitTime() + cooling > System.currentTimeMillis()) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31008, "退出联盟冷却时间（6小时）内，不能加入其它联盟");
            }
            if (!StringUtils.isPrarmsEmpty(leagueLord.getLeagueId())) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31000, "已加入联盟");
            }
            if (leagueLord.getApplyNumber() >= 3) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31003, "申请数量达到上限");
            }
            if (lord.getLevel() < league.getLimitLevel()) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31004, "主公等级未达到申请要求");
            }
            if (lord.getVipLevel() < league.getLimitVip()) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31004, "VIP等级未达到申请要求");
            }
            if (league.isAutomate()) {
                // 自动通过
                int memberMax = this.getDataConfig().get("league_level").get(league.getLevel() + "")
                        .getInteger("memberMax");
                List<String> lordList = league.getLordList();
                if (lordList.size() >= memberMax) { // 联盟人数超过，切换到手动通过
                    league.setAutomate(false);
                } else {
                    if (!leagueLord.getLastLeagueId().equals(league.getId())) {
                        leagueLord.setEveryDonate(new HashMap<String, Integer>());
                    }
                    lordList.add(lord.getId());
                    leagueLord.setLeagueId(league.getId());
                    leagueLord.setApplyNumber(0);
                    leagueLord.setDuty(DUTY_COMMON);
                    leagueLord.setJoinTime(System.currentTimeMillis());
                    league.setLordList(lordList);
                    this.gameModel.addObject(ResponseKey.LEAGUE, league);
                }
            }
            if (!league.isAutomate()) {
                // 手动通过
                applicant.add(lord.getId());
                leagueLord.setApplyNumber(leagueLord.getApplyNumber() + 1);
                league.setApplicant(applicant);
            }
        }

        leagueLordRepository.save(leagueLord);
        leagueRepository.save(league);
        List<League> ls = leagueRepository.findByApplicantIn(lord.getId());
        List<String> leagueIds = new ArrayList<String>();
        for (League l : ls) {
            leagueIds.add(l.getId());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyNumber", leagueLord.getApplyNumber());
        map.put("applyLeague", leagueIds);
        this.gameModel.addObject(ResponseKey.LEAGUE_LORD, map);

    }

    /**
     * @Description:申请列表
     * @throws
     */
    public void applyList() {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        List<Map<String, Object>> list = this.getApplyList(league);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applicant", list);
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
    }

    private List<Map<String, Object>> getApplyList(League league) {
        List<String> applicant = league.getApplicant();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<LeagueLord> leagueLordList = leagueLordRepository.findByIdIn(applicant);
        List<Lord> applyLordList = lordRepository.findByIdIn(applicant);
        Map<String, Lord> lordMap = new HashMap<String, Lord>();
        for (Lord applyLord : applyLordList) {
            lordMap.put(applyLord.getId(), applyLord);
        }
        boolean changeApply = false;
        for (int i = 0; i < leagueLordList.size(); i++) {
            LeagueLord applyleagueLord = leagueLordList.get(i);
            if (!StringUtils.isPrarmsEmpty(applyleagueLord.getLeagueId())) {
                changeApply = true;
                applicant.remove(applyleagueLord.getId());
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            Lord applyLord = lordMap.get(applyleagueLord.getId());
            int rank = duelService.getRank(applyLord, duelService.getRankKey(duelService.getDataLogicId(applyLord)));
            map.put("id", applyLord.getId());
            map.put("name", applyLord.getName());
            map.put("level", applyLord.getLevel());
            map.put("rank", rank);
            list.add(map);
        }
        if (changeApply) {
            league.setApplicant(applicant);
            leagueRepository.save(league);
        }
        return list;
    }

    /**
     * @Description:通过申请
     * @param lordId
     * @throws
     */
    public void passApply(String lordId) {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        this.checkDutySuthority(leagueLord, AUTHORITY_APPLY_PASS);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        List<String> applicant = league.getApplicant();
        List<String> lordList = league.getLordList();
        if (!applicant.contains(lordId)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31015, "玩家已取消申请");
        }
        // 联盟人数校验
        int memberMax = this.getDataConfig().get("league_level").get(league.getLevel() + "").getInteger("memberMax");
        if (lordList.size() >= memberMax) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31007, "联盟人数达到上限");
        }

        LeagueLord applyLeagueLord = leagueLordRepository.findOne(lordId);
        if (!StringUtils.isPrarmsEmpty(applyLeagueLord.getLeagueId())) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31000, "已加入联盟");
        }
        if (!applyLeagueLord.getLastLeagueId().equals(league.getId())) {
            applyLeagueLord.setEveryDonate(new HashMap<String, Integer>());
        }
        applyLeagueLord.setDuty(DUTY_COMMON);
        applyLeagueLord.setApplyNumber(0);
        applyLeagueLord.setLeagueId(league.getId());
        applyLeagueLord.setJoinTime(System.currentTimeMillis());
        lordList.add(lordId);
        applicant.remove(lordId);
        league.setApplicant(applicant);
        league.setLordList(lordList);
        leagueLordRepository.save(applyLeagueLord);
        leagueRepository.save(league);
        List<Map<String, Object>> list = this.getApplyList(league);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applicant", list);
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
        sendMail(JION, lordId, league.getName());
    }

    /**
     * @Description:拒绝
     * @param lordId
     * @throws
     */
    public void refusalApply(String lordId) {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        this.checkDutySuthority(leagueLord, AUTHORITY_APPLY_REFUSAL);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        LeagueLord applyLeagueLord = leagueLordRepository.findOne(lordId);
        List<String> applicant = league.getApplicant();
        applicant.remove(lordId);
        league.setApplicant(applicant);
        int number = applyLeagueLord.getApplyNumber() - 1 < 0 ? 0 : applyLeagueLord.getApplyNumber() - 1;
        applyLeagueLord.setApplyNumber(number);
        leagueLordRepository.save(applyLeagueLord);
        leagueRepository.save(league);
        List<Map<String, Object>> list = this.getApplyList(league);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applicant", list);
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
    }

    /**
     * @Description:清空申请列表
     * @throws
     */
    public void emptyApply() {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        this.checkDutySuthority(leagueLord, AUTHORITY_APPLY_REFUSAL);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        List<String> ids = league.getApplicant();
        List<LeagueLord> leagueLords = leagueLordRepository.findByIdIn(ids);
        List<LeagueLord> list = new ArrayList<LeagueLord>();
        for (LeagueLord l : leagueLords) {
            int number = l.getApplyNumber() - 1 < 0 ? 0 : l.getApplyNumber() - 1;
            l.setApplyNumber(number);
            list.add(l);
        }
        league.setApplicant(new ArrayList<String>());
        leagueRepository.save(league);
        leagueLordRepository.save(list);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applicant", league.getApplicant());
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
    }

    /**
     * @Description:申请设置
     * @param isAutomate false:手动通过，true：被动通过
     * @param limitLevel
     * @param limitVipLevel
     * @throws
     */
    public void setApply(boolean isAutomate, int limitLevel, int limitVipLevel) {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        this.checkDutySuthority(leagueLord, AUTHORITY_SET_LIMIT);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        league.setAutomate(isAutomate);
        league.setLimitLevel(limitLevel);
        league.setLimitVip(limitVipLevel);
        leagueRepository.save(league);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isAutomate", isAutomate);
        map.put("limitLevel", limitLevel);
        map.put("limitVip", limitVipLevel);
        this.gameModel.addObject(ResponseKey.LEAGUE, league);
    }

    /**
     * @Description:公会成员列表
     * @throws
     */
    public void lordList() {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        List<String> lordIds = league.getLordList();
        // 联盟玩家列表的信息
        List<Map<String, Object>> responseList = this.getleagueLordInfo(lordIds);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("playerList", responseList);
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
    }

    /**
     * @Description:联盟玩家列表的信息
     * @return
     * @throws
     */
    private List<Map<String, Object>> getleagueLordInfo(List<String> lordIds) {
        List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
        List<Lord> lords = lordRepository.findByIdIn(lordIds);
        Map<String, Lord> lordMap = new HashMap<String, Lord>();
        for (Lord applyLord : lords) {
            lordMap.put(applyLord.getId(), applyLord);
        }
        List<LeagueLord> leagueLords = leagueLordRepository.findByIdIn(lordIds);
        for (LeagueLord applyLeagueLord : leagueLords) {
            Lord applyLord = lordMap.get(applyLeagueLord.getId());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", applyLeagueLord.getId());
            map.put("name", applyLord.getName());
            map.put("level", applyLord.getLevel());
            map.put("duty", applyLeagueLord.getDuty());
            int donate = 0;
            for (int i : applyLeagueLord.getEveryDonate().values()) {
                donate += i;
            }
            map.put("donate", donate);
            map.put("joinTime", applyLeagueLord.getJoinTime());
            map.put("lastTime", applyLord.getLastTime());
            responseList.add(map);
        }
        return responseList;
    }

    /**
     * @Description:捐献
     * @param type
     * @throws
     */
    public void donate(String type) {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        DataConfig config = this.getDataConfig().get("league_danate").get(type);
        int amount = config.getInteger("amount");
        int frequency = config.getInteger("frequency");
        int donateTimes = 0;
        if (ItemID.GOLD.equals(type)) {
            donateTimes = leagueLord.getGoldDonateTimes();
            leagueLord.setLastGoldDonateTime(System.currentTimeMillis());
            leagueLord.setGoldDonateTimes(donateTimes + 1);
        } else if (ItemID.DIAMOND.equals(type)) {
            donateTimes = leagueLord.getDiamondDonateTimes();
            leagueLord.setLastDiamondDonateTime(System.currentTimeMillis());
            leagueLord.setDiamondDonateTimes(donateTimes + 1);
        }
        if (donateTimes >= frequency) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31005, "贡献次数用完");
        }
        gainPayService.pay(lord, type, amount);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        // 获得联盟贡献
        int donate = config.getInteger("contribution");
        league.setDonate(league.getDonate() + donate);
        // 个人捐献记录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(System.currentTimeMillis());
        Map<String, Integer> everyDonate = leagueLord.getEveryDonate();
        if (everyDonate.containsKey(date)) {
            everyDonate.put(date, everyDonate.get(date) + donate);
        } else {
            everyDonate.put(date, donate);
        }
        leagueLord.setEveryDonate(everyDonate);
        // 联盟经验值
        int exp = config.getInteger("gain");
        this.addLeagueExp(league, exp);
        // 个人资金
        int fund = config.getInteger("bankroll");
        leagueLord.setFund(leagueLord.getFund() + fund);
        leagueLordRepository.save(leagueLord);
        leagueRepository.save(league);
        this.gameModel.addObject(ResponseKey.LEAGUE, league);
        this.gameModel.addObject(ResponseKey.LEAGUE_LORD, leagueLord);
    }

    /**
     * @Description:联盟升级
     * @param league
     * @param exp
     * @throws
     */
    private void addLeagueExp(League league, int exp) {
        DataConfig config = this.getDataConfig().get("league_level");
        exp = league.getExp() + exp;
        int preLevel = league.getLevel();
        int postLevel = league.getLevel();
        if (preLevel >= config.getJsonObject().size()) {
            return;
        }
        for (int i = league.getLevel(); i < config.getJsonObject().size(); i++) {
            int exp1 = config.get(i + "").getInteger("exp");
            if (exp < exp1) {
                break;
            }
            postLevel++;
            exp = exp - exp1;
        }
        league.setLevel(postLevel);
        league.setExp(exp);
    }

    /**
     * @Description:踢出联盟
     * @param lordId
     * @throws
     */
    public void out(String lordId) {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        this.checkDutySuthority(leagueLord, AUTHORITY_OUNT_LEAGUE);
        // 检查是否对自己操作
        this.checkIsOneself(lord.getId(), lordId);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        List<String> lordList = league.getLordList();
        lordList.remove(lordId);
        league.setLordList(lordList);
        LeagueLord applyLeagueLord = leagueLordRepository.findOne(lordId);
        applyLeagueLord.setLastLeagueId(league.getId());
        applyLeagueLord.setLeagueId("");
        int fund = applyLeagueLord.getFund();
        applyLeagueLord.setFund((int) Math.ceil(fund * OUT_LEAGUE_PROBABILITY));
        leagueLordRepository.save(applyLeagueLord);
        leagueRepository.save(league);
        // 联盟玩家列表的信息
        List<Map<String, Object>> responseList = this.getleagueLordInfo(lordList);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("playerList", responseList);
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
        sendMail(EXIT, lordId, league.getName());
    }

    /**
     * @Description:降低职务
     * @param lordId
     * @throws
     */
    public void dutyLower(String lordId) {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        // 权限校验
        this.checkDutySuthority(leagueLord, AUTHORITY_DUTY_LOW);
        // 检查是否对自己操作
        this.checkIsOneself(lord.getId(), lordId);
        LeagueLord lowerLeagueLord = leagueLordRepository.findOne(lordId);
        if (leagueLord.getDuty() + 2 < lowerLeagueLord.getDuty()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31009, "只能对低于自己职务两级的玩家操作");
        }
        if (lowerLeagueLord.getDuty() >= DUTY_COMMON) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31010, "该玩家职位已经最低");
        }
        lowerLeagueLord.setDuty(lowerLeagueLord.getDuty() + 1);
        leagueLordRepository.save(lowerLeagueLord);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        List<String> ids = league.getLordList();
        // 联盟玩家列表的信息
        List<Map<String, Object>> responseList = this.getleagueLordInfo(ids);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("playerList", responseList);
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
    }

    /**
     * @Description:提升职务
     * @param lordId
     * @throws
     */
    public void dutyUp(String lordId) {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        // 权限校验
        this.checkDutySuthority(leagueLord, AUTHORITY_DUTY_UP);
        // 检查是否对自己操作
        this.checkIsOneself(lord.getId(), lordId);
        LeagueLord upLeagueLord = leagueLordRepository.findOne(lordId);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        if (leagueLord.getDuty() == DUTY_HEGEMON && upLeagueLord.getDuty() == DUTY_PORTREEVE) {
            this.checkDutySuthority(leagueLord, AUTHORITY_TRANSFER);
            // 盟主转让会长
            leagueLord.setDuty(leagueLord.getDuty() + 1); // 盟主降职
            league.setPresidentId(upLeagueLord.getId()); // 公会修改盟主
            leagueLordRepository.save(leagueLord); // 修改盟主职位为副盟主
        } else {
            if (leagueLord.getDuty() + 2 > upLeagueLord.getDuty()) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31009, "只能对低于自己职务两级的玩家操作");
            }
            // 校验副盟主个数
            int numMax = this.getDataConfig().get("league_dutyl").get(DUTY_PORTREEVE + "").getInteger("numMax");
            if (upLeagueLord.getDuty() - 1 == DUTY_PORTREEVE && numMax > 0) {
                // 副盟主个数
                int portreeve = leagueLordRepository.findByDutyAndIdIn(DUTY_PORTREEVE, league.getLordList()).size();
                if (portreeve >= numMax) {
                    GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31011, "该职务人数达到上限");
                }
            }

        }
        upLeagueLord.setDuty(upLeagueLord.getDuty() - 1);
        leagueLordRepository.save(upLeagueLord);
        leagueRepository.save(league);
        List<String> ids = league.getLordList();
        // 联盟玩家列表的信息
        List<Map<String, Object>> responseList = this.getleagueLordInfo(ids);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("playerList", responseList);
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
    }

    /**
     * @Description:
     * @param goodKey
     * @throws
     */
    public void buyGood(Integer goodKey) {
        Lord lord = this.getLord();
        LeagueLord leagueLord = this.getLeagueLord(lord);
        League league = leagueRepository.findOne(leagueLord.getLeagueId());
        if (league.getLevel() < goodKey) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31014, "联盟等级不够，当前物品未解锁");
        }
        LeagueShop shop = leagueLord.getShop();
        if (shop.getRecord().containsKey(goodKey)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31012, "物品不能重复购买");
        }
        DataConfig config = this.getDataConfig().get("league_level").get(goodKey + "");
        if (config == null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_31013, "购买的商品不存在");
        }
        String goodId = config.get("unlock").getString("id");
        int amount = config.get("unlock").getInteger("value");
        int price = config.get("unlock").getInteger("price");
        gainPayService.gain(lord, goodId, amount);
        gainPayService.pay(leagueLord, ItemID.FUND, price);
        shop.getRecord().put(goodKey, 1);
        leagueLordRepository.save(leagueLord);
        Object obj = this.gameModel.getModel(ResponseKey.LEAGUE_LORD);
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj != null) {
            map = (Map<String, Object>) obj;
        }
        map.put("record", shop.getRecord());
        this.gameModel.addObject(ResponseKey.LEAGUE_LORD, map);
    }

    /**
     * @Description:
     * @param leagueId 公会id或者名称
     * @throws
     */
    public void findByLeagueId(String leagueId) {
        Lord lord = this.getLord();
        League league = leagueRepository.findOne(leagueId);
        if (league == null) {
            league = leagueRepository.findByName(leagueId);
        }
        List<Object> list = new ArrayList<Object>();
        if (league != null) {
            Map<String, Object> leagueMap = new HashMap<String, Object>();
            leagueMap.put("id", league.getId());
            leagueMap.put("name", league.getName());
            leagueMap.put("level", league.getLevel());
            leagueMap.put("count", league.getLordList().size());
            leagueMap.put("notice", league.getNotice());
            leagueMap.put("presidentName", lordRepository.findOne(league.getPresidentId()).getName());
            boolean isApply = false; // 是否已申请，true:已申请，false：未申请
            if (league.getApplicant().contains(lord.getId())) {
                isApply = true;
            }
            leagueMap.put("isApply", isApply);
            list.add(leagueMap);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        this.gameModel.addObject(ResponseKey.LEAGUE, map);
    }

}
