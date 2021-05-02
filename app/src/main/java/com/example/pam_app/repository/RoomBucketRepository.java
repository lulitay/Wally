package com.example.pam_app.repository;

import com.example.pam_app.db.BucketDao;
import com.example.pam_app.db.BucketEntity;
import com.example.pam_app.db.BucketEntryEntity;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

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
    public void create(Bucket bucket) {
        bucketDao.create(bucketMapper.toEntity(bucket));
    }

    @Override
    public void delete(int id) {
        this.bucketDao.delete(id);
    }

    @Override
    public Flowable<Bucket> get(int id) {
        return this.bucketDao.get(id).map(bucketMapper::toModel);
    }

    @Override
    public Flowable<Bucket> get(final String title) {
        return this.bucketDao.getBucket(title).map(bucketMapper::toModel);
    }

    @Override
    public Flowable<List<BucketEntry>> getEntryList() {
        return this.bucketDao.getEntryList().map(bucketEntryEntityList -> {
            final List<BucketEntry> entries = new ArrayList<>();
            for (final BucketEntryEntity be: bucketEntryEntityList) {
                entries.add(bucketMapper.toModel(be));
            }
            return entries;
        });
    }

    @Override
    public void addEntry(BucketEntry entry, final String bucketTitle) {
        final Bucket bucket = get(bucketTitle).blockingFirst();
        bucketDao.addEntry(bucketMapper.toEntity(entry, bucket.id));
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
