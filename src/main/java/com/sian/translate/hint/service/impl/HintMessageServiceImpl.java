package com.sian.translate.hint.service.impl;

import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.hint.repository.HintMessageRepository;
import com.sian.translate.utlis.CommonUtlis;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.ConnectException;

@Slf4j
@Service
public class HintMessageServiceImpl implements HintMessageService {


    private final String HINT_MESSAGE_PREFIX = "hint_message_%s_%s";

    @Autowired
    HintMessageRepository hintMessageRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String getHintMessage(Integer id, String type) {

        //如果赋值为0默认取中文
        if ( StringUtils.isEmpty(type)){
            type = "0";
        }

        String key = String.format(HINT_MESSAGE_PREFIX,String.valueOf(id),type);

        String result = stringRedisTemplate.opsForValue().get(key);

        if(StringUtils.isEmpty(result)){
            if (type.equals("0")){
                result = hintMessageRepository.getChineseMessage(id);
            }else{
                result = hintMessageRepository.getZangMessage(id);
            }
            if (!StringUtils.isEmpty(result)){
                stringRedisTemplate.opsForValue().set(key, result);
            }else{
                HintMessageEnum hintMessageEnum = CommonUtlis.getByCode(HintMessageEnum.class, id);
                result = hintMessageEnum.getMessage();
            }
            log.info("HintMessageServiceImpl={}", result);
        }



        return result;
    }
}
