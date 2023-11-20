package com.act.HR_management.Repos;

import com.act.HR_management.Models.OvertimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OvertimeTypeRepository extends JpaRepository<OvertimeType,Long> {
}
