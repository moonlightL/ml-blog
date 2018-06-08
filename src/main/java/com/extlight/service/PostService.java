package com.extlight.service;


import com.extlight.model.Post;
import com.extlight.web.exception.GlobalException;

import java.util.List;
import java.util.Map;

public interface PostService extends BaseService<Post> {


    /**
     * 通过分类ID获取文章列表
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param title
     * @return
     */
    List<Post> getPyCategoryId(Integer categoryId, Integer pageNum, Integer pageSize, String title) throws GlobalException;

    /**
     * 获取文章列表
     * @param status 状态
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Post> getListPyPage(Integer status,Integer pageNum, Integer pageSize) throws GlobalException;

    /**
     * 归档列表
     * @return
     */
    Map<String,Object> getArchiveList() throws GlobalException;

    /**
     * 通过分类获取文章列表
     * @param categoryName
     * @return
     */
    List<Post> queryByCategory(String categoryName, Integer pageNum, Integer pageSize) throws GlobalException;

    /**
     * 通过 文章URL 获取文章内容
     * @param postUrl
     * @return
     * @throws GlobalException
     */
    Post getByPostUrl(String postUrl) throws GlobalException;

    /**
     * 获取上一篇文章
     * @param id
     * @return
     */
    Post getPreviousInfo(Integer id) throws GlobalException;

    /**
     * 获取下一篇文章
     * @param id
     * @return
     */
    Post getNextInfo(Integer id) throws GlobalException;

    /**
     * 通过关键字搜索文章
     * @param keyword
     * @return
     */
    List<Post> queryByKeyworld(String keyword) throws GlobalException;

    /**
     * 发表文章数
     * @param status 1:显示 0：隐藏  null:全部
     * @return
     * @throws GlobalException
     */
    int getPostCount(Integer status) throws GlobalException;

    /**
     * 标签数
     * @return
     */
    int getTagCount() throws GlobalException;

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(String ids) throws GlobalException;

    /**
     * 导入文件
     * @param path 文件目录
     */
    void importFiles(String path) throws GlobalException;
}
