package protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by zhangchenlin on 17/4/7.
 * protoStuff序列化的工具类
 */
public class ProtoStuffSerializerUtil {

    //序列化对象
    public static <T> byte[] serialize(T obj)
    {
        if(obj==null)
        {
            throw new RuntimeException("序列化对象为空");
        }
        Schema<T> schema=(Schema<T>)RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer linkedBuffer=LinkedBuffer.allocate(1024*1024);
        byte[] protobuff=null;
        try {
            protobuff = ProtobufIOUtil.toByteArray(obj, schema, linkedBuffer);
        }catch(Exception e)
        {
            throw new RuntimeException("序列化对象失败。");
        }finally {
            linkedBuffer.clear();
        }
        return protobuff;
    }

    //反序列化
    public static <T> T deserialize(byte[] paramArray, Class<T> targetClass)
    {
        if(paramArray==null||paramArray.length==0)
        {
            throw new RuntimeException("反序列化对象异常，字节序列为空！");
        }
        T instance=null;
        try {
            instance = targetClass.newInstance();
        }catch (Exception e)
        {
            throw new RuntimeException("反序列化过程中依据类型创建对象失败!", e);
        }
        Schema<T> schema=RuntimeSchema.getSchema(targetClass);
        ProtobufIOUtil.mergeFrom(paramArray,instance,schema);
        return instance;
    }


    /**
     * 序列化列表
     * */

    public  static <T> byte[] serializeList(List<T> objList)
    {
        if (objList == null || objList.isEmpty()) {
            throw new RuntimeException("序列化对象列表(" + objList + ")参数异常!");
        }
        Schema<T> schema= (Schema<T>) RuntimeSchema.getSchema(objList.get(0).getClass());
        LinkedBuffer linkedBuffer=LinkedBuffer.allocate(1024*1024);
        byte[] protobuff=null;
        ByteArrayOutputStream bos=null;
        try{
            bos=new ByteArrayOutputStream();
            ProtobufIOUtil.writeListTo(bos,objList,schema,linkedBuffer);
            protobuff=bos.toByteArray();
        }catch (Exception e)
        {
            throw new RuntimeException("序列化对象列表(" + objList + ")发生异常!", e);
        } finally {
            linkedBuffer.clear();
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return protobuff;
    }


    //反序列化列表
    public  static <T> List<T> deserializeList(byte[] paramArray,Class<T> targetClass)
    {
        if (paramArray == null || paramArray.length == 0) {
            throw new RuntimeException("反序列化对象发生异常,byte序列为空!");
        }
        Schema<T> schema=RuntimeSchema.getSchema(targetClass);
        List<T> result=null;

        try {
            result=ProtobufIOUtil.parseListFrom(new ByteArrayInputStream(paramArray),schema);
        } catch (IOException e) {
            throw new RuntimeException("反序列化对象列表发生异常!", e);
        }
        return  result;

    }


}
