
package com.talentwalker.game.md.core.domain.statistics;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @ClassName: ActiveBasePackage
 * @Description: 活跃存留   包数据（每日详细数据）
 * @author <a href="mailto:zhangfutao@talentwalker.com">张福涛</a> 于 2016年12月22日 下午8:45:38
 */
public class ActiveBasePackage {
    /**
     * 1.   活跃数：查询当天有登陆行为的玩家数量。
     */
    @Field("active_num")
    private Integer activeNum;
    /**
     * 2.   新增用户数：查询当天新建账号的用户数量。
     */
    @Field("new_user_num")
    private Integer newUserNum;
    /**
     * 3.   新增付费用户：查询当天第一次充值的用户数量。(首冲用户)
     */
    @Field("new_payer_num")
    private Integer newPayerNum;
    /**
     * 4.   付费人数：查询当天付费充值的用户数量。
     */
    @Field("payer_num")
    private Integer payerNum;
    /**
     * 5.   收入金额：查询当天收入金额（当天玩家共充值了多少金额）。
     */
    @Field("income_num")
    private Double incomeNum;
    /**
     * 6.   收入次数：查询当天收入次数（当天玩家共充值了多少次）。
     */
    @Field("income_times")
    private Integer incomeTimes;
    /**
     * 前一天新增用户数
     */
    @Field("pre_one_new_user_num")
    private Integer preOneNewUserNum;
    /**
     * 前两天新增用户数
     */
    @Field("pre_two_new_user_num")
    private Integer preTwoNewUserNum;
    /**
     * 前三天新增用户
     */
    @Field("pre_three_new_user_num")
    private Integer preThreeNewUserNum;
    /**
     * 前4天新增用户
     */
    @Field("pre_four_new_user_num")
    private Integer preFourNewUserNum;
    /**
     * 前5天新增用户
     */
    @Field("pre_five_new_user_num")
    private Integer preFiveNewUserNum;
    /**
     * 前六天新增用户数
     */
    @Field("pre_six_new_user_num")
    private Integer preSixNewUserNum;
    /**
     * 前13天新增用户数
     */
    @Field("pre_thirteen_new_user_num")
    private Integer preThirteenNewUserNum;
    /**
     * 前15天新增用户
     */
    @Field("pre_fourteen_new_user_num")
    private Integer preFourteenNewUserNum;
    /**
     * 前20天新增用户
     */
    @Field("pre_twenty_new_user_num")
    private Integer preTwentyNewUserNum;
    /**
     * 前27天新增用户
     */
    @Field("pre_twenty_seven_new_user_num")
    private Integer preTwentySevenNewUserNum;
    /**
     * 前29天新增用户
     */
    @Field("pre_twenty_nine_new_user_num")
    private Integer preTwentyNineNewUserNum;
    /**
     * 前34天新增用户
     */
    @Field("pre_thirty_four_nine_new_user_num")
    private Integer preThirtyFourNewUserNum;
    /**
     * 前48天新增用户
     */
    @Field("pre_forty_eight_nine_new_user_num")
    private Integer preFortyEightNewUserNum;
    /**
     * 前55天新增用户
     */
    @Field("pre_fifty_five_nine_new_user_num")
    private Integer preFiftyFiveNewUserNum;
    /**
     * 前一天存留
     */
    @Field("pre_one_persistence")
    private Integer preOnePersistence;
    /**
     * 前两天存留
     */
    @Field("pre_two_persistence")
    private Integer preTwoPersistence;
    /**
     * 前3天存留
     */
    @Field("pre_three_persistence")
    private Integer preThreePersistence;
    /**
     * 前4天存留
     */
    @Field("pre_four_persistence")
    private Integer preFourPersistence;
    /**
     * 前5天存留
     */
    @Field("pre_five_persistence")
    private Integer preFivePersistence;
    /**
     * 前六天存留
     */
    @Field("pre_six_presistence")
    private Integer preSixPersistence;
    /**
     * 前15天存留
     */
    @Field("pre_fourteen_presistence")
    private Integer preFourteenPersistence;
    /**
     * 前29天存留
     */
    @Field("pre_twenty_nine_presistence")
    private Integer preTwentyNinePersistence;
    /**
     * 前一天注册用户到查询当天的付费总数
     */
    @Field("pre_one_income_num")
    private Double preOneIncomeNum;
    /**
     * 前两天注册用户到查询当天付费总数
     */
    @Field("pre_two_income_num")
    private Double preTwoIncomeNum;

