package com.ict.bbs;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import mybatis.dao.BbsDAO;
import mybatis.vo.BbsVO;
import spring.util.FileUploadUtil;

@Controller
public class EditController {
	
	private String imgPath = "/resources/editor_img";
	private String uploadPath = "/resources/upload";
	
	@Autowired
	private BbsDAO bbsDao;
	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private HttpServletRequest request;

	/*@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(String b_idx, String nowPage, String bname)  {
		
		//BbsDAO의 getBbs 메서드를 호출하기 위해 Map 구조 생성
				Map<String, String> map = new Hashtable<String, String>();
				
				map.put("b_idx", b_idx);
				map.put("bname", bname);
				
				BbsVO vo = bbsDao.getBbs(map);
				
				//edit.jsp로 이동할 준비
				ModelAndView mv = new ModelAndView();
								//vo가 있을수도 있고 없을수도 있고 =>
				if(vo != null) { 
					mv.addObject("vo", vo);
					//mv.addObject("nowPage", nowPage);
					mv.setViewName("edit");
					
				}else {
					mv.setViewName("redirect:/list");
				}
				return mv ;   //edit.jsp로 이동
	}*/
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(BbsVO bvo)  {
		//view.jsp에서 전달해 오는 파라미터들이 bvo에 저장된 상태!
		
		String ctx = request.getContentType();
		System.out.println(ctx);   //application....으로 출력
		
		//BbsDAO의 getBbs 메서드를 호출하기 위해 Map 구조 생성
				Map<String, String> map = new Hashtable<String, String>();
				
				map.put("b_idx", bvo.getB_idx());
				map.put("bname", bvo.getBname());
				
				BbsVO vo = bbsDao.getBbs(map);
				
				//edit.jsp로 이동할 준비
				ModelAndView mv = new ModelAndView();
								//vo가 있을수도 있고 없을수도 있고 =>
				if(vo != null) { 
					mv.addObject("vo", vo);
					//mv.addObject("nowPage", nowPage);
					mv.setViewName("edit");
					
				}else {
					mv.setViewName("redirect:/list");
				}
				mv.addObject("nowPage", bvo.getNowPage());
				mv.addObject("bname", bvo.getBname());
				return mv ;   //edit.jsp로 이동
	}
	@RequestMapping("/edit_ok")
	public ModelAndView editOk(BbsVO vo) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		String ctx = request.getContentType();
		System.out.println(ctx);   //multipart....으로 출력
		
		//if(ctx.startsWith("multipart")) {
		
		//edit.jsp에서 넘어온 파라미터들을 vo에 담고, 그 값들을 bbs_t테이블에 업데이트 해야한다.
		
		MultipartFile mf = vo.getFile();
		if(mf.getSize() >0 && mf != null) {
			//절대 경로 얻기
			String path = application.getRealPath(uploadPath);
			String f_name = mf.getOriginalFilename();
			
			f_name = FileUploadUtil.checkSameFileName(f_name, path);
			
			//업로드!
			mf.transferTo(new File(path, f_name));
			//파일명을 vo에 저장해둔다.
			vo.setFile_name(f_name);
		}
		
		//ip vo에 저장!
		vo.setIp(request.getRemoteAddr());
		
		//bbs_t 테이블에 수정!
		boolean chk = bbsDao.edit(vo);
		
		//성공여부를 확인하기 위해 작업이 필요하면 chk를 저장하고 가야한다.
		
		mv.setViewName("redirect:/view?b_idx="+vo.getB_idx()+
				"&nowPage="+vo.getNowPage()+"&bname="+
					vo.getBname()+"&ch="+chk);		
		return mv;
	}

}
