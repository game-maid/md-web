/**
 * @Title: LordService.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月13日  占志灵
 */

package com.talentwalker.game.md.core.service.gameworld;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.talentwalker.game.md.core.constant.ItemID;
import com.talentwalker.game.md.core.dataconfig.DataConfig;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.gameworld.Duel;
import com.talentwalker.game.md.core.domain.gameworld.Equip;
import com.talentwalker.game.md.core.domain.gameworld.FormHold;
import com.talentwalker.game.md.core.domain.gameworld.Hero;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Lordname;
import com.talentwalker.game.md.core.domain.gameworld.MonthCard;
import com.talentwalker.game.md.core.domain.gameworld.SignInRecord;
import com.talentwalker.game.md.core.domain.statistics.Login;
import com.talentwalker.game.md.core.domain.statistics.Register;
import com.talentwalker.game.md.core.exception.GameErrorCode;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.GameZoneRepository;
import com.talentwalker.game.md.core.repository.gameworld.DuelRepository;
import com.talentwalker.game.md.core.repository.gameworld.LordnameRepository;
import com.talentwalker.game.md.core.repository.gameworld.TopUpFirstRecordRepository;
import com.talentwalker.game.md.core.repository.statistics.LoginRepository;
import com.talentwalker.game.md.core.repository.statistics.RegisterRepository;
import com.talentwalker.game.md.core.response.Player;
import com.talentwalker.game.md.core.response.ResponseKey;
import com.talentwalker.game.md.core.sensitiveword.SensitiveWord;
import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.GameSupport;
import com.talentwalker.game.md.core.util.RandomUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: LordService
 * @Description: Description of this class
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月27日 下午2:29:33
 */
@Service
public class LordService extends GameSupport {

    @Autowired
    private LordnameRepository lordnameRepository;

    @Autowired
    private FormHoldService formHoldService;

    @Autowired
    private DuelRepository duelRepository;

    @Autowired
    private StageService stageService;

    @Autowired
    private MissionService missionService;

    @Autowired
    private GainPayService gainPayService;

    @Autowired
    private MailService mailService;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private DuelService duelService;

    @Autowired
    private ShopKanbanService shopKanbanService;

    @Autowired
    private SoulStoreService soulStoreService;

    @Resource
    private GameZoneRepository gameZoneRepository;

    @Autowired
    private TopUpFirstRecordRepository topUpFirstRecordRepository;

    @Resource
    private GameUserRepository gameUserRepository;

    @Resource
    private RegisterRepository registerRepository;

    @Resource
    private LoginRepository loginRepository;
    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private HeroService heroService;

    /**
     * 默认
     */
    private static final int HEAD_TYPE_DEFAULT = 0;
    /**
     * 获得解锁
     */
    private static final int HEAD_TYPE_LOCK = 1;
    /**
     * 擂台排名解锁
     */
    private static final int HEAD_TYPE_DUEL = 2;
    /**
     * VIP等级解锁
     */
    private static final int HEAD_TYPE_VIP = 3;
    /**
     * 月卡解锁
     */
    private static final int HEAD_TYPE_CARD = 4;
    /**
     * 更名卡
     */
    private static final String CHANGE_NAME_CARD = "item_1102";
    /**
     * daysEnergy_open 活动持续时间
     */
    public static final String DAYS_ENERGY_OPEN = "daysEnergy_open";
    /**
     * daysEnergy_extra 日常体力及额外体力配置表
     */
    public static final String DAYS_ENERGY_EXTRA = "daysEnergy_extra";
    /**
     * daysEnergy_game 日常比赛折扣
     */
    public static final String DAYS_ENERGY_GAME = "daysEnergy_game";
    /**
     * Lvup_prize 冲级活动奖励配置
     */
    public static final String LVUP_PRIZE = "Lvup_prize";
    /**
     * 冲级活动持续时间
     */
    public static final Integer LVUP_KEEP_DAYS = 7;
    /**
     * item 道具配置
     */
    public static final String ITEM = "item";
    /**
     * goldBell_other 黄金钟 活动配置
     */
    public static final String GOLD_BELL_OTHER = "goldBell_other";
    /**
     * goldBell_range 黄金钟 奖励配置
     */
    public static final String GOLD_BELL_RANGE = "goldBell_range";
    /**
     * 签到奖励配置
     */
    public static final String SIGN_IN_REWARD = "signIn_reward";
    /**
     * 补签配置 
     */
    public static final String SIGN_IN_PRICE = "signIn_Price";
    /**
     * 名称
     */
    public static final String NAME_1 = "name1";
    /**
     * 名称
     */
    public static final String NAME_2 = "name2";

