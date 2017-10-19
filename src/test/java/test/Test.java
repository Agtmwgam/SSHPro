package test;

import haoran.entity.Userinfo;
import haoran.service.IUserService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	@org.junit.Test
	public  void t() {
		ApplicationContext as=new ClassPathXmlApplicationContext(new String[]{"classpath:spring.xml"});
		IUserService is=(IUserService)as.getBean("userService");
		Userinfo u=new Userinfo();
		u.setUsername("test");
		u.setAge(20);
		is.saveUser(u);
		System.out.println("save ok");
	}

}
