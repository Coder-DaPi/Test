package cn.bigskin.jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
    解决sql注入问题:只要用户提供的信息并不参与sql语句的编译过程，问题就解决了。
    即使用户提供的信息含有sql语句的关键字，但是没有参与编译，不起作用。
    要想用户不参与sql语句的编译，那么必须使用java.sql.PreparedStatement
    PreparedStatement继承了Statement是属于预编译的数据库数据库操作对象。
    PreparedStatement的原理:预先对sql语句的框架进行编译，然后给sql语句传"值"。
 */
public class JDBCTest02 {
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
//      Statement stmt = null;
        PreparedStatement ps = null; //预编译的Statement对象
        ResultSet rs = null;
        try{
            //1.注册驱动
            Class.forName("com.mysql.jdbc.Driver");

            //2.获取链接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode",
                    "root", "AC1109..");

            //3.获取数据库操作对象
            //(解决注入问题)3.获取预编译数据库操作对象
//            stmt = conn.createStatement();


            //由4转3
            //SQL语句的框架，一个？代表一个占位符，一个？将来接收一个"值",占位符不能用''
            String sql = "select * from t_user where loginName = ? and loginPwd = ?";
            ps = conn.prepareStatement(sql);
            //给占位符传值(第几个？,下标就是几);
            ps.setString(1, loginName);
            ps.setString(2, loginPwd);

            //4.执行sql语句
            rs = ps.executeQuery();

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
            if (ps != null){
                try {
                    ps.close();
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