    public void checkName(String name) {
        Lordname lordName = lordnameRepository.findByNameAndDataLogicId(name,
                this.getGameUser().getGameZone().getDataLogic().getId());
        if (lordName != null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21008);
        }
    }

    public void rename(String name) {
        String regEx = ".*[(\\pP|\\p{Space}|\\p{Blank}|\\p{Cntrl})].*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);
        if (m.matches()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21015);
        }
        if (SensitiveWord.isContaintSensitiveWord(name)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21016);
        }
        // if (name.length() > 10) {
        // GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21017);
        // }

        Lord lord = this.getLord();
        if (lord.getItems().containsKey(CHANGE_NAME_CARD)) {
            gainPayService.pay(lord, CHANGE_NAME_CARD, 1);
        } else {
            int diamond = this.getDataConfig().get("item").get(CHANGE_NAME_CARD).getInteger("purchase");
            gainPayService.pay(lord, ItemID.DIAMOND, diamond);
        }
        lord.setName(name);
        this.saveName(lord);

        lordRepository.save(lord);
        Object obj = this.gameModel.getModel(ResponseKey.LORD);
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj != null) {
            map = (Map<String, Object>) obj;
        }
        map.put("name", lord.getName());
        this.gameModel.addObject(ResponseKey.LORD, map);
    }

    public void notes(String notes) {
        Lord lord = this.getLord();
        if (lord.getLastNotesTime() + 1 * 60 * 1000 > System.currentTimeMillis()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21018, "签名冷却时间");
        }
        if (!StringUtils.isEmpty(notes)) {
            // String regEx = ".*[(\\pP|\\p{Space}|\\p{Blank}|\\p{Cntrl})].*";
            // Pattern p = Pattern.compile(regEx);
            // Matcher m = p.matcher(notes);
            // if (m.matches()) {
            // GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21015);
            // }
            if (SensitiveWord.isContaintSensitiveWord(notes)) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21016);
            }
        }
        lord.setNotes(notes);
        lord.setLastNotesTime(System.currentTimeMillis());
        lordRepository.save(lord);
        Map<String, String> map = new HashMap<String, String>();
        map.put("notes", lord.getNotes());
        this.gameModel.addObject(ResponseKey.LORD, map);
    }

    public Lord login() {
        Lord lord = this.getLord();
        // 看板娘兼容旧数据
        getKanbanId(lord);

        lordRepository.save(lord);
        this.loginData(lord);

        // heroService.setFormHoldFP(lord);

        recordLogin(lord);

        return lord;
    }

    /**
     * @Description: 记录每天活跃度
     * @param lord
     * @throws
     */
    private void recordLogin(Lord lord) {
        GameUser gameUser = getGameUser();

        Login login = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long startTime = 0L;
        Long endTime = 0L;
        try {
            startTime = sdf.parse(sdf.format(cal.getTime())).getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            endTime = sdf.parse(sdf.format(cal.getTime())).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Login> loginList = loginRepository.findByLordId(lord.getId(), startTime, endTime);
        if (loginList != null && loginList.size() != 0) {
            login = loginList.get(0);
        }
        if (login == null) {
            login = new Login();
            login.setLoginTime(System.currentTimeMillis());
            login.setLordId(lord.getId());
            login.setPackageId(gameUser.getPackageId());
            login.setZoneId(gameUser.getGameZoneId());
            loginRepository.save(login);
        }

    }

    public Lord findOne(String id) {
        return lordRepository.findOne(id);
    }

    public void save(Lord lord) {
        lordRepository.save(lord);
    }

    public Lord register(String name) {
        GameUser gameUser = getGameUser();
        Lord lord = lordRepository.findOne(gameUser.getId());
        if (lord != null) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21002);
        }
        lord = init(name);
        lord = lordRepository.insert(lord);
        this.loginData(lord);

        Register register = new Register();
        register.setId(lord.getId());
        register.setRegisterTime(System.currentTimeMillis());
        register.setPackageId(gameUser.getPackageId());
        register.setZoneId(gameUser.getGameZoneId());
        registerRepository.save(register);
        // 记录玩家（活跃度）
        recordLogin(lord);
        return lord;
    }

    private Lord init(String name) {
        DataConfig config = getDataConfig().get("newPlayer");
        // 加载体力、能量配置文件
        DataConfig strengthConfig = getDataConfig().get("strength");
        GameUser gameUser = getGameUser();
        Map<Integer, Integer> formStart = formHoldService.initFormStart();
        Lord lord = new Lord();
        lord.setId(gameUser.getId());
        lord.setName(name);
        lord.setHeadIcon(config.get("headIcon").getString("value"));
        // 初始化签名
        lord.setNotes(config.get("playerSign").getString("value"));
        lord.setLevel(config.get("lv").getInteger("value"));
        lord.setExp(config.get("exp").getInteger("value"));
        lord.setGold(config.get("gold").getInteger("value"));
        lord.setPersentDiamond(config.get("diamond").getInteger("value"));
        lord.setVipLevel(config.get("VIP").getInteger("value"));
        lord.setVipScore(config.get("score").getInteger("value"));
        lord.setHeroLimit(config.get("heroLimit").getInteger("value"));
        lord.setEquipLimit(config.get("bagEquip").getInteger("value"));
        lord.setItemLimit(config.get("bagNormal").getInteger("value"));
        lord.setSkillLimit(config.get("bagSkill").getInteger("value"));
        lord.setEnergyTime(System.currentTimeMillis());
        lord.setStrengthTime(System.currentTimeMillis());
        lord.setRegisterTime(System.currentTimeMillis());
        lord.setForm(new ArrayList<List<FormHold>>());
        lord.setHandbook(new HashMap<String, Map<String, Integer>>());
        lord.setHeros(new HashMap<String, Hero>());
        lord.setEquips(new HashMap<String, Equip>());
        lord.setFormStart(formStart);
        lord.setSkills(new HashMap<>());
        Map<String, Integer> items = new HashMap<>();
        items.put(config.get("box").getString("value"), 1);
        lord.setItems(items);
        lord.setSouls(new HashMap<>());
        lord.setLastTime(System.currentTimeMillis());
        // 初始化体力、能量
        lord.setStrength(strengthConfig.get(lord.getLevel() + "").getInteger("strength"));
        lord.setEnergy(strengthConfig.get(lord.getLevel() + "").getInteger("energy"));
        // 初始化英雄、装备
        /*
         * JSONArray initHero = config.get("heroes").get("value").getJsonArray(); for (int i = 0; i < initHero.size();
         * i++) { String heroId = initHero.getString(i); gainPayService.gain(lord, heroId, 1); }
         */
        JSONArray initEquip = config.get("equips").get("value").getJsonArray();
        for (int i = 0; i < initEquip.size(); i++) {
            String equipId = initEquip.getString(i);
            gainPayService.gain(lord, equipId, 1);
        }

        // 初始化默认看板娘
        shopKanbanService.initKanBan(lord);
        // 初始化每日vip奖励
        Map<Integer, Integer> dailyVipReward = new HashMap<>();
        dailyVipReward.put(lord.getVipLevel(), 0);
        lord.setDailyVipReward(dailyVipReward);
        lord.setLastGetDailyVipRewardTime(System.currentTimeMillis());
        lord.setLevelVipReward(new ArrayList<Integer>());
        // 初始化签到信息
        lord.setSignInRecords(new HashMap<Integer, SignInRecord>());
        lord.setReplenishSignTimes(0);
        this.saveName(lord);
        return lord;
    }

    private void saveName(Lord lord) {
        try {
            lordnameRepository.insert(new Lordname(lord.getId(), lord.getName(),
                    this.getGameUser().getGameZone().getDataLogic().getId()));
        } catch (Exception e) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21008);
        }
    }

    private void loginData(Lord lord) {
        // 免费领取体力活动的持续时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        DataConfig days = getDataConfig().get(LordService.DAYS_ENERGY_OPEN);
        String begin1 = days.get("begin1").getString("value");
        String begin2 = days.get("begin2").getString("value");
        String format = sdf.format(new Date());// 当前年月日
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH");
        long keep = days.get("keep").getInteger("value");
        long begin1L = 0L;
        long end1L = 0L;
        long begin2L = 0L;
        long end2L = 0L;
        try {
            begin1L = sdf1.parse(format + begin1).getTime();
            end1L = begin1L + keep * 1000;
            begin2L = sdf1.parse(format + begin2).getTime();
            end2L = begin2L + keep * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        lord.setDailyFirstFreeStrenthStartTime(begin1L);
        lord.setDailyFirstFreeStrenthEndTime(end1L);
        lord.setDailySecondFreeStrenthStartTime(begin2L);
        lord.setDailySecondFreeStrenthEndTime(end2L);

        this.gameModel.addObject(ResponseKey.KANBAN_IDS, lord.getKanbanIds());
        this.gameModel.addObject(ResponseKey.LORD, new Player(lord));
        this.gameModel.addObject(ResponseKey.EQUIPS, lord.getEquips());
        this.gameModel.addObject(ResponseKey.SKILLS, lord.getSkills());
        this.gameModel.addObject(ResponseKey.HEROES, lord.getHeros());
        this.gameModel.addObject(ResponseKey.ITEMS, lord.getItems());
        this.gameModel.addObject(ResponseKey.SOULS, lord.getSouls());
        this.gameModel.addObject(ResponseKey.FORM, lord.getForm());
        this.gameModel.addObject(ResponseKey.FORM_START, lord.getFormStart());
        this.gameModel.addObject(ResponseKey.STAGE, stageService.stageData(lord));
        this.gameModel.addObject(ResponseKey.MISSION_DAILY, missionService.getDailyMissions(lord));
        this.gameModel.addObject(ResponseKey.MISSION_ONCE, missionService.getOnceMissions(lord));
        this.gameModel.addObject(ResponseKey.HANDBOOK, this.getHandbook(lord));
        this.gameModel.addObject(ResponseKey.MAIL, mailService.getMailNCP());
        this.gameModel.addObject(ResponseKey.DUEL, duelService.getMain(lord));
        this.gameModel.addObject(ResponseKey.SOUL_STORE, soulStoreService.getSoulStore(lord));
        this.gameModel.addObject(ResponseKey.TOP_UP_FIRST_RECORD, topUpFirstRecordRepository.findOne(lord.getId()));
        this.gameModel.addObject(ResponseKey.TOP_UP_FIRST_AWARD, lord.isTopUpFirstAward());
        this.gameModel.addObject(ResponseKey.ZONE_CREATE_TIME, lord.getRegisterTime());
        this.gameModel.addObject(ResponseKey.ROMANCE, lord.getRomance());
        this.gameModel.addObject(ResponseKey.ROMANCE_RANDOM_STORY, lord.getRomanceRandomStory());
        // 公会数据
        leagueService.loginInfo(lord);

    }

    /**
     * @Description:兼容旧账号无看板娘id情况
     * @param kanbanId
     * @return
     * @throws
     */
    private String getKanbanId(Lord lord) {
        if (StringUtils.isEmpty(lord.getKanbanId())) {
            shopKanbanService.initKanBan(lord);
            lordRepository.save(lord);
        }
        return lord.getKanbanId();
    }

    public void getHandbook() {
        Lord lord = this.getLord();
        this.gameModel.addObject(ResponseKey.HANDBOOK, this.getHandbook(lord));
    }

    /**
     * @Description:
     * @param lord
     * @return
     * @throws
     */
    private Map<String, Set> getHandbook(Lord lord) {
        Map<String, Map<String, Integer>> handbook = lord.getHandbook();
        Map<String, Set> handbookList = new HashMap<String, Set>();
        if (handbook == null) {
            handbook = new HashMap<String, Map<String, Integer>>();
        }
        for (String key : handbook.keySet()) {
            handbookList.put(key, handbook.get(key).keySet());
        }
        return handbookList;
    }

    public void heroHandbook(String type, String itemId, Lord lord) {
        Map<String, Map<String, Integer>> handbook = lord.getHandbook();
        if (handbook == null) {
            handbook = new HashMap<String, Map<String, Integer>>();
        }
        if (handbook.get(type) == null) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put(itemId, 1);
            handbook.put(type, map);
            lord.setHandbook(handbook);
        } else if (!handbook.get(type).containsKey(itemId)) {
            Map<String, Integer> map = handbook.get(type);
            map.put(itemId, 1);
            handbook.put(type, map);
            lord.setHandbook(handbook);
        }
    }

    /**
     * @Description:更换头像
     * @param headIcon
     * @throws
     */
    public void changeHeadicon(String headIcon) {
        Lord lord = this.getLord();
        DataConfig headdConfig = this.getDataConfig().get("playerHead");
        if (!headdConfig.getJsonObject().containsKey(headIcon)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21011);
        }
        int type = headdConfig.get(headIcon).getInteger("type");
        if (type == HEAD_TYPE_LOCK) {
            Map<String, Map<String, Integer>> heroHandbook = lord.getHandbook();
            String progress = headdConfig.get(headIcon).getString("progress");
            if (heroHandbook == null) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21012);
            }
            if (StringUtils.startsWith(progress, ItemID.HERO)) {
                if (!heroHandbook.get(ItemID.HERO).containsKey(progress)) {
                    GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21012);
                }
            } else if (StringUtils.startsWith(progress, ItemID.EQUIP)) {
                if (!heroHandbook.get(ItemID.EQUIP).containsKey(progress)) {
                    GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21012);
                }
            } else if (StringUtils.startsWith(progress, ItemID.SKILL)) {
                if (!heroHandbook.get(ItemID.SKILL).containsKey(progress)) {
                    GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21012);
                }
            } else if (StringUtils.startsWith(progress, ItemID.ITEM)) {
                if (!heroHandbook.get(ItemID.ITEM).containsKey(progress)) {
                    GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21012);
                }
            }

        } else if (type == HEAD_TYPE_VIP) {
            int vipLevel = lord.getVipLevel();
            int progress = headdConfig.get(headIcon).getInteger("progress");
            if (vipLevel < progress) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21012);
            }
        } else if (type == HEAD_TYPE_DUEL) {
            Duel duel = duelRepository.findOne(lord.getId());
            if (duel == null) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21012);
            }
            int hrank = duel.getHrank();
            int progress = headdConfig.get(headIcon).getInteger("progress");
            if (hrank > progress) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21012);
            }
        } else if (type == HEAD_TYPE_CARD) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21012);
        }
        lord.setHeadIcon(headIcon);
        this.save(lord);
        Map<String, String> map = new HashMap<String, String>();
        map.put("headIcon", headIcon);
        this.gameModel.addObject(ResponseKey.LORD, map);
    }

    /**
     * @Description:
     * @throws
     */
    public void getHeadicon() {
        Lord lord = this.getLord();
        DataConfig headdConfig = this.getDataConfig().get("playerHead");
        Iterator it = headdConfig.getJsonObject().keys();
        List<String> headList = new ArrayList<>();
        while (it.hasNext()) {
            String headIcon = it.next().toString();
            int type = headdConfig.get(headIcon).getInteger("type");
            if (type == HEAD_TYPE_DEFAULT) {
                headList.add(headIcon);
            } else if (type == HEAD_TYPE_LOCK) {
                Map<String, Map<String, Integer>> heroHandbook = lord.getHandbook();
                String progress = headdConfig.get(headIcon).getString("progress");
                if (heroHandbook == null) {
                    continue;
                }
                if (StringUtils.startsWith(progress, ItemID.HERO)) {
                    if (heroHandbook.get(ItemID.HERO).containsKey(progress)) {
                        headList.add(headIcon);
                    }
                } else if (StringUtils.startsWith(progress, ItemID.EQUIP)) {
                    if (heroHandbook.get(ItemID.EQUIP).containsKey(progress)) {
                        headList.add(headIcon);
                    }
                } else if (StringUtils.startsWith(progress, ItemID.SKILL)) {
                    if (heroHandbook.get(ItemID.SKILL).containsKey(progress)) {
                        headList.add(headIcon);
                    }
                } else if (StringUtils.startsWith(progress, ItemID.ITEM)) {
                    if (heroHandbook.get(ItemID.ITEM).containsKey(progress)) {
                        headList.add(headIcon);
                    }
                }
            } else if (type == HEAD_TYPE_VIP) {
                int vipLevel = lord.getVipLevel();
                int progress = headdConfig.get(headIcon).getInteger("progress");
                if (vipLevel >= progress) {
                    headList.add(headIcon);
                }
            } else if (type == HEAD_TYPE_DUEL) {
                Duel duel = duelRepository.findOne(lord.getId());
                if (duel != null) {
                    int hrank = duel.getHrank();
                    int progress = headdConfig.get(headIcon).getInteger("progress");
                    if (hrank < progress) {
                        headList.add(headIcon);
                    }
                }
            } else if (type == HEAD_TYPE_CARD) {
                // 暂无
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("headIconList", headList);
        this.gameModel.addObject(map);
    }

    /**
     * @Description:
     * @param items
     * @throws
     */
    public void addStrength(JSONObject items) {
        Lord lord = this.getLord();
        Iterator<String> it = items.keys();
        int strength = 0;
        while (it.hasNext()) {
            String itemId = it.next();
            int amount = items.getInt(itemId);
            DataConfig conf = this.getDataConfig().get("item").get(itemId);
            if (!"strength".equals(conf.getString("type"))) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24016, itemId + " 道具不能使用");
            }
            strength += (conf.getInteger("params") * amount);
            gainPayService.pay(lord, itemId, amount);
        }
        gainPayService.gain(lord, ItemID.STRENGTH, strength);
        lordRepository.save(lord);
    }

    /**
     * @Description:
     * @param items
     * @throws
     */
    public void addEnergy(JSONObject items) {
        Lord lord = this.getLord();
        Iterator<String> it = items.keys();
        int energy = 0;
        while (it.hasNext()) {
            String itemId = it.next();
            int amount = items.getInt(itemId);
            DataConfig conf = this.getDataConfig().get("item").get(itemId);
            if (!"energy".equals(conf.getString("type"))) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24016, itemId + " 道具不能使用");
            }
            energy += (conf.getInteger("params") * amount);
            gainPayService.pay(lord, itemId, amount);
        }
        gainPayService.gain(lord, ItemID.ENERGY, energy);
        lordRepository.save(lord);
    }

    /**
     * @Description:
     * @throws
     */
    public void buyStrength() {
        Lord lord = this.getLord();
        DataConfig config = this.getDataConfig().get("buyStrength");
        if (lord.getStrengthTimes() < config.getJsonObject().size()) {
            lord.setStrengthTimes(lord.getStrengthTimes() + 1);
        }
        lord.setStrengthBuyTime(System.currentTimeMillis());
        gainPayService.pay(lord, ItemID.DIAMOND, config.get(lord.getStrengthTimes() + "").getInteger("diamond"));
        gainPayService.gain(lord, ItemID.STRENGTH, config.get(lord.getStrengthTimes() + "").getInteger("number"));
        lordRepository.save(lord);
        Object obj = this.gameModel.getModel(ResponseKey.LORD);
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj != null) {
            map = (Map<String, Object>) obj;
        }
        map.put("strengthTimes", lord.getStrengthTimes());
        this.gameModel.addObject(ResponseKey.LORD, map);
    }

    /**
     * @Description:领取月卡奖励
     * @param cardId
     * @throws
     */
    public void monthCardAward(String cardId) {
        Lord lord = this.getLord();
        if (!lord.getMonthCard().containsKey(cardId)) {
            // 月卡不存在
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24017, cardId + " 月卡不存在");
        }
        Map<String, MonthCard> monthCards = lord.getMonthCard();
        if (DateUtils.isSameDay(new Date(monthCards.get(cardId).getLastTime()), new Date())) {
            // 已领取
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24018, cardId + " 已领取");
        }
        long endTime = monthCards.get(cardId).getCreateTime()
                + this.getDataConfig().get(TopUpCardService.CONFIG_CASH_SHOP).get(cardId)
                        .getInteger(TopUpCardService.CONFIG_CASH_SHOP_EFFECT_DAYS) * DateUtils.MILLIS_PER_DAY
                        * monthCards.get(cardId).getAmount();
        if (this.getDataConfig().get(TopUpCardService.CONFIG_CASH_SHOP).get(cardId)
                .getInteger("type") != TopUpCardService.CONFIG_SHOP_TYPE_CARD_UNLIMIT) {
            if (System.currentTimeMillis() < monthCards.get(cardId).getCreateTime()
                    || System.currentTimeMillis() > endTime) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_24019, cardId + " 不在生效区间内");
            }
        }
        monthCards.get(cardId).setLastTime(System.currentTimeMillis());
        DataConfig config = this.getDataConfig().get(TopUpCardService.CONFIG_CASH_SHOP).get(cardId).get("dayAward");
        Iterator<String> it = config.getJsonObject().keySet().iterator();
        while (it.hasNext()) {
            String itemId = it.next();
            gainPayService.gain(lord, itemId, config.getInteger(itemId));
        }
        lordRepository.save(lord);
        this.gameModel.addObject("monthCard", lord.getMonthCard());
    }

    /**
     * @Description:领取每日免费体力
     * @throws
     */
    public void freeStrength() {
        Date curDate = new Date();
        this.checkfreeStrengthTime(curDate);

        Lord lord = getLord();
        long freeStrengthTime = lord.getFreeStrengthTime();
        Integer keep = getDataConfig().get(LordService.DAYS_ENERGY_OPEN).get("keep").getInteger("value");
        // 同时间段内只能领取一次
        if (freeStrengthTime + keep * 1000 >= curDate.getTime()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_27014, "已经领取过体力");
        }
        lord.setFreeStrengthTime(curDate.getTime());
        Integer freeEnergyAmount = getDataConfig().get(LordService.DAYS_ENERGY_EXTRA).get("energy")
                .getInteger("freeEnergy");
        gainPayService.gain(lord, ItemID.STRENGTH, freeEnergyAmount);
        Map<String, Object> map = (Map) this.gameModel.getModel(ResponseKey.LORD);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put("freeStrengthTime", lord.getFreeStrengthTime());
        lordRepository.save(lord);
    }

    /**
     * @Description:检查是否在每日ling
     * @param curDate
     * @throws
     */
    private void checkfreeStrengthTime(Date curDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        DataConfig days = getDataConfig().get(LordService.DAYS_ENERGY_OPEN);
        String begin1 = days.get("begin1").getString("value");
        String begin2 = days.get("begin2").getString("value");
        String format = sdf.format(curDate);// 当前年月日
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            long keep = days.get("keep").getInteger("value");
            long begin1L = sdf1.parse(format + begin1).getTime();
            long end1L = begin1L + keep * 1000;
            long begin2L = sdf1.parse(format + begin2).getTime();
            long end2L = begin2L + keep * 1000;
            long curL = curDate.getTime();
            if (curL < begin1L || (curL > end1L && curL < begin2L) || curL > end2L) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28007, "不在活动期间");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:根据小游戏用时不同得到不同的折扣奖励
     * @param time
     * @throws
     */
    public void freeStrengthDiscount(Integer time) {
        Date curDate = new Date();
        this.checkfreeStrengthTime(curDate);
        Lord lord = getLord();
        DataConfig dataConfig = getDataConfig().get(LordService.DAYS_ENERGY_GAME);
        Iterator<String> keys = dataConfig.getJsonObject().keys();
        int size = 0;
        while (keys.hasNext()) {
            size++;
            String i = keys.next();
            DataConfig dataConfig2 = dataConfig.get(i + "");
            if (time <= dataConfig2.getInteger("time")) {
                lord.setStrengthDiscount(dataConfig.get(i + "").getDouble("discount"));
                lord.setStrengthDiscountTime(curDate.getTime());
                break;
            }
        }

        if (curDate.getTime() != lord.getStrengthDiscountTime()) {
            lord.setStrengthDiscount(dataConfig.get(size + "").getDouble("discount"));
            lord.setStrengthDiscountTime(curDate.getTime());
        }
        lordRepository.save(lord);
        HashMap<String, Object> map = new HashMap<>();
        map.put("strengthDiscount", lord.getStrengthDiscount());
        map.put("strengthDiscountTime", lord.getDiscountStrengthTime());
        this.gameModel.addObject(ResponseKey.LORD, map);
    }

    /**
     * @Description: 在每日体力活动期间购买折扣体力
     * @param buyTimes 要购买第几次折扣体力
     * @throws
     */
    public void buyDiscountStrength(Integer buyTimes) {
        Date curDate = new Date();
        Lord lord = getLord();
        this.checkfreeStrengthTime(curDate);
        // 校验是否买过该体力
        Map<Integer, Long> discountStrengthTime = lord.getDiscountStrengthTime();
        if (discountStrengthTime == null)
            discountStrengthTime = new HashMap<>();
        Long time = discountStrengthTime.get(buyTimes);
        if (time != null) {
            if (!this.checkParticipationTime(curDate, time))
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_26002, "已经购买过本次商品");
        }
        DataConfig extraConfig = getDataConfig().get(LordService.DAYS_ENERGY_EXTRA);
        DataConfig diamondConfig = extraConfig.get("diamond");
        DataConfig energyConfig = extraConfig.get("energy");
        DataConfig vIPLimitConfig = extraConfig.get("VIPLimit");
        // 检查上一个是否购买
        Integer prevBuyTimes = buyTimes - 1;// 上一个
        if (diamondConfig.get("extraEnergy").getInteger(prevBuyTimes + "") != null) {
            Long prevTime = discountStrengthTime.get(prevBuyTimes);
            if (prevTime != null) {
                if (this.checkParticipationTime(curDate, prevTime))
                    GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29006, "上一次商品没有购买，无法购买本次商品");
            } else {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29006, "上一次商品没有购买，无法购买本次商品");
            }
        }
        // vip等级校验
        Integer vipLimit = vIPLimitConfig.get("extraEnergy").getInteger(buyTimes.toString());
        if (lord.getVipLevel() < vipLimit)
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_21009, "vip等级不够");

        discountStrengthTime.put(buyTimes, curDate.getTime());
        lord.setDiscountStrengthTime(discountStrengthTime);
        gainPayService.gain(lord, ItemID.STRENGTH, energyConfig.get("extraEnergy").getInteger(buyTimes + ""));
        gainPayService.pay(lord, ItemID.DIAMOND, (int) Math
                .ceil(diamondConfig.get("extraEnergy").getInteger(buyTimes.toString()) * lord.getStrengthDiscount()));
        Map<String, Object> map = (Map) this.gameModel.getModel(ResponseKey.LORD);
        map.put("discountStrengthTime", lord.getDiscountStrengthTime());
        this.gameModel.addObject(ResponseKey.LORD, map);
        lordRepository.save(lord);

    }

    /**
     * @Description:检查是否参加过本次活动
     * @param curDate 当前时间
     * @param targetLong 目标时间
     * @return false: 参加过本次活动 ；true：没有参加过本次活动
     * @throws
     */
    private boolean checkParticipationTime(Date curDate, Long targetLong) {
        Integer keep = getDataConfig().get(DAYS_ENERGY_OPEN).get("keep").getInteger("value");
        if (targetLong + keep * 1000 > curDate.getTime()) {
            return false;
        }
        return true;
    }

    /**
     * @Description:兑换玩家冲级奖励
     * @param level ： 要兑换的等级
     * @throws
     */
    public void drawLvup(Integer level) {
        Lord lord = getLord();
        GameUser gameUser = getGameUser();
        // 检查是否在冲级活动时间内
        Calendar curCal = Calendar.getInstance();
        Date curDate = curCal.getTime();
        // 由开服时间改为玩家注册时间
        long createZoneLong = lord.getRegisterTime();
        curCal.add(Calendar.DATE, -LVUP_KEEP_DAYS);

        if (createZoneLong < curCal.getTimeInMillis() || curDate.getTime() < createZoneLong) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28007, "不在活动时间内");
        }

        // 检查玩家是否已经领取
        Map<Integer, Long> lvupRecord = lord.getLvupRecord();
        if (lvupRecord == null) {
            lvupRecord = new HashMap<Integer, Long>();
        }
        if (lvupRecord.containsKey(level)) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_29001, "已经领取过奖励了");
        }
        // 检查玩家等级是否达到
        if (level > lord.getLevel()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_34007, "等级不够");
        }
        // 发送奖励
        String rewardKey = getDataConfig().get(LVUP_PRIZE).get(level + "").getString("prize");
        JSONObject jsonObject = getDataConfig().get(LordService.ITEM).get(rewardKey).get("params").getJsonObject();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String itemId = keys.next();
            int amount = jsonObject.getInt(itemId);
            gainPayService.gain(lord, itemId, amount);
        }
        // 保存记录
        lvupRecord.put(level, curDate.getTime());
        lord.setLvupRecord(lvupRecord);
        Map<String, Object> map = (Map) gameModel.getModel(ResponseKey.LORD);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put("lvupRecord", lvupRecord);
        this.gameModel.addObject(ResponseKey.LORD, map);
        // 新手引导记录步数
        int step = lord.getGuidanceStep();
        if (step < 999) {
            lord.setGuidanceStep(++step);
        }
        lordRepository.save(lord);
    }

    /**
     * @Description:黄金钟
     * @param amount ： 花费钻石的数量
     * @throws
     */
    public void goldBell(Integer amount) {
        // 校验活动时间
        Calendar curCal = Calendar.getInstance();
        Lord lord = getLord();
        Integer keepTime = getDataConfig().get(LordService.GOLD_BELL_OTHER).get("keepTime").getInteger("value");
        curCal.add(Calendar.HOUR_OF_DAY, -keepTime);
        if (lord.getRegisterTime() < curCal.getTimeInMillis()) {
            GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_28007, "不在活动期间");
        }
        // 计算奖励
        DataConfig dataConfig = getDataConfig().get(LordService.GOLD_BELL_RANGE).get(amount + "");
        Double min = dataConfig.getDouble("scaleMin");
        Double max = dataConfig.getDouble("scaleMax");
        int reward = (int) Math.ceil((min + Math.random() * (max - min)) * amount);// 奖励向下取整
        gainPayService.pay(lord, ItemID.DIAMOND, amount);
        gainPayService.gain(lord, ItemID.DIAMOND, reward);
        lordRepository.save(lord);
    }

    /**
     * @Description:签到、补签
     * @param times 签到次数 本月第一次签到为1
     * @param status 签到状态  0：签到  1： 补签
     * @throws
     */
    public void signIn(Integer times, Integer status) {
        Calendar cal = Calendar.getInstance();
        // 校验今天是否领取过该次签到
        Lord lord = getLord();
        Map<Integer, SignInRecord> signInRecords = lord.getSignInRecords();
        SignInRecord signInRecord = null;
        Date curDate = cal.getTime();
        String month = cal.get(Calendar.MONTH) + 1 + "";
        String key = month.length() == 1 ? "0" + month + "_" + times : cal.get(Calendar.MONTH) + 1 + "_" + times;
        DataConfig rewardConfig = getDataConfig().get(LordService.SIGN_IN_REWARD).get(key);
        int multiple = rewardConfig.getInteger("VIP" + lord.getVipLevel());// 奖励倍数
        String itemId = rewardConfig.getString("rewardsID");
        int amount = rewardConfig.getInteger("quantity");
        int replenishSignTimes = lord.getReplenishSignTimes();

        if ((signInRecord = signInRecords.get(times)) != null) {
            // 检查是否是今天领取的 && 检查是否已经领取vip奖励 && 检查vip等级
            long time = signInRecord.getTime();
            if (!DateUtils.isSameDay(new Date(time), curDate) || signInRecord.getStatus() == 2 || multiple == 1) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_38001, "已经领奖");
            }
            // 领取vip奖励
            gainPayService.gain(lord, itemId, amount);
            signInRecord.setRewardStatus(SignInRecord.STATUS_2);
        } else {// 未领取奖励
            // 检查是否应该领取该次签到奖励
            if (signInRecords.size() + 1 != times) {
                GameExceptionUtils.throwException(GameErrorCode.GAME_ERROR_38002, "不能领取该天奖励");
            }
            // 补签
            if (status == 1) {
                // 计算补签次数
                Integer diamondAmount = getDataConfig().get(LordService.SIGN_IN_PRICE).get(++replenishSignTimes + "")
                        .getInteger("diamond");
                gainPayService.pay(lord, ItemID.DIAMOND, diamondAmount);
                lord.setReplenishSignTimes(replenishSignTimes);
            }
            // 领取奖励
            gainPayService.gain(lord, itemId, amount * multiple);
            // 保存状态
            signInRecord = new SignInRecord();
            signInRecord.setRewardStatus(multiple);
            signInRecord.setStatus(status);
            signInRecord.setTime(curDate.getTime());
        }

        signInRecords.put(times, signInRecord);
        lord.setSignInRecords(signInRecords);
        Map<String, Object> map = (Map) this.gameModel.getModel(ResponseKey.LORD);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put("signInRecords", signInRecords);
        map.put("replenishSignTimes", replenishSignTimes);
        this.gameModel.addObject(ResponseKey.LORD, map);
        lordRepository.save(lord);
    }

    /**
     * @Description:产生随机名称
     * @throws
     */
    public void randomName() {
        String name = this.getRandomName();
        while (true) {
            Lordname lordname = lordnameRepository.findByNameAndDataLogicId(name,
                    this.getGameUser().getGameZone().getDataLogic().getId());
            if (lordname == null) {
                break;
            }
        }
        this.gameModel.addObject("randomName", name);
    }

    /**
     * @Description:
     * @return
     * @throws
     */
    private String getRandomName() {
        DataConfig name1Config = getDataConfig().get(NAME_1);
        DataConfig name2Config = getDataConfig().get(NAME_2);
        int size1 = name1Config.getJsonObject().size();
        int size2 = name2Config.getJsonObject().size();
        int key1 = RandomUtils.randomInt(1, size1);
        int key2 = RandomUtils.randomInt(1, size2);
        String name = name1Config.get(key1 + "").getString("name1") + name2Config.get(key2 + "").getString("name2");
        return name;
    }

    /**
     * @Description:选定默认英雄
     * @param heroId
     * @throws
     */
    public void initHero(String heroId) {
        Lord lord = getLord();
        gainPayService.gain(lord, heroId, 1);
        lord.setGuidanceHeroId(heroId);
        List<List<FormHold>> forms = lord.getForm();
        List<FormHold> list = new ArrayList<>();
        FormHold formHold = new FormHold();
        Map<String, Hero> heros = lord.getHeros();
        Set<String> setHeroUids = heros.keySet();
        String heroUid = "";
        for (String string : setHeroUids) {
            Hero hero = heros.get(string);
            if (hero.getHeroId().equals(heroId)) {
                heroUid = string;
            }
        }
        formHold.setHeroUid(heroUid);
        formHold.setEquipUid(new ArrayList<>());
        formHold.setSkillUid(new ArrayList<>());
        list.add(formHold);
        forms.add(0, list);
        lord.setForm(forms);
        this.gameModel.addObject("form", forms);
        lordRepository.save(lord);
    }

    /**
     * @Description:记录新手引导
     * @param step
     * @throws
     */
    public void guidanceStep(Integer step) {
        Lord lord = getLord();
        lord.setGuidanceStep(step);
        lordRepository.save(lord);
        Map<String, Object> map = new HashMap<>();
        map.put("guidanceStep", step);
        this.gameModel.addObject(ResponseKey.LORD, map);
    }

    /**
     * @Description:刷新玩家数据
     * @throws
     */
    public void updateLord() {
        Lord lord = getLord();
        Map<String, Object> map = new HashMap<>();
        map.put(ItemID.DIAMOND, lord.getDiamond());
        map.put(ItemID.PERSENT_DIAMOND, lord.getPersentDiamond());
        map.put("monthCard", lord.getMonthCard());
        map.put("vipLevel", lord.getVipLevel());
        map.put("vipScore", lord.getVipScore());
        this.gameModel.addObject(ResponseKey.LORD, map);

    }
}
