package com.extlight.web.controller;

import com.extlight.common.constant.PageConstant;
import com.extlight.common.constant.ParamConstant;
import com.extlight.common.utils.IPUtil;
import com.extlight.common.utils.JsonUtil;
import com.extlight.common.utils.MarkdownUtil;
import com.extlight.common.utils.ParamUtil;
import com.extlight.common.vo.GeetestVO;
import com.extlight.common.vo.Result;
import com.extlight.component.GeetestService;
import com.extlight.component.LuceneService;
import com.extlight.model.AboutMe;
import com.extlight.model.Guestbook;
import com.extlight.model.Post;
import com.extlight.service.AboutMeService;
import com.extlight.service.CategoryService;
import com.extlight.service.GuestbookService;
import com.extlight.service.PostService;
import com.extlight.web.exception.GlobalException;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 博客主页相关
 */
@Controller
public class PortalController {

    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GuestbookService guestbookService;
    @Autowired
    private AboutMeService aboutMeService;
    @Autowired
    private LuceneService luceneService;
    @Autowired
    private GeetestService geetestService;

    /**
     * 首页列表
     *
     * @param model
     * @return
     */
    @GetMapping(value = {"/", "/index.html"})
    public String index(Model model) throws Exception {
        List<Post> postList = this.postService.getListPyPage(1,PageConstant.PAGE_NUM, PageConstant.PAGE_SIZE);
        model.addAttribute("pageInfo", new PageInfo<>(postList));
        return render(model, "portal/index");
    }

    @GetMapping("/page/{pageNum}/")
    public String page(@PathVariable Integer pageNum, Model model) throws Exception {
        List<Post> postList = this.postService.getListPyPage(1,pageNum, PageConstant.PAGE_SIZE);
        model.addAttribute("pageInfo", new PageInfo<>(postList));
        return render(model, "portal/index");
    }

    /**
     * 归档列表
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/archives/")
    public String archive(Model model) throws Exception {
        Map<String, Object> dataMap = this.postService.getArchiveList();
        model.addAttribute("archiveMap", dataMap.get("archiveMap"));
        model.addAttribute("count", dataMap.get("count"));
        return render(model, "portal/archive");
    }

    /**
     * 分类列表
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/categories/")
    public String category(Model model) throws Exception {

        List<Map<String,Object>> categoryList = this.categoryService.getCategoryList();
        model.addAttribute("categoryList", categoryList);
        return render(model, "portal/category");
    }


    @GetMapping(value = "/categories/{categoryName}/")
    public String categoryList(@PathVariable String categoryName, Model model) throws Exception {

        List<Post> postList = this.postService.queryByCategory(categoryName,PageConstant.PAGE_NUM,PageConstant.PAGE_SIZE);
        model.addAttribute("pageInfo", new PageInfo<>(postList));
        model.addAttribute("name", categoryName);
        return render(model, "portal/postlist");
    }

    @GetMapping("/categories/{categoryName}/page/{pageNum}/")
    public String categoryListPage(@PathVariable String categoryName,@PathVariable Integer pageNum, Model model) throws Exception {
        List<Post> postList = this.postService.queryByCategory(categoryName,pageNum,PageConstant.PAGE_SIZE);
        model.addAttribute("pageInfo", new PageInfo<>(postList));
        model.addAttribute("name", categoryName);
        return render(model, "portal/postlist");
    }

    /**
     * 文章内容，URL 的配置格式是为了兼容 hexo
     *
     * @param model
     * @return
     */
    @GetMapping("/{year}/{month}/{day}/{title}/")
    public String post(@PathVariable("year") String year,
                       @PathVariable("month") String month,
                       @PathVariable("day") String day,
                       @PathVariable("title") String title,
                       Model model) throws Exception {

        String postUrl = year + "/" + month + "/" + day + "/" + title + "/";
        Post post = this.postService.getByPostUrl(postUrl);

        if (post == null || post.getStatus() == 0) {
            model.addAttribute("msg","该文章不存在");
            return render(model, "portal/error");
        }

        Post previous = this.postService.getPreviousInfo(post.getId());
        Post next = this.postService.getNextInfo(post.getId());

        model.addAttribute("post", post);
        model.addAttribute("previous", previous);
        model.addAttribute("next", next);
        model.addAttribute("md", MarkdownUtil.class);

        return render(model, "portal/detail");
    }


