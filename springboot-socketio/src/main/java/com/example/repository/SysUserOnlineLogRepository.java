package com.example.repository;
import com.example.entity.SysUserOnlineLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SysUserOnlineLogRepository extends JpaRepository<SysUserOnlineLog, Long>, JpaSpecificationExecutor {
}
