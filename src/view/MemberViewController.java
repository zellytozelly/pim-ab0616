package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import controller.MemberService;
import controller.MemberServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Member;

public class MemberViewController implements Initializable {
	@FXML	private Button btnCreate;
	@FXML	private Button btnUpdate;
	@FXML	private Button btnDelete;
	
	@FXML	private TextArea taFindResult;
	@FXML	private Button btnFindByName;
	@FXML	private Button btnFindByAddress;
	@FXML	private TextField tfFindCondition;
	
	//age는 자동생성이라 없다.
	@FXML	private TextField tfEmail;
	@FXML	private PasswordField tfPw;
	@FXML	private TextField tfName;
	@FXML	private TextField tfBirth;
	@FXML	private TextField tfAddress;
	@FXML	private TextField tfContact;
	
	@FXML 	private TableView<Member> tableViewMember;
	@FXML	private TableColumn<Member, String> columnEmail;
	@FXML	private TableColumn<Member, String> columnName;	
	@FXML	private TableColumn<Member, String> columnPw;
	@FXML	private TableColumn<Member, String> columnBirth;
	@FXML	private TableColumn<Member, String> columnAge;
	@FXML	private TableColumn<Member, String> columnAddress;
	@FXML	private TableColumn<Member, String> columnContact;
	
	// Member : model이라고도 하고 DTO, VO 라고도 함
	// 시스템 밖에 저장된 정보를 객체들간에 사용하는 정보로 변환한 자료구조 또는 객체
	private final ObservableList<Member> data = FXCollections.observableArrayList();
	
	// 목록 : 이중연결리스트는 아니지만 리스트의 특징과 배열 특징을 잘 혼용해 놓은 클래스 ArrayList 
	ArrayList<Member> memberList;
	MemberService memberService;
	
	public MemberViewController() {
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		memberService = new MemberServiceImpl();
		// 람다식 : java 8  함수형 언어 지원 
		
		columnEmail.setCellValueFactory(cvf -> cvf.getValue().emailProperty());
		columnName.setCellValueFactory(cvf -> cvf.getValue().nameProperty());
		//columnPW.setCellValueFactory(cvf -> cvf.getValue().pwProperty());
		columnBirth.setCellValueFactory(cvf -> cvf.getValue().birthProperty());
		columnAge.setCellValueFactory(cvf -> cvf.getValue().ageProperty());
		columnAddress.setCellValueFactory(cvf -> cvf.getValue().addressProperty());
		columnContact.setCellValueFactory(cvf -> cvf.getValue().contactProperty());
		
		tableViewMember.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showMemberInfo(newValue));

		btnCreate.setOnMouseClicked(event -> handleCreate());		// 이밴트 연결은 1.initialize 2. fxml 둘 다 가능하다.
		//btnDelete.setOnMouseClicked(e -> handleDelete());		
		
		btnFindByAddress.setOnMouseClicked(event -> handleFindByAddress());	//주소버튼
		btnFindByName.setOnMouseClicked(event -> handleFindByName());		//이름버튼
		
