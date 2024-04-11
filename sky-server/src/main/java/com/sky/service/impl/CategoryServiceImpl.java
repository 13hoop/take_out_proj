package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        // 复制参数
        BeanUtils.copyProperties(categoryDTO, category);
        // 状态默认开启
        category.setStatus(StatusConstant.ENABLE);
        // 时间装填
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        // 添加着id
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
        return category;
    }

    @Override
    public PageResult query(CategoryPageQueryDTO categoryPageQueryDTO) {
        int page = categoryPageQueryDTO.getPage();
        int szie = categoryPageQueryDTO.getPageSize();
        String name = categoryPageQueryDTO.getName();
        Integer type =categoryPageQueryDTO.getType();

        PageHelper.startPage(page, szie);
        Page<Category> pages = categoryMapper.find(categoryPageQueryDTO);
        List<Category> list = pages.getResult();
        long total = pages.getTotal();

        PageResult result = new PageResult();
        result.setRecords(list);
        result.setTotal(total);
        return result;
    }
}
