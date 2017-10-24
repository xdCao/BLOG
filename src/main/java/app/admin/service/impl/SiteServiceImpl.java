package app.admin.service.impl;
/*
    created by xdCao on 2017/10/10
*/

import app.admin.controller.AttachController;
import app.admin.dao.AttachVoMapper;
import app.admin.dao.CommentVoMapper;
import app.admin.dao.ContentVoMapper;
import app.admin.dao.MetaVoMapper;
import app.admin.model.others.*;
import app.admin.model.vo.*;
import app.admin.service.SiteService;

import app.common.backup.Backup;
import app.common.constant.Constants;
import app.common.exceptions.TipException;
import app.common.utils.DateKit;
import app.common.utils.TaleUtils;
import app.common.utils.ZipUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class SiteServiceImpl implements SiteService {

    private static final Logger LOGGER= LoggerFactory.getLogger(SiteServiceImpl.class);

    @Autowired
    private CommentVoMapper commentVoMapper;

    @Autowired
    private ContentVoMapper contentVoMapper;

    @Autowired
    private AttachVoMapper attachVoMapper;

    @Autowired
    private MetaVoMapper metaVoMapper;


    @Override
    public List<CommentVo> recentComments(int limit) {
        LOGGER.debug("----------取出最近评论----------");
        if (limit<0||limit>10){
            limit=10;
        }
        CommentVoExample example=new CommentVoExample();
        example.setOrderByClause("created desc");
        PageHelper.startPage(1,limit);
        List<CommentVo> commentVoList=commentVoMapper.selectByExampleWithBLOBs(example);
        LOGGER.debug("----------取出最近评论成功----------");
        return commentVoList;
    }

    @Override
    public List<ContentVo> recentContents(int limit) {
        LOGGER.debug("----------取出最近内容----------");
        if (limit<0||limit>10){
            limit=10;
        }
        ContentVoExample example=new ContentVoExample();
        example.createCriteria().andStatusEqualTo(Types.PUBLISH.getType()).andTypeEqualTo(Types.ARTICLE.getType());
        example.setOrderByClause("created desc");
        PageHelper.startPage(1,limit);
        List<ContentVo> contentVos=contentVoMapper.selectByExample(example);
        LOGGER.debug("----------取出最近内容成功----------");
        return contentVos;
    }

    @Override
    public CommentVo getComment(Integer coid) {
        if (coid!=null){
            return commentVoMapper.selectByPrimaryKey(coid);
        }
        return null;
    }

    @Override
    public BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception {
        BackResponseBo backResponse = new BackResponseBo();
        if (bk_type.equals("attach")) {
            if (StringUtils.isBlank(bk_path)) {
                throw new TipException("请输入备份文件存储路径");
            }
            if (!(new File(bk_path)).isDirectory()) {
                throw new TipException("请输入一个存在的目录");
            }
            String bkAttachDir = AttachController.CLASSPATH + "upload";
            String bkThemesDir = AttachController.CLASSPATH + "templates/themes";

            String fname = DateKit.dateFormat(new Date(), fmt) + "_" + TaleUtils.getRandomNumber(5) + ".zip";

            String attachPath = bk_path + "/" + "attachs_" + fname;
            String themesPath = bk_path + "/" + "themes_" + fname;

            ZipUtils.zipFolder(bkAttachDir, attachPath);
            ZipUtils.zipFolder(bkThemesDir, themesPath);

            backResponse.setAttachPath(attachPath);
            backResponse.setThemePath(themesPath);
        }
        if (bk_type.equals("db")) {

            String bkAttachDir = AttachController.CLASSPATH + "upload/";
            if (!(new File(bkAttachDir)).isDirectory()) {
                File file = new File(bkAttachDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            String sqlFileName = "tale_" + DateKit.dateFormat(new Date(), fmt) + "_" + TaleUtils.getRandomNumber(5) + ".sql";
            String zipFile = sqlFileName.replace(".sql", ".zip");

            Backup backup = new Backup(TaleUtils.getNewDataSource().getConnection());
            String sqlContent = backup.execute();

            File sqlFile = new File(bkAttachDir + sqlFileName);
            write(sqlContent, sqlFile, Charset.forName("UTF-8"));

            String zip = bkAttachDir + zipFile;
            ZipUtils.zipFile(sqlFile.getPath(), zip);

            if (!sqlFile.exists()) {
                throw new TipException("数据库备份失败");
            }
            sqlFile.delete();

            backResponse.setSqlPath(zipFile);

            // 10秒后删除备份文件
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new File(zip).delete();
                }
            }, 10 * 1000);
        }
        return backResponse;
    }

    private void write(String data, File file, Charset charset) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data.getBytes(charset));
        } catch (IOException var8) {
            throw new IllegalStateException(var8);
        } finally {
            if(null != os) {
                try {
                    os.close();
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }
        }

    }

    @Override
    public StatisticsBo getStatistics() {
        LOGGER.debug("---------获取后台统计数据--------");
        StatisticsBo statisticsBo=new StatisticsBo();
        ContentVoExample contentVoExample=new ContentVoExample();
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        int articles=contentVoMapper.countByExample(contentVoExample);
        int comments = commentVoMapper.countByExample(new CommentVoExample());
        int attachs = attachVoMapper.countByExample(new AttachVoExample());
        MetaVoExample metaVoExample=new MetaVoExample();
        metaVoExample.createCriteria().andTypeEqualTo(Types.LINK.getType());
        int links = metaVoMapper.countByExample(metaVoExample);
        statisticsBo.setArticles((long) articles);
        statisticsBo.setAttachs((long) attachs);
        statisticsBo.setComments((long) comments);
        statisticsBo.setLinks((long) links);
        LOGGER.debug("----------返回后台统计数据---------");
        return statisticsBo;
    }

    @Override
    public List<ArchiveBo> getArchives() {
        LOGGER.debug("Enter getArchives method");
        List<ArchiveBo> archives = contentVoMapper.findReturnArchiveBo();
        if (null != archives) {
            archives.forEach(archive -> {
                ContentVoExample example = new ContentVoExample();
                ContentVoExample.Criteria criteria = example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
                example.setOrderByClause("created desc");
                String date = archive.getDate();
                Date sd = DateKit.dateFormat(date, "yyyy年MM月");
                int start = DateKit.getUnixTimeByDate(sd);
                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;
                criteria.andCreatedGreaterThan(start);
                criteria.andCreatedLessThan(end);
                List<ContentVo> contentss = contentVoMapper.selectByExample(example);
                archive.setArticles(contentss);
            });
        }
        LOGGER.debug("Exit getArchives method");
        return archives;
    }

    @Override
    public List<MetaDto> metas(String type, String orderBy, int limit) {
        LOGGER.debug("Enter metas method:type={},order={},limit={}", type, orderBy, limit);
        List<MetaDto> retList=null;
        if (StringUtils.isNotBlank(type)) {
            if(StringUtils.isBlank(orderBy)){
                orderBy = "count desc, a.mid desc";
            }
            if(limit < 1 || limit > Constants.MAX_POSTS){
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("order", orderBy);
            paraMap.put("limit", limit);
            retList= metaVoMapper.selectFromSql(paraMap);
        }
        LOGGER.debug("Exit metas method");
        return retList;
    }
}
