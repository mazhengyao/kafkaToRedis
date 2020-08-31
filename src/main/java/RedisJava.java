import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RedisJava {
    public static void main(String[] args) {
        //���ӱ��ص� Redis ����
        Jedis jedis = new Jedis("localhost");
        // ��� Redis �������������룬��Ҫ�������У�û�оͲ���Ҫ
        // jedis.auth("123456"); 
        System.out.println("���ӳɹ�");
        //�鿴�����Ƿ�����
        System.out.println("������������: "+jedis.ping());
        //���� redis �ַ�������
        jedis.set("google", "www.google.com");
        // ��ȡ�洢�����ݲ����
        System.out.println("redis �洢���ַ���Ϊ: "+ jedis.get("google"));
        //�洢���ݵ��б���
        jedis.lpush("site-list", "Google");
        jedis.lpush("site-list", "Youtube");
        jedis.lpush("site-list", "Github");
        // ��ȡ�洢�����ݲ����
        List<String> list = jedis.lrange("site-list", 0 ,2);
        for(int i=0; i<list.size(); i++) {
            System.out.println("�б���Ϊ: "+list.get(i));
        }
        // ��ȡ���ݲ����
        Set<String> keys = jedis.keys("*");
        Iterator<String> it = keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }
    }
}