package app.admin.controller;
/*
    created by xdCao on 2017/10/15
*/

import app.admin.model.RestResponseBo;
import app.admin.model.vo.CommentVo;
import app.admin.model.vo.CommentVoExample;
import app.admin.model.vo.UserVo;
import app.admin.service.CommentService;
import app.common.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin/comments")
public class CommentController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @GetMapping(value = "")
    public String index(@RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "limit",defaultValue = "15") int limit,
                        HttpServletRequest request){
        UserVo userVo=this.getUser(request);
        CommentVoExample example=new CommentVoExample();
        example.setOrderByClause("coid desc");
        example.createCriteria().andAuthorIdNotEqualTo(userVo.getUid());
        PageInfo<CommentVo> commentVoPageInfo=commentService.getCommentOfArticle(example,page,limit);
        request.setAttribute("comments",commentVoPageInfo);
        return "admin/comment_list";
    }

    //删除一条评论
    @PostMapping(value = "/delete")
    @ResponseBody
    public RestResponseBo delete(@RequestParam Integer coid){
        try {
            CommentVo commentVo=commentService.getCommentById(coid);
            if (null==commentVo){
                return RestResponseBo.fail("不存在该评论");
            }else {
                commentService.delete(coid,commentVo.getCid());
            }
        }catch (Exception e){
            String msg="评论删除失败";
            LOGGER.error(msg,e);
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    //更改评论的状态
    @PostMapping(value = "status")
    @ResponseBody
    public RestResponseBo changeStatus(@RequestParam Integer coid,@RequestParam String status){
        try {
            CommentVo commentVo=commentService.getCommentById(coid);
            if (commentVo!=null){
                commentVo.setCoid(coid);
                commentVo.setStatus(status);
                commentService.update(commentVo);
            }else {
                return RestResponseBo.fail("操作失败");
            }
        }catch (Exception e){
            String msg="操作失败";
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }


}
