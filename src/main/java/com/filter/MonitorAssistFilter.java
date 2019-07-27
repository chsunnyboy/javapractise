package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;

public class MonitorAssistFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String logFileTraceId = MDC.get("hps.traceid");
			HttpServletResponse httpResponse = (HttpServletResponse)response;
			httpResponse.setHeader("fileLogTraceid", logFileTraceId);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
