package cn.bigskin.jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
    实现功:模拟用户登录功能的实现。
    描述:程序运行的时候，提供一个可以输入的入口，可以让用户输入用户 名与密码
        用户输入完之后提交信息/java收集到用户信息，java程序连接到数据库验证
        用户名与密码是否合法。

    程序存在的问题:存在sql注入现象(安全隐患)(黑客经常使用)
        例如:用户名:asd
              密码:asd' or '1'='1
     出现sql注入的根本原因:用户输入的信息中含有sql语句的关键字,并且这些关键字参与
     sql语句的编译过程,导致SQL语句的原意被扭曲，进而达到sql注入.

     解决方法:
 */
public class JDBCTest {
    public static void main(String[] args) {
        //初始化界面
        Map<String, String> userLoginInfo = initUI();
        //验证用户名和密码
        boolean loginSuccess = login(userLoginInfo);
        //输出登录结果
        System.out.println(loginSuccess? "登录成功" : "登录失败");
    }

    /**
     * 用户登录
     * @param userLoginInfo
     * @return true表示成功，false表示失败
     */
    private static boolean login(Map<String, String> userLoginInfo) {
        //单独定义变量
        String loginName = userLoginInfo.get("loginName");
        String loginPwd = userLoginInfo.get("loginPwd");
        //
        boolean flag = false;
        //JDBC代码
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            //1.注册驱动
            Class.forName("com.mysql.jdbc.Driver");

            //2.获取链接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode",
                    "root", "AC1109..");

            //3.获取数据库操作对象
            stmt = conn.createStatement();
            //4.执行sql语句
            String sql = "select * from t_user where loginName = '"+loginName+"' and loginPwd = '"+loginPwd+"'";
            rs = stmt.executeQuery(sql);

            //5.处理结果集
            if(rs.next()){
                flag = true;
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //6.释放资源
            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }


        return flag;
    }

    /**
     * 初始化用户信息
     * @return 用户输入的用户名和密码等登录信息
     */
    private static Map<String, String> initUI() {
        Scanner in = new Scanner(System.in);

        System.out.println("用户名:");
        String userName = in.nextLine();

        System.out.println("密码:");
        String passWrod = in.nextLine();

        Map<String,String> userLoginInfo = new HashMap<>();
       userLoginInfo.put("loginName", userName);
       userLoginInfo.put("loginPwd", passWrod);

       return userLoginInfo;
    }
}
