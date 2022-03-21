package com.example.pcr_search.batch;

import com.example.pcr_search.domain.Hospital;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HospDataDownloadBatchTest {

    @Test
    public void start(){
        //1. 공공데이터 다운로드
        RestTemplate rt = new RestTemplate();
        //serviceKey 인코딩 문제로 URI 객체로 감싸야한다
        String url = "http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=tzoFe2PJS55%2FBfRhEvfC8JBK7KNrxnPfJ1Gisq%2FeX7bo%2B%2FjMzT9tMNKS7D%2BhmvjYmmkjoNG%2F2BEVKA3h093OjA%3D%3D&pageNo=1&numOfRows=10&_type=json";
        URI uri = null;
        try{
            uri = new URI(url);
            ResponseDto dto = rt.getForObject(uri,ResponseDto.class);
            //System.out.println(dto);
            List<Item> hospitals = dto.getResponse().getBody().getItems().getItem();
            for(Item item : hospitals){
                System.out.println(item.getYadmNm());
                System.out.println("PCR 여부 : " + item.getPcrPsblYn());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @DisplayName("공공 데이터 다운로드 테스트 컬력센 담기 (전체 데이터)")
    @Test
    public void download(){

        //1. 담을 그릇 준비
        List<Hospital> hospitals = new ArrayList<>();

        //2. api 한번 호출해서 totalcount 확인
        RestTemplate rt = new RestTemplate();
        //serviceKey 인코딩 문제로 URI 객체로 감싸야한다
        //사이즈를 1로 했더니 item이 컬렉션이 아니라서 파싱이 안되서 2로 바꿈
        int totalCount = 2;
        String url = "http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=tzoFe2PJS55%2FBfRhEvfC8JBK7KNrxnPfJ1Gisq%2FeX7bo%2B%2FjMzT9tMNKS7D%2BhmvjYmmkjoNG%2F2BEVKA3h093OjA%3D%3D&pageNo=1&numOfRows="+totalCount+"&_type=json";
        URI uri = null;

        try{
            uri = new URI(url);
            ResponseDto totalCountDto = rt.getForObject(uri,ResponseDto.class);
            totalCount = totalCountDto.getResponse().getBody().getTotalCount();

            url = "http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=tzoFe2PJS55%2FBfRhEvfC8JBK7KNrxnPfJ1Gisq%2FeX7bo%2B%2FjMzT9tMNKS7D%2BhmvjYmmkjoNG%2F2BEVKA3h093OjA%3D%3D&pageNo=1&numOfRows="+totalCount+"&_type=json";
            uri = new URI(url);

            ResponseDto responseDto = rt.getForObject(uri,ResponseDto.class);

            List<Item> items = responseDto.getResponse().getBody().getItems().getItem();
            System.out.println("가져온 데이터 사이즈 : "+ items.size());

            hospitals = items.stream().map(
                    (e) -> {
                        return Hospital.builder()
                            .addr(e.getAddr())
                            .mgtStaDd(e.getMgtStaDd())
                            .pcrPsblYn(e.getPcrPsblYn())
                            .ratPsblYn(e.getRatPsblYn())
                            .recuClCd(e.getRecuClCd())
                            .rnum(e.getRnum())
                            .rprtWorpClicFndtTgtYn(e.getRprtWorpClicFndtTgtYn())
                            .sgguCdNm(e.getSgguCdNm())
                            .sidoCdNm(e.getSidoCdNm())
                            .telno(e.getTelno())
                            .xPosWgs84(e.getXPosWgs84())
                            .yPosWgs84(e.getYPosWgs84())
                            .yadmNm(e.getYadmNm())
                            .ykihoEnc(e.getYkihoEnc())
                            .xPos(e.getXPos())
                            .yPos(e.getYPos())
                            .build();
                    }).collect(Collectors.toList());

            assertEquals(totalCount,items.size());

//            for(Item item : items){
//                System.out.println(item.getYadmNm());
//                System.out.println("PCR 여부 : " + item.getPcrPsblYn());
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}