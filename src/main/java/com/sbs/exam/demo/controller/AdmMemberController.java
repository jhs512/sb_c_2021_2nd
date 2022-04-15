package com.sbs.exam.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sbs.exam.demo.util.Ut;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sbs.exam.demo.service.MemberService;
import com.sbs.exam.demo.vo.Member;
import com.sbs.exam.demo.vo.Rq;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdmMemberController {
    private MemberService memberService;
    private Rq rq;

    public AdmMemberController(MemberService memberService, Rq rq) {
        this.memberService = memberService;
        this.rq = rq;
    }

    @RequestMapping("/adm/member/list")
    public String showList(Model model, @RequestParam(defaultValue = "0") int authLevel,
                           @RequestParam(defaultValue = "loginId,name,nickname") String searchKeywordTypeCode,
                           @RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "1") int page) {
        int membersCount = memberService.getMembersCount(authLevel, searchKeywordTypeCode, searchKeyword);

        int itemsCountInAPage = 10;
        int pagesCount = (int) Math.ceil((double) membersCount / itemsCountInAPage);
        List<Member> members = memberService.getForPrintMembers(authLevel, searchKeywordTypeCode, searchKeyword, itemsCountInAPage, page);

        model.addAttribute("authLevel", authLevel);
        model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
        model.addAttribute("searchKeyword", searchKeyword);

        model.addAttribute("pagesCount", pagesCount);
        model.addAttribute("page", page);

        model.addAttribute("membersCount", membersCount);
        model.addAttribute("members", members);

        return "adm/member/list";
    }

    @RequestMapping("/adm/member/doDeleteMembers")
    @ResponseBody
    public String doDeleteMembers(@RequestParam(defaultValue = "") String ids, @RequestParam(defaultValue = "/adm/member/list") String replaceUri) {
        List<Integer> memberIds = new ArrayList<>();

        for (String idStr : ids.split(",")) {
            memberIds.add(Integer.parseInt(idStr));
        }

        memberService.deleteMembers(memberIds);

        return Ut.jsReplace("해당 회원들이 삭제되었습니다.", replaceUri);
    }
}
