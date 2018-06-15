package com.extlight;

import com.extlight.component.AsyncService;
import com.extlight.model.Post;
import com.extlight.service.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @Title: AsyncServiceTest
 * @ProjectName ml-blog
 * @Description: TODO
 * @date 2018/6/15 0015 下午 02:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncServiceTest {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private PostService postService;

    @Test
    public void test() {
        Post post = this.postService.getById(28);

        this.asyncService.push2Baidu(post.getPostUrl());
    }
}
