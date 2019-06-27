package com.real.name.face.service.repository;

import com.real.name.face.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface RecordRepository extends JpaRepository<Record, Integer> {

    List<Record> findByPersonIdAndTimeBetween(Integer personId, Date beginTime, Date endTime);

    HashSet<String> findByTimeBetween(Date beginTime, Date endTime);

}
