package cn.bridgeli.livingsmallhelper.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.bridgeli.livingsmallhelper.entity.Joke;
import cn.bridgeli.livingsmallhelper.mapper.JokeMapper;
import cn.bridgeli.livingsmallhelper.service.JokeService;

@Service
public class JokeServiceImpl implements JokeService {

    @Resource
    private JokeMapper jokeMapper;

    @Override
    public Joke getJoke() {
        return jokeMapper.getJoke();
    }

}
