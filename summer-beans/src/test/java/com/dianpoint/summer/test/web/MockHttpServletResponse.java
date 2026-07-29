package com.dianpoint.summer.test.web;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

public class MockHttpServletResponse implements HttpServletResponse {

    private final ByteArrayOutputStream content = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(content);
    private String contentType;

    @Override public void addCookie(Cookie cookie) {}
    @Override public boolean containsHeader(String name) { return false; }
    @Override public String encodeURL(String url) { return null; }
    @Override public String encodeRedirectURL(String url) { return null; }
    @Override public void sendError(int sc, String msg) {}
    @Override public void sendError(int sc) {}
    @Override public void sendRedirect(String location) {}
    @Override public void setDateHeader(String name, long date) {}
    @Override public void addDateHeader(String name, long date) {}
    @Override public void setHeader(String name, String value) {}
    @Override public void addHeader(String name, String value) {}
    @Override public void setIntHeader(String name, int value) {}
    @Override public void addIntHeader(String name, int value) {}
    @Override public void setStatus(int sc) {}
    @Override public void setStatus(int sc, String msg) {}
    @Override public int getStatus() { return 0; }
    @Override public String getHeader(String name) { return null; }
    @Override public Collection<String> getHeaders(String name) { return null; }
    @Override public Collection<String> getHeaderNames() { return null; }
    @Override public String getCharacterEncoding() { return null; }
    @Override public String getContentType() { return contentType; }
    @Override public ServletOutputStream getOutputStream() { return null; }
    @Override public PrintWriter getWriter() { return writer; }
    @Override public void setCharacterEncoding(String charset) {}
    @Override public void setContentLength(int len) {}
    @Override public void setContentLengthLong(long len) {}
    @Override public void setContentType(String type) { this.contentType = type; }
    @Override public void setBufferSize(int size) {}
    @Override public int getBufferSize() { return 0; }
    @Override public void flushBuffer() { writer.flush(); }
    @Override public void resetBuffer() {}
    @Override public boolean isCommitted() { return false; }
    @Override public void reset() {}
    @Override public void setLocale(Locale loc) {}
    @Override public Locale getLocale() { return null; }
    @Override public String encodeUrl(String url) { return null; }
    @Override public String encodeRedirectUrl(String url) { return null; }

    public String getContentAsString() {
        writer.flush();
        return content.toString();
    }
}
