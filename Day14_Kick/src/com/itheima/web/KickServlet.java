package com.itheima.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.itheima.domain.User;
import com.itheima.util.DaoUtils;

public class KickServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.获取要踢下线的人的id
		String id = request.getParameter("id");
		//2.根据id查询出用户
		String sql = "select * from user where id = ?";
		QueryRunner runner = new QueryRunner(DaoUtils.getSource());
		User user = null;
		try {
			user = runner.query(sql, new BeanHandler<User>(User.class),id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//3.从usermap中找到这个用户对应的session
		ServletContext context = this.getServletContext();
		Map<User,HttpSession> map = (Map<User, HttpSession>) context.getAttribute("usermap");
		HttpSession session = map.get(user);
		//4.杀死这个用户session
		if(session!=null)
			session.invalidate();
		//5.重定向到用户列表页面
		response.sendRedirect(request.getContextPath()+"/userList.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
