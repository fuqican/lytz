/**
 * 
 */
package com.lytz.finance.service.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lytz.finance.common.TopicQuery;
import com.lytz.finance.dao.TopicDAO;
import com.lytz.finance.dao.UserDAO;
import com.lytz.finance.service.TopicService;
import com.lytz.finance.vo.RoleNameEnum;
import com.lytz.finance.vo.Topic;
import com.lytz.finance.vo.TopicStatus;

/**
 * @author cloudlu
 *
 */
@Service("topicService")
public class TopicServiceImpl extends BaseServiceImpl<Topic, Integer> implements TopicService{

    private TopicDAO topicDAO;

    @Autowired
    @Qualifier("topicDAO")
    public void setTopicDAO(TopicDAO topicDAO) {
        this.dao = topicDAO;
        this.topicDAO = topicDAO;
    }
    
    private UserDAO userDAO;

    @Autowired
    @Qualifier("userDAO")
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    public List<Topic> findByQuery(TopicQuery query) {
        Subject currentUser = SecurityUtils.getSubject();
        if(!currentUser.hasRole(RoleNameEnum.ROLE_ADMIN.name())){
            query.setUsername((String)currentUser.getPrincipal());
        }
        return topicDAO.findByQuery(query);
    }

    public int getTotalCount(TopicQuery query) {
        Subject currentUser = SecurityUtils.getSubject();
        if(!currentUser.hasRole(RoleNameEnum.ROLE_ADMIN.name())){
            query.setUsername((String)currentUser.getPrincipal());
        }
        return topicDAO.getTotalCount(query);
    }

    @Override
    public Topic save(Topic topic){
        if(null == topic){
            throw new IllegalArgumentException("topic should not be null");
        }
        Subject currentUser = SecurityUtils.getSubject();
        if(null != topic.getId()){
            //due to create/update time is not passed back from page
            Topic oldTopic = findById(topic.getId());
            if(null == oldTopic){
                new IllegalArgumentException("topic id invalid");
            }
            if(currentUser.hasRole(RoleNameEnum.ROLE_ADMIN.name()) ||
                    ((String)currentUser.getPrincipal()).equals(oldTopic.getOwner().getUsername())){
                return super.save(topic);
            } else {
                throw new IllegalArgumentException("Invalid user");
            }
        } else {
            topic.setOwner(userDAO.getUserByName((String)currentUser.getPrincipal()));
            return super.save(topic);
        }
    }
    
    @Override
    public void remove(Topic topic) {
        Subject currentUser = SecurityUtils.getSubject();
        if(!currentUser.hasRole(RoleNameEnum.ROLE_ADMIN.name())){
            if(((String)currentUser.getPrincipal()).equals(topic.getOwner().getUsername())){
                topic.setStatus(TopicStatus.CANCELLED);
                super.save(topic);
            } else {
                return;
            }
        } else {
            super.remove(topic);
        }
    }
    
    @Override
    public void remove(Integer id) {
        Topic topic = super.findById(id);
        if(null == topic){
            return;
        }
        remove(topic);
    }
}
