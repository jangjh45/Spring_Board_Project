package com.practice.app1.controller;

import com.practice.app1.domain.CommentDto;
import com.practice.app1.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
//@ResponseBody // 밑에 4개 메서드에 붙어있는 클래스 위헤 @ResponseBody 써주면 4개가 다 써지는것
//@RestController // 위에 2개 @Controller, @ResponseBody 쓰는거랑 같음
public class CommentController {
    @Autowired
    CommentService commentService;

    // 지정된 게시물의 댓글을 수정하는 메서드
    @PatchMapping("/comments/{cno}") // /app1/comments/1 PATCH
    @ResponseBody
    // CommentDto 입력한 내용을 가지고 와야한다. @RequestBody 는 JSON객체를 JAVA객체로 변환해서 dto에 줌
    // 쿼리 스트링(comments?bno=1029)이 아니고 REST방식(/comments/1)으로 가지고오면
    // "/comments/{cno}" 이렇게 쓰고 @PathVariable 를 추가해주어야 한다.
    public ResponseEntity<String> modify(@PathVariable Integer cno, @RequestBody CommentDto dto) {
//        String commenter = (String) session.getAttribute("id");
        dto.setCno(cno);
        System.out.println("cno = " + cno);

        try {
            if(commentService.modify(dto)!=1)
                throw new Exception("Write failed.");

            return new ResponseEntity<>("MODIFY_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("MODIFY_ERROR", HttpStatus.BAD_REQUEST);
        }
    }

    // 지정된 게시물의 댓글을 등록하는 메서드
    @PostMapping("/comments") // /app1/comments?bno=1092 POST (게시물등록과 비슷)
    @ResponseBody
    // CommentDto 입력한 내용을 가지고 와야한다. @RequestBody 는 JSON객체를 JAVA객체로 변환해서 dto에 줌
    public ResponseEntity<String> write(@RequestBody CommentDto dto, Integer bno, HttpSession session) {
        // comenter 는 세션에서 가지고와야한다. 다른사람이 아닌 댓글 작성자가 삭제해야 하니까
//        String commenter = (String) session.getAttribute("id");
        String commenter = "asdfasdf"; // 하드코딩
        dto.setCommenter(commenter);
        dto.setBno(bno);
        System.out.println("dto = " + dto);

        try {
            if(commentService.write(dto)!=1)
                throw new Exception("Write failed.");

            return new ResponseEntity<>("WRITE_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("WRITE_ERROR", HttpStatus.BAD_REQUEST);
        }
    }


    // 지정된 게시물의 댓글을 삭제하는 메서드
    @DeleteMapping("/comments/{cno}") // /comments/1?bno=1029 <-- 삭제할 댓글 번호
    @ResponseBody // @ResponseBody 안 붙이면 뷰 이름으로 해석이됨
    // 쿼리 스트링(comments?bno=1029)이 아니고 REST방식(/comments/1)으로 가지고오면
    // "/comments/{cno}" 이렇게 쓰고 @PathVariable 를 추가해주어야 한다.
    public ResponseEntity<String> remove(@PathVariable Integer cno, Integer bno, HttpSession session) {
        // comenter 는 세션에서 가지고와야한다. 다른사람이 아닌 댓글 작성자가 삭제해야 하니까
//        String commenter = (String) session.getAttribute("id");
        String commenter = "asdfasdf"; // 하드코딩
        try {
            // 삭제하려면 필요한게 cno, bno, commenter 이다.
            int rowCnt = commentService.remove(cno, bno, commenter); // 결과는 1,2로 오니까 반환값 int

            if(rowCnt!=1)
                throw new Exception("Delete Failde");

            return new ResponseEntity<>("DELETE_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("DELETE_ERROR", HttpStatus.BAD_REQUEST);
        }
    }

    // 지정된 게시물의 모든 댓글을 가져오는 메서드
    @GetMapping("/comments") // comments?bno=1029  GET
    @ResponseBody
    // @ResponseBody 안 붙이면 뷰 이름으로 해석이됨
    public ResponseEntity<List<CommentDto>> list(Integer bno) {
        List<CommentDto> list = null;
        try {
            list = commentService.getList(bno);
            return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK); // 200
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<CommentDto>>(HttpStatus.BAD_REQUEST); // 400
        }
    }
}
