package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("品类相关接口")
@RestController
@RequestMapping("admin/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("查询菜品分类列表")
    @GetMapping("page")
    public Result<PageResult> queryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult result = categoryService.query(categoryPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 新增品类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("新增品类")
    @PostMapping
    public Result<Category> save(CategoryDTO categoryDTO) {
        Category category = categoryService.insert(categoryDTO);
        return Result.success(category);
    }
}
