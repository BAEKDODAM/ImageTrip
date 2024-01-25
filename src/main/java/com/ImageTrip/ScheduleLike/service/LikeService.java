package com.ImageTrip.ScheduleLike.service;

import com.ImageTrip.ScheduleLike.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository repository;

    public LikeService(LikeRepository repository) {
        this.repository = repository;
    }

    public int scheduleLikeCnt(long scheduleId){
        return (int) repository.findAllByScheduleScheduleId(scheduleId).stream().count();
    }
    /*public int imageLikeCnt(long imageId){
        return (int) repository.findAllByImageImageId(imageId).stream().count();
    }*/

    public void createLike(){

    }
    public void deleteLikeByScheduleId(long scheduleId){
        repository.deleteAllByScheduleScheduleId(scheduleId);
    }
    public void deleteLikeByImageId(long imageId){}
}
