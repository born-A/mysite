package com.poscodx.mysite.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	private SqlSession sqlSession;

	public BoardRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public int insert(BoardVo vo) {
		return sqlSession.insert("board.insert", vo);
	}

	public int updateGroupNo(int no) {
		return sqlSession.update("board.updateGroupNo", no);
	}

	public List<BoardVo> getList(int pageNum, int amount) {
		int newAmount = (pageNum - 1) * amount;
		List<BoardVo> list = sqlSession.selectList("board.getList", Map.of("amount", amount, "newAmount", newAmount));
		int totalPosts = getTotal();
		int index = totalPosts - ((pageNum - 1) * amount);

		for (int i = 0; i < list.size(); i++) {
			list.get(i).setNumbering(index--);
		}

		return list;
	}

	public int updateOrderNo(int groupNo, int orderNo) {
		return sqlSession.update("board.updateOrderNo", Map.of("groupNo", groupNo, "orderNo", orderNo));
	}

	public int insertReplyBoard(BoardVo vo) {
		vo.setGroupNo(vo.getGroupNo());
		vo.setOrderNo(vo.getOrderNo() + 1);
		vo.setDepth(vo.getDepth() + 1);
		return sqlSession.insert("board.insertReplyBoard", vo);
	}

	public int deleteByNoAndUserNo(Long no, Long userNo) {
		return sqlSession.delete("board.deleteByNoAndUserNo", Map.of("no", no, "userNo", userNo));
	}

	public int modify(BoardVo vo) {
		return sqlSession.update("board.modify", vo);
	}

	public BoardVo findByNo(Long no) {
		return sqlSession.selectOne("board.findByNo", no);
	}

	public List<BoardVo> getSearch(String kwd, int pageNum, int amount) {
		List<BoardVo> result = new ArrayList<>();
		int totalPosts = getTotalSearchResults(kwd);

		String searchPattern = "%" + kwd + "%";
		int limit = amount;
		int offset = (pageNum - 1) * amount;
		result = sqlSession.selectList("board.getSearch",
				Map.of("kwd", searchPattern, "limit", limit, "offset", offset));

		int index = totalPosts - ((pageNum - 1) * amount);

		for (int i = 0; i < result.size(); i++) {
			result.get(i).setNumbering(index--);
		}

		return result;
	}

	public int getTotalSearchResults(String searchText) {
		String searchPattern = "%" + searchText + "%";
		return sqlSession.selectOne("board.getTotalSearchResults", searchPattern);
	}

	public int getTotal() {
		return sqlSession.selectOne("board.getTotal");
	}

	public int increaseHit(Long no) {
		return sqlSession.update("board.increaseHit", no);
	}

}
