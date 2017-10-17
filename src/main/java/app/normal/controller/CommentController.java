package app.normal.controller;

import app.admin.model.others.RestResponseBo;
import app.admin.model.others.Types;
import app.admin.model.vo.CommentVo;
import app.admin.service.CommentService;
import app.common.constant.Constants;
import app.common.controller.BaseController;

import app.common.utils.IPKit;
import app.common.utils.PatternKit;
import app.common.utils.TaleUtils;
import app.normal.model.ErrorCode;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/*
    created by xdCao on 2017/10/17
*/
@Controller
public class CommentController extends BaseController{

    private static final Logger LOGGER= LoggerFactory.getLogger(CommentController.class);


    @Autowired
    private CommentService commentService;


    @PostMapping(value = "/comment")
    @ResponseBody
    public RestResponseBo comment(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam Integer cid,@RequestParam Integer coid,
                                  @RequestParam String author,@RequestParam String mail,
                                  @RequestParam String url,@RequestParam String text,@RequestParam String _csrf_token){
        String ref=request.getHeader("Referer");
        if (StringUtils.isBlank(ref)||StringUtils.isBlank(_csrf_token)){
            return RestResponseBo.fail(ErrorCode.BAD_REQUEST);
        }
        String token=cache.hashGet(Types.CSRF_TOKEN.getType(),_csrf_token);
        if (StringUtils.isBlank(token)){
            return RestResponseBo.fail(ErrorCode.BAD_REQUEST);
        }
        if (null==cid||StringUtils.isBlank(text)){
            return RestResponseBo.fail("请输入完整");
        }
        if (StringUtils.isNotBlank(author)&&author.length()>50){
            return RestResponseBo.fail("姓名过长");
        }
        if (StringUtils.isNotBlank(mail)&&!TaleUtils.isEmail(mail)){
            return RestResponseBo.fail("请输入正确的邮箱格式");
        }
        if (StringUtils.isNotBlank(url)&&!PatternKit.isURL(url)){
            return RestResponseBo.fail("请输入正确的url格式");
        }
        if (text.length()>200){
            return RestResponseBo.fail("评论过长，请不要多于200字符");
        }

        String val= IPKit.getIpAddrByRequest(request)+":"+cid;
        Integer count=cache.hashGet(Types.COMMENTS_FREQUENCY.getType(),val);
        if (null!=count&&count>0){
            return RestResponseBo.fail("您发表评论过快，等等吧");
        }
        author=TaleUtils.cleanXSS(author);
        text=TaleUtils.cleanXSS(text);
        author= EmojiParser.parseToAliases(author);
        text= EmojiParser.parseToAliases(text);

        CommentVo commentVo=new CommentVo();
        commentVo.setAuthor(author);
        commentVo.setContent(text);
        commentVo.setCid(cid);
        commentVo.setIp(request.getRemoteAddr());
        commentVo.setUrl(url);
        commentVo.setMail(mail);
        commentVo.setParent(coid);
        try {
            String result=commentService.insertComment(commentVo);
            cookie("tale_remember_author", URLEncoder.encode(author,"utf-8"),7*24*60*60,response);
            cookie("tale_remember_mail",URLEncoder.encode(mail,"utf-8"),7*24*60*60,response);
            if (StringUtils.isNotBlank(url)){
                cookie("tale_remember_url",URLEncoder.encode(url,"utf-8"),7*24*60*60,response);
            }
            //设置评论频率
            cache.hashSet(Types.COMMENTS_FREQUENCY.getType(),val,1,60);
            if (!Constants.SUCCESS_RESULT.equalsIgnoreCase(result)){
                return RestResponseBo.fail(result);
            }
            return RestResponseBo.ok();
        }catch (Exception e){
            String msg="评论发布失败";
            LOGGER.error(msg,e);
            return RestResponseBo.fail(msg);
        }

    }

}
