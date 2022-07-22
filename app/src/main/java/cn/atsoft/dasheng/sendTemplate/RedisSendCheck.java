package cn.atsoft.dasheng.sendTemplate;

import cn.atsoft.dasheng.core.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisSendCheck {

    //redis模板
    @Autowired
    private RedisTemplate<String, Object> getRedisTemplate;


    //查询地址
    public List<Object> getList(String code) {
        //判断redis库是否有这个key
        if(Boolean.TRUE.equals(getRedisTemplate.hasKey(code))){
            //如果有就查询redis里这个list集合（第一个参数是key,0,-1是查询所有）
            List<Object> range = getRedisTemplate.opsForList().range(code, 0,-1);
            //返回这个集合
            return range;
        }else{
            return null;
        }

    }
    //查询地址
    public Integer pushList(String code, List<Object> carts,Long  effectiveTime) {
        //判断redis库是否有这个key
        if(getRedisTemplate.hasKey(code)){
            //如果有就查询redis里这个list集合（第一个参数是key,0,-1是查询所有）
            List<Object> range = getRedisTemplate.opsForList().range(code, 0,-1);
            if (ToolUtil.isNotEmpty(carts)){
                range.addAll(carts);
            }

        }else{
            //从mysql里查询这个集合
            List<Object> newUserIds = new ArrayList<>();
            if (ToolUtil.isNotEmpty(carts)){
                newUserIds.addAll(carts);
            }
            getRedisTemplate.opsForList().leftPushAll(code, carts);
            getRedisTemplate.expire(code,effectiveTime, TimeUnit.MILLISECONDS);
        }
        return null;
    }
    public void deleteList(String code) {
        //判断redis库是否有这个key
        if(getRedisTemplate.hasKey(code)){
            //如果有就查询redis里这个list集合（第一个参数是key,0,-1是查询所有）
            getRedisTemplate.delete(code);
        }
    }
}