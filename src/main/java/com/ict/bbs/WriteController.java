package com.ict.bbs;

import java.io.File;
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
public class WriteController {
	
	//STS에서는 resources까지를 인식하고 있다.
	private String imgPath = "/resources/editor_img";
	private String uploadPath = "/resources/upload";
	
	@Autowired
	private BbsDAO bbsDao;
		
	@Autowired
	private ServletContext application;   // jsp 내장 객체 선언
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping("/write")
	public String writeForm() {
		return "write";   //write.jsp로 이동
	}
	
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public ModelAndView write(BbsVO vo) throws Exception{
		
		ModelAndView mv = new ModelAndView();
		MultipartFile mf = vo.getFile();
		
		//첨부파일 확인 후 저장
		if(mf != null && mf.getSize() > 0) {
			String fileName = vo.getFile().getOriginalFilename();
			vo.setFile_name(fileName);
		}
		
		//절대 경로 지정
		String r_path = application.getRealPath(uploadPath);
		
		//실제 파일명 얻기
		vo.setOri_name(mf.getOriginalFilename());
		
		//파일명 중복 처리
		String fname = FileUploadUtil.checkSameFileName(vo.getOri_name(), r_path);
		
		//파일 올리기
		mf.transferTo(new File(r_path, fname));
		
		//변경된 파일명 얻기
		vo.setFile_name(fname);
		
		//bname 
		if(vo.getBname() == null)
			vo.setBname("BBS");
		
		// ip지정
		String ip = request.getRemoteAddr();
		vo.setIp(ip);

		//dao를 통해 bbs_t에 insert 작업
		boolean chk = bbsDao.addData(vo);
		
		mv.addObject("chk", chk);
		
		//ListController 다시 호출
		mv.setViewName("redirect:/list");    //mv.setViewName("redirect:/list?cnt=" + cnt)
		return mv;
	}
		
		@RequestMapping(value="/saveImage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
		@ResponseBody  //이걸 쓰기 위한 작업을 해야함. pom.xml에 디펜던시 추가, servlet-context.xml에 beans 태그 추가
		public Map<String, String> saveImage(BbsVO vo)throws Exception{
			//write.jsp에서 비동기식 통신을 이용하여 이미지를 서버에 저장하기 위해 호출되는 곳!
			//그리고 끝나기 전에 반드시 저장된 이미지 경로를 보내야 한다.
			
			MultipartFile mf = vo.getFile();
			String fileName = null;
			
			Map<String, String> map = new Hashtable<String, String>();
			
			//첨부된 파일이 있는지 검증
			if(mf.getSize() >0 && mf != null) {
				
				//절대경로 지정
				
				String realPath = application.getRealPath(imgPath);
				
				//파일명 얻기
				fileName = mf.getOriginalFilename();
				
				//파일명 중복 체크
				fileName = FileUploadUtil.checkSameFileName(fileName, realPath);
				
				//파일 올리기
				mf.transferTo(new File(realPath, fileName));
				
				//write.jsp 로 반환할 값(url)
				map.put("url", request.getContextPath()+imgPath+"/"+ fileName);
	}
			
			return map;
		}

}
