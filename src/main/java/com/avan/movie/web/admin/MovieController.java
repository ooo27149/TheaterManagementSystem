package com.avan.movie.web.admin;

import com.avan.movie.po.Admin;
import com.avan.movie.po.Movie;
import com.avan.movie.service.MovieService;
import com.avan.movie.service.ScheduleService;
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

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class MovieController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private ScheduleService scheduleService;

//    @GetMapping("/moviesssssss")
    public String movieList(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {    //每页5条， 按id倒序(DESC)排列

        model.addAttribute("page", movieService.listMovie(pageable));
        return "admin/movie_list";
    }

    @GetMapping("/movies")
    public String movieConditionList(@PageableDefault(size = 8, sort = {"stat"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Movie movie, Model model) {
        model.addAttribute("page", movieService.listConditionMovie(pageable, movie));
        return "admin/movie_list";
    }
    @PostMapping("/movies/search")
    public String movieSearch(@PageableDefault(size = 8, sort = {"stat"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Movie movie, Model model) {

        model.addAttribute("page", movieService.listConditionMovie(pageable, movie));
        return "admin/movie_list :: movieList";
    }

    @GetMapping("/movie_edit")
    public String movieEdit(Model model) {
        model.addAttribute(new Movie());
        return "admin/movie_edit";
    }

    @GetMapping("/{id}/movie_edit")
    public String movieUpdate(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.getMovie(id));
        return "admin/movie_edit";
    }

    @PostMapping("/movie_save")
    public String movieSave(@Valid Movie movie, BindingResult bindingResult,
                            @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (!file.isEmpty()) {
            savePoster(file, movie);
        } else {
            bindingResult.rejectValue("poster", "posterError", "请上传海报图片");
        }
        if (bindingResult.hasErrors()) return "admin/movie_edit";

        movie.setUpdateTime(new Date());
        Movie m = movieService.saveMovie(movie);
        if (m == null) {
            attributes.addFlashAttribute("message", "操作失败");

        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }

        return "redirect:/admin/movies";
    }

    private static void savePoster(MultipartFile file, Movie movie) {
        String fileName = file.getOriginalFilename();
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists()) {
                path = new File("");
            }
            File upload = new File(path.getAbsolutePath(),"static/movie_poster/");
            if(!upload.exists()) {
                upload.mkdirs();
            }
            File dest = new File(path.getAbsolutePath(),"static/movie_poster/"+ movie.getName() + fileName);
            file.transferTo(dest); //保存文件
            movie.setPoster("/movie_poster/" + movie.getName() + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/movie_save/{id}")
    public String movieUpdate(@Valid Movie movie,
                              @RequestParam("file") MultipartFile file,
                              @PathVariable Long id, RedirectAttributes attributes) {
        if (!file.isEmpty()) {
            savePoster(file, movie);
        }

        movie.setUpdateTime(new Date());
        Movie m = movieService.updateMovie(id, movie);
        if (m == null) {
            attributes.addFlashAttribute("message", "更新失败");

        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }

        return "redirect:/admin/movies";
    }

    @GetMapping("/{id}/movie_delete")
    public String movieDelete(@PathVariable Long id, RedirectAttributes attributes) {
        if (scheduleService.listScheduleByMovie(movieService.getMovie(id)).size() >= 1) {
            attributes.addFlashAttribute("messageError", "该电影已被安排场次放映，删除失败");
            return "redirect:/admin/movies";
        }
        movieService.deleteMovie(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/movies";
    }
}
