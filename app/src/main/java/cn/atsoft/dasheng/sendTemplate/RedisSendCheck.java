package cn.atsoft.dasheng.sendTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    public Object getObject(String code) {
        //判断redis库是否有这个key
        if(Boolean.TRUE.equals(getRedisTemplate.hasKey(code))){
            return getRedisTemplate.opsForValue().get(code);
        }else{
            return null;
        }
    }
    //查询地址
    public void pushList(String code, List<Object> carts, Long  effectiveTime) {
        //判断redis库是否有这个key
        if(Boolean.TRUE.equals(getRedisTemplate.hasKey(code))){
            //如果有就查询redis里这个list集合（第一个参数是key,0,-1是查询所有）
            //TODO 这个判断暂时不用  在外层调用时先通过key获取是否存在key   如果存在 则从新生成code
            List<Object> range = getRedisTemplate.opsForList().range(code, 0,-1);

        }else{
            getRedisTemplate.opsForList().leftPushAll(code, carts);
            getRedisTemplate.expire(code,effectiveTime, TimeUnit.MILLISECONDS);
        }
    }
    public void pushObject(String code, Object o,Long  effectiveTime) {
        //判断redis库是否有这个key
        if(Boolean.TRUE.equals(getRedisTemplate.hasKey(code))){
            //如果有就查询redis里这个list集合（第一个参数是key,0,-1是查询所有）
            Object object = getRedisTemplate.opsForValue().get(code);
        }else{
            getRedisTemplate.opsForValue().set(code, o);
            getRedisTemplate.expire(code,effectiveTime, TimeUnit.MILLISECONDS);
        }
    }
    public void deleteListOrObject(String code) {
        //判断redis库是否有这个key
        if(Boolean.TRUE.equals(getRedisTemplate.hasKey(code))){
            getRedisTemplate.delete(code);
        }
    }


}