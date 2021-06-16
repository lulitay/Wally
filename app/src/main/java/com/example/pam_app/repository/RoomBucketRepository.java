package com.example.pam_app.repository;

import com.example.pam_app.db.BucketDao;
import com.example.pam_app.db.BucketEntity;
import com.example.pam_app.db.BucketEntryWithBucketEntity;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static com.example.pam_app.model.BucketType.SPENDING;

public class RoomBucketRepository implements BucketRepository {

    private final BucketDao bucketDao;
    private final BucketMapper bucketMapper;

    public RoomBucketRepository(BucketDao bucketDao, BucketMapper bucketMapper) {
        this.bucketDao = bucketDao;
        this.bucketMapper = bucketMapper;
    }

    @Override
    public Flowable<List<Bucket>> getList() {
        return this.bucketDao.getList().map(bucketEntityList -> {
            final List<Bucket> buckets = new ArrayList<>();
            for (final BucketEntity be: bucketEntityList) {
                buckets.add(bucketMapper.toModel(be));
            }
            return buckets;
        });
    }

    @Override
    public Flowable<List<Bucket>> getList(boolean type, Date date) {
        return this.bucketDao.getList(type, date).map(bucketEntityList -> {
            final List<Bucket> buckets = new ArrayList<>();
            for (final BucketEntity be: bucketEntityList) {
                buckets.add(bucketMapper.toModel(be));
            }
            return buckets;
        });
    }

    @Override
    public void update(Bucket bucket) {
        bucketDao.update(bucketMapper.toEntity(bucket));
    }

    @Override
    public Single<Long> create(Bucket bucket) {
        return Single.fromCallable(() -> bucketDao.create(bucketMapper.toEntity(bucket)));
    }

    @Override
    public Single<Integer> delete(int id) {
        return Single.fromCallable(() -> bucketDao.delete(id));
    }

    @Override
    public Flowable<Bucket> get(int id) {
        return this.bucketDao.get(id).map(bucketMapper::toModel);
    }

    @Override
    public Single<Bucket> get(final String title) {
        return this.bucketDao.getBucket(title).map(bucketMapper::toModel);
    }

    @Override
    public Flowable<List<BucketEntry>> getEntryList() {
        return this.bucketDao.getEntryList().map(bucketEntryEntityList -> {
            final List<BucketEntry> entries = new ArrayList<>();
            for (final BucketEntryWithBucketEntity be: bucketEntryEntityList) {
                entries.add(bucketMapper.toModel(be));
            }
            return entries;
        });
    }

    @Override
    public Flowable<List<Double>> getSpendingTotal() {
        return bucketDao.getTotalAmountByType(SPENDING.ordinal());
    }

    @Override
    public Single<Long> addEntry(BucketEntry entry, final int bucketId) {
        return Single.fromCallable(() -> bucketDao.addEntry(bucketMapper.toEntity(entry, bucketId)));
    }

    @Override
    public void removeEntry(BucketEntry entry, final int idBucket) {
        bucketDao.removeEntry(bucketMapper.toEntity(entry, idBucket));
    }

    @Override
    public Flowable<List<String>> getTitleListByType(int type) {
        return this.bucketDao.getTitleListByType(type);
    }
}
