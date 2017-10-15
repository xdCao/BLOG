package app.admin.controller;
/*
    created by xdCao on 2017/10/15
*/

import app.admin.service.CommentService;
import app.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/comments")
public class CommentController extends BaseController {

    private static final Logger LOGGER= LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

}
