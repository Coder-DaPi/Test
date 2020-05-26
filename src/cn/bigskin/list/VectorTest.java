package cn.bigskin.list;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Vector:
 * 底层时数组
 * 初始容量10
 * 每次扩容两倍
 * 是线程安全的
 * 效率低，用的少
 */
public class VectorTest {
    public static void main(String[] args) {

        List vector = new Vector();
        vector.add(1);

        //线程不安全
        List myList = new ArrayList();

        //转换为线程安全
        //java.util.Collections 工具类
        Collections.synchronizedList(myList);

    }
}
