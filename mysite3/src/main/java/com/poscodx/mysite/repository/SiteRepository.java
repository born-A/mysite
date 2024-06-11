package com.poscodx.mysite.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class SiteRepository {
	private SqlSession sqlSession;
	
	public SiteRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
}
