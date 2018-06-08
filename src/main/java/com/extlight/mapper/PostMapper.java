package com.extlight.mapper;

import com.extlight.model.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {


    /**
     * 通过分类ID获取文章列表
     * @param categoryId
     * @return
     */
    List<Post> queryPostByCategoryId(@Param("categoryId")Integer categoryId,@Param("status") Integer status, @Param("title") String title);

    /**
     * 获取文章列表
     * @param status 状态 1:发布 2：操作 null：全部
     * @return
     */
    List<Post> getList(@Param("status") Integer status);

    /**
     * 归档列表
     * @return
     */
    List<Post> getArchiveList();

    /**
     * 通过文章 URL 获取文章内容
     * @param postUrl
     * @return
     */
    Post getByPostUrl(String postUrl);

    /**
     * 获取上一篇文章
     * @param id
     * @return
     */
    Post getPreviousInfo(Integer id);

    /**
     * 获取下一篇文章
     * @param id
     * @return
     */
    Post getNextInfo(Integer id);

    /**
     * 通过关键字搜索文章
     * @param keyword
     * @return
     */
    List<Post> queryByKeyworld(String keyword);

    /**
     * 批量删除
     * @param idList
     */
    void deleteBatch(@Param("idList") List<Integer> idList);

    /**
     * 批量插入
     * @param postList
     */
    void insertBatch(@Param("postList") List<Post> postList);

    /**
     * 插入前检测
     * @param post
     */
    void checkInsert(Post post);

}
