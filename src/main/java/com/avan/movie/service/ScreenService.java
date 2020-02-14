package com.avan.movie.service;

import com.avan.movie.po.Screen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScreenService {

    Screen saveScreen(Screen screen);

    Screen getScreen(Long id);

    Page<Screen> listScreen(Pageable pageable, Screen screen);

    List<Screen> listScreen();

    Screen updateScreen(Long id, Screen screen);

    void deleteScreen(Long id);

    Screen getScreenByName(String name);
}
