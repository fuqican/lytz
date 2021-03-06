/**
 * 
 */
package com.lytz.finance.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lytz.finance.dao.RoleDAO;
import com.lytz.finance.vo.Role;

/**
 * @author cloudlu
 *
 */
@Repository("roleDAO")
public class RoleDAOImpl extends BaseDAOImpl<Role, Integer> implements
		RoleDAO {

	/* (non-Javadoc)
	 * @see com.elulian.CustomerSecurityManagementSystem.dao.IRoleDAO#getRoleByName(java.lang.String)
	 */
	public Role getRoleByName(String name) {
        @SuppressWarnings("unchecked")
		List<Role> list = getSession().getNamedQuery("findRoleByName").setString("name", name).list();
		
		if(list.isEmpty())
			return null;
		return list.get(0);
		
	}
}
