package com.example.pcr_search.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Entity
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String addr; //병원주소
    private Integer mgtStaDd; //운영시작일지
    private String pcrPsblYn; //PCR검사 여부
    private String ratPsblYn; //호흡기 전담 클리닉 여부
    private Integer recuClCd; // 요양종별코드 (종합병원 11 병원 21 의원 31)
    private Integer rnum; //
    private String rprtWorpClicFndtTgtYn; //
    private String sgguCdNm; //
    private String sidoCdNm; // 시도명 : 경상남도
    private String telno; //전화번호
    private Integer xPos; // x좌표 격자
    private Double xPosWgs84; // x좌표  위도
    private Integer yPos; // y좌표 격자
    private Double yPosWgs84; //y좌표 경도
    private String yadmNm; // 요양기관명
    private String ykihoEnc; //암호화된 요양 기호

}
