package app.admin.dao;

import app.admin.model.vo.RelationShipVoExample;
import app.admin.model.vo.RelationShipVoKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface RelationShipVoMapper {
    int countByExample(RelationShipVoExample example);

    int deleteByExample(RelationShipVoExample example);

    int deleteByPrimaryKey(RelationShipVoKey key);

    int insert(RelationShipVoKey record);

    int insertSelective(RelationShipVoKey record);

    List<RelationShipVoKey> selectByExample(RelationShipVoExample example);

    int updateByExampleSelective(@Param("record") RelationShipVoKey record, @Param("example") RelationShipVoExample example);

    int updateByExample(@Param("record") RelationShipVoKey record, @Param("example") RelationShipVoExample example);
}