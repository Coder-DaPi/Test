package cn.bigskin.list;

import java.util.*;

public class LinkedListTest {
    public static void main(String[] args) {
        //面向接口编程
//      List listArray = new ArrayList();
//      Map hashMap = new HashMap();
        List list = new LinkedList();
        list.add("a");
        list.add("b");
        list.add("c");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

}