		loadMemberTableView();
	}
	String str = ""; // 인스턴스 변수 - 객체 변수, 객체가 존재하는 동안 메모리에 존재
	
	/*
	@FXML 
	private void handleExecute() { // event source, listener, handler
		str = str + tfExecute.getText() + "\n";
		//str = ts.setTextArea(tfExecute.getText());
		
		//str = taExecute.getText();
		//String name = tfExecute.getText();
		//str = str + ts.appendTextArea(name);
		
		taExecute.setText(str);
	}
	*/
	
	private void showMemberInfo(Member member) {
		if (member != null) {
			tfEmail.setText(member.getEmail());
			tfPw.setText(member.getPw());
			tfName.setText(member.getName());
			tfBirth.setText(member.getBirth());
			tfAddress.setText(member.getAddress());
			tfContact.setText(member.getContact());
//			tfMobilePhone.setText(member.getMobilePhone());
		}
		 else {
			 //tfID.setText("");
			 //tfPW.setText("");
		     //tfName.setText("");
//		     tfMobilePhone.setText("010");
		 }
	}
	
	private void loadMemberTableView() {
		memberList = memberService.readList();
		for(Member m : memberList) {
			data.add(m);
		}
		tableViewMember.setItems(data);
	}
	
	private boolean checkValidForm() {	//무결성검사
		if(tfEmail.getText().length() > 0 && tfPw.getText().length() > 0 && tfName.getText().length() > 0 && tfBirth.getText().length() > 0)
			return true;
		if(tfEmail.getText().length() < 0 || !tfEmail.getText().contains("@")) {
			this.showAlert("Email를 확인하십시요");
			return false;
		}
		return false;
	}
	
	
	@FXML 
	private void handleFindByAddress() {	//주소검색
		String condition = tfFindCondition.getText();
		taFindResult.setText("");
		if(condition.length() > 0) {		//length길이의 유무
			List<Member> searched = memberService.findByAddress(condition);
			if(searched.size() > 0) {		//검색된 결과의 유무
				int no = 1;
				for(Member m : searched) {	//출력 부분
					taFindResult.appendText(no++ + " ) " + m.getAddress() + " : " + m.getEmail() + " : " + m.getName() + " \n");
				}
			}
			else
				taFindResult.setText("검색 조건에 맞는 정보가 없습니다.");
		}
		else
			this.showAlert("검색 조건을 입력하십시요");			
	}
	
	
	@FXML 
	private void handleFindByName() {	//이름검색
		String condition = tfFindCondition.getText();
		taFindResult.setText("");
		if(condition.length() > 0) {		//length길이의 유무
			List<Member> searched = memberService.findByName(condition);
			if(searched.size() > 0) {		//검색된 결과의 유무
				int no = 1;
				for(Member m : searched) {	//출력 부분
					taFindResult.appendText(no++ + " ) " + m.getAddress() + " : " + m.getEmail() + " : " + m.getName() + " \n");
				}
			}
			else
				taFindResult.setText("검색 조건에 맞는 정보가 없습니다.");
		}
		else
			this.showAlert("검색 조건을 입력하십시요");			
	}
	
	
	@FXML 
	private void handleCreate() { // event source, listener, handler
		//이메일중복
		if(tfEmail.getText().length() > 0) {
			if(checkValidForm()) {			
				Member newMember = 
						new Member(tfEmail.getText(), tfPw.getText(), tfName.getText(), 
								tfBirth.getText(), "20", tfAddress.getText(), tfContact.getText()); // 7개 필드임
				if( memberService.findByUid(newMember) < 0) {
					data.add(newMember);			
					tableViewMember.setItems(data);
					memberService.create(newMember);
				}else {
					showAlert("아이디 중복으로 등록할 수 없습니다.");
				}			
			} else
				showAlert("필수항목 완벽한 입력 ");
		}
		/*
			//이메일중복
			if(tfEmail.getText().length() > 0) {
				Member newMember = 
						new Member(tfEmail.getText(), tfPw.getText(), tfName.getText(), 
								tfBirth.getText(), "20", tfAddress.getText(), tfContact.getText()); // 7개 필드임
				if( memberService.findByUid(newMember) < 0) {
					data.add(newMember);			
					tableViewMember.setItems(data);
					memberService.create(newMember);
				}
				else {
					showAlert("아이디 중복으로 등록할 수 없습니다.");
				}			
			} else
				showAlert("ID 입력오류");		
		*/
	}
	@FXML 
	private void handleUpdate() {
		Member newMember = new Member(tfEmail.getText(), tfPw.getText(), tfName.getText(), 
				tfBirth.getText(), "20", tfAddress.getText(), tfContact.getText());

		int selectedIndex = tableViewMember.getSelectionModel().getSelectedIndex();
		// uid를 변경하고 수정 -> 생성으로 처리하게 된다.
		// uid로 조회하는데 uid가 수정이되면 실제로 수정이 불가능함, findByUid() 가 -1 반환
		
		 if (selectedIndex != memberService.findByUid(newMember)) {
			showAlert("아이디를 수정하면 업데이트 할 수 없습니다.");    
		}
		
		if (selectedIndex >= 0) {
			tableViewMember.getItems().set(selectedIndex, newMember);
			memberService.update(newMember);			
		} else {
			showAlert("업데이트할 수 없습니다.");          
        }
	}
	
	@FXML 
	private void handleDelete() {
		int selectedIndex = tableViewMember.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			memberService.delete(tableViewMember.getItems().remove(selectedIndex));			
		} else {
			showAlert("삭제 대상이 없습니다.");
        }
	}
	
	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(mainApp.getRootStage());
        alert.setTitle("알림");
        alert.setContentText("확인 : " + message);            
        alert.showAndWait();
	}

	private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}
