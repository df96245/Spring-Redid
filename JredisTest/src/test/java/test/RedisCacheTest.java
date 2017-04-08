package test;

import RedisCache.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test_entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchenlin on 17/4/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-cluster.xml")
public class RedisCacheTest {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
   private Cache cache;
    @Test
    public void run()
    {
        User user=new User("zhangsan","nan",23);
        User user1=new User("lisi","nv",18);
        List<User> list=new ArrayList<User>();
        list.add(user);
        list.add(user1);
//        cache.putCache("zhangsan",user);
//        //cache.delkey("zhangsan");
//        User user1= cache.getCache("zhangsan",User.class);
//        System.out.print(user1);

      //  cache.putCachewithExpiretime("zhangsan",user,50);

         cache.putListCache("list",list);
        System.out.println(cache.getListCache("list",User.class));
    }



}
