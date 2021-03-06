/**
 * 
 */
package com.lytz.finance.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j2;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author cloudlu
 *
 */
@Log4j2
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

    public CaptchaFormAuthenticationFilter() {  
    }  
    
    
    private DefaultWebSessionManager sessionManager;
    
    @Autowired
	public void setSessionManager(DefaultWebSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	} 
    
    private MessageSource messageSource;
    
    /**
     * @param messageSource the messageSource to set
     */
    @Autowired
    @Qualifier(value="messageSource")
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private LocaleResolver localeResolver;
    
    /**
     * @param localResolver the localResolver to set
     */
    @Autowired
    @Qualifier(value="localeResolver")
    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
    
    @Override  
    /** 
     * 登录验证 
     */  
    protected boolean executeLogin(ServletRequest request,  
            ServletResponse response) throws Exception {  
        CaptchaUsernamePasswordToken token = createToken(request, response);
        try {
            if(null == token){
                throw new AuthenticationException(messageSource.getMessage("errors.invalid", new Object[]{"token"}, localeResolver.resolveLocale((HttpServletRequest) request)));
            }
            doCaptchaValidate((HttpServletRequest) request, token);  
            Subject subject = getSubject(request, response);  
            subject.login(token);
            if(LOG.isDebugEnabled()){
                LOG.info(token.getUsername()+"login success");  
            }
            return onLoginSuccess(token, subject, request, response);  
        }catch (AuthenticationException e) {  
            if(LOG.isWarnEnabled()){
                LOG.warn(token.getUsername()+"login failure--"+e);  
            }
            return onLoginFailure(token, e, request, response);  
        }  
    }  
  

    protected void doCaptchaValidate(HttpServletRequest request,  
            CaptchaUsernamePasswordToken token) {  
        String captcha = (String) request.getSession().getAttribute(  
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);   
        if (captcha != null && !captcha.equalsIgnoreCase(token.getCaptcha())) {  
            throw new IncorrectCaptchaException(messageSource.getMessage("errors.invalid", new Object[]{"captcha"}, localeResolver.resolveLocale(request)));  
        }  
    }  
  
    @Override  
    protected CaptchaUsernamePasswordToken createToken(ServletRequest request,  
            ServletResponse response) {  
        String username = getUsername(request);  
        String password = getPassword(request);  
        String captcha = getCaptcha(request);  
        boolean rememberMe = isRememberMe(request);  
        String host = getHost(request);  
  
        return new CaptchaUsernamePasswordToken(username,  
                password.toCharArray(), rememberMe, host, captcha);  
    }  
  
    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";  
  
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;  
  
    public String getCaptchaParam() {  
        return captchaParam;  
    }  
  
    public void setCaptchaParam(String captchaParam) {  
        this.captchaParam = captchaParam;  
    }  
  
    protected String getCaptcha(ServletRequest request) {  
        return WebUtils.getCleanParam(request, getCaptchaParam());  
    }  
      
//保存异常对象到request  
    @Override  
    protected void setFailureAttribute(ServletRequest request,  
            AuthenticationException ae) {  
        request.setAttribute(getFailureKeyAttribute(), ae);  
    } 
}  