    /**
     * 前六天注册的用户到查询当天付费总数
     */
    @Field("pre_six_incom_num")
    private Double preSixIncomeNum;
    /**
     * 前十三天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_thirteen_income_num")
    private Double preThirteenIncomeNum;
    /**
     * 前二十天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_twenty_income_num")
    private Double preTwentyIncomeNum;

    /**
     * 前二十七天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_twenty_seven_incom_num")
    private Double preTwentySevenIncomeNum;
    /**
     * 前三十四天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_thirty_four_income_num")
    private Double preThirtyFourIncomeNum;
    /**
     * 前四十八天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_forty_eight_income_num")
    private Double preFortyEightIncomeNum;

    /**
     * 前五十五天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_fifty_five_incom_num")
    private Double preFiftyFiveIncomeNum;

    /**
     * 新注册付费用户（注册当天完成首冲）
     */
    @Field("new_user_payer")
    private Integer newUserPayer;

    /**
     * @return activeNum
     */
    public Integer getActiveNum() {
        return activeNum;
    }

    /**
     * @param activeNum 要设置的 activeNum
     */
    public void setActiveNum(Integer activeNum) {
        this.activeNum = activeNum;
    }

    /**
     * @return newUserNum
     */
    public Integer getNewUserNum() {
        return newUserNum;
    }

    /**
     * @param newUserNum 要设置的 newUserNum
     */
    public void setNewUserNum(Integer newUserNum) {
        this.newUserNum = newUserNum;
    }

    /**
     * @return newPayerNum
     */
    public Integer getNewPayerNum() {
        return newPayerNum;
    }

    /**
     * @param newPayerNum 要设置的 newPayerNum
     */
    public void setNewPayerNum(Integer newPayerNum) {
        this.newPayerNum = newPayerNum;
    }

    /**
     * @return payerNum
     */
    public Integer getPayerNum() {
        return payerNum;
    }

    /**
     * @param payerNum 要设置的 payerNum
     */
    public void setPayerNum(Integer payerNum) {
        this.payerNum = payerNum;
    }

    /**
     * @return incomeNum
     */
    public Double getIncomeNum() {
        return incomeNum;
    }

    /**
     * @param incomeNum 要设置的 incomeNum
     */
    public void setIncomeNum(Double incomeNum) {
        this.incomeNum = incomeNum;
    }

    /**
     * @return incomeTimes
     */
    public Integer getIncomeTimes() {
        return incomeTimes;
    }

    /**
     * @param incomeTimes 要设置的 incomeTimes
     */
    public void setIncomeTimes(Integer incomeTimes) {
        this.incomeTimes = incomeTimes;
    }

    /**
     * @return preOneNewUserNum
     */
    public Integer getPreOneNewUserNum() {
        return preOneNewUserNum;
    }

    /**
     * @param preOneNewUserNum 要设置的 preOneNewUserNum
     */
    public void setPreOneNewUserNum(Integer preOneNewUserNum) {
        this.preOneNewUserNum = preOneNewUserNum;
    }

    /**
     * @return preTwoNewUserNum
     */
    public Integer getPreTwoNewUserNum() {
        return preTwoNewUserNum;
    }

    /**
     * @param preTwoNewUserNum 要设置的 preTwoNewUserNum
     */
    public void setPreTwoNewUserNum(Integer preTwoNewUserNum) {
        this.preTwoNewUserNum = preTwoNewUserNum;
    }

    /**
     * @return preThreeNewUserNum
     */
    public Integer getPreThreeNewUserNum() {
        return preThreeNewUserNum;
    }

