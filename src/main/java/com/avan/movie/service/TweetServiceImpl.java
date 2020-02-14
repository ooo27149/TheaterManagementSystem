package com.avan.movie.service;

import com.avan.movie.NotFoundException;
import com.avan.movie.dao.TweetRepository;
import com.avan.movie.po.Tweet;
import com.avan.movie.util.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TweetServiceImpl implements TweetService {

    @Autowired
    private TweetRepository tweetRepository;
    @Transactional
    @Override
    public Tweet getTweet(Long id) {
        return tweetRepository.findOne(id);
    }
    @Transactional
    @Override
    public Page<Tweet> listTweet(Pageable pageable, Tweet tweet) {
        return tweetRepository.findAll(new Specification<Tweet>() {
            @Override
            public Predicate toPredicate(Root<Tweet> root, CriteriaQuery<?> cQ, CriteriaBuilder cB) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(tweet.getTitle()) && tweet.getTitle() != null) {
                    predicates.add(cB.like(root.<String>get("title"), "%"+tweet.getTitle()+"%"));
                }
                cQ.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }
    @Transactional
    @Override
    public Page<Tweet> listTweet(Pageable pageable) {
        return tweetRepository.findPublishedTweet(pageable);
    }
    @Transactional
    @Override
    public Tweet savaTweet(Tweet tweet) {

        tweet.setUpdateTime(new Date());
        return tweetRepository.save(tweet);
    }
    @Transactional
    @Override
    public Tweet updateTweet(Long id, Tweet tweet) {
        Tweet t = tweetRepository.findOne(id);
        if (t == null) {
            throw new NotFoundException("this tweet doesn't exit");
        }
        BeanUtils.copyProperties(t, tweet);
        return tweetRepository.save(t);
    }
    @Transactional
    @Override
    public void deleteTweet(Long id) {
        tweetRepository.delete(id);
    }
    @Transactional
    @Override
    public Tweet getAndConvertTweet(Long id) {
        Tweet t = tweetRepository.findOne(id);
        if (t == null) {
            throw new NotFoundException("this tweet doesn't exit");
        }
        Tweet temp = new Tweet();
        BeanUtils.copyProperties(t, temp);
        temp.setContent(MarkdownUtils.markdownToHtml(t.getContent()));
        return temp;
    }
}
