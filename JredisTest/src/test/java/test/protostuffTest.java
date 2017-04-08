package test;

import org.junit.Test;
import protostuff.ProtoStuffSerializerUtil;
import test_entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchenlin on 17/4/7.
 */

public class protostuffTest {

    @Test
    public void test()
    {
        User user=new User("zhangsan","nan",26);
        byte[] array= ProtoStuffSerializerUtil.serialize(user);
        System.out.println(ProtoStuffSerializerUtil.deserialize(array,User.class));
        User user1=new User("lisi","nv",24);
        User user2=new User("wangwwu","nan",23);
        List<User> list=new ArrayList<User>();
        list.add(user);
        list.add(user1);
        list.add(user2);
        byte[] array2=ProtoStuffSerializerUtil.serializeList(list);
        System.out.println(ProtoStuffSerializerUtil.deserializeList(array2,User.class));
    }
    @Test
    public void test2()
    {
        User user=new User("zhangsan","nan",26);
        byte[] array= ProtoStuffSerializerUtil.serialize(user);
        System.out.println(ProtoStuffSerializerUtil.deserialize(array,User.class));
    }

}
