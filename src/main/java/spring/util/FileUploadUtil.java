package spring.util;

import java.io.File;

public class FileUploadUtil {

	public static String checkSameFileName(String fileName, String path) {
		//인자인 fileName에서 확장자를 뺀 파일명 가려내자!
		//그렇게 하려면 먼저 "."의 위치를 알아내야 한다.
		int period = fileName.lastIndexOf(".");
		
		String f_name = fileName.substring(0, period);//파일명
		String suffix = fileName.substring(period);// .txt와 같은 확장자
		
		//전체경로
		String saveFileName = path + 
				System.getProperty("file.separator") + fileName;
		
		//위의 경로를 가지고 java.io.File객체를 생성하자!
		//이유는 File객체의 exists()함수를 통하여 존재여부를 확인할 수
		//있기때문이다. ★중복체크를 간편하게 할 수 있다!!!
		File f = new File(saveFileName);
		
		//같은 이름이 있을 경우 파일명 뒤에 숫자를 붙여줘야 한다.
		//그러면 int형 변수가 필요하다.
		int idx = 1;
		while(f != null && f.exists()) {
			//같은 파일이 있는 경우에만 수행!!
			
			// 파일명 뒤에 숫자를 붙여 파일명을 변경해야 한다.
			StringBuffer sb = new StringBuffer();
			sb.append(f_name);//파일명
			sb.append(idx++);//숫자 붙인 후 1 증가
			sb.append(suffix);// 확장자
			
			fileName = sb.toString();//imsi1.txt
			
			saveFileName = path +
					System.getProperty("file.separator") + fileName;
			f = new File(saveFileName);
		}
		
		return fileName;// 중복되지 않는 파일명을 반환한다.
	}
}
