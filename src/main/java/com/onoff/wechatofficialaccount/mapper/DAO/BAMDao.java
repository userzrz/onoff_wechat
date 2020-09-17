package com.onoff.wechatofficialaccount.mapper.DAO;

import com.mongodb.BasicDBObject;
import com.onoff.wechatofficialaccount.entity.DO.*;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import javafx.geometry.Pos;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
     * 保存口令信息
     */
    public void saveCommand(Command command) {
        this.mongoTemplate.insert(command, CommonUtils.MONGODB_COMMAND);
    }

    /**
     * 使用口令查询信息
     */
    public Command queryCommand(String code) {
        Query query = Query.query(Criteria.where("id").is(code));
        return this.mongoTemplate.findOne(query,Command.class, CommonUtils.MONGODB_COMMAND);
    }

    /**
     * 保存用户请求海报时间
     * @return
     */
    public void savePoster(Poster poster){
        this.mongoTemplate.insert(poster, CommonUtils.MONGODB_POSTER);
    }

    /**
     * 查询用户请求海报时间
     * @return
     */
    public Poster queryPoster(String id){
        Query query = Query.query(Criteria.where("id").is(id));
        return this.mongoTemplate.findOne(query, Poster.class, CommonUtils.MONGODB_POSTER);
    }

    /**
     * 修改用户请求海报时间
     * @return
     */
    public void putPoster(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = Update.update("time", System.currentTimeMillis());
        this.mongoTemplate.upsert(query,update,Poster.class, CommonUtils.MONGODB_POSTER);
    }


    /**
     * 使用id查询推广二维码信息
     */
    public Cycle queryCycle() {
        Query query = Query.query(Criteria.where("id").is("1"));
        return this.mongoTemplate.findOne(query, Cycle.class, CommonUtils.MONGODB_CYCLE);
    }

    /**
     * 更新起始日期
     *
     */
    public void putCycle(Cycle cycle) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is("1"));
        Update update;
        if (cycle.getWeekStartDate() != null&&cycle.getWeekPeriod()!=null) {
            update = Update.update("weekStartDate", cycle.getWeekStartDate());
            this.mongoTemplate.upsert(query,update,Cycle.class, CommonUtils.MONGODB_CYCLE);
            update = Update.update("weekPeriod", cycle.getWeekPeriod());
            this.mongoTemplate.upsert(query,update,Cycle.class, CommonUtils.MONGODB_CYCLE);
        }
        if (cycle.getMonthStartDate() != null&&cycle.getMonthPeriod()!=null) {
            update = Update.update("monthStartDate", cycle.getMonthStartDate());
            this.mongoTemplate.upsert(query,update,Cycle.class, CommonUtils.MONGODB_CYCLE);
            update = Update.update("monthPeriod", cycle.getMonthPeriod());
            this.mongoTemplate.upsert(query,update,Cycle.class, CommonUtils.MONGODB_CYCLE);
        }
    }

    /**
     * 保存推广二维码信息
     */
    public void savePromotionQR(PromotionQR promotionQR) {
        this.mongoTemplate.insert(promotionQR, CommonUtils.MONGODB_PROMOTIONQR);
    }

    /**
     * 使用id查询推广二维码信息
     */
    public PromotionQR queryPromotionQR(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return this.mongoTemplate.findOne(query, PromotionQR.class, CommonUtils.MONGODB_PROMOTIONQR);
    }

    /**
     * 使用time查询推广二维码信息
     */
    public PromotionQR queryPromotionQR(Long time) {
        Query query = Query.query(Criteria.where("time").is(time));
        return this.mongoTemplate.findOne(query, PromotionQR.class, CommonUtils.MONGODB_PROMOTIONQR);
    }

    /**
     * 保存打卡二维码信息
     */
    public void saveSignInQR(SignInQR signInQR) {
        this.mongoTemplate.insert(signInQR, CommonUtils.MONGODB_SIGINQR);
    }

    /**
     * 查询打卡二维码信息
     */
    public SignInQR querySignInQR(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return this.mongoTemplate.findOne(query, SignInQR.class, CommonUtils.MONGODB_SIGINQR);
    }

    /**
     * 保存排行榜数据
     *
     * @param weekLeaderboard
     */
    public void saveLeaderboard(WeekLeaderboard weekLeaderboard, String collection) {
        this.mongoTemplate.insert(weekLeaderboard, collection);
    }

    /**
     * 查询指定id的数据
     *
     * @param id         id
     * @param collection 指定集合
     * @return
     */
    public WeekLeaderboard queryLeaderboard(String id, String collection) {
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", new ObjectId(id));
        Query query = Query.query(Criteria.where("id").is(id));
        return this.mongoTemplate.findOne(query, WeekLeaderboard.class, collection);
    }


    /**
     * 查询全部的数据
     *
     * @param collection
     */
    public List<WeekLeaderboard> queryLeaderboardAll(String collection) {
        return this.mongoTemplate.findAll(WeekLeaderboard.class, collection);
    }

    /**
     * 删除集合
     */
    public void delCollection(String collection) {
        this.mongoTemplate.dropCollection(collection);
    }

    /**
     * 返回集合总数
     *
     * @param collection
     * @return
     */
    public long countCollection(String collection) {
        //查询字段不为空的数据
        Query query = Query.query(Criteria.where("id").ne(null).ne(""));
        return this.mongoTemplate.count(query, collection);
    }
}