    /**
     * 搜索
     *
     * @param keyword
     * @param model
     * @return
     */
    @GetMapping("/search/")
    public String search(String keyword, Model model) throws Exception {
        if (StringUtils.isEmpty(keyword)) {
            model.addAttribute("keyword", keyword);
            model.addAttribute("postList", null);
            return render(model, "portal/search");
        }
          // 从数据库中查询
//        List<Post> postList = this.postService.queryByKeyworld(keyword.trim());
        // 使用 lucene 查询
        List<Post> postList = this.luceneService.query(keyword.trim());
        model.addAttribute("keyword", keyword.trim());
        model.addAttribute("postList", postList);
        return render(model, "portal/search");
    }

    /**
     * 留言板
     *
     * @param model
     * @return
     */
    @GetMapping("/guestbook/")
    public String guestbook( Model model) throws Exception {
        List<Guestbook> list = this.guestbookService.getListPyPage(0,PageConstant.PAGE_NUM, PageConstant.PAGE_SIZE);
        model.addAttribute("pageInfo", new PageInfo<>(list));
        return render(model, "portal/guestbook");
    }

    @GetMapping("/guestbook/page/{pageNum}/")
    public String guestbook(@PathVariable Integer pageNum, Model model) throws Exception {
        List<Guestbook> list = this.guestbookService.getListPyPage(0,pageNum, PageConstant.PAGE_SIZE);
        model.addAttribute("pageInfo", new PageInfo<>(list));
        return render(model, "portal/guestbook");
    }

    /**
     * 留言板 发言
     *
     * @param guestbook
     * @return
     */
    @PostMapping("/guestbook/")
    @ResponseBody
    public Result saveGuestbook(@Valid Guestbook guestbook, String captcha, HttpServletRequest request) throws Exception {

        String capText = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (StringUtils.isEmpty(capText)) {
            throw new GlobalException(500,"验证码失效");
        }

        if (!capText.equals(captcha)) {
            throw new GlobalException(500,"验证码不正确");
        }

        guestbook.setIp(IPUtil.getIpAddr(request));
        guestbook.setIpAddr(IPUtil.getCity(guestbook.getIp()));
        this.guestbookService.save(guestbook);
        return Result.success();
    }

    /**
     * 获取极验验证码
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/getCaptcha")
    @ResponseBody
    public Result getCaptcha(HttpServletRequest request) throws Exception {

        if (!ParamUtil.checkParameter(3)) {
            throw new GlobalException(500,"未配置极验参数");
        }

        String captchaStr = this.geetestService.StartCaptcha(request);
        GeetestVO geetestVO = JsonUtil.string2Obj(captchaStr, GeetestVO.class);
        return Result.success(geetestVO); // 返回极验验证码
    }

    /**
     * 留言板 发言(极验)
     *
     * @param guestbook
     * @return
     */
    @PostMapping("/guestbook-gt")
    @ResponseBody
    public Result saveGuestbookGt(@Valid Guestbook guestbook,HttpServletRequest request) throws Exception {

        if (!ParamUtil.checkParameter(ParamConstant.GEETEST)) {
            throw new GlobalException(500,"未配置极验参数");
        }

        if (!this.geetestService.verifyCaptcha(request)) {
            throw new GlobalException(500,"验证错误");
        }

        guestbook.setIp(IPUtil.getIpAddr(request));
        guestbook.setIpAddr(IPUtil.getCity(guestbook.getIp()));
        this.guestbookService.save(guestbook);
        return Result.success();
    }

    /**
     * 关于我
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/about/")
    public String aboutMe(Model model) throws Exception {
        AboutMe aboutMe = this.aboutMeService.getAboutMe(1);
        model.addAttribute("aboutMe",aboutMe);
        model.addAttribute("md", MarkdownUtil.class);
        return render(model, "portal/aboutMe");
    }

    private String render(Model model, String path) {
        model.addAttribute("menu", path.substring(path.indexOf("/") + 1, path.length()));
        return path;
    }
}
