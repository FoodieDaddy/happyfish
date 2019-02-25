package com.mdmd.entity.bean;

import com.mdmd.entity.FishRuleEntity;
import com.mdmd.service.GameRuleService;
import com.mdmd.util.CommonUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * 存储捕鱼概率和记录概率类
 */
public class FishProbabilityBean {

    private ArrayList<Integer> values;//概率显示化集合
    private int index;//当前角标
    private int price;//倍率
    private int targetValue;//红包数值
    private double minReturn;//不中最小返还
    private double maxReturn;//不中最大返还

    public FishProbabilityBean(int probability,int volume, int price, int targetValue, double minReturn, double maxReturn) {
        this.price = price;
        this.targetValue = targetValue;
        this.maxReturn = maxReturn;
        this.minReturn = minReturn;
        this.index = 0;
        this.initValues(probability,volume);
    }

    public void updateValue(FishRuleEntity fishRuleEntity){
        this.price = fishRuleEntity.getPrice();
        this.targetValue = fishRuleEntity.getTargetValue();
        this.minReturn = fishRuleEntity.getMinReturn();
        this.maxReturn = fishRuleEntity.getMaxReturn();
        this.index = 0;
        this.initValues(fishRuleEntity.getProbability(),fishRuleEntity.getVolume());
    }

    /**
     * 返回结果
     */
    public GameResultBean getResult(){
        int res = values.get(index);
        ++ index;
        //未中 输出返利
        if(res == 0 )
        {
            double dif = maxReturn - minReturn;
            if(dif == 0)
                return new GameResultBean(false,0);

            //生成区间内随机数
            double flag = 100.00;
            double j = (double) (new Random().nextInt((int)(dif*flag)))/flag + minReturn;
            return new GameResultBean(false, CommonUtil.formatDouble_two(j));
        }
        //红包数值为0 既为？ 必中
        if(this.targetValue == 0)
        {
            double dif = maxReturn - minReturn;
            if(dif == 0)
                return new GameResultBean(true,0);
            double flag = 100.00;
            double j = (double) (new Random().nextInt((int)(dif*flag)))/flag + minReturn;
            return new GameResultBean(true, CommonUtil.formatDouble_two(j));
        }
       return new GameResultBean(true,this.targetValue);
    }

    /**
     * 初始化值
     * @param probability
     * @param volume
     */
    private void initValues(int probability,int volume){
        int done ,none = 0;
        if(probability >= volume)
        {
            done = volume;
        }
        else
        {
            done = probability;
            none = volume - probability;
        }
        values = new ArrayList<>();
        //添加不中次数
        for (int i = 0; i < none; i++) {
            values.add(0);
        }
        //添加必中次数
        for (int i = 0; i < done; i++) {
            values.add(1);
        }
        //打乱顺序
        Collections.shuffle(values);

    }


    public int getIndex() {
            return index;
    }

    public void setIndex(int index) {
            this.index = index;
    }

    public int getPrice() {
        return price;
    }


    public int getTargetValue() {
        return targetValue;
    }


}