    /**
     * @param preThreeNewUserNum 要设置的 preThreeNewUserNum
     */
    public void setPreThreeNewUserNum(Integer preThreeNewUserNum) {
        this.preThreeNewUserNum = preThreeNewUserNum;
    }

    /**
     * @return preFourNewUserNum
     */
    public Integer getPreFourNewUserNum() {
        return preFourNewUserNum;
    }

    /**
     * @param preFourNewUserNum 要设置的 preFourNewUserNum
     */
    public void setPreFourNewUserNum(Integer preFourNewUserNum) {
        this.preFourNewUserNum = preFourNewUserNum;
    }

    /**
     * @return preFiveNewUserNum
     */
    public Integer getPreFiveNewUserNum() {
        return preFiveNewUserNum;
    }

    /**
     * @param preFiveNewUserNum 要设置的 preFiveNewUserNum
     */
    public void setPreFiveNewUserNum(Integer preFiveNewUserNum) {
        this.preFiveNewUserNum = preFiveNewUserNum;
    }

    /**
     * @return preSixNewUserNum
     */
    public Integer getPreSixNewUserNum() {
        return preSixNewUserNum;
    }

    /**
     * @param preSixNewUserNum 要设置的 preSixNewUserNum
     */
    public void setPreSixNewUserNum(Integer preSixNewUserNum) {
        this.preSixNewUserNum = preSixNewUserNum;
    }

    /**
     * @return preThirteenNewUserNum
     */
    public Integer getPreThirteenNewUserNum() {
        return preThirteenNewUserNum;
    }

    /**
     * @param preThirteenNewUserNum 要设置的 preThirteenNewUserNum
     */
    public void setPreThirteenNewUserNum(Integer preThirteenNewUserNum) {
        this.preThirteenNewUserNum = preThirteenNewUserNum;
    }

    /**
     * @return preFourteenNewUserNum
     */
    public Integer getPreFourteenNewUserNum() {
        return preFourteenNewUserNum;
    }

    /**
     * @param preFourteenNewUserNum 要设置的 preFourteenNewUserNum
     */
    public void setPreFourteenNewUserNum(Integer preFourteenNewUserNum) {
        this.preFourteenNewUserNum = preFourteenNewUserNum;
    }

    /**
     * @return preTwentyNewUserNum
     */
    public Integer getPreTwentyNewUserNum() {
        return preTwentyNewUserNum;
    }

    /**
     * @param preTwentyNewUserNum 要设置的 preTwentyNewUserNum
     */
    public void setPreTwentyNewUserNum(Integer preTwentyNewUserNum) {
        this.preTwentyNewUserNum = preTwentyNewUserNum;
    }

    /**
     * @return preTwentySevenNewUserNum
     */
    public Integer getPreTwentySevenNewUserNum() {
        return preTwentySevenNewUserNum;
    }

    /**
     * @param preTwentySevenNewUserNum 要设置的 preTwentySevenNewUserNum
     */
    public void setPreTwentySevenNewUserNum(Integer preTwentySevenNewUserNum) {
        this.preTwentySevenNewUserNum = preTwentySevenNewUserNum;
    }

    /**
     * @return preTwentyNineNewUserNum
     */
    public Integer getPreTwentyNineNewUserNum() {
        return preTwentyNineNewUserNum;
    }

    /**
     * @param preTwentyNineNewUserNum 要设置的 preTwentyNineNewUserNum
     */
    public void setPreTwentyNineNewUserNum(Integer preTwentyNineNewUserNum) {
        this.preTwentyNineNewUserNum = preTwentyNineNewUserNum;
    }

    /**
     * @return preThirtyFourNewUserNum
     */
    public Integer getPreThirtyFourNewUserNum() {
        return preThirtyFourNewUserNum;
    }

    /**
     * @param preThirtyFourNewUserNum 要设置的 preThirtyFourNewUserNum
     */
    public void setPreThirtyFourNewUserNum(Integer preThirtyFourNewUserNum) {
        this.preThirtyFourNewUserNum = preThirtyFourNewUserNum;
    }

