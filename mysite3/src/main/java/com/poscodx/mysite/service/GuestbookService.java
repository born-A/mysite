package com.poscodx.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscodx.mysite.repository.GuestbookRepository;
import com.poscodx.mysite.vo.GuestbookVo;

@Service
public class GuestbookService {
	@Autowired
	private GuestbookRepository guestbookRepository;

	public List<GuestbookVo> getContentsList() {
		List<GuestbookVo> list = guestbookRepository.findAll();
		for (GuestbookVo vo : list) {
		    vo.setContents(vo.getContents().replaceAll(">", "&gt;")
		                                  .replaceAll("<", "&lt;")
		                                  .replaceAll("\n", "<br/>"));
		}
		return list;
	}
	
	public void deleteContents(Long no, String password) {
		guestbookRepository.deleteByNoAndPassword(no, password);
	}
	
	public void addContents(GuestbookVo vo) {
		guestbookRepository.insert(vo);
	}
}