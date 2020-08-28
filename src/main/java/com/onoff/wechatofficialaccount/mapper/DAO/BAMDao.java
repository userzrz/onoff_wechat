package com.onoff.wechatofficialaccount.mapper.DAO;

import com.mongodb.BasicDBObject;
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
     * 保存排行榜数据
     * @param weekLeaderboard
     */
    public void saveLeaderboard(WeekLeaderboard weekLeaderboard,String collection) {
        this.mongoTemplate.insert(weekLeaderboard,collection);
    }

    /**
     * 查询全部的数据
     * @param id
     */
    public WeekLeaderboard queryLeaderboard(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        return this.mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)),WeekLeaderboard.class);
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

}
