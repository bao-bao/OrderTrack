package com.ordertrack.dao;

/* Created by AMXPC on 2018/5/8. */

import com.ordertrack.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageDao extends JpaRepository<Package, Long> {

    List<Package> findByIsActive(boolean isactive);
    List<Package> findByType(Integer type);
    List<Package> findByStandardContaining(String standard);
    List<Package> findByIsActiveAndType(boolean isactive, Integer type);
    List<Package> findByTypeAndStandardContaining(Integer type, String standard);
    List<Package> findByIsActiveAndStandardContaining(boolean isactive, String standard);
    List<Package> findByIsActiveAndTypeAndStandardContaining(boolean isactive, Integer type, String standard);

}
