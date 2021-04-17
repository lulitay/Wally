package com.example.pam_app.repository;

import com.example.pam_app.db.BucketDao;
import com.example.pam_app.db.BucketEntity;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

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
        Completable.fromRunnable(
                () -> bucketDao.create(bucketMapper.toEntity(bucket))).subscribeOn(Schedulers.io()
        ).subscribe();
    }

    @Override
    public void delete(Bucket bucket) {
        Completable.fromRunnable(
                () -> bucketDao.delete(bucketMapper.toEntity(bucket))
        ).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public Flowable<Bucket> get(int id) {
        return this.bucketDao.get(id).map(bucketMapper::toModel);
    }

    @Override
    public void addEntry(BucketEntry entry, final int idBucket) {
        Completable.fromRunnable(
                () -> bucketDao.addEntry(bucketMapper.toEntity(entry, idBucket))
        ).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void removeEntry(BucketEntry entry, final int idBucket) {
        Completable.fromRunnable(
                () -> bucketDao.removeEntry(bucketMapper.toEntity(entry, idBucket))
        ).subscribeOn(Schedulers.io()).subscribe();
    }
}
