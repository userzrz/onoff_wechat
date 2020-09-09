package com.onoff.wechatofficialaccount.mapper.DAO;

import com.mongodb.BasicDBObject;
import com.onoff.wechatofficialaccount.entity.DO.PromotionQR;
import com.onoff.wechatofficialaccount.entity.DO.SignInQR;
import com.onoff.wechatofficialaccount.entity.DO.WeekLeaderboard;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/8/14 12:33
 * @VERSION 1.0
 **/
@Repository
public class BAMDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存推广二维码信息
     */
    public void savePromotionQR(PromotionQR promotionQR){
        this.mongoTemplate.insert(promotionQR,CommonUtils.MONGODB_PROMOTIONQR);
    }

    /**
     * 使用id查询推广二维码信息
     */
    public PromotionQR queryPromotionQR(String id){
        Query query = Query.query(Criteria.where("id").is(id));
        return this.mongoTemplate.findOne(query,PromotionQR.class,CommonUtils.MONGODB_PROMOTIONQR);
    }

    /**
     * 使用time查询推广二维码信息
     */
    public PromotionQR queryPromotionQR(Long time){
        Query query = Query.query(Criteria.where("time").is(time));
        return this.mongoTemplate.findOne(query,PromotionQR.class,CommonUtils.MONGODB_PROMOTIONQR);
    }

    /**
     * 保存打卡二维码信息
     */
    public void saveSignInQR(SignInQR signInQR){
        this.mongoTemplate.insert(signInQR,CommonUtils.MONGODB_SIGINQR);
    }

    /**
     * 查询打卡二维码信息
     */
    public SignInQR querySignInQR(String id){
        Query query = Query.query(Criteria.where("id").is(id));
        return this.mongoTemplate.findOne(query,SignInQR.class,CommonUtils.MONGODB_SIGINQR);
    }

    /**
     * 保存排行榜数据
     * @param weekLeaderboard
     */
    public void saveLeaderboard(WeekLeaderboard weekLeaderboard,String collection) {
        this.mongoTemplate.insert(weekLeaderboard,collection);
    }

    /**
     * 查询指定id的数据
     * @param id id
     * @param collection 指定集合
     * @return
     */
    public WeekLeaderboard queryLeaderboard(String id,String collection) {
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", new ObjectId(id));
        Query query = Query.query(Criteria.where("id").is(id));
        return this.mongoTemplate.findOne(query,WeekLeaderboard.class,collection);
    }


    /**
     * 查询全部的数据
     * @param collection
     */
    public List<WeekLeaderboard> queryLeaderboardAll(String collection) {
        return this.mongoTemplate.findAll(WeekLeaderboard.class,collection);
    }

    /**
     * 删除集合
     */
    public void delCollection(String collection){
        this.mongoTemplate.dropCollection(collection);
    }

    /**
     * 返回集合总数
     * @param collection
     * @return
     */
    public long countCollection(String collection){
        //查询字段不为空的数据
        Query query = Query.query(Criteria.where("id").ne(null).ne(""));
        return this.mongoTemplate.count(query,collection);
    }
}
