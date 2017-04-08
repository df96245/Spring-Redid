package RedisCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import protostuff.ProtoStuffSerializerUtil;

import java.util.List;
import java.util.Set;

/**
 * Created by zhangchenlin on 17/4/7.
 * Redis缓存
 */
@Component
public class Cache {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //存入缓存
    public  <T> boolean putCache(String key,T obj)
    {
        final byte[] bkey=key.getBytes();
        final byte[] bvalue= ProtoStuffSerializerUtil.serialize(obj);
        boolean result=redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.setNX(bkey,bvalue);
            }
        });
        return  result;
    }

    //存入缓存，有效期为Expiretime
    public <T> void putCachewithExpiretime(String key,T obj,final int Expiretime)
    {
        final byte[] bkey=key.getBytes();
        final byte[] bvalue= ProtoStuffSerializerUtil.serialize(obj);
        redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                  connection.setEx(bkey,Expiretime,bvalue);
                return  true;
            }
        });
    }



    //从缓存中读取
    public <T> T getCache(final String key,Class<T> targetClass)
    {
        byte[] result=redisTemplate.execute(new RedisCallback<byte[]>() {
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(key.getBytes());
            }
        });
        if(result==null)
        {
            return null;
        }
        return ProtoStuffSerializerUtil.deserialize(result,targetClass);
    }

    //将列表放入缓存
    public <T> boolean putListCache(String key,List<T> list)
    {
        //用于匿名内部类的外部变量必须为final
        final byte[] bkey=key.getBytes();
        final byte[] blist=ProtoStuffSerializerUtil.serializeList(list);

        boolean result=redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.setNX(bkey,blist);
            }
        });
        return result;
    }

    public <T> void putListCachewithExpiretime(String key,List<T> objList,final int Expiretime)
    {
        final byte[] bkey=key.getBytes();
        final byte[] bvalue= ProtoStuffSerializerUtil.serialize(objList);
        redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setEx(bkey,Expiretime,bvalue);
                return  true;
            }
        });
    }



    //读取列表
    public <T> List<T> getListCache(final String key,Class<T> targetClass)
    {
        byte[] result=redisTemplate.execute(new RedisCallback<byte[]>() {
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(key.getBytes());
            }
        });
        if(result==null)
        {
            return null;
        }
        return ProtoStuffSerializerUtil.deserializeList(result,targetClass);
    }


    //精删除key
    public void delkey(final String key)
    {
        redisTemplate.delete(key);
    }

   //模糊删除key
    public void delkeywithPattern(String pattern)
    {
        Set<String> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }


}
