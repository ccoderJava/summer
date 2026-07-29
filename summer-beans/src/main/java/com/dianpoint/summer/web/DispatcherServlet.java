package com.dianpoint.summer.web;

import com.dianpoint.summer.context.ClassPathXmlApplicationContext;
import com.dianpoint.summer.web.servlet.HandlerAdapter;
import com.dianpoint.summer.web.servlet.HandlerMethod;
import com.dianpoint.summer.web.servlet.HandlerMapping;
import com.dianpoint.summer.web.servlet.RequestMappingHandlerAdapter;
import com.dianpoint.summer.web.servlet.RequestMappingHandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextConfigLocation);

        handlerMappings = new ArrayList<>();
        handlerMappings.add(new RequestMappingHandlerMapping(context.getBeanFactory()));

        handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new RequestMappingHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handler = getHandler(request);
        if (handler == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        HandlerAdapter adapter = getHandlerAdapter(handler);
        if (adapter == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        adapter.handle(request, response, handler);
    }

    private HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        for (HandlerMapping mapping : handlerMappings) {
            HandlerMethod handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        return null;
    }
}