    /**
     * @return preFortyEightNewUserNum
     */
    public Integer getPreFortyEightNewUserNum() {
        return preFortyEightNewUserNum;
    }

    /**
     * @param preFortyEightNewUserNum 要设置的 preFortyEightNewUserNum
     */
    public void setPreFortyEightNewUserNum(Integer preFortyEightNewUserNum) {
        this.preFortyEightNewUserNum = preFortyEightNewUserNum;
    }

    /**
     * @return preFiftyFiveNewUserNum
     */
    public Integer getPreFiftyFiveNewUserNum() {
        return preFiftyFiveNewUserNum;
    }

    /**
     * @param preFiftyFiveNewUserNum 要设置的 preFiftyFiveNewUserNum
     */
    public void setPreFiftyFiveNewUserNum(Integer preFiftyFiveNewUserNum) {
        this.preFiftyFiveNewUserNum = preFiftyFiveNewUserNum;
    }

    /**
     * @return preOnePersistence
     */
    public Integer getPreOnePersistence() {
        return preOnePersistence;
    }

    /**
     * @param preOnePersistence 要设置的 preOnePersistence
     */
    public void setPreOnePersistence(Integer preOnePersistence) {
        this.preOnePersistence = preOnePersistence;
    }

    /**
     * @return preTwoPersistence
     */
    public Integer getPreTwoPersistence() {
        return preTwoPersistence;
    }

    /**
     * @param preTwoPersistence 要设置的 preTwoPersistence
     */
    public void setPreTwoPersistence(Integer preTwoPersistence) {
        this.preTwoPersistence = preTwoPersistence;
    }

    /**
     * @return preThreePersistence
     */
    public Integer getPreThreePersistence() {
        return preThreePersistence;
    }

    /**
     * @param preThreePersistence 要设置的 preThreePersistence
     */
    public void setPreThreePersistence(Integer preThreePersistence) {
        this.preThreePersistence = preThreePersistence;
    }

    /**
     * @return preFourPersistence
     */
    public Integer getPreFourPersistence() {
        return preFourPersistence;
    }

    /**
     * @param preFourPersistence 要设置的 preFourPersistence
     */
    public void setPreFourPersistence(Integer preFourPersistence) {
        this.preFourPersistence = preFourPersistence;
    }

    /**
     * @return preFivePersistence
     */
    public Integer getPreFivePersistence() {
        return preFivePersistence;
    }

    /**
     * @param preFivePersistence 要设置的 preFivePersistence
     */
    public void setPreFivePersistence(Integer preFivePersistence) {
        this.preFivePersistence = preFivePersistence;
    }

    /**
     * @return preSixPersistence
     */
    public Integer getPreSixPersistence() {
        return preSixPersistence;
    }

    /**
     * @param preSixPersistence 要设置的 preSixPersistence
     */
    public void setPreSixPersistence(Integer preSixPersistence) {
        this.preSixPersistence = preSixPersistence;
    }

    /**
     * @return preFourteenPersistence
     */
    public Integer getPreFourteenPersistence() {
        return preFourteenPersistence;
    }

    /**
     * @param preFourteenPersistence 要设置的 preFourteenPersistence
     */
    public void setPreFourteenPersistence(Integer preFourteenPersistence) {
        this.preFourteenPersistence = preFourteenPersistence;
    }

    /**
     * @return preTwentyNinePersistence
     */
    public Integer getPreTwentyNinePersistence() {
        return preTwentyNinePersistence;
    }

    /**
     * @param preTwentyNinePersistence 要设置的 preTwentyNinePersistence
     */
    public void setPreTwentyNinePersistence(Integer preTwentyNinePersistence) {
        this.preTwentyNinePersistence = preTwentyNinePersistence;
    }

    /**
     * @return preOneIncomeNum
     */
    public Double getPreOneIncomeNum() {
        return preOneIncomeNum;
    }

