package com.poscodx.mysite.repository;

import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscodx.mysite.vo.UserVo;

@Repository
public class UserRepository {
	@Autowired
	private SqlSession sqlSession;

	public int insert(UserVo vo) {
		return sqlSession.insert("user.insert", vo);
	}

	public UserVo findByEmailAndPassword(String email, String password) {
		return sqlSession.selectOne("user.findByEmailAndPassword", Map.of("email", email, "password", password));
	}

	public UserVo findByNo(Long no) {
		return sqlSession.selectOne("user.findByNo", no);
	}

	public int update(UserVo vo) {
		return sqlSession.update("user.update", vo);
	}
	
	public <R> R findByEmail(String email, Class<R> resultType) {
		FindByEmailResultHandler<R> findByEmailResultHandler = new FindByEmailResultHandler<>(resultType);
		sqlSession.select("user.findByEmail", email, findByEmailResultHandler);

		return findByEmailResultHandler.result;
	}

	private class FindByEmailResultHandler<R> implements ResultHandler<Map<String, Object>> {
		private R result;
		private Class<R> resultType;

		FindByEmailResultHandler(Class<R> resultType) {
			this.resultType = resultType;
		}

		@Override
		public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
			Map<String, Object> resultMap = resultContext.getResultObject();
			result = new ObjectMapper().convertValue(resultMap, resultType);
		}
	}
}