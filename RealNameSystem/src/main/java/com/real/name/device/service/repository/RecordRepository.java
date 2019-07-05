package com.real.name.device.service.repository;

import com.real.name.device.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Integer> {

    List<Record> findByPersonIdAndTimeBetween(Integer personId, Date beginTime, Date endTime);

    HashSet<String> findByTimeBetween(Date beginTime, Date endTime);

}
