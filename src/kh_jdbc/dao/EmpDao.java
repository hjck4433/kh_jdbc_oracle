package kh_jdbc.dao;

import kh_jdbc.common.Common;
import kh_jdbc.vo.EmpVO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// DAO : Data Access Object, 데이터베이스에 접근해 데이터를 조회하거나 수정 하기 위해 사용 (DML과 유사한 기능)
public class EmpDao {
    Connection conn = null;
    Statement stmt = null; // 쿼리 관련
    PreparedStatement pstmt = null; // 쿼리 관련
    ResultSet rs = null; // 결과 돌려 받기
    Scanner sc = new Scanner(System.in);

    public List<EmpVO> empSelect() {
        List<EmpVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection(); // 연결
            stmt = conn.createStatement();
            String sql = "SELECT * FROM EMP";
            rs = stmt.executeQuery(sql); // executeQuery => Select문을 날릴때 // updateQuery => Select 제외 DML

            while(rs.next()) { // 결과에서 읽은 내용이 있으면 True(테이블의 행의 개수 만큼 순회) // print문 사용 X
                int empNO = rs.getInt("EMPNO");
                String name = rs.getString("ENAME");
                String job = rs.getString("JOB");
                int mgr = rs.getInt("MGR");
                Date date = rs.getDate("HIREDATE");
                BigDecimal sal = rs.getBigDecimal("SAL");
                BigDecimal comm = rs.getBigDecimal("COMM");
                int deptNo = rs.getInt("DEPTNO");
                list.add(new EmpVO(empNO, name, job, mgr, date, sal, comm, deptNo));
            }
            Common.close(rs); // 역순으로 close
            Common.close(stmt);
            Common.close(conn);

        }catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void empInsert() {
        System.out.println("사원 정보를 입력 하세요.");
        System.out.print("사원번호(4자리) : ");
        int no = sc.nextInt();
        System.out.print("이름 : ");
        String name = sc.next();
        System.out.print("직책 : ");
        String job = sc.next();
        System.out.print("상관 사원 번호 : ");
        int mgr = sc.nextInt();
        System.out.print("입사일 : ");
        String date = sc.next();
        System.out.print("급여 : ");
        BigDecimal sal = sc.nextBigDecimal();
        System.out.print("성과급 : ");
        BigDecimal comm = sc.nextBigDecimal();
        System.out.print("부서번호 : ");
        int deptNo = sc.nextInt();

        String sql = "INSERT INTO EMP(EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) VALUES (?,?,?,?,?,?,?,?)";

        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, no);
            pstmt.setString(2, name);
            pstmt.setString(3, job);
            pstmt.setInt(4, mgr);
            pstmt.setString(5, date);
            pstmt.setBigDecimal(6, sal);
            pstmt.setBigDecimal(7, comm);
            pstmt.setInt(8, deptNo);
            int rst = pstmt.executeUpdate(); // INSERT INTO 쿼리 실행
            // 실행 결과가 정수 값으로 반환 됨 (INSERT는 주로 1/ 영향받은 행개수 반환) 0은 실패 (반영된것이 없음)
        }catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
    }

    public void empSelectPrint(List<EmpVO> list) {
        for (EmpVO e : list) {
            System.out.print(e.getEmpNO() + " ");
            System.out.print(e.getName() + " ");
            System.out.print(e.getJob() + " ");
            System.out.print(e.getMgr() + " ");
            System.out.print(e.getDate() + " ");
            System.out.print(e.getSal() + " ");
            System.out.print(e.getComm() + " ");
            System.out.print(e.getDeptNO() + " ");
            System.out.println();
        }
    }


}
