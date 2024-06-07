package com.poscodx.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.poscodx.mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			String url = "jdbc:mariadb://192.168.64.4:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} 
		
		return conn;
	}
	
	public String insert(BoardVo vo) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			
			PreparedStatement pstmt1 = conn.prepareStatement("insert into board values(null, ?, ?, 0, now(), 0, 1, 0, ?)");
			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
			PreparedStatement pstmt3 = conn.prepareStatement("update board set g_no = ? where no = ?");
		) {
			
			pstmt1.setString(1, vo.getTitle());
			pstmt1.setString(2, vo.getContents());
			pstmt1.setLong(3, vo.getUserNo());
			result = pstmt1.executeUpdate();

			int lastInsertId = 0;
			ResultSet rs = pstmt2.executeQuery();
			if (rs.next()) {
			    lastInsertId = rs.getInt(1);
			}

			pstmt3.setInt(1,lastInsertId);
			pstmt3.setLong(2,lastInsertId);
			result = pstmt3.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}
			
		if(result == 1) {
			return "success";
		} else {
			return "failed";
		}
	}
	
	public int updateGroupNo(Long no) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("update board set g_no = ? where no = ?");
		) {
			pstmt.setLong(1,no);
			pstmt.setLong(2,no);
			pstmt.executeQuery();
			
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}
		
		return result;		
	}
	
	public List<BoardVo> findAll() {
		List<BoardVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
				"SELECT \n"
				+ "    b.no, \n"
				+ "    b.title, \n"
				+ "    b.contents, \n"
				+ "    b.hit, \n"
				+ "    DATE_FORMAT(b.reg_date, '%Y/%m/%d') AS reg_date, \n"
				+ "    b.depth, \n"
				+ "    u.name \n"
				+ "FROM \n"
				+ "    board b \n"
				+ "JOIN \n"
				+ "    user u \n"
				+ "ON \n"
				+ "    b.user_no = u.no \n"
				+ "ORDER BY \n"
				+ "    b.g_no DESC, \n"
				+ "    b.o_no ASC, \n"
				+ "    b.reg_date DESC;\n"
				+ "");
					
			ResultSet rs = pstmt.executeQuery();
		) {
			
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				int hit = rs.getInt(4);
				String regDate = rs.getString(5);
				int depth = rs.getInt(6);
				String userName = rs.getString(7);
				
			
			
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				vo.setUserName(userName);
				
				result.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}	
		
		return result;
	}
	
	public List<BoardVo> getList(int pageNum, int amount) {
	    List<BoardVo> result = new ArrayList<>();
	    int totalPosts = 0;

	    try (Connection conn = getConnection()) {
	        String countQuery = "SELECT COUNT(*) FROM board";
	        try (PreparedStatement countPstmt = conn.prepareStatement(countQuery);
	             ResultSet rs = countPstmt.executeQuery()) {
	            if (rs.next()) {
	                totalPosts = rs.getInt(1);
	            }
	        }

	        String listQuery = "SELECT \n"
	                + "    b.no, \n"
	                + "    b.title, \n"
	                + "    b.contents, \n"
	                + "    b.hit, \n"
	                + "    DATE_FORMAT(b.reg_date, '%Y/%m/%d') AS reg_date, \n"
	                + "    b.depth, \n"
	                + "    u.name \n"
	                + "FROM \n"
	                + "    board b \n"
	                + "JOIN \n"
	                + "    user u \n"
	                + "ON \n"
	                + "    b.user_no = u.no \n"
	                + "ORDER BY \n"
	                + "    b.g_no DESC, \n"
	                + "    b.o_no ASC, \n"
	                + "    b.reg_date DESC \n"
	                + "LIMIT ? OFFSET ?;";

	        try (PreparedStatement listPstmt = conn.prepareStatement(listQuery)) {
	            listPstmt.setInt(1, amount);
	            listPstmt.setInt(2, (pageNum - 1) * amount);

	            try (ResultSet rs = listPstmt.executeQuery()) {
	                int index = totalPosts - ((pageNum - 1) * amount);
	                while (rs.next()) {
	                    Long no = rs.getLong(1);
	                    String title = rs.getString(2);
	                    String contents = rs.getString(3);
	                    int hit = rs.getInt(4);
	                    String regDate = rs.getString(5);
	                    int depth = rs.getInt(6);
	                    String userName = rs.getString(7);

	                    BoardVo vo = new BoardVo();
	                    vo.setNo(no);
	                    vo.setTitle(title);
	                    vo.setContents(contents);
	                    vo.setHit(hit);
	                    vo.setRegDate(regDate);
	                    vo.setDepth(depth);
	                    vo.setUserName(userName);

	                    // 넘버링을 추가
	                    vo.setNumbering(index--);

	                    result.add(vo);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return result;
	}
	
	
	public void updateOrderNo(int groupNo, int orderNo) {
		try (
				Connection conn = getConnection();
				
				PreparedStatement pstmt = conn.prepareStatement("update board set o_no = o_no + 1 where g_no = ? and o_no > ?");
			) {
				pstmt.setInt(1, groupNo);
				pstmt.setInt(2, orderNo);
				
				pstmt.executeQuery();
				
			} catch (SQLException e) {
				System.out.println("Error:" + e);
			}	
	}

	public void insertReplyBoard(BoardVo vo, int groupNo, int orderNo, int depth) {
		try (
			Connection conn = getConnection();
			
			PreparedStatement pstmt1 = conn.prepareStatement("insert into board values(null, ?, ?, 0, current_date(), ?, ?, ?, ?)");
			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			
			pstmt1.setString(1, vo.getTitle());
			pstmt1.setString(2, vo.getContents());
			pstmt1.setInt(3, groupNo);
			pstmt1.setInt(4, orderNo + 1);
			pstmt1.setInt(5, depth + 1);
			pstmt1.setLong(6, vo.getUserNo());
			
			pstmt1.executeQuery();
			pstmt2.executeQuery();
			
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}	
	}

	public String deleteByNoAndUserNo(Long no, Long userNo) {
		int result = 0;
		try (
			Connection conn = getConnection();	
			PreparedStatement pstmt = conn.prepareStatement("delete from board where no = ? and user_no = ?");
		){
			pstmt.setLong(1, no);
			pstmt.setLong(2, userNo);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}
		
		if(result == 1) {
			return "success";
		} else {
			return "failed";
		}
	}

	public String modify(BoardVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("update board set title=?, contents=? where no=? and user_no=?");
		) {
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());
			pstmt.setLong(4, vo.getUserNo());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}
		
		
		if(result == 1) {
			return "success";
		} else {
			return "failed";
		}			
	}

	public BoardVo findByNo(Long no) {
		BoardVo vo = new BoardVo();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select title, contents, g_no, o_no, depth, user_no from board where no = ?");
		) {	
			pstmt.setLong(1, no);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String title = rs.getString(1);
				String contents = rs.getString(2);
				int groupNo = rs.getInt(3); 
				int orderNo = rs.getInt(4); 
				int depth = rs.getInt(5); 
				Long userNo = rs.getLong(6);

				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);
			}
			
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}	
		
		return vo;
	}
	
	public List<BoardVo> getSearch(String searchText, int pageNum, int amount) {
		List<BoardVo> result = new ArrayList<>();
	    int totalPosts = getTotalSearchResults(searchText);
	
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
				"SELECT \n"
				+ "    b.no, \n"
				+ "    b.title, \n"
				+ "    b.contents, \n"
				+ "    b.hit, \n"
				+ "    DATE_FORMAT(b.reg_date, '%Y/%m/%d') AS reg_date, \n"
				+ "    b.depth, \n"
				+ "    u.name \n"
				+ "FROM \n"
				+ "    board b \n"
				+ "JOIN \n"
				+ "    user u \n"
				+ "ON \n"
				+ "    b.user_no = u.no \n"
				+ "where b.title LIKE ? \n"
				+ "ORDER BY \n"
				+ "    b.g_no DESC, \n"
				+ "    b.o_no ASC, \n"
				+ "    b.reg_date DESC \n"
                + "LIMIT ? OFFSET ?");
					
			
		) {
			String searchPattern = "%" + searchText + "%";
	        pstmt.setString(1, searchPattern);
	        pstmt.setInt(2, amount);
	        pstmt.setInt(3, (pageNum - 1) * amount);
	        
			try (ResultSet rs = pstmt.executeQuery()) {
                int index = totalPosts - ((pageNum - 1) * amount);
                while (rs.next()) {
                    Long no = rs.getLong(1);
                    String title = rs.getString(2);
                    String contents = rs.getString(3);
                    int hit = rs.getInt(4);
                    String regDate = rs.getString(5);
                    int depth = rs.getInt(6);
                    String userName = rs.getString(7);

                    BoardVo vo = new BoardVo();
                    vo.setNo(no);
                    vo.setTitle(title);
                    vo.setContents(contents);
                    vo.setHit(hit);
                    vo.setRegDate(regDate);
                    vo.setDepth(depth);
                    vo.setUserName(userName);

                    // 넘버링을 추가
                    vo.setNumbering(index--);

                    result.add(vo);
                }
            }
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}
		return result;
	}
	
	public int getTotalSearchResults(String searchText) {
	    int total = 0;
	    try (
	        Connection conn = getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(
	            "SELECT COUNT(*) \n"
	            + "FROM board \n"
	            + "WHERE title LIKE ?");
	    ) {
	        String searchPattern = "%" + searchText + "%";
	        pstmt.setString(1, searchPattern);

	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            total = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        System.out.println("Error:" + e);
	    }
	    return total;
	}
	
	public int getTotal() {
		int result = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select count(*) as total from board");
				ResultSet rs = pstmt.executeQuery();
		){
			if(rs.next()) {
				result = rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	
		return result;
	}
	
	public int increaseHit(Long no) {
		int result = 0;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("update board set hit = hit + 1 where no = ?");
		) {
			pstmt.setLong(1, no);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}

}
