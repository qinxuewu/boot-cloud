### Spring Security简介
- Spring Security为基于Java EE的企业软件应用程序提供全面的安全服务。
- Spring Security的原理就是使用很多的拦截器对URL进行拦截，以此来管理登录验证和用户权限验证。
- 用户登陆，会被AuthenticationProcessingFilter拦截，调用AuthenticationManager的实现，而且AuthenticationManager会调用ProviderManager来获取用户验证信息（不同的Provider调用的服务不同，因为这些信息可以是在数据库上，可以是在LDAP服务器上，可以是xml配置文件上等），如果验证通过后会将用户的权限信息封装一个User放到spring的全局缓存SecurityContextHolder中，以备后面访问资源时使用。
- 所以我们要自定义用户的校验机制的话，我们只要实现自己的AuthenticationProvider就可以了。在用AuthenticationProvider 这个之前，我们需要提供一个获取用户信息的服务，实现  UserDetailsService 接口
- 用户名密码->(Authentication(未认证)  ->  AuthenticationManager ->AuthenticationProvider->UserDetailService->UserDetails->Authentication(已认证）



### 参考
- 官方文档： https://docs.spring.io/spring-security/site/docs/4.1.0.RELEASE/reference/htmlsingle/