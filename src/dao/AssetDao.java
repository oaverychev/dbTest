//package dao;
//
//import com.marchex.crp.core.enums.AssetStatusEnum;
//import com.marchex.crp.core.model.Asset;
//import org.hibernate.criterion.DetachedCriteria;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//import static org.hibernate.criterion.Restrictions.eq;
//import static org.hibernate.criterion.Restrictions.not;
//
//
//public class AssetDao extends BaseDao {
//
//    public Asset getById(long assetId) {
//        return get(Asset.class, assetId);
//    }
//
//    public List<Asset> getAssetsByCampaignId(long campaignId) {
//        DetachedCriteria criteria = DetachedCriteria.forClass(Asset.class);
//        criteria.add(eq("campaign.id", campaignId));
//        return findByCriteria(criteria);
//    }
//
//    public List<Asset> getAssetsByStatus(long campaignId, AssetStatusEnum status) {
//        DetachedCriteria criteria = DetachedCriteria.forClass(Asset.class);
//        criteria.add(eq("status", status));
//        criteria.add(eq("campaign.id", campaignId));
//        return findByCriteria(criteria);
//    }
//
//    public List<Asset> getNotArchivedAssets(long campaignId) {
//        DetachedCriteria criteria = DetachedCriteria.forClass(Asset.class);
//        criteria.add(not(eq("status", AssetStatusEnum.ARCHIVED)));
//        criteria.add(eq("campaign.id", campaignId));
//        return findByCriteria(criteria);
//    }
//}
