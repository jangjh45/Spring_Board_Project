package com.practice.app1.controller;

import com.practice.app1.domain.BoardDto;
import com.practice.app1.domain.PageHandler;
import com.practice.app1.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping("/modify") // BoardDto 로 입력한 게시물을 받는다.
    public String modify(BoardDto boardDto, Integer page, Integer pageSize,
                         Model m, HttpSession session, RedirectAttributes arttr) {
        // BoardDto 에 세션에 있는 작성자 id를 넣어야 한다.
        // getAttribute 가 Object 라서 형변환 해줘야함
        String writer = (String) session.getAttribute("id");
        // 작성자를 넣는다.
        boardDto.setWriter(writer);

        try {
            int rowCnt = boardService.modify(boardDto);

            if(rowCnt!=1)
                throw new Exception("Write faild");

            arttr.addFlashAttribute("msg", "MODIFY_OK");
        } catch (Exception e) {
            e.printStackTrace();
            // 예외가 발생하면 boardDto 에 있는 작성했던 자료들을 다시 보낸다.
            m.addAttribute(boardDto);
            m.addAttribute("msg", "MODIFY_ERROR");
            // 자신이 작성했던 화면으로 돌아간다.
            return "board";
        }
        m.addAttribute("page", page);
        m.addAttribute("pageSize", pageSize);
        return "redirect:/board/list";
    }

    @PostMapping("/write") // BoardDto 로 입력한 게시물을 받는다.
    public String write(BoardDto boardDto, Model m, HttpSession session, RedirectAttributes arttr) {
        // BoardDto 에 세션에 있는 작성자 id를 넣어야 한다.
        // getAttribute 가 Object 라서 형변환 해줘야함
        String writer = (String) session.getAttribute("id");
        // 작성자를 넣는다.
        boardDto.setWriter(writer);

        try {
            int rowCnt = boardService.write(boardDto);

            if(rowCnt!=1)
                throw new Exception("Write faild");

            arttr.addFlashAttribute("msg", "WRITE_OK");
        } catch (Exception e) {
            e.printStackTrace();
            // 예외가 발생하면 boardDto 에 있는 작성했던 자료들을 다시 보낸다.
            m.addAttribute(boardDto);
            m.addAttribute("msg", "WRITE_ERROR");
            // 자신이 작성했던 화면으로 돌아간다.
            return "board";
        }
        return "redirect:/board/list";
    }

    @GetMapping("/write")
    public String write(Model m) {
        m.addAttribute("mode", "new");
        return "board";
    }

    @PostMapping("/remove")
    public String remove(Integer bno, Integer page, Integer pageSize, Model m,
                         HttpSession session,
                         RedirectAttributes rattr) {
        // 다른 작성자는 삭제 불가능 (로그인 한 id와 작성자가 같아야 삭제가능)
        String writer = (String)session.getAttribute("id");
        m.addAttribute("page", page);
        m.addAttribute("pageSize", pageSize);

        try {
            // BoardMapper에 bno와 작성자가 일치하는 게시물 삭제
            int rowCnt = boardService.remove(bno, writer);
            if(rowCnt!=1)
                // remove 실패하면 예외를 던짐
                throw new Exception("board remove error");


            // RedirectAttributes rattr 는 메시지 한번만 쓰고 없어짐(세션을 이용하는 것)
            m.addAttribute("msg", "DELETE_OK");
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("msg", "DELETE_ERROR");
        }
        // 윗 model 두줄이 ~list 뒤에 ?page=&pageSize 자동으로 추가됨
        return "redirect:/board/list";
    }

    @GetMapping("/read")
    // Integet로 게시물 번호를 받는다.
    public String read(Integer bno, Integer page, Integer pageSize, Model m) {
        try {
            // BoardMapper에 bno 게시물 번호를 주고 게시글 정보 받는다.
            BoardDto boardDto = boardService.read(bno);

            m.addAttribute("boardDto", boardDto);
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "board";
    }

    @GetMapping("/list")
    // page와 pageSize를 얻는다.
    public String list(Integer page, Integer pageSize, Model m, HttpServletRequest request) {

        // 로그인을 안했으면 로그인 화면으로 이동
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();

        // 로그인 했다면 board 첫 페이지가 보이도록 함
        if(page==null) page=1;
        if(pageSize==null) pageSize=10;

        try {

            int totalCnt = boardService.getCount();
            PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);

            // BoardMapper에 있는 selectPage의 offset과 pageSize를 넘겨줄 준비
            Map map = new HashMap();
            map.put("offset", (page-1)*pageSize);
            map.put("pageSize", pageSize);

            // map을 boardService에 넘겨준다.
            // BoardMapper에 있는 selectPage의 offset과 pageSize의 값이
            // DB를 거쳐 반환된 값이 list에 저장
            List<BoardDto> list = boardService.getPage(map);

            m.addAttribute("list", list);
            m.addAttribute("ph", pageHandler);
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id")!=null;
    }
}
