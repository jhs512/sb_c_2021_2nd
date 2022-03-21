package com.sbs.exam.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sbs.exam.demo.vo.Rq;

@Component
public class NeedLogoutInterceptor implements HandlerInterceptor {
	private Rq rq;
	
	public NeedLogoutInterceptor(Rq rq) {
		this.rq = rq;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		if (rq.isLogined()) {
			if ( rq.isAjax() ) {
				resp.setContentType("application/json; charset=UTF-8");
				resp.getWriter().append("{\"resultCode\":\"F-B\",\"msg\":\"로그아웃 후 이용해주세요.\"}");
			}
			else {
				rq.printHistoryBackJs("로그아웃 후 이용해주세요.");
			}
			
			return false;
		}

		return HandlerInterceptor.super.preHandle(req, resp, handler);
	}
}
