package test;

import RedisCache.ClusterCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;
import test_entity.User;

/**
 * Created by zhangchenlin on 17/4/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-cluster.xml")
public class jedistest {

    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private ClusterCache clusterCache;

    @Test
    public void run()
    {
       // System.out.println(jedisCluster.set("zhangsan","lisi"));
        System.out.println(jedisCluster.get("zhangsan"));
//
         User user =new User("zhangsan","nan",23);
        clusterCache.putCache("zhangsan",user);
        System.out.println(clusterCache.getCache("zhangsan",User.class));
        System.out.println(clusterCache.keys("*"));
    }


}
