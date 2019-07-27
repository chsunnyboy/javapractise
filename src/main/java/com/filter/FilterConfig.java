package com.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {

    @Bean 
    FilterRegistrationBean mDCFilterRegistration(){
    	FilterRegistrationBean reg = new FilterRegistrationBean();
    	MDCFilter MDCFilter = new MDCFilter();
        reg.setFilter(MDCFilter);
        reg.setOrder(-1);
        return reg;
    }
    @Bean 
    FilterRegistrationBean monitorAssistFilterRegistration(){
    	FilterRegistrationBean reg = new FilterRegistrationBean();
    	MonitorAssistFilter monitorAssistFilter = new MonitorAssistFilter();
        reg.setFilter(monitorAssistFilter);
        reg.setOrder(Integer.MAX_VALUE);
        return reg;
    }
}
