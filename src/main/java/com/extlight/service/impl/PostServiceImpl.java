package com.extlight.service.impl;

import com.extlight.common.utils.CacheUtil;
import com.extlight.common.utils.DateUtil;
import com.extlight.common.vo.PostVo;
import com.extlight.component.LuceneService;
import com.extlight.mapper.BaseMapper;
import com.extlight.mapper.PostMapper;
import com.extlight.model.Category;
import com.extlight.model.Post;
import com.extlight.service.CategoryService;
import com.extlight.service.PostService;
import com.extlight.web.exception.GlobalException;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "postCache")
@Service
public class PostServiceImpl extends BaseServiceImpl<Post> implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LuceneService luceneService;

    @Override
    public BaseMapper<Post> getBaseMapper() {
        return this.postMapper;
    }

    @Transactional
    @Override
    public void save(Post post) throws GlobalException {

        Category category = this.categoryService.getById(post.getCategoryId());
        if (category == null) {
            throw new GlobalException(400, "该分类不存在");
        }

        Date now = new Date();
        String dateStr = DateUtil.formateToStr(now, "yyyy-MM-dd");
        String[] dates = dateStr.split("-");

        post.setPublishDate(now)
                .setImgUrl("material-" + (new Random().nextInt(30) + 1) + ".jpg")
                .setYear(dates[0])
                .setMonth(dates[1])
                .setDay(dates[2])
                .setCategoryName(category.getName())
                .setPostUrl(post.getYear() + "/" + post.getMonth() + "/" + post.getDay() + "/" + post.getTitle().replace(" ", "-") + "/");

        this.postMapper.insert(post);

        // 加索引
        this.luceneService.add(post);
        // 清理缓存
        CacheUtil.deleteAll();
    }

    @Transactional
    @Override
    public void update(Post post) throws GlobalException {
        Category category = this.categoryService.getById(post.getCategoryId());
        if (category == null) {
            throw new GlobalException(400, "该分类不存在");
        }

        Post dbPost = this.postMapper.selectByPrimaryKey(post.getId());

        // 修改名字同时必须修改 postUrl
        post.setPostUrl(dbPost.getYear() + "/" + dbPost.getMonth() + "/" + dbPost.getDay() + "/" + post.getTitle().replace(" ", "-") + "/")
                .setCategoryName(category.getName())
                .setPublishDate(dbPost.getPublishDate());

        this.postMapper.updateByPrimaryKeySelective(post);

        // 修改索引
        this.luceneService.update(post);
        // 清理缓存
        CacheUtil.deleteAll();
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        this.postMapper.deleteByPrimaryKey(id);

        // 删除索引
        this.luceneService.delete(id);
        // 清理缓存
        CacheUtil.deleteAll();
    }


    @Override
    public List<Post> getPyCategoryId(Integer categoryId, Integer pageNum, Integer pageSize, String title) throws GlobalException {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> list = this.postMapper.queryPostByCategoryId(categoryId, null, title);
        return list;
    }

    @Cacheable(key = "'page:' + #pageNum")
    @Override
    public List<Post> getListPyPage(Integer status, Integer pageNum, Integer pageSize) throws GlobalException {
        PageHelper.startPage(pageNum, pageSize);
        return this.postMapper.getList(status);
    }

    @Cacheable(key = "'archive:list'")
    @Override
    public List<Post> getArchiveList() throws GlobalException {
        // 获取显示状态的文章
        List<Post> postList = this.postMapper.getArchiveList();
        return postList;
    }

    @Cacheable(key = "'post:list' + #categoryName + ':' + #pageNum")
    @Override
    public List<Post> queryByCategory(String categoryName, Integer pageNum, Integer pageSize) throws GlobalException {
        Category category = this.categoryService.getCategoryByName(categoryName);

        if (category == null) {
            return null;
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Post> list = this.postMapper.queryPostByCategoryId(category.getId(), 1, null);

        return list;
    }

    @Cacheable(key = "'postContent:' + #postUrl")
    @Override
    public Post getByPostUrl(String postUrl) throws GlobalException {
        return this.postMapper.getByPostUrl(postUrl);
    }

    @Cacheable(key = "'previousInfo:' + #id")
    @Override
    public Post getPreviousInfo(Integer id) throws GlobalException {
        return this.postMapper.getPreviousInfo(id);
    }

    @Cacheable(key = "'nextInfo:' + #id")
    @Override
    public Post getNextInfo(Integer id) throws GlobalException {
        return this.postMapper.getNextInfo(id);
    }

    @Override
    public List<Post> queryByKeyworld(String keyword) throws GlobalException {
        return this.postMapper.queryByKeyworld(keyword);
    }

    @Override
    public int getPostCount(Integer status) throws GlobalException {
        Post post = null;
        if (status != null) {
            post = new Post();
            post.setStatus(status);
        }
        return this.postMapper.selectCount(post);
    }

    @Override
    public int getTagCount() throws GlobalException {
        List<Post> postList = this.postMapper.getList(null);
        List<String> tagList = postList.stream().map(p -> p.getTags()).collect(Collectors.toList());
        Set<String> set = new HashSet<>();
        for (String tag : tagList) {
            String[] split = tag.split(",");
            for (String s : split) {
                set.add(s.trim());
            }
        }
        return set.size();
    }

    @Override
    public void deleteBatch(String ids) throws GlobalException {
        String[] idArr = ids.split(",");
        List<Integer> idList = new ArrayList<>(idArr.length);
        for (String id : idArr) {
            idList.add(Integer.parseInt(id));
            // 删除索引
            this.luceneService.delete(Integer.parseInt(id));
        }
        // 批量删除
        this.postMapper.deleteBatch(idList);
        // 清理缓存
        CacheUtil.deleteAll();
    }

    @Override
    public void importFiles(String path) throws GlobalException {
        File dir = new File(path);
        if (!dir.isDirectory()) {
            throw new GlobalException(400, "不是文件目录");
        }

        File[] files = dir.listFiles(pathname -> pathname.getName().endsWith("md"));

        if (files.length == 0) {
            throw new GlobalException(400, "没有可导入的 Markdown 文件");
        }

        List<Post> postList = packageToList(files);

//        this.postMapper.insertBatch(postList);

        for (Post post : postList) {
            // 检测和插入
            this.postMapper.checkInsert(post);
            // 加索引
            this.luceneService.add(post);
        }
        // 清理缓存
        CacheUtil.deleteAll();
    }

    private List<Post> packageToList(File[] files) {
        List<Post> postList = new ArrayList<>(files.length);
        BufferedReader br = null;
        Post post;
        for (File file : files) {
            try {
                br = new BufferedReader(new FileReader(file));
                br.readLine();
                String titleStr = br.readLine();
                String createTimeStr = br.readLine();
                String tagsStr = br.readLine();
                String categoryNameStr = br.readLine();
                br.readLine();

                StringBuilder sb = new StringBuilder();
                String content;
                while ((content = br.readLine()) != null) {
                    sb.append(content).append("\r\n");
                }

                post = new Post();
                post.setTitle(titleStr.substring(titleStr.indexOf(":") + 1).trim())
                        .setContent(sb.toString());

                String categoryName = categoryNameStr.substring(categoryNameStr.indexOf(":") + 1).trim();
                if (StringUtils.isEmpty(categoryName)) {
                    Category category = this.categoryService.getCategoryByName("默认");
                    post.setCategoryId(category.getId());
                    post.setCategoryName(category.getName());
                } else {
                    post.setCategoryName(categoryName);
                    Category category = this.categoryService.getCategoryByName(post.getCategoryName());
                    if (category == null) {
                        category = new Category();
                        category.setName(post.getCategoryName())
                                .setDescr(post.getCategoryName());
                        this.categoryService.save(category);
                    }
                    post.setCategoryId(category.getId());
                }

                String tags = tagsStr.substring(tagsStr.indexOf(":") + 1).trim();
                if (!StringUtils.isEmpty(tags)) {
                    tags = tags.replace("[", "").replace("]", "");
                }
                post.setStatus(1)
                        .setImgUrl("material-" + (new Random().nextInt(30) + 1) + ".jpg")
                        .setTags(tags);

                Date date = DateUtil.parseToDate(createTimeStr.substring(createTimeStr.indexOf(":") + 1).trim(), "yyyy-MM-dd HH:mm:ss");
                post.setPublishDate(date)
                        .setCreateTime(date)
                        .setUpdateTime(date);

                String dateStr = DateUtil.formateToStr(date, "yyyy-MM-dd");
                String[] dates = dateStr.split("-");
                post.setYear(dates[0])
                        .setMonth(dates[1])
                        .setDay(dates[2])
                        .setPostUrl(post.getYear() + "/" + post.getMonth() + "/" + post.getDay() + "/" + post.getTitle().replace(" ", "-") + "/");

                postList.add(post);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return postList;
    }
}
