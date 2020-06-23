package com.leaves.util;

import com.leaves.config.ShiroSpringConfig;
import com.leaves.entity.BrowseRecord;
import com.leaves.entity.PostInfo;
import com.leaves.entity.User;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 综合工具类
 *
 *
 */
@Component
public class IntegrateUtils {

    private static final String ALGORITHM_NAME = "MD5";
    private static final Integer HASH_ITERATIONS = 1024;

    /**
     * 加盐加密的策略非常多,根据实际业务来
     */
    public void entryptPassword(User user) {
        String salt = UUID.randomUUID().toString();
        String temPassword = user.getPlainPassword();
        Object md5Password = new SimpleHash(ALGORITHM_NAME, temPassword, ByteSource.Util.bytes(salt), HASH_ITERATIONS);
        user.setSalt(salt);
        user.setPassword(md5Password.toString());
    }

    /**
     * 加盐加密的策略非常多,根据实际业务来
     */
    public static boolean isEncrypted() {
        ShiroSpringConfig shiroSpringConfig = new ShiroSpringConfig();
        CustomConfiguration customConfiguration = shiroSpringConfig.setIsEncrypted();
        if ("yes".equals(customConfiguration.getIsEncrypted())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取ShiroSession
     *
     * @return
     */
    public Session getShiroSession() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return session;
    }

    /**
     * 获取Session用户信息
     *
     * @return
     */
    public User getSessionUser() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        System.out.println("^&^&^&^&^&^&^&^&^&"+user.toString());
        return user;
    }

    public String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) { //有可能是负数
            hashCodeV = -hashCodeV;
        }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    public String getProductUUId(String code) {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) { //有可能是负数
            hashCodeV = -hashCodeV;
        }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型
        return code + (machineId + String.format("%015d", hashCodeV)).substring(6, 15);
    }

    /**
     * 判断用户是否登录
     */
    public boolean isGogin() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isAuthenticated();
    }

    public static boolean stringIsNotBlack(String string) {
        if (string != null && !"".equals(string)) {
            return true;
        }
        return false;
    }

    /**
     * 写到资源文件
     *
     * @throws IOException
     */
    public String writeToD(String data, List<BrowseRecord> browseRecords) throws IOException {
        //所有id的string类型和long类型一一对应通过
        MemoryIDMigrator thing2long = new MemoryIDMigrator();
        String path = "D:\\User\\" + data + ".csv";
        File file1 = new File(path);
        boolean newFile1 = file1.createNewFile();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
        for (BrowseRecord browseRecord : browseRecords) {
            StringBuffer buffer = new StringBuffer();
            String userId = browseRecord.getUserId();
            long userIdLong = thing2long.toLongID(userId);
            buffer.append(userIdLong);
            buffer.append(",");
            String productId = browseRecord.getProductId();
            long productIdLong = thing2long.toLongID(productId);
            buffer.append(productIdLong);
            buffer.append(",");
            buffer.append(browseRecord.getFrequency());
            buffer.append("\r\n");
            writer.write(buffer.toString());
        }
        writer.flush();
        writer.close();
        return path;
    }

    /**
     * 协同过滤算法
     *
     * @param userId
     * @param number
     * @param path
     * @return
     * @throws Exception
     */
    final static int NEIGHBORHOOD_NUM = 2;//临近的用户个数
    final static int RECOMMENDER_NUM = 6;//推荐物品的最大个数

    public List<String> getSlopeOneCF(String userId, String path, List<PostInfo> productList) throws Exception {
        //所有id的string类型和long类型一一对应通过
        MemoryIDMigrator thing2long = new MemoryIDMigrator();
        HashMap<Long, String> map = new HashMap<>();
        for (PostInfo postInfo : productList) {
            map.put(thing2long.toLongID(postInfo.getId()), postInfo.getId());
        }
        DataModel model = new FileDataModel(new File(path));//数据模型
        UserSimilarity user = new EuclideanDistanceSimilarity(model);//用户相识度算法
        NearestNUserNeighborhood neighbor = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, user, model);
        //用户近邻算法
        Recommender r = new GenericUserBasedRecommender(model, neighbor, user);//用户推荐算法
        LongPrimitiveIterator iter = model.getUserIDs();///得到用户ID
        ArrayList<String> list1 = new ArrayList<>();
        while (iter.hasNext()) {
            long uid = iter.nextLong();
            if (uid == thing2long.toLongID(userId)) {
                List<RecommendedItem> list = r.recommend(uid, RECOMMENDER_NUM);
                for (RecommendedItem ritem : list) {
                    System.out.printf("(%s,%f)", ritem.getItemID(), ritem.getValue());
                    list1.add(map.get(ritem.getItemID()));
                }
            }
        }
        return list1;
    }
}
