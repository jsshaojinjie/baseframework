package com.baseframework.config.security;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONUtil;
import com.baseframework.pojo.common.CommonConstants;
import com.baseframework.pojo.common.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        //设置获取token的url
        super.setFilterProcessesUrl("/oauth/token");
    }

    // 接收并解析用户凭证,登录页传过来的用户名密码数据
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password,
                        new ArrayList<>())
        );
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = Jwts.builder()
                .setSubject(((CurrentUser) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                .signWith(SignatureAlgorithm.HS512, "MyJwtSecret")
                .compact();
        res.setCharacterEncoding(CharsetUtil.UTF_8);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        JSONObject object = new JSONObject();
        object.put("token", token);
        R r = R.builder().data(object).build();
        PrintWriter out;
        try {
            out = res.getWriter();
            out.print(JSONUtil.toJsonStr(r));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {
        res.setCharacterEncoding(CharsetUtil.UTF_8);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(JSONUtil.toJsonStr(R.builder().code(CommonConstants.FAIL).msg(failed.getMessage()).build()));
    }
}