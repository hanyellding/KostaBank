package com.my.model;

import java.io.File;
import java.text.SimpleDateFormat;

import com.my.service.BoardService;
import com.oreilly.servlet.multipart.FileRenamePolicy;

public class BoramRenamePolicy implements FileRenamePolicy{
   private String board_id;
   public BoramRenamePolicy(String board_id) {
      this.board_id = board_id;
   }
   @Override
   public File rename(File file) {
      
      //         System.out.println("rename(" + file+")호출");
      //1.파일이름 얻기
      System.out.println(file.getName() + ", " +file.getAbsolutePath());
      //String filePath = file.getAbsolutePath();

      String parent = file.getParent();
      String oldName = file.getName();

      //1-1. 게시글번호용 첨부파일 삭제
      File dir = new File(parent);
      File []files = dir.listFiles();
      for(File f: files) {
         String fileName = f.getName();
         String fileNamePre = fileName.substring(0, fileName.indexOf('_'));
         System.out.println("파일이름 앞:" + fileNamePre);
         if(board_id.equals(fileNamePre)){
            f.delete();
            System.out.println("삭제된 기존파일명: " + fileName);
         }
      }      
      //2.파일이름바꾸기-yyMMddHHmmss
      //2.파일이름바꾸기-글번호_기존파일명
      //2-1) 확장자제외한 파일이름 얻기
      int extIdx = oldName.lastIndexOf(".");
      String name = oldName.substring(0, extIdx);
      

      //2-2) 이름에 yyMMddHHmmss이어붙이기
      //SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
      //String now = sdf.format(new java.util.Date());

      //name+=now;
      
      
      name = board_id + "_" + name; 

      //2-3) 이름과 확장자 이어붙이기
      String newName = name + oldName.substring(extIdx);
      
      
      

      //3.새파일 생성후 반환
      File f = new File(parent,  newName);
      return f;
   }   
   
}