    /**
     * @param preOneIncomeNum 要设置的 preOneIncomeNum
     */
    public void setPreOneIncomeNum(Double preOneIncomeNum) {
        this.preOneIncomeNum = preOneIncomeNum;
    }

    /**
     * @return preTwoIncomeNum
     */
    public Double getPreTwoIncomeNum() {
        return preTwoIncomeNum;
    }

    /**
     * @param preTwoIncomeNum 要设置的 preTwoIncomeNum
     */
    public void setPreTwoIncomeNum(Double preTwoIncomeNum) {
        this.preTwoIncomeNum = preTwoIncomeNum;
    }

    /**
     * @return preSixIncomeNum
     */
    public Double getPreSixIncomeNum() {
        return preSixIncomeNum;
    }

    /**
     * @param preSixIncomeNum 要设置的 preSixIncomeNum
     */
    public void setPreSixIncomeNum(Double preSixIncomeNum) {
        this.preSixIncomeNum = preSixIncomeNum;
    }

    /**
     * @return preThirteenIncomeNum
     */
    public Double getPreThirteenIncomeNum() {
        return preThirteenIncomeNum;
    }

    /**
     * @param preThirteenIncomeNum 要设置的 preThirteenIncomeNum
     */
    public void setPreThirteenIncomeNum(Double preThirteenIncomeNum) {
        this.preThirteenIncomeNum = preThirteenIncomeNum;
    }

    /**
     * @return preTwentyIncomeNum
     */
    public Double getPreTwentyIncomeNum() {
        return preTwentyIncomeNum;
    }

    /**
     * @param preTwentyIncomeNum 要设置的 preTwentyIncomeNum
     */
    public void setPreTwentyIncomeNum(Double preTwentyIncomeNum) {
        this.preTwentyIncomeNum = preTwentyIncomeNum;
    }

    /**
     * @return preTwentySevenIncomeNum
     */
    public Double getPreTwentySevenIncomeNum() {
        return preTwentySevenIncomeNum;
    }

    /**
     * @param preTwentySevenIncomeNum 要设置的 preTwentySevenIncomeNum
     */
    public void setPreTwentySevenIncomeNum(Double preTwentySevenIncomeNum) {
        this.preTwentySevenIncomeNum = preTwentySevenIncomeNum;
    }

    /**
     * @return preThirtyFourIncomeNum
     */
    public Double getPreThirtyFourIncomeNum() {
        return preThirtyFourIncomeNum;
    }

    /**
     * @param preThirtyFourIncomeNum 要设置的 preThirtyFourIncomeNum
     */
    public void setPreThirtyFourIncomeNum(Double preThirtyFourIncomeNum) {
        this.preThirtyFourIncomeNum = preThirtyFourIncomeNum;
    }

    /**
     * @return preFortyEightIncomeNum
     */
    public Double getPreFortyEightIncomeNum() {
        return preFortyEightIncomeNum;
    }

    /**
     * @param preFortyEightIncomeNum 要设置的 preFortyEightIncomeNum
     */
    public void setPreFortyEightIncomeNum(Double preFortyEightIncomeNum) {
        this.preFortyEightIncomeNum = preFortyEightIncomeNum;
    }

    /**
     * @return preFiftyFiveIncomeNum
     */
    public Double getPreFiftyFiveIncomeNum() {
        return preFiftyFiveIncomeNum;
    }

    /**
     * @param preFiftyFiveIncomeNum 要设置的 preFiftyFiveIncomeNum
     */
    public void setPreFiftyFiveIncomeNum(Double preFiftyFiveIncomeNum) {
        this.preFiftyFiveIncomeNum = preFiftyFiveIncomeNum;
    }

    /**
     * @return newUserPayer
     */
    public Integer getNewUserPayer() {
        return newUserPayer;
    }

    /**
     * @param newUserPayer 要设置的 newUserPayer
     */
    public void setNewUserPayer(Integer newUserPayer) {
        this.newUserPayer = newUserPayer;
    }

}
