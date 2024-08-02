package com.example.notificationservice.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListedNumberRepository extends CrudRepository<BlackListedNumber, String> {

}
