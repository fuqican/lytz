/**
 * 
 */
package com.lytz.finance.dao.impl;

import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import com.lytz.finance.common.query.ShowQuery;
import com.lytz.finance.dao.ShowDAO;
import com.lytz.finance.vo.Show;
import com.lytz.finance.vo.Status;

/**
 * @author cloudlu
 *
 */
@Repository("showDAO")
public class ShowDAOImpl extends BaseDAOImpl<Show, Integer> implements ShowDAO {

    @SuppressWarnings("unchecked")
    public List<Show> findByQuery(ShowQuery query) {
        if(null == query){
            throw new IllegalArgumentException("query should not be null");
        }
        if (StringUtils.isBlank(query.getKeyword())) {
            Criteria search = createCriteria(query);
            if (query.getStartRow() != null) {
                search.setFirstResult(query.getStartRow());
            }
            if (query.getQuerySize() != null) {
                search.setMaxResults(query.getQuerySize());
            }
            return search.list();
        } else {
            FullTextQuery hibernateQuery = findByKeywordQuery(query);
            if (query.getStartRow() != null) {
                hibernateQuery.setFirstResult(query.getStartRow());
            }
            if (query.getQuerySize() != null) {
                hibernateQuery.setMaxResults(query.getQuerySize());
            }
            return hibernateQuery.list();
        }
    }

    private FullTextQuery findByKeywordQuery(ShowQuery query) {
        FullTextSession fullTextSession = Search
                .getFullTextSession(getSession());

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory()
                .buildQueryBuilder().forEntity(Show.class).get();
        org.apache.lucene.search.Query luceneQuery = null;
        if (null == query.getStatus()) {
            luceneQuery = queryBuilder.keyword()// .wildcard()
                    .onFields("title", "content").matching(query.getKeyword())
                    // .matching("*" + query.getKeyword() + "*")
                    .createQuery();
        } else {
            luceneQuery = queryBuilder
                    .bool()
                    .must(queryBuilder.keyword()
                            // .wildcard()
                            .onField("status")
                            .matching(query.getStatus()).createQuery())
                    .must(queryBuilder.keyword()
                            // .wildcard()
                            .onFields("title", "content")
                            .matching(query.getKeyword()).createQuery())
                    .createQuery();
        }
        // BooleanQuery
        FullTextQuery hibernateQuery = fullTextSession.createFullTextQuery(
                luceneQuery, Show.class);
        return hibernateQuery;
    }

    public int getTotalCount(ShowQuery query) {
        if(null == query){
            throw new IllegalArgumentException("query should not be null");
        }
        if (StringUtils.isBlank(query.getKeyword())) {
            Criteria c = createCriteria(query);
            Long count = (Long) c.setProjection(Projections.rowCount())
                    .uniqueResult();
            if(null == count)
                return 0;
            return count.intValue();
        } else {
            FullTextQuery hibernateQuery = findByKeywordQuery(query);
            return hibernateQuery.getResultSize();
        }
    }

    private Criteria createCriteria(ShowQuery query) {
        Criteria search = getSession().createCriteria(Show.class);
        if (query.getTitle() != null) {
            search.add(Restrictions.eq("title", query.getTitle()));
        }
        if (query.getStatus() != null
                && EnumUtils.isValidEnum(Status.class, query.getStatus()
                        .name())) {
            search.add(Restrictions.eq("status", query.getStatus()));
        }
        return search;
    }
}
