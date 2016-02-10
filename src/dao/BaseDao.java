//package dao;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.hibernate.*;
//import org.hibernate.criterion.DetachedCriteria;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Projections;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.*;
//import java.math.BigInteger;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.util.Collection;
//import java.util.List;
//
//import static java.time.ZoneOffset.UTC;
//
//public class BaseDao {
//
//    protected final Log log = LogFactory.getLog(getClass());
//
//    @Autowired(required = false/*workaround for tests*/)
//    private SessionFactory sessionFactory;
//
//    public Session getSession() {
//        return getSessionFactory().getCurrentSession();
//    }
//
//    public <T> void save(T o) {
//        getSession().saveOrUpdate(o);
//    }
//
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public <T> void saveInNewTransaction(T o) {
//        o = (T)getSession().merge(o);
//        getSession().saveOrUpdate(o);
//    }
//
//    public <T> void saveAll(Collection<T> items) {
//        for (T obj: items) {
//            save(obj);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    /** @return object with specified id or null if not found */
//    public <T> T get(Class<T> clazz, Serializable id) {
//        if (id == null)
//            return null;
//        T object = (T)getSession().get(clazz, id, LockOptions.NONE);
//        return object;
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T> List<T> getAll(Class<T> clazz) {
//        return (List<T>) getSession().createCriteria(clazz).list();
//    }
//
//    public void delete(Object obj){
//        getSession().delete(obj);
//    }
//
//    public <T> void deleteAll(Collection<T> items) {
//        for(T item : items){
//            delete(item);
//        }
//    }
//
//    public void detach(Object obj) {
//        if (obj != null)
//            getSession().evict(obj);
//    }
//
//    public void flush() {
//        getSession().flush();
//    }
//
//    public void clear() {
//        getSession().clear();
//    }
//
//    public <T> void refresh(T object) {
//        flush();
//        detach(object);
//        getSession().refresh(object);
//    }
//
//    public LocalDateTime getServerDateTime() {
//        // use numeric function to avoid uncontrollable timezone conversions
//        BigInteger timestampSeconds = (BigInteger)getSession().createSQLQuery("select round(date_part('epoch', now()));").uniqueResult();
//        Instant instant = Instant.ofEpochMilli(1000 * timestampSeconds.longValue());
//        // return it in UTC timezone
//        return LocalDateTime.ofInstant(instant, UTC);
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <T> List<T> find(String query){
//        return getSession().createQuery(query).list();
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <T> List<T> find(String query, List<Object> args){
//        Query q = getSession().createQuery(query);
//
//        for(int i=0; i<args.size(); i++){
//            q.setParameter(i, args.get(i));
//        }
//
//        return q.list();
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <T> List<T> find(String query, Object...args){
//        Query q = getSession().createQuery(query);
//
//        for(int i=0; i<args.length; i++){
//            q.setParameter(i, args[i]);
//        }
//
//        return q.list();
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <T> T findUniqueByCriteria(DetachedCriteria criteria){
//        return (T)criteria.getExecutableCriteria(getSession()).uniqueResult();
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <T> List<T> findByCriteria(DetachedCriteria criteria){
//        return criteria.getExecutableCriteria(getSession()) .list();
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <T> List<T> findByCriteria(DetachedCriteria criteria, Order... orders){
//        return findByCriteria(criteria, 0, Integer.MAX_VALUE, orders);
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <T> List<T>
//    findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults){
//        return criteria.getExecutableCriteria(getSession())
//                .setFirstResult(firstResult)
//                .setMaxResults(maxResults)
//                .list();
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <T> List<T>
//    findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults, Order... orders){
//        Criteria execCriteria = criteria.getExecutableCriteria(getSession())
//                                        .setFirstResult(firstResult)
//                                        .setMaxResults(maxResults);
//        for (Order order: orders) {
//            execCriteria.addOrder(order);
//        }
//        return execCriteria.list();
//    }
//
//    // Counts objects without affecting initial criteria
//    protected int countByCriteria(DetachedCriteria criteria){
//        Long count = (Long)criteria.getExecutableCriteria(getSession()).setProjection(Projections.rowCount()).uniqueResult();
//        return count.intValue();
//    }
//
//    public SessionFactory getSessionFactory() {
//        return sessionFactory;
//    }
//
//    public void setSessionFactory(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    /**
//     * Copy (deep clone) the provided criteria using serialization.
//     *
//     * @param criteria object to copy
//     * @see <a href="https://forum.hibernate.org/viewtopic.php?t=939781">Cloning criteria using serialization</a>
//     * @return copy of criteria
//     */
//    protected static DetachedCriteria clone(DetachedCriteria criteria) {
//        try {
//            ByteArrayOutputStream baostream = new ByteArrayOutputStream();
//            ObjectOutputStream oostream = new ObjectOutputStream(baostream);
//            oostream.writeObject(criteria);
//            oostream.flush();
//            oostream.close();
//            ByteArrayInputStream baistream = new ByteArrayInputStream(baostream.toByteArray());
//            ObjectInputStream oistream = new ObjectInputStream(baistream);
//            DetachedCriteria copy = (DetachedCriteria) oistream.readObject();
//            oistream.close();
//            return copy;
//        } catch (Throwable t) {
//            throw new HibernateException(t);
//        }
//    }
//
//    public long getLastInsertId() {
//        return ((BigInteger) getSession().createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult()).longValue();
//    }
//
//}
