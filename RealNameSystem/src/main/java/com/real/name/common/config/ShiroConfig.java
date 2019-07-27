package com.real.name.common.config;


import com.real.name.auth.shiro.LoginShiroRealm;
import com.real.name.auth.shiro.MyCredentialsMatcher;
import com.real.name.auth.shiro.MySessionManager;
import com.real.name.auth.shiro.ShiroSessionListener;
import com.real.name.auth.shiro.filter.MyFormAuthenticationFilter;
import com.real.name.auth.shiro.filter.MyLogoutFilter;
import com.real.name.auth.shiro.filter.MyPermissionsAuthorizationFilter;
import com.real.name.auth.shiro.filter.MyUserFilter;
import com.real.name.common.utils.PathUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private String noLoginUrl = "/user/noLogin";

    private String loginSuccessUrl = "/user/loginSuccess";

    private String unauthorizedUrl = "/user/unauthorized";

    private String localUrl = "http://192.168.230.126:9901";

    private String serverUrl = "http://139.9.47.190:9901";


    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        //设置登录URL, 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        /*bean.setLoginUrl(noLoginUrl);
        //登录成功后要跳转的链接
        /*bean.setSuccessUrl(loginSuccessUrl);*/
        //未授权界面
        //bean.setUnauthorizedUrl(unauthorizedUrl);

        //配置自定义拦截器
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("authc", new MyFormAuthenticationFilter());
        filterMap.put("perms", new MyPermissionsAuthorizationFilter());
        filterMap.put("logout", new MyLogoutFilter());
        filterMap.put("user", new MyUserFilter());
        bean.setFilters(filterMap);

        //配置访问权限
        Map<String, String> map = new LinkedHashMap<>();
        //登录
        //map.put(noLoginUrl, "anon");
        //map.put(loginSuccessUrl, "anon");
        //map.put(unauthorizedUrl, "anon");
        map.put("/user/submitLogin", "anon");
        //注册
        map.put("/user/userRegister", "anon");
        //静态资源
        map.put("/static/**", "anon");
        //登出
        map.put("/user/logout", "logout");
        //项目
        //map.put("/project/searchProject", "perms[searchProject]");
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问,就是不用登录
        map.put("/**", "user");
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }

    /**
     * 配置核心安全事务管理器
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        //注入缓存
        securityManager.setCacheManager(ehCacheManager());
        //注入登录的realm
        securityManager.setRealm(loginShiroRealm());
        //注入记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());
        //配置自定义session管理，使用ehcache
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 登录认证的realm
     */
    @Bean
    public LoginShiroRealm loginShiroRealm(){
        LoginShiroRealm loginShiroRealm = new LoginShiroRealm();
        loginShiroRealm.setCredentialsMatcher(myCredentialsMatcher());
        return loginShiroRealm;
    }

    /**
     * 缓存管理器
     */
    @Bean
    public CacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return ehCacheManager;
    }

    /**
     * ============================SessionManager相关配置======================================
     */

    /**
     * 会话管理
     */
    @Bean
    public SessionManager sessionManager() {
        //DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        MySessionManager mySessionManager = new MySessionManager();
        //配置监听
        ArrayList<SessionListener> listeners = new ArrayList<>();
        listeners.add(sessionListener());
        mySessionManager.setSessionListeners(listeners);
        mySessionManager.setSessionIdCookie(sessionIdCookie());
        mySessionManager.setCacheManager(ehCacheManager());
        mySessionManager.setSessionDAO(sessionDAO());
        //全局会话超时时间（单位毫秒），默认30分钟, 这里设置为12小时1000 * 3600 * 12
        mySessionManager.setGlobalSessionTimeout(1000 * 3600 * 12);
        //是否开启删除无效的session对象  默认为true
        mySessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        mySessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间 单位毫秒, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时, 这里设置为4小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        mySessionManager.setSessionValidationInterval(1000 * 3600 * 4);
        return mySessionManager;
    }

    /**
     * 配置session监听
     */
    @Bean("sessionListener")
    public SessionListener sessionListener() {
        return new ShiroSessionListener();
    }

    /**
     * 配置会话ID生成器
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     */
    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        //使用ehCacheManager
        enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("sessionCache");
        //sessionId生成器
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }

    /**
     * rememberMeCookie
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        //simpleCookie.setDomain(localUrl);
        //设置记住我的有效时间,单位秒, 设置为7天 3600 * 24 * 7
        simpleCookie.setMaxAge(3600 * 24 * 7);
        return simpleCookie;
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("sessionIdCookie");
        simpleCookie.setHttpOnly(true);
        //simpleCookie.setDomain(localUrl);
        //设置有效时间,单位秒, 12小时 3600 * 24 * 12
        simpleCookie.setMaxAge(3600 * 24 * 12);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥
        rememberMeManager.setCipherKey(Base64.decodeBase64("2AvVhdsgUs0FSA3SDFAdag=="));
        return rememberMeManager;
    }

    /**
     * 配置自定义密码比较器
     */
    @Bean
    public MyCredentialsMatcher myCredentialsMatcher() {
        return new MyCredentialsMatcher();
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro生命周期处理器 * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,
     * 并在必要时进行安全逻辑验证 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和
     * AuthorizationAttributeSourceAdvisor)即可实现此功能 * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


}
