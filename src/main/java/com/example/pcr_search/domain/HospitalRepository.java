package com.example.pcr_search.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface HospitalRepository extends JpaRepository<Hospital,Integer> {
    @Query(value = "SELECT * FROM hospital WHERE sidoCdNm = :sidoCdNm AND sgguCdNm = :sgguCdNm AND pcrPsblYn = 'Y'", nativeQuery = true)
    public List<Hospital> mFindHospital(String sidoCdNm, String sgguCdNm);

    //
    @Query(value = "SELECT distinct sidoCdNm FROM hospital order by sidoCdNm", nativeQuery = true)
    public List<String> mFindSidoCdNm();

    @Query(value = "SELECT distinct sgguCdNm FROM HOSPITAL WHERE sidoCdNm = :sidoCdNm order by sgguCdNm", nativeQuery = true)
    public List<String> mFindSggucdnm(@Param("sidoCdNm") String sidoCdNm);
}