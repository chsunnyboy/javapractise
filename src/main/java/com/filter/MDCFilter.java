package com.filter;

import java.io.IOException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.MDC;

public class MDCFilter implements Filter {
	
	Random random = new Random();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String traceid = String.valueOf(Math.abs(this.random.nextLong()));
			MDC.put("hps.traceid", traceid);
			chain.doFilter(request, response);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}finally {
			MDC.remove("hps.traceid");
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
