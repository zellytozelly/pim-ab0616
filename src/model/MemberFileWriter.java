package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemberFileWriter {
	BufferedWriter bw = null;
	FileWriter fw = null;
	public MemberFileWriter(File f) throws IOException {
		fw = new FileWriter(f);
	}
	public void saveMember(ArrayList<Member> memberList) {
		for(Member m : memberList) {
			try {
				fw.write(m.getEmail() + "\t");	// 필수
				fw.write(m.getPw() + "\t");		// 필수
				fw.write(m.getName() + "\t");	// 필수
				fw.write(m.getBirth() + "\t"); 	// 필수
				fw.write(AgeKorean(m.getBirth()) + "\t");			// 자동 계산, 수정이 필요함
				fw.write(m.getAddress() + "\t");
				fw.write(m.getContact() + "\n");				
				fw.flush();
			} catch (IOException e) {
			}			
		}
	}
	
	public int AgeKorean(String ssn) {
        String today = ""; // 오늘 날짜
        int manAge = 0; // 만 나이
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        today = formatter.format(new Date()); // 시스템 날짜를 가져와서 yyyyMMdd 형태로 변환
        // today yyyyMMdd
        int todayYear = Integer.parseInt(today.substring(0, 4));
        int todayMonth = Integer.parseInt(today.substring(4, 6));
        int todayDay = Integer.parseInt(today.substring(6, 8));
        int ssnYear = Integer.parseInt(ssn.substring(0, 4));
        int ssnMonth = Integer.parseInt(ssn.substring(4, 6));
        int ssnDay = Integer.parseInt(ssn.substring(6, 8));
        manAge = todayYear - ssnYear;
        if (todayMonth < ssnMonth) { // 생년월일 "월"이 지났는지 체크
            manAge--;
        } else if (todayMonth == ssnMonth) { // 생년월일 "일"이 지났는지 체크
            if (todayDay < ssnDay) {
                manAge--; // 생일 안지났으면 (만나이 - 1)
            }
        }
        return manAge + 1; // 한국나이를 측정하기 위해서 +1살 (+1을 하지 않으면 외국나이 적용됨)
    }
	
	
}
