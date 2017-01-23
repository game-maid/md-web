
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
    private int activeNum;
    /**
     * 2.   新增用户数：查询当天新建账号的用户数量。
     */
    @Field("new_user_num")
    private int newUserNum;
    /**
     * 3.   新增付费用户：查询当天第一次充值的用户数量。(首冲用户)
     */
    @Field("new_payer_num")
    private int newPayerNum;
    /**
     * 4.   付费人数：查询当天付费充值的用户数量。
     */
    @Field("payer_num")
    private int payerNum;
    /**
     * 5.   收入金额：查询当天收入金额（当天玩家共充值了多少金额）。
     */
    @Field("income_num")
    private double incomeNum;
    /**
     * 6.   收入次数：查询当天收入次数（当天玩家共充值了多少次）。
     */
    @Field("income_times")
    private int incomeTimes;
    /**
     * 前一天新增用户数
     */
    @Field("pre_one_new_user_num")
    private int preOneNewUserNum;
    /**
     * 前两天新增用户数
     */
    @Field("pre_two_new_user_num")
    private int preTwoNewUserNum;
    /**
     * 前三天新增用户
     */
    @Field("pre_three_new_user_num")
    private int preThreeNewUserNum;
    /**
     * 前4天新增用户
     */
    @Field("pre_four_new_user_num")
    private int preFourNewUserNum;
    /**
     * 前5天新增用户
     */
    @Field("pre_five_new_user_num")
    private int preFiveNewUserNum;
    /**
     * 前六天新增用户数
     */
    @Field("pre_six_new_user_num")
    private int preSixNewUserNum;
    /**
     * 前13天新增用户数
     */
    @Field("pre_thirteen_new_user_num")
    private int preThirteenNewUserNum;
    /**
     * 前15天新增用户
     */
    @Field("pre_fourteen_new_user_num")
    private int preFourteenNewUserNum;
    /**
     * 前20天新增用户
     */
    @Field("pre_twenty_new_user_num")
    private int preTwentyNewUserNum;
    /**
     * 前27天新增用户
     */
    @Field("pre_twenty_seven_new_user_num")
    private int preTwentySevenNewUserNum;
    /**
     * 前29天新增用户
     */
    @Field("pre_twenty_nine_new_user_num")
    private int preTwentyNineNewUserNum;
    /**
     * 前34天新增用户
     */
    @Field("pre_thirty_four_nine_new_user_num")
    private int preThirtyFourNewUserNum;
    /**
     * 前48天新增用户
     */
    @Field("pre_forty_eight_nine_new_user_num")
    private int preFortyEightNewUserNum;
    /**
     * 前55天新增用户
     */
    @Field("pre_fifty_five_nine_new_user_num")
    private int preFiftyFiveNewUserNum;
    /**
     * 前一天存留
     */
    @Field("pre_one_persistence")
    private int preOnePersistence;
    /**
     * 前两天存留
     */
    @Field("pre_two_persistence")
    private int preTwoPersistence;
    /**
     * 前3天存留
     */
    @Field("pre_three_persistence")
    private int preThreePersistence;
    /**
     * 前4天存留
     */
    @Field("pre_four_persistence")
    private int preFourPersistence;
    /**
     * 前5天存留
     */
    @Field("pre_five_persistence")
    private int preFivePersistence;
    /**
     * 前六天存留
     */
    @Field("pre_six_presistence")
    private int preSixPersistence;
    /**
     * 前15天存留
     */
    @Field("pre_fourteen_presistence")
    private int preFourteenPersistence;
    /**
     * 前29天存留
     */
    @Field("pre_twenty_nine_presistence")
    private int preTwentyNinePersistence;
    /**
     * 新注册玩家在注册当天的充值总数
     */
    private double persentIncomeNum;
    /**
     * 前一天注册用户到查询当天的付费总数
     */
    @Field("pre_one_income_num")
    private double preOneIncomeNum;
    /**
     * 前两天注册用户到查询当天付费总数
     */
    @Field("pre_two_income_num")
    private double preTwoIncomeNum;

    /**
     * 前六天注册的用户到查询当天付费总数
     */
    @Field("pre_six_incom_num")
    private double preSixIncomeNum;
    /**
     * 前十三天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_thirteen_income_num")
    private double preThirteenIncomeNum;
    /**
     * 前二十天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_twenty_income_num")
    private double preTwentyIncomeNum;

    /**
     * 前二十七天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_twenty_seven_incom_num")
    private double preTwentySevenIncomeNum;
    /**
     * 前三十四天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_thirty_four_income_num")
    private double preThirtyFourIncomeNum;
    /**
     * 前四十八天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_forty_eight_income_num")
    private double preFortyEightIncomeNum;

    /**
     * 前五十五天那天注册的用户从注册天数到查询当天为止的付费总数
     */
    @Field("pre_fifty_five_incom_num")
    private double preFiftyFiveIncomeNum;

    /**
     * 新注册付费用户（注册当天完成首冲）
     */
    @Field("new_user_payer")
    private int newUserPayer;

    /**
     * @return activeNum
     */
    public int getActiveNum() {
        return activeNum;
    }

    /**
     * @param activeNum 要设置的 activeNum
     */
    public void setActiveNum(int activeNum) {
        this.activeNum = activeNum;
    }

    /**
     * @return newUserNum
     */
    public int getNewUserNum() {
        return newUserNum;
    }

    /**
     * @param newUserNum 要设置的 newUserNum
     */
    public void setNewUserNum(int newUserNum) {
        this.newUserNum = newUserNum;
    }

    /**
     * @return newPayerNum
     */
    public int getNewPayerNum() {
        return newPayerNum;
    }

    /**
     * @param newPayerNum 要设置的 newPayerNum
     */
    public void setNewPayerNum(int newPayerNum) {
        this.newPayerNum = newPayerNum;
    }

    /**
     * @return payerNum
     */
    public int getPayerNum() {
        return payerNum;
    }

    /**
     * @param payerNum 要设置的 payerNum
     */
    public void setPayerNum(int payerNum) {
        this.payerNum = payerNum;
    }

    /**
     * @return incomeNum
     */
    public double getIncomeNum() {
        return incomeNum;
    }

    /**
     * @param incomeNum 要设置的 incomeNum
     */
    public void setIncomeNum(double incomeNum) {
        this.incomeNum = incomeNum;
    }

    /**
     * @return incomeTimes
     */
    public int getIncomeTimes() {
        return incomeTimes;
    }

    /**
     * @param incomeTimes 要设置的 incomeTimes
     */
    public void setIncomeTimes(int incomeTimes) {
        this.incomeTimes = incomeTimes;
    }

    /**
     * @return preOneNewUserNum
     */
    public int getPreOneNewUserNum() {
        return preOneNewUserNum;
    }

    /**
     * @param preOneNewUserNum 要设置的 preOneNewUserNum
     */
    public void setPreOneNewUserNum(int preOneNewUserNum) {
        this.preOneNewUserNum = preOneNewUserNum;
    }

    /**
     * @return preTwoNewUserNum
     */
    public int getPreTwoNewUserNum() {
        return preTwoNewUserNum;
    }

    /**
     * @param preTwoNewUserNum 要设置的 preTwoNewUserNum
     */
    public void setPreTwoNewUserNum(int preTwoNewUserNum) {
        this.preTwoNewUserNum = preTwoNewUserNum;
    }

    /**
     * @return preThreeNewUserNum
     */
    public int getPreThreeNewUserNum() {
        return preThreeNewUserNum;
    }

    /**
     * @param preThreeNewUserNum 要设置的 preThreeNewUserNum
     */
    public void setPreThreeNewUserNum(int preThreeNewUserNum) {
        this.preThreeNewUserNum = preThreeNewUserNum;
    }

    /**
     * @return preFourNewUserNum
     */
    public int getPreFourNewUserNum() {
        return preFourNewUserNum;
    }

    /**
     * @param preFourNewUserNum 要设置的 preFourNewUserNum
     */
    public void setPreFourNewUserNum(int preFourNewUserNum) {
        this.preFourNewUserNum = preFourNewUserNum;
    }

    /**
     * @return preFiveNewUserNum
     */
    public int getPreFiveNewUserNum() {
        return preFiveNewUserNum;
    }

    /**
     * @param preFiveNewUserNum 要设置的 preFiveNewUserNum
     */
    public void setPreFiveNewUserNum(int preFiveNewUserNum) {
        this.preFiveNewUserNum = preFiveNewUserNum;
    }

    /**
     * @return preSixNewUserNum
     */
    public int getPreSixNewUserNum() {
        return preSixNewUserNum;
    }

    /**
     * @param preSixNewUserNum 要设置的 preSixNewUserNum
     */
    public void setPreSixNewUserNum(int preSixNewUserNum) {
        this.preSixNewUserNum = preSixNewUserNum;
    }

    /**
     * @return preThirteenNewUserNum
     */
    public int getPreThirteenNewUserNum() {
        return preThirteenNewUserNum;
    }

    /**
     * @param preThirteenNewUserNum 要设置的 preThirteenNewUserNum
     */
    public void setPreThirteenNewUserNum(int preThirteenNewUserNum) {
        this.preThirteenNewUserNum = preThirteenNewUserNum;
    }

    /**
     * @return preFourteenNewUserNum
     */
    public int getPreFourteenNewUserNum() {
        return preFourteenNewUserNum;
    }

    /**
     * @param preFourteenNewUserNum 要设置的 preFourteenNewUserNum
     */
    public void setPreFourteenNewUserNum(int preFourteenNewUserNum) {
        this.preFourteenNewUserNum = preFourteenNewUserNum;
    }

    /**
     * @return preTwentyNewUserNum
     */
    public int getPreTwentyNewUserNum() {
        return preTwentyNewUserNum;
    }

    /**
     * @param preTwentyNewUserNum 要设置的 preTwentyNewUserNum
     */
    public void setPreTwentyNewUserNum(int preTwentyNewUserNum) {
        this.preTwentyNewUserNum = preTwentyNewUserNum;
    }

    /**
     * @return preTwentySevenNewUserNum
     */
    public int getPreTwentySevenNewUserNum() {
        return preTwentySevenNewUserNum;
    }

    /**
     * @param preTwentySevenNewUserNum 要设置的 preTwentySevenNewUserNum
     */
    public void setPreTwentySevenNewUserNum(int preTwentySevenNewUserNum) {
        this.preTwentySevenNewUserNum = preTwentySevenNewUserNum;
    }

    /**
     * @return preTwentyNineNewUserNum
     */
    public int getPreTwentyNineNewUserNum() {
        return preTwentyNineNewUserNum;
    }

    /**
     * @param preTwentyNineNewUserNum 要设置的 preTwentyNineNewUserNum
     */
    public void setPreTwentyNineNewUserNum(int preTwentyNineNewUserNum) {
        this.preTwentyNineNewUserNum = preTwentyNineNewUserNum;
    }

    /**
     * @return preThirtyFourNewUserNum
     */
    public int getPreThirtyFourNewUserNum() {
        return preThirtyFourNewUserNum;
    }

    /**
     * @param preThirtyFourNewUserNum 要设置的 preThirtyFourNewUserNum
     */
    public void setPreThirtyFourNewUserNum(int preThirtyFourNewUserNum) {
        this.preThirtyFourNewUserNum = preThirtyFourNewUserNum;
    }

    /**
     * @return preFortyEightNewUserNum
     */
    public int getPreFortyEightNewUserNum() {
        return preFortyEightNewUserNum;
    }

    /**
     * @param preFortyEightNewUserNum 要设置的 preFortyEightNewUserNum
     */
    public void setPreFortyEightNewUserNum(int preFortyEightNewUserNum) {
        this.preFortyEightNewUserNum = preFortyEightNewUserNum;
    }

    /**
     * @return preFiftyFiveNewUserNum
     */
    public int getPreFiftyFiveNewUserNum() {
        return preFiftyFiveNewUserNum;
    }

    /**
     * @param preFiftyFiveNewUserNum 要设置的 preFiftyFiveNewUserNum
     */
    public void setPreFiftyFiveNewUserNum(int preFiftyFiveNewUserNum) {
        this.preFiftyFiveNewUserNum = preFiftyFiveNewUserNum;
    }

    /**
     * @return preOnePersistence
     */
    public int getPreOnePersistence() {
        return preOnePersistence;
    }

    /**
     * @param preOnePersistence 要设置的 preOnePersistence
     */
    public void setPreOnePersistence(int preOnePersistence) {
        this.preOnePersistence = preOnePersistence;
    }

    /**
     * @return preTwoPersistence
     */
    public int getPreTwoPersistence() {
        return preTwoPersistence;
    }

    /**
     * @param preTwoPersistence 要设置的 preTwoPersistence
     */
    public void setPreTwoPersistence(int preTwoPersistence) {
        this.preTwoPersistence = preTwoPersistence;
    }

    /**
     * @return preThreePersistence
     */
    public int getPreThreePersistence() {
        return preThreePersistence;
    }

    /**
     * @param preThreePersistence 要设置的 preThreePersistence
     */
    public void setPreThreePersistence(int preThreePersistence) {
        this.preThreePersistence = preThreePersistence;
    }

    /**
     * @return preFourPersistence
     */
    public int getPreFourPersistence() {
        return preFourPersistence;
    }

    /**
     * @param preFourPersistence 要设置的 preFourPersistence
     */
    public void setPreFourPersistence(int preFourPersistence) {
        this.preFourPersistence = preFourPersistence;
    }

    /**
     * @return preFivePersistence
     */
    public int getPreFivePersistence() {
        return preFivePersistence;
    }

    /**
     * @param preFivePersistence 要设置的 preFivePersistence
     */
    public void setPreFivePersistence(int preFivePersistence) {
        this.preFivePersistence = preFivePersistence;
    }

    /**
     * @return preSixPersistence
     */
    public int getPreSixPersistence() {
        return preSixPersistence;
    }

    /**
     * @param preSixPersistence 要设置的 preSixPersistence
     */
    public void setPreSixPersistence(int preSixPersistence) {
        this.preSixPersistence = preSixPersistence;
    }

    /**
     * @return preFourteenPersistence
     */
    public int getPreFourteenPersistence() {
        return preFourteenPersistence;
    }

    /**
     * @param preFourteenPersistence 要设置的 preFourteenPersistence
     */
    public void setPreFourteenPersistence(int preFourteenPersistence) {
        this.preFourteenPersistence = preFourteenPersistence;
    }

    /**
     * @return preTwentyNinePersistence
     */
    public int getPreTwentyNinePersistence() {
        return preTwentyNinePersistence;
    }

    /**
     * @param preTwentyNinePersistence 要设置的 preTwentyNinePersistence
     */
    public void setPreTwentyNinePersistence(int preTwentyNinePersistence) {
        this.preTwentyNinePersistence = preTwentyNinePersistence;
    }

    /**
     * @return preOneIncomeNum
     */
    public double getPreOneIncomeNum() {
        return preOneIncomeNum;
    }

    /**
     * @param preOneIncomeNum 要设置的 preOneIncomeNum
     */
    public void setPreOneIncomeNum(double preOneIncomeNum) {
        this.preOneIncomeNum = preOneIncomeNum;
    }

    /**
     * @return preTwoIncomeNum
     */
    public double getPreTwoIncomeNum() {
        return preTwoIncomeNum;
    }

    /**
     * @param preTwoIncomeNum 要设置的 preTwoIncomeNum
     */
    public void setPreTwoIncomeNum(double preTwoIncomeNum) {
        this.preTwoIncomeNum = preTwoIncomeNum;
    }

    /**
     * @return preSixIncomeNum
     */
    public double getPreSixIncomeNum() {
        return preSixIncomeNum;
    }

    /**
     * @param preSixIncomeNum 要设置的 preSixIncomeNum
     */
    public void setPreSixIncomeNum(double preSixIncomeNum) {
        this.preSixIncomeNum = preSixIncomeNum;
    }

    /**
     * @return preThirteenIncomeNum
     */
    public double getPreThirteenIncomeNum() {
        return preThirteenIncomeNum;
    }

    /**
     * @param preThirteenIncomeNum 要设置的 preThirteenIncomeNum
     */
    public void setPreThirteenIncomeNum(double preThirteenIncomeNum) {
        this.preThirteenIncomeNum = preThirteenIncomeNum;
    }

    /**
     * @return preTwentyIncomeNum
     */
    public double getPreTwentyIncomeNum() {
        return preTwentyIncomeNum;
    }

    /**
     * @param preTwentyIncomeNum 要设置的 preTwentyIncomeNum
     */
    public void setPreTwentyIncomeNum(double preTwentyIncomeNum) {
        this.preTwentyIncomeNum = preTwentyIncomeNum;
    }

    /**
     * @return preTwentySevenIncomeNum
     */
    public double getPreTwentySevenIncomeNum() {
        return preTwentySevenIncomeNum;
    }

    /**
     * @param preTwentySevenIncomeNum 要设置的 preTwentySevenIncomeNum
     */
    public void setPreTwentySevenIncomeNum(double preTwentySevenIncomeNum) {
        this.preTwentySevenIncomeNum = preTwentySevenIncomeNum;
    }

    /**
     * @return preThirtyFourIncomeNum
     */
    public double getPreThirtyFourIncomeNum() {
        return preThirtyFourIncomeNum;
    }

    /**
     * @param preThirtyFourIncomeNum 要设置的 preThirtyFourIncomeNum
     */
    public void setPreThirtyFourIncomeNum(double preThirtyFourIncomeNum) {
        this.preThirtyFourIncomeNum = preThirtyFourIncomeNum;
    }

    /**
     * @return preFortyEightIncomeNum
     */
    public double getPreFortyEightIncomeNum() {
        return preFortyEightIncomeNum;
    }

    /**
     * @param preFortyEightIncomeNum 要设置的 preFortyEightIncomeNum
     */
    public void setPreFortyEightIncomeNum(double preFortyEightIncomeNum) {
        this.preFortyEightIncomeNum = preFortyEightIncomeNum;
    }

    /**
     * @return preFiftyFiveIncomeNum
     */
    public double getPreFiftyFiveIncomeNum() {
        return preFiftyFiveIncomeNum;
    }

    /**
     * @param preFiftyFiveIncomeNum 要设置的 preFiftyFiveIncomeNum
     */
    public void setPreFiftyFiveIncomeNum(double preFiftyFiveIncomeNum) {
        this.preFiftyFiveIncomeNum = preFiftyFiveIncomeNum;
    }

    /**
     * @return newUserPayer
     */
    public int getNewUserPayer() {
        return newUserPayer;
    }

    /**
     * @param newUserPayer 要设置的 newUserPayer
     */
    public void setNewUserPayer(int newUserPayer) {
        this.newUserPayer = newUserPayer;
    }

    /**
     * @return persentIncomeNum
     */
    public double getPersentIncomeNum() {
        return persentIncomeNum;
    }

    /**
     * @param persentIncomeNum 要设置的 persentIncomeNum
     */
    public void setPersentIncomeNum(double persentIncomeNum) {
        this.persentIncomeNum = persentIncomeNum;
    }

    /**.
     * <p>Title: toString</p>
     * <p>Description: </p>
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ActiveBasePackage [activeNum=" + activeNum + ", newUserNum=" + newUserNum + ", newPayerNum="
                + newPayerNum + ", payerNum=" + payerNum + ", incomeNum=" + incomeNum + ", incomeTimes=" + incomeTimes
                + ", preOneNewUserNum=" + preOneNewUserNum + ", preTwoNewUserNum=" + preTwoNewUserNum
                + ", preThreeNewUserNum=" + preThreeNewUserNum + ", preFourNewUserNum=" + preFourNewUserNum
                + ", preFiveNewUserNum=" + preFiveNewUserNum + ", preSixNewUserNum=" + preSixNewUserNum
                + ", preThirteenNewUserNum=" + preThirteenNewUserNum + ", preFourteenNewUserNum="
                + preFourteenNewUserNum + ", preTwentyNewUserNum=" + preTwentyNewUserNum + ", preTwentySevenNewUserNum="
                + preTwentySevenNewUserNum + ", preTwentyNineNewUserNum=" + preTwentyNineNewUserNum
                + ", preThirtyFourNewUserNum=" + preThirtyFourNewUserNum + ", preFortyEightNewUserNum="
                + preFortyEightNewUserNum + ", preFiftyFiveNewUserNum=" + preFiftyFiveNewUserNum
                + ", preOnePersistence=" + preOnePersistence + ", preTwoPersistence=" + preTwoPersistence
                + ", preThreePersistence=" + preThreePersistence + ", preFourPersistence=" + preFourPersistence
                + ", preFivePersistence=" + preFivePersistence + ", preSixPersistence=" + preSixPersistence
                + ", preFourteenPersistence=" + preFourteenPersistence + ", preTwentyNinePersistence="
                + preTwentyNinePersistence + ", preOneIncomeNum=" + preOneIncomeNum + ", preTwoIncomeNum="
                + preTwoIncomeNum + ", preSixIncomeNum=" + preSixIncomeNum + ", preThirteenIncomeNum="
                + preThirteenIncomeNum + ", preTwentyIncomeNum=" + preTwentyIncomeNum + ", preTwentySevenIncomeNum="
                + preTwentySevenIncomeNum + ", preThirtyFourIncomeNum=" + preThirtyFourIncomeNum
                + ", preFortyEightIncomeNum=" + preFortyEightIncomeNum + ", preFiftyFiveIncomeNum="
                + preFiftyFiveIncomeNum + ", newUserPayer=" + newUserPayer + "]";
    }

}
