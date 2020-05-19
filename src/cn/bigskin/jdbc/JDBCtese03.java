package cn.bigskin.jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
    JDBC事务
        1.JDBC中的事务是自动提交的(只要执行任意一条DML语句，则自动提交一次)
        2.在实际的业务开发中，通常都是N条DML语句共同联合才能完成，必须保证
            这些DML语句在同一个事务中同时成功，或者同时失败

        3.转账例子
            sql脚本:
            drop table if exists t_act;
            create table t_act(
                actno bigint,
                balance double(7,2) //7表示有效数字，2表示小数的位数
            );
            insert into t_act(actno,balance) values(111,20000);
            insert into t_act(actno,balance) values(222,0);
            commit;
            select * from t_act;

         4.关闭事务自动提交
         conn.setAutoCommit(false);
         5.提交事务
         conn.commit();
         6.回滚事务
         conn.rollback();

 */
public class JDBCtese03 {
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

            //2.获取连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode",
                    "root", "AC1109..");
            //将自动提交改为手动
            conn.setAutoCommit(false);

            //获取与编译的数据库操作对象
            String sql = "update t_act set balance = ? where actno = ?";
            ps = conn.prepareStatement(sql);
            //给?传值
            ps.setDouble(1, 10000);
            ps.setInt(2, 111);
            int count = ps.executeUpdate();

            //给?传值
            ps.setDouble(1, 10000);
            ps.setInt(2, 222);
            count += ps.executeUpdate();

            //程序走到这里就说明以上语句全部成功
            conn.commit();
            //5.处理结果集
            if(rs.next()){
                flag = true;
            }
        }catch (ClassNotFoundException e){
            if(conn != null){
                try {
                    //回滚事务
                    conn.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
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
