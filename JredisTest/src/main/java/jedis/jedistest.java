package jedis;

import redis.clients.jedis.Jedis;



/**
 * Created by zhangchenlin on 17/4/6.
 */
public class jedistest {

    public static void main(String[] args)
    {
        Jedis j=new Jedis("172.16.34.136",7001);
        System.out.println(j);
        j.set("zhangsan","wangadian");
    }





}
