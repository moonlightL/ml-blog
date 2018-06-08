package com.extlight.service.impl;

import com.extlight.common.utils.CacheUtil;
import com.extlight.mapper.BaseMapper;
import com.extlight.mapper.CategoryMapper;
import com.extlight.model.Category;
import com.extlight.service.CategoryService;
import com.extlight.web.exception.GlobalException;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.Random;

@CacheConfig(cacheNames = "categoryCache")
@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements CategoryService {

    // 颜色数组
    private static final String[] COLORS = {"text-default","text-primary","text-success","text-info","text-warning","text-danger","text-purple","text-pink"};

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public BaseMapper<Category> getBaseMapper() {
        return this.categoryMapper;
    }

    @Override
    public Category getCategoryByName(String categoryName) throws GlobalException {
        return this.categoryMapper.getByName(categoryName);
    }

    @Override
    public int getCategoryCount() throws GlobalException {
        return this.categoryMapper.selectCount(null);
    }

    @Cacheable(key = "'categoryList'")
    @Override
    public List<Map<String,Object>> getCategoryList() {
        return this.categoryMapper.getCategoryList();
    }

    @Transactional
    @Override
    public void save(Category category) throws GlobalException {

        if (StringUtils.isEmpty(category.getImgUrl())) {
            category.setImgUrl("/portal/images/category_default.jpg");
        }

        if (StringUtils.isEmpty(category.getColor())) {
            category.setColor(COLORS[new Random().nextInt(COLORS.length)]);
        }

        this.categoryMapper.insert(category);
        CacheUtil.delete("categoryCache");
    }

    @Transactional
    @Override
    public void update(Category category) throws GlobalException {

        if (StringUtils.isEmpty(category.getImgUrl())) {
            category.setImgUrl("/portal/images/category_default.jpg");
        }

        if (StringUtils.isEmpty(category.getColor())) {
            category.setColor(COLORS[new Random().nextInt(COLORS.length)]);
        }

        this.categoryMapper.updateByPrimaryKeySelective(category);
        CacheUtil.delete("categoryCache");
    }

    @Transactional
    @Override
    public void delete(Integer id) throws GlobalException {
        this.categoryMapper.deleteByPrimaryKey(id);
        CacheUtil.delete("categoryCache");
    }

    @Override
    public List<Category> getPyPage(Integer pageNum, Integer pageSize) throws GlobalException {

        Example example = new Example(Category.class);
        example.orderBy("sort").asc();

        PageHelper.startPage(pageNum,pageSize);
        return this.categoryMapper.selectByExample(example);
    }
}
