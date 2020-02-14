package com.avan.movie.web.admin;

import com.avan.movie.po.Admin;
import com.avan.movie.po.Tweet;
import com.avan.movie.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class TweetController {

    @Autowired
    private TweetService tweetService;

    private static final String EDIT = "admin/tweet_edit";
    private static final String LIST = "admin/tweet_list";
    private static final String REDIRECT_LIST = "redirect:/admin/tweet_list";

    @GetMapping("/tweet_list")
    public String tweetList(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Tweet tweet, Model model) {    //每页5条， 按id倒序(DESC)排列
        model.addAttribute("page", tweetService.listTweet(pageable, tweet));
        return LIST;
    }

    @PostMapping("/tweets/search")
    public String tweetSearch(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                      Pageable pageable, Tweet tweet, Model model) {

        model.addAttribute("page", tweetService.listTweet(pageable, tweet));
        return "admin/tweet_list :: tweetList";
    }

    @GetMapping("/tweet_edit")
    public String tweetNew(Model model) {
        model.addAttribute("tweet", new Tweet());
        return EDIT;
    }

    @GetMapping("{id}/tweet_edit")
    public String tweetEdit(@PathVariable Long id, Model model) {
        model.addAttribute("tweet", tweetService.getTweet(id));
        return EDIT;
    }

    private static void savePoster(MultipartFile file, Tweet tweet) {
        String fileName = file.getOriginalFilename();
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists()) {
                path = new File("");
            }
            File upload = new File(path.getAbsolutePath(),"static/tweet_poster/");
            if(!upload.exists()) {
                upload.mkdirs();
            }
            File dest = new File(path.getAbsolutePath(),"static/tweet_poster/"+ tweet.getTitle() + fileName);
            file.transferTo(dest); //保存文件
            tweet.setPoster("/tweet_poster/" + tweet.getTitle() + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @PostMapping("/tweets/post")
    public String tweetPost(@Valid Tweet tweet, BindingResult bindingResult,
                            @RequestParam("file") MultipartFile file,
                            RedirectAttributes attributes, HttpSession httpSession) {
        if (!file.isEmpty()) {
            savePoster(file, tweet);
        } else if (tweet.getId() == null) {
            bindingResult.rejectValue("poster", "posterError", "请上传海报图片");
        }
        if (bindingResult.hasErrors()) return EDIT;

        tweet.setAdmin((Admin) httpSession.getAttribute("admin"));
        Tweet t = tweetService.savaTweet(tweet);
        if (t == null) {
            attributes.addFlashAttribute("message", "操作失败");

        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("{id}/tweet_delete")
    public String tweetDelete(@PathVariable Long id, RedirectAttributes attributes) {
        tweetService.deleteTweet(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}
