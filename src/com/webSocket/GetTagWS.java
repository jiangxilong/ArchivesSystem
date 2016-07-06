package com.webSocket;

import com.Constants;
import com.bean.UserBean;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by drc on 16-7-6.
 */
@ServerEndpoint(value = "/webSocket/getTag", configurator = ServletAwareConfig.class)
public class GetTagWS implements WebSocket {
    private UserBean user;
    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig cfg){
        this.session = session;
        HttpSession httpSession = (HttpSession) cfg.getUserProperties().get("httpSession");
        int userID = (int) httpSession.getAttribute(Constants.getUserID());
        System.out.println(userID);
        user = UserBean.getUserByID(userID);
        user.setWebSocket(this);
    }

    @OnClose
    public void onClose(Session session){
        if(user != null)user.setWebSocket(null);
    }

    @Override
    public void send(String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}