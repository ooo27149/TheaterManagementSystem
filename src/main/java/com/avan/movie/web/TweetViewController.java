package com.avan.movie.web;

import com.avan.movie.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TweetViewController {
    @Autowired
    private TweetService tweetService;

    private static final String LIST = "tweets";
    private static final String REDIRECT_LIST = "redirect:/tweets";

    @GetMapping("/tweets")
    public String tweetList(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {    //每页5条， 按id倒序(DESC)排列
        model.addAttribute("page", tweetService.listTweet(pageable));
        return LIST;
    }

    @GetMapping("/tweet/{id}")
    public String tweetList(@PathVariable Long id, Model model) {
        model.addAttribute(tweetService.getAndConvertTweet(id));
        return "tweet";
    }
}
