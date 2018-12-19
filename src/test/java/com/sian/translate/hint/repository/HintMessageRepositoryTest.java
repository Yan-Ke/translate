package com.sian.translate.hint.repository;

import com.sian.translate.TranslateApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class HintMessageRepositoryTest extends TranslateApplicationTests {

    @Autowired
    HintMessageRepository hintMessageRepository;

    @Test
    public void getChineseMessage() {

        String message = hintMessageRepository.getChineseMessage(1);
        Assert.assertTrue(message.equals("成功"));

    }

    @Test
    public void getZangMessage() {

        String message = hintMessageRepository.getZangMessage(1);
        Assert.assertTrue(message.equals("ལེགས་གྲུབ།"));

    }
}