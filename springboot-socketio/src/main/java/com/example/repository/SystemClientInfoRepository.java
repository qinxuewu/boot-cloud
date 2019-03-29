package com.example.repository;
import com.example.entity.SystemClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface SystemClientInfoRepository extends JpaRepository<SystemClientInfo, Long>, JpaSpecificationExecutor {

}