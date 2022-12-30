package com.practice.app1.controller;

import com.practice.app1.domain.BoardDto;
import com.practice.app1.domain.PageHandler;
import com.practice.app1.domain.SearchCondition;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping("/modify") // BoardDto 로 입력한 게시물을 받는다.
    public String modify(BoardDto boardDto, Integer page, Integer pageSize,
                         Model m, RedirectAttributes rattr, HttpSession session) {
        // BoardDto 에 세션에 있는 작성자 id를 넣어야 한다.
        // getAttribute 가 Object 라서 형변환 해줘야함
        String writer = (String) session.getAttribute("id");
        // 작성자를 넣는다.
        boardDto.setWriter(writer);

        rattr.addAttribute("page", page);
        rattr.addAttribute("pageSize", pageSize);

        try {
            if(boardService.modify(boardDto)!=1)
                throw new Exception("Modify faild");

            rattr.addFlashAttribute("msg", "MODIFY_OK");
            return "redirect:/board/list";

        } catch (Exception e) {
            e.printStackTrace();
            // 예외가 발생하면 boardDto 에 있는 작성했던 자료들을 다시 보낸다.
            m.addAttribute(boardDto);
            rattr.addFlashAttribute("msg", "MODIFY_ERROR");
            // 자신이 작성했던 화면으로 돌아간다.
            return "board";
        }
    }

    @PostMapping("/write") // BoardDto 로 입력한 게시물을 받는다.
    public String write(BoardDto boardDto, Model m, HttpSession session, RedirectAttributes arttr) {
        // BoardDto 에 세션에 있는 작성자 id를 넣어야 한다.
        // getAttribute 가 Object 라서 형변환 해줘야함
        String writer = (String) session.getAttribute("id");
        // 작성자를 넣는다.
        boardDto.setWriter(writer);

        try {
            if(boardService.write(boardDto)!=1)
                throw new Exception("Write faild");

            arttr.addFlashAttribute("msg", "WRITE_OK");
            return "redirect:/board/list";
        } catch (Exception e) {
            e.printStackTrace();
            // 예외가 발생하면 boardDto 에 있는 작성했던 자료들을 다시 보낸다.
            m.addAttribute(boardDto);
            arttr.addFlashAttribute("msg", "WRITE_ERROR");
            // 자신이 작성했던 화면으로 돌아간다.
            return "board";
        }
    }

    @GetMapping("/write")
    public String write(Model m) {
        m.addAttribute("mode", "new");
        return "board";
    }

    @PostMapping("/remove")
    public String remove(Integer bno, Integer page, Integer pageSize, Model m,
                         RedirectAttributes rattr, HttpSession session) {
        String writer = (String)session.getAttribute("id");
        String msg = "DELETE_OK";

        try {
            if(boardService.remove(bno, writer)!=1)
                throw new Exception("Delete failed.");
        } catch (Exception e) {
            e.printStackTrace();
            msg = "DELETE_ERR";
        }

        rattr.addAttribute("page", page);
        rattr.addAttribute("pageSize", pageSize);
        rattr.addFlashAttribute("msg", msg);
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
    public String list(SearchCondition sc, Model m, HttpServletRequest request) {

        // 로그인을 안했으면 로그인 화면으로 이동
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();

        try {

            int totalCnt = boardService.getSearchResultCnt(sc);
            m.addAttribute("totalCnt", totalCnt);

            PageHandler pageHandler = new PageHandler(totalCnt, sc);

            // map을 boardService에 넘겨준다.
            // BoardMapper에 있는 selectPage의 offset과 pageSize의 값이
            // DB를 거쳐 반환된 값이 list에 저장
            List<BoardDto> list = boardService.getSearchResultPage(sc);
            m.addAttribute("list", list);
            m.addAttribute("ph", pageHandler);

            Instant startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
            m.addAttribute("startOfToday", startOfToday.toEpochMilli());
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("msg", "LIST_ERR");
            m.addAttribute("totalCnt", 0);
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
