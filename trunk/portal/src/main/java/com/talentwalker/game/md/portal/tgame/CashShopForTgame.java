/**
 * @Title: CashShopForTgameController.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年11月15日  赵丽宝
 */

package com.talentwalker.game.md.portal.tgame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talentwalker.game.md.core.dataconfig.IDataConfigManager;
import com.talentwalker.game.md.core.domain.GameUser;
import com.talentwalker.game.md.core.domain.gameworld.Lord;
import com.talentwalker.game.md.core.domain.gameworld.Order;
import com.talentwalker.game.md.core.domain.gameworld.PushOrderTgame;
import com.talentwalker.game.md.core.domain.statistics.StatisticsPayer;
import com.talentwalker.game.md.core.repository.GameUserRepository;
import com.talentwalker.game.md.core.repository.gameworld.LordRepository;
import com.talentwalker.game.md.core.repository.gameworld.OrderRepository;
import com.talentwalker.game.md.core.repository.statistics.StatisticsPayerRepository;
import com.talentwalker.game.md.core.service.gameworld.TopUpCardService;
import com.talentwalker.game.md.core.util.MD5Utils;

/**
 * TGame平台支付
 * @ClassName: CashShopForTgameController
 * @Description: Description of this class
 * @author <a href="mailto:zhaolibao@talentwalker.com">赵丽宝</a> 于 2016年11月15日 下午7:34:42
 */
@Controller
@RequestMapping(value = "portal")
public class CashShopForTgame {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    GameUserRepository gameUserRepository;
    @Autowired
    LordRepository lordRepository;
    @Autowired
    TopUpCardService topUpCardService;
    @Resource
    IDataConfigManager configManager;
    @Resource
    StatisticsPayerRepository statisticsPayerRepository;

    public static Map<String, String> appKey = new HashMap<String, String>();
    public static Map<String, String> appId = new HashMap<String, String>();
    public static Map<String, String> server = new HashMap<String, String>();

    private static final Logger logger = Logger.getLogger(CashShopForTgame.class);

    static {
        appKey.put("tgameiOS", "38d0078d7b166b3dcd0b5a9d0b1e718d");
        appKey.put("tgameAndroid", "1b3cbee57e790086e2dadf1a6c3b2ade");

        appId.put("tgameiOS", "20986231");
        appId.put("tgameAndroid", "20369887");

        server.put("tgameiOS", "http://vn.payment.7talent.com/platform/api/user/verifyToken");
        server.put("tgameAndroid", "http://vn.payment.7talent.com/platform/api/user/verifyToken");
    }

    /**
    * TGame支付回调
    * @Description:
    * @param order 推送订单
    * @throws
    */
    @RequestMapping(value = "tgame/orderAsyn", method = RequestMethod.POST)
    @ResponseBody
    public String addOrderAsyn(PushOrderTgame order) {
        logger.info("**************订单id-" + order.getOrderId() + "*************");
        String orderId = order.getExtInfo();
        Order orderRecord = orderRepository.findOne(orderId);
        GameUser gameUser = orderRecord.getGameUser();
        Lord lord = orderRecord.getLord();
        logger.info("**************签名-" + order.getSign() + "*************");
        if (checkSign(order, appKey.get(gameUser.getPackageId()))) {
            logger.info("**************签名验证成功-" + order.getSign() + "*************");
            if (orderRecord.getState() == Order.STATE_NO) {
                topUpCardService.topUp(lord, order.getProductId(), gameUser);
                orderRecord.setState(Order.STATE_YES);
                orderRecord.setOrderId(order.getOrderId());
                orderRecord.setPayTime(System.currentTimeMillis());
                orderRepository.save(orderRecord);
                // 后台统计
                statistics(gameUser, lord);
            } else {
                // 处理过了
            }
        }

        /*
         * GameUser gameUser = gameUserRepository.findDistinctBySsoIdAndPlatformIdAndGameZoneId(order.getUserId(),
         * PushOrderTgame.PLATFORM_ID, order.getExtInfo()); if (checkSign(order, appKey.get(gameUser.getPackageId()))) {
         * Order orderRecord = orderRepository.findOne(order.getOrderId()); if (orderRecord == null) { DataConfig config
         * = configManager.getTest(); Double price = config.get(ConfigKey.CASH_SHOP_CONFIG).get(order.getProductId())
         * .getDouble(ConfigKey.CASH_SHOP_CONFIG_PRICE); Lord lord = lordRepository.findOne(gameUser.getId());
         * topUpCardService.topUp(lord, order.getProductId(), gameUser); orderRecord = new Order();
         * orderRecord.setId(order.getOrderId()); orderRecord.setGameUser(gameUser); orderRecord.setLord(lord);
         * orderRecord.setProductId(order.getProductId()); orderRecord.setQuantity(order.getProductNum());
         * orderRecord.setCustomId(extInfo); orderRecord.setZoneId(gameUser.getGameZoneId());
         * orderRecord.setPackageId(gameUser.getPackageId()); orderRecord.setPrice(price);
         * orderRecord.setLordId(lord.getId()); orderRepository.save(orderRecord); // 后台统计 statistics(gameUser, lord); }
         * else {// 已经处理过了 } }
         */
        return "success";

    }

    /**
     * @Description:后台统计
     * @param productId
     * @param gameUser
     * @throws
     */
    private void statistics(GameUser gameUser, Lord lord) {
        Calendar cal = Calendar.getInstance();
        long now = cal.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        String format = sdf.format(cal.getTime());
        try {
            cal.setTime(sdf.parse(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long start = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long end = cal.getTimeInMillis();

        List<StatisticsPayer> statisticsPayers = statisticsPayerRepository.findBylordIdAndPayTime(lord.getId(), start,
                end);
        if (statisticsPayers == null || statisticsPayers.size() == 0) {
            StatisticsPayer statisticsPayer = new StatisticsPayer();
            statisticsPayer.setLordId(lord.getId());
            statisticsPayer.setPackageId(gameUser.getPackageId());
            statisticsPayer.setZoneId(gameUser.getGameZoneId());
            statisticsPayer.setPayTime(now);
            statisticsPayerRepository.save(statisticsPayer);
        }
    }

    /**
    * @Description: 校验签名
    * @param order
    * @throws
    */
    private boolean checkSign(PushOrderTgame order, String appKey) {
        String sign = order.getSign();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", order.getOrderId());
        map.put("appId", order.getAppId());
        map.put("productId", order.getProductId());
        map.put("productNum", order.getProductNum());
        map.put("dealPrice", order.getDealPrice());
        map.put("extInfo", order.getExtInfo());
        map.put("userId", order.getUserId());
        String localSign = MD5Utils.md5Encode(createLinkString(paraFilterPush(map)) + appKey);
        if (localSign.equals(sign)) {
            return true;
        }
        return false;
    }

    /**
    * 过滤空参数
    *
    * @param map 参数map
    * @return 过滤后的map
    */
    private static Map<String, String> paraFilterPush(Map<String, Object> map) {

        Map<String, String> result = new HashMap<String, String>();
        if (map == null || map.size() <= 0) {
            return result;
        }
        for (String key : map.keySet()) {
            String value = null == map.get(key) ? "" : map.get(key).toString();
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
    * 将参数map中的key进行字典排序
    *
    * @param map 参数map
    * @return 处理后的字符串
    */
    private String createLinkString(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

}